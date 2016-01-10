package com.cctv.xiqu.android.widget;

import java.io.IOException;

import com.mengle.lib.utils.Utils;
import com.mengle.lib.wiget.ConfirmDialog;

import com.cctv.xiqu.android.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.AttributeSet;

import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

public class VideoView extends FrameLayout implements SurfaceHolder.Callback,
		MediaPlayer.OnPreparedListener, VideoControllerView.MediaPlayerControl,
		MediaPlayer.OnBufferingUpdateListener,OnKeyListener,OnClickListener,OnInfoListener {

	public VideoView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public VideoView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public VideoView(Context context) {
		super(context);
		init();
	}

	private SurfaceView videoSurface;
	private MediaPlayer player;
	private VideoControllerView controller;
	private SurfaceHolder videoHolder;
	private View loadingView,playView;
	
	private GestureDetector gestureDetector;

	private void init() {
		LayoutInflater.from(getContext()).inflate(
				R.layout.activity_video_player, this);
		playView = findViewById(R.id.play);
		playView.setVisibility(View.VISIBLE);
		playView.setOnClickListener(this);
		gestureDetector = new GestureDetector(getContext(), new SingleTapConfirm());
		videoSurface = (SurfaceView) findViewById(R.id.videoSurface);
		videoSurface.setVisibility(View.GONE);
		loadingView = findViewById(R.id.loading);
		loadingView.setVisibility(View.GONE);
		videoHolder = videoSurface.getHolder();
		videoHolder.addCallback(this);
		setClickable(true);
		
		controller = new VideoControllerView(getContext());
		player = new MediaPlayer();
		player.setOnInfoListener(this);
		player.setAudioStreamType(AudioManager.STREAM_MUSIC);
		player.setOnPreparedListener(this);
		player.setScreenOnWhilePlaying(true);
		setOnKeyListener(this);
	}
	private String videpPath;
	public void setVidepPath(String videpPath) {
		this.videpPath = videpPath;
	}


	private boolean prepared = false;

	class SingleTapConfirm extends SimpleOnGestureListener {

		@Override
		public boolean onSingleTapConfirmed(MotionEvent event) {
			if (prepared) {
				if(controller.isShowing()){
					controller.hide();
				}else{
					controller.show();
				}
				
			}
			return true;
		}
		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		gestureDetector.onTouchEvent(ev);
		return true;
	}
	

	// Implement SurfaceHolder.Callback
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		if(isPlaying){
			player.start();
			
			isPlaying = false;
		}
		controller.updatePausePlay();
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		player.setDisplay(holder);

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		
	}

	// End SurfaceHolder.Callback

	// Implement MediaPlayer.OnPreparedListener
	@Override
	public void onPrepared(MediaPlayer mp) {
		videoSurface.setVisibility(View.VISIBLE);
		controller.setMediaPlayer(this);
		controller
				.setAnchorView((FrameLayout) findViewById(R.id.videoSurfaceContainer));
		
		player.start();
		loadingView.setVisibility(View.GONE);
		prepared = true;
	}

	// End MediaPlayer.OnPreparedListener

	// Implement VideoMediaController.MediaPlayerControl
	@Override
	public boolean canPause() {
		return true;
	}

	@Override
	public boolean canSeekBackward() {
		return true;
	}

	@Override
	public boolean canSeekForward() {
		return true;
	}

	@Override
	public int getBufferPercentage() {
		return 0;
	}

	@Override
	public int getCurrentPosition() {
		return player.getCurrentPosition();
	}

	@Override
	public int getDuration() {
		return player.getDuration();
	}

	@Override
	public boolean isPlaying() {
		return player.isPlaying();
	}

	@Override
	public void pause() {
		player.pause();
	}

	@Override
	public void seekTo(int i) {
		player.seekTo(i);
	}

	@Override
	public void start() {
		player.start();
	}
	
	public void release(){
		player.release();
	}

	@Override
	public boolean isFullScreen() {
		return fullScreen;
	}
	
	public boolean isPrepared() {
		return prepared;
	}

	public static interface OnToggleFullScreenListener {
		public void onToggleFullScreen(boolean isFullScreen);
	}

	private OnToggleFullScreenListener onToggleFullScreenListener;

	public void setOnToggleFullScreenListener(
			OnToggleFullScreenListener onToggleFullScreenListener) {
		this.onToggleFullScreenListener = onToggleFullScreenListener;
	}

	private boolean fullScreen = false;

	private ViewGroup parent;

	private int indexOfChild;
	
	private boolean isPlaying;

	@SuppressLint("NewApi")
	@Override
	public void toggleFullScreen() {
		fullScreen = !fullScreen;
		isPlaying = player.isPlaying();
		player.pause();
		if (fullScreen) {
			screenFull();
		} else {
			screenNormal();
		}
//		player.start();
	}
	
	private void screenNormal(){
		Activity activity = ((Activity) getContext());
		activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		((ViewGroup) getParent()).removeView(this);
		parent.addView(this, indexOfChild);
		
	}
	
	private void screenFull(){
		parent = ((ViewGroup) getParent());
		indexOfChild = parent.indexOfChild(this);
		parent.removeView(this);
		final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
				WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				PixelFormat.TRANSLUCENT);
		Activity activity = ((Activity) getContext());
		activity.getWindow().addContentView(this, params);
		activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		setFocusableInTouchMode(true);
		requestFocus();
		
	}

	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		System.out.println("zzm" + percent);

	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if(isFullScreen()){
			controller.fullscreenButtonPerformClick();
			return true;
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.play:
			ConnectivityManager connManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

			if (mWifi.isConnected()) {
				onplay();
			}else{
				ConfirmDialog.open(getContext(), "提示", "你现在使用的是非WIFI网络，建议在Wifi下观看，土豪请随意～", new ConfirmDialog.OnClickListener() {
					
					@Override
					public void onPositiveClick() {
						onplay();
						
					}
					
					@Override
					public void onNegativeClick() {
						// TODO Auto-generated method stub
						
					}
				});
			}
			break;

		default:
			break;
		}
		
	}

	private void onplay() {
		try {
			playView.setVisibility(View.GONE);
			loadingView.setVisibility(View.VISIBLE);
			player.setOnErrorListener(new OnErrorListener() {
				
				@Override
				public boolean onError(MediaPlayer mp, int what, int extra) {
					playView.setVisibility(View.VISIBLE);
					loadingView.setVisibility(View.GONE);
					return false;
				}
			});
			player.setDataSource(getContext(), Uri.parse(videpPath));
			player.prepareAsync();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public boolean onInfo(MediaPlayer mp, int what, int extra) {
		switch (what) {  
        case MediaPlayer.MEDIA_INFO_BUFFERING_START:  
        	loadingView.setVisibility(View.VISIBLE);
            break;  
        case MediaPlayer.MEDIA_INFO_BUFFERING_END:  
        	loadingView.setVisibility(View.GONE);
            break;  
        case MediaPlayer.MEDIA_INFO_BAD_INTERLEAVING:  
            break;  
        case MediaPlayer.MEDIA_INFO_METADATA_UPDATE:  
            break;  
        case MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING:  
            break;  
        case MediaPlayer.MEDIA_INFO_NOT_SEEKABLE:  
            break;  
        }  
		return false;
	}
}
