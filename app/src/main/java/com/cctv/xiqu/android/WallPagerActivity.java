package com.cctv.xiqu.android;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import org.apache.commons.io.FileUtils;

import com.cctv.xiqu.android.APP.DisplayOptions;
import com.cctv.xiqu.android.PhotoViewActivity.Photo;
import com.cctv.xiqu.android.adapter.SGridAdapter;
import com.cctv.xiqu.android.utils.Dirctionary;
import com.cctv.xiqu.android.utils.ImageUtils;
import com.cctv.xiqu.android.utils.ShareUtils;
import com.cctv.xiqu.android.widget.IOSPopupWindow;
import com.cctv.xiqu.android.widget.OnLongTapFrameLayout;
import com.cctv.xiqu.android.widget.SPopupWindow;
import com.cctv.xiqu.android.widget.IOSPopupWindow.OnIOSItemClickListener;
import com.mengle.lib.utils.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.DisplayImageOptions.Builder;
import com.nostra13.universalimageloader.core.assist.DiscCacheUtil;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.UMImage;

import com.cctv.xiqu.android.R;

import uk.co.senab.photoview.PhotoViewAttacher;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

public class WallPagerActivity extends BaseActivity implements OnClickListener,OnLongClickListener{

	public static void open(Context context, Model model) {
		Intent intent = new Intent(context, WallPagerActivity.class);
		intent.putExtra("model", model);
		context.startActivity(intent);
		((Activity) context).overridePendingTransition(R.anim.fade_in,
				R.anim.fade_out);
	}
	
	
	public static class Model implements Serializable{
		private String thunbnail;
		private String name;
		private String origin;
		
		public Model(String thunbnail, String name, String origin) {
			super();
			this.thunbnail = thunbnail;
			this.name = name;
			this.origin = origin;
		}

	}
	
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}
	
	private PhotoViewAttacher attacher;
	
	private Model model;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		model = (Model) getIntent().getSerializableExtra("model");
		setContentView(R.layout.activity_wallpager);
		findViewById(R.id.share).setOnClickListener(this);
		((OnLongTapFrameLayout)findViewById(R.id.longtap)).setOnLongClickListener1(this);
		((TextView)findViewById(R.id.title)).setText(model.name);
		final View loading = findViewById(R.id.loading);
		final ImageView imageView = (ImageView) findViewById(R.id.img);
		imageView.setVisibility(View.GONE);
		final ImageView thumbnail = (ImageView) findViewById(R.id.thumbnail);
		findViewById(R.id.back).setOnClickListener(this);
		findViewById(R.id.share).setOnClickListener(this);
		ImageLoader.getInstance().displayImage(model.thunbnail, thumbnail,DisplayOptions.IMG.getOptions());
//		attacher.setScaleType(ScaleType.FIT_CENTER);
		ImageLoader.getInstance().displayImage(
				model.origin,
				imageView,
				DisplayOptions.IMG.getOptions(),
				new SimpleImageLoadingListener() {
					@Override
					public void onLoadingComplete(String imageUri,
							View view, Bitmap loadedImage) {
						loading.setVisibility(View.GONE);
						thumbnail.setVisibility(View.GONE);
						imageView.setVisibility(View.VISIBLE);
						attacher = new PhotoViewAttacher(imageView);
					}
				});
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		attacher.cleanup();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.share:
			onshare();
			break;
		default:
			break;
		}
		
	}
	

	private void onshare() {
		new SPopupWindow(this, new ArrayList<SGridAdapter.Model>(){{
			add(new SGridAdapter.Model(R.drawable.s_image, "保存到相册"));
			add(new SGridAdapter.Model(R.drawable.s_qq, "QQ好友"));
			add(new SGridAdapter.Model(R.drawable.s_qzone, "QQ空间"));
			add(new SGridAdapter.Model(R.drawable.s_weixin, "微信好友"));
			add(new SGridAdapter.Model(R.drawable.s_timeline, "微信朋友圈"));
			add(new SGridAdapter.Model(R.drawable.s_sina, "新浪微博"));
			add(new SGridAdapter.Model(R.drawable.s_mail, "用邮件发送"));
		}}, new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				if(position == 0 || position ==6){
					ImageLoader.getInstance().loadImage(model.origin, DisplayOptions.IMG.getOptions(),new ImageLoadingListener() {
						
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
							File file = ImageLoader.getInstance().getDiscCache().get(imageUri);
							if(position == 0){
								File dest = new File(Dirctionary.getPictureDir(),new Date().getTime()+".jpg");
								try {
									FileUtils.copyFile(file, dest);
									ImageUtils.addImageToGallery(dest.toString(), WallPagerActivity.this);
									Utils.tip(WallPagerActivity.this, "保存成功");
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}else{
								Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
								emailIntent.setType("application/image");
//								emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{strEmail}); 
								emailIntent.putExtra(Intent.EXTRA_SUBJECT,"央视戏曲官方壁纸下载链接");
//								emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "From My App"); 
								emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
								startActivity(Intent.createChooser(emailIntent, "发送邮件"));
							}
						}
						
						@Override
						public void onLoadingCancelled(String imageUri, View view) {
							// TODO Auto-generated method stub
							
						}
					});
					
				}
				if (position >= 1 && position <= 5) {
					int index = position-1;
					SHARE_MEDIA media = new SHARE_MEDIA[] {
							SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE,
							SHARE_MEDIA.WEIXIN,
							SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA }[index];
					File bitmapFile = ImageLoader.getInstance().getDiscCache().get(model.thunbnail);;
					ShareUtils.shareWebsite(WallPagerActivity.this,media, "央视戏曲官方壁纸下载链接", model.origin, bitmapFile);
				}
				
			}
		});
		
		
	}

	@Override
	public boolean onLongClick(View v) {
		onshare();
		return false;
	}
	
}
