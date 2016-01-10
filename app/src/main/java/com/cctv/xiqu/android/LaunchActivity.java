package com.cctv.xiqu.android;

import java.util.Timer;
import java.util.TimerTask;

import com.cctv.xiqu.android.APP.DisplayOptions;
import com.cctv.xiqu.android.fragment.network.AdvertisementRequest;
import com.cctv.xiqu.android.fragment.network.AdvertisementRequest.Advertisement;
import com.cctv.xiqu.android.fragment.network.BaseClient.SimpleRequestHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

import com.cctv.xiqu.android.R;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.VideoView;

public class LaunchActivity extends BaseActivity{
	
	private ImageView imgView;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_luanch);
		imgView = (ImageView) findViewById(R.id.img);
		request();
		VideoView view = (VideoView) findViewById(R.id.video);
		String path = "android.resource://" + getPackageName() + "/" + R.raw.launch;
		view.setVideoURI(Uri.parse(path));
		view.setOnPreparedListener(new OnPreparedListener() {
			
			@Override
			public void onPrepared(MediaPlayer mp) {
				mp.start();
				
			}
		});
		view.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				Handler handler = new Handler(getMainLooper());
				handler.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						onAnimEnd();
						
					}
				}, 1000);
			}

			
		});
	}
	
	private static class ADModel{
		private Bitmap bitmap;
		private String link;
		public ADModel(Bitmap bitmap, String link) {
			super();
			this.bitmap = bitmap;
			this.link = link;
		}
		
	}
	
	private ADModel model;
	
	private static final int REQ_WEBVIEW = 10;
	
	private void onAnimEnd() {
		if(model != null){
			imgView.setVisibility(View.VISIBLE);
			imgView.setImageBitmap(model.bitmap);
			final Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				
				@Override
				public void run() {
					finish();
				}
			}, 3000);
			imgView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					timer.cancel();
					Intent intent = new Intent(LaunchActivity.this, WebViewActivity.class);
					intent.putExtra(WebViewActivity.PARAM_URL,model.link);
					LaunchActivity.this.startActivityForResult(intent, REQ_WEBVIEW);
				}
			});
			
		}else{
			finish();
		}
		
	}
	private void request() {
		AdvertisementRequest request = new AdvertisementRequest(this);
		request.request(new SimpleRequestHandler(){
			@Override
			public void onSuccess(Object object) {
				AdvertisementRequest.Result result = (AdvertisementRequest.Result)object;
				final Advertisement advertisement = result.getAdvertisement();
				if(result.getAdvertisement() != null){
					ImageLoader.getInstance().loadImage(advertisement.getImageurl(), DisplayOptions.IMG.getOptions(), new ImageLoadingListener() {
						
						@Override
						public void onLoadingStarted(String imageUri, View view) {
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
							model = new ADModel(loadedImage, advertisement.getLinkurl());
							
						}
						
						@Override
						public void onLoadingCancelled(String imageUri, View view) {
							// TODO Auto-generated method stub
							
						}
					});
				}
			}
		});
		
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQ_WEBVIEW:
			finish();
			break;

		default:
			break;
		}
	}
	
	@Override
	public void finish() {
		MainActivity.open(LaunchActivity.this);
		super.finish();
	}
	
}
