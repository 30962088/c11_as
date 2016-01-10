package com.cctv.xiqu.android;

import java.io.File;
import java.io.Serializable;

import com.cctv.xiqu.android.R;

import com.baidu.android.pushservice.CustomPushNotificationBuilder;
import com.baidu.android.pushservice.PushManager;
import com.cctv.xiqu.android.APP.AppConfig;
import com.cctv.xiqu.android.utils.Dirctionary;
import com.cctv.xiqu.android.widget.PhotoSelectPopupWindow;
import com.cctv.xiqu.android.widget.PhotoSelectPopupWindow.OnItemClickListener;
import com.mengle.lib.wiget.ConfirmDialog;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;

import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;

public class BaseActivity extends FragmentActivity implements Serializable{

	private Uri cameraPic;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		try {
			initUmeng();
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}
	
	private void initUmeng() throws NameNotFoundException {
		AppConfig config = APP.getAppConfig();
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(this,
				config.getQQ_APPID(), config.getQQ_APPKEY());
		qqSsoHandler.addToSocialSDK();
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(this, config.getQQ_APPID(),config.getQQ_APPKEY());
		qZoneSsoHandler.addToSocialSDK();
		UMWXHandler wxHandler = new UMWXHandler(this,config.getWX_APPID());
		wxHandler.addToSocialSDK();
		UMWXHandler wxCircleHandler = new UMWXHandler(this,config.getWX_AppSecret());
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
	}

	private static final int ACTION_REQUEST_GALLERY = 1;
	
	private static final int ACTION_REQUEST_CAMERA = 2;
	
	private static final int ACTION_REQUEST_CITY = 3;
	
	private static final int ACTION_REQUEST_NICKNAME = 4;
	
	private static final int ACTION_REQUEST_MODIFY_PHONE = 5;
	
	private static final int ACTION_REQUEST_MODIFY_PWD = 6;
	
	private static final int ACTION_REQUEST_BINDING_WEIBO = 7;
	
	public static interface OnNicknameFillListener{
		public void onNicknameFill(String nickname);
	}
	
	public static interface OnWeiboBindingListener{
		public void onWeiboBinding();
	}
	
	public static interface OnGallerySelectionListener{
		public void onGallerySelection(File file);
	}
	
	public static interface OnCitySelectionListener{
		public void onCitySelection(String city);
	}
	
	public static interface OnModifyPhoneListener{
		public void onModifyPhone(String phone);
	}
	
	public static interface OnModifyPasswordListener{
		public void onModifyPassword(String password);
	}
	
	private OnGallerySelectionListener onGallerySelectionListener;
	
	private OnCitySelectionListener onCitySelectionListener;
	
	private OnNicknameFillListener onNicknameFillListener;
	
	private OnModifyPhoneListener onModifyPhoneListener;
	
	private OnModifyPasswordListener onModifyPasswordListener;
	
	private OnWeiboBindingListener onWeiboBindingListener;
	
	public void modifyPassowrd(OnModifyPasswordListener onModifyPasswordListener){
		this.onModifyPasswordListener = onModifyPasswordListener;
		Intent intent = new Intent(this, ModifyPasswordActivity.class);
		startActivityForResult(intent, ACTION_REQUEST_MODIFY_PWD);
	}
	
	public void bindingWeibo(OnWeiboBindingListener onWeiboBindingListener){
		this.onWeiboBindingListener = onWeiboBindingListener;
		Intent intent = new Intent(this, BindingWeiboActivity.class);
		startActivityForResult(intent, ACTION_REQUEST_BINDING_WEIBO);
	}
	
	public void modifyPhone(OnModifyPhoneListener onModifyPhoneListener){
		this.onModifyPhoneListener = onModifyPhoneListener;
		Intent intent = new Intent(this, ModifyPhoneActivity.class);
		startActivityForResult(intent, ACTION_REQUEST_MODIFY_PHONE);
	}
	
	public void getNickname(String nickname,OnNicknameFillListener onNicknameFillListener){
		this.onNicknameFillListener = onNicknameFillListener;
		Intent intent = new Intent(this, NicknameActivity.class);
		intent.putExtra("nickname", nickname);
		startActivityForResult(intent, ACTION_REQUEST_NICKNAME);
	}
	
	public void getCity(OnCitySelectionListener onCitySelectionListener){
		this.onCitySelectionListener = onCitySelectionListener;
		Intent intent = new Intent(this, CityActivity.class);
		startActivityForResult(intent, ACTION_REQUEST_CITY);
	}
	
	public void getPhoto(OnGallerySelectionListener onGallerySelectionListener){
		this.onGallerySelectionListener = onGallerySelectionListener;
		new PhotoSelectPopupWindow(this,new OnItemClickListener() {
			
			@Override
			public void onItemClick(int id) {
				switch (id) {
				case R.id.take_photo:
					Intent getCameraImage = new Intent("android.media.action.IMAGE_CAPTURE");
		            cameraPic = Uri.fromFile(Dirctionary.creatPicture());
		            getCameraImage.putExtra(MediaStore.EXTRA_OUTPUT, cameraPic);
		            startActivityForResult(getCameraImage, ACTION_REQUEST_CAMERA);
					break;
				case R.id.read_photo:
					Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		            intent.setType("image/*");

		            Intent chooser = Intent.createChooser(intent, "从本地相册读取");
		            startActivityForResult(chooser, ACTION_REQUEST_GALLERY);
					break;
				default:
					break;
				}
			}
		},"设置头像");
	}
	
	public static String convertMediaUriToPath(Context context, Uri uri) {
		if(uri.toString().startsWith("file://")){
			return uri.getPath();
		}
		Cursor cursor = context.getContentResolver().query(uri, null, null,
				null, null);
		cursor.moveToFirst();
		String document_id = cursor.getString(0);
		document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
		cursor.close();

		cursor = context.getContentResolver().query(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				null, MediaStore.Images.Media._ID + " = ? ",
				new String[] { document_id }, null);
		cursor.moveToFirst();
		String path = cursor.getString(cursor
				.getColumnIndex(MediaStore.Images.Media.DATA));
		cursor.close();

		return path;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case ACTION_REQUEST_CITY:
			if (resultCode == Activity.RESULT_OK) { 
				if(onCitySelectionListener != null){
					onCitySelectionListener.onCitySelection(data.getStringExtra("city"));
				}
			}
			break;
		case ACTION_REQUEST_GALLERY:
			if (resultCode == Activity.RESULT_OK) { 
				if(onGallerySelectionListener != null){
					File file = new File(convertMediaUriToPath(this, data.getData()));
					
					onGallerySelectionListener.onGallerySelection(file);
				}
			}
			break;
		case ACTION_REQUEST_CAMERA:
			if (resultCode == Activity.RESULT_OK) {
				if(onGallerySelectionListener != null){
					File file = new File(convertMediaUriToPath(this, cameraPic));
					onGallerySelectionListener.onGallerySelection(file);
				}
			}
			break;
		case ACTION_REQUEST_NICKNAME:
			if (resultCode == Activity.RESULT_OK) { 
				if(onNicknameFillListener != null){
					onNicknameFillListener.onNicknameFill(data.getStringExtra("nickname"));
				}
			}
			break;
		case ACTION_REQUEST_MODIFY_PHONE:
			if (resultCode == Activity.RESULT_OK) { 
				if(onModifyPhoneListener != null){
					onModifyPhoneListener.onModifyPhone(data.getStringExtra("phone"));
				}
			}
			break;
		case ACTION_REQUEST_MODIFY_PWD:
			if (resultCode == Activity.RESULT_OK) { 
				if(onModifyPasswordListener != null){
					onModifyPasswordListener.onModifyPassword(data.getStringExtra("password"));
				}
			}
			break;
		case ACTION_REQUEST_BINDING_WEIBO:
			if (resultCode == Activity.RESULT_OK) { 
				if(onWeiboBindingListener != null){
					onWeiboBindingListener.onWeiboBinding();
				}
			}
			break;
		default:
			break;
		}
	}
	
	public void toLogin(){
		toLogin("您需要登录才能评论");
	}
	
	public void toLogin(String title){
		ConfirmDialog.open(this, "提醒", title, new ConfirmDialog.OnClickListener() {
			
			@Override
			public void onPositiveClick() {
				Intent intent = new Intent(BaseActivity.this, MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
						| Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.setAction(MainActivity.ACTION_TOLOGIN);
				startActivity(intent);
				
			}
			
			@Override
			public void onNegativeClick() {
				// TODO Auto-generated method stub
				
			}
		});
		
	}
	
	public void openGuide(){
	
		if(!APP.getSession().isGuide()){
			GuideActivity.open(this);
			APP.getSession().setGuide(true);
		}
		
	}
	
}
