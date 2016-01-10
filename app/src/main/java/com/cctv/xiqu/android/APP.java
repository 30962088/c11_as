package com.cctv.xiqu.android;

import com.cctv.xiqu.android.R;

import com.baidu.frontia.FrontiaApplication;
import com.cctv.xiqu.android.utils.Dirctionary;
import com.cctv.xiqu.android.utils.Preferences.Session;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.DisplayImageOptions.Builder;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;

public class APP extends FrontiaApplication {

	
	
	
	public enum DisplayOptions {
		IMG(new Builder().showImageForEmptyUri(R.drawable.empty)
				.showImageOnLoading(R.drawable.empty).cacheInMemory(true)
				.cacheOnDisc(true).build());
		/*
		 * THUNBNAIL(new Builder()
		 * .showImageForEmptyUri(R.drawable.icon_thunbnail_loading)
		 * .showImageOnLoading(R.drawable.icon_thunbnail_loading)
		 * .cacheInMemory(true).cacheOnDisc(true).build()), COMMENT( new
		 * Builder().showImageOnFail(R.drawable.icon_person_gray)
		 * .cacheInMemory(true).cacheOnDisc(true).build()),
		 * 
		 * BANNER(new Builder()
		 * .showImageForEmptyUri(R.drawable.icon_loading_banner)
		 * .showImageOnLoading(R.drawable.icon_loading_banner)
		 * .cacheInMemory(true).cacheOnDisc(true).build()),
		 */
		// SPLASH(new Builder().cacheInMemory(true).cacheOnDisc(true).build());

		DisplayImageOptions options;

		DisplayOptions(DisplayImageOptions options) {
			this.options = options;
		}

		public DisplayImageOptions getOptions() {
			return options;
		}
	}

	private static APP instance = null;
	
	@Override
	public void onCreate() {
		super.onCreate();
		appConfig = new AppConfig(this);
		instance = this;
		
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext()).denyCacheImageMultipleSizesInMemory()
				.memoryCacheExtraOptions(768, 1280)
				.memoryCache(new UsingFreqLimitedMemoryCache(8 * 1024 * 1024))
				.memoryCacheSize(8 * 1024 * 1024)
				.discCacheSize(50 * 1024 * 1024)
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.threadPoolSize(3).threadPriority(Thread.NORM_PRIORITY - 1)
				.tasksProcessingOrder(QueueProcessingType.LIFO).build();
		// 初始化ImageLoader的与配置。
		mImageLoader.init(config);
		Dirctionary.init(this);
		initPreference();
		
	}
	
	public static AppConfig getAppConfig() {
		return appConfig;
	}
	private static AppConfig appConfig;

	private static Session session;
	
	public static class AppConfig{
		private String WX_APPID;
		private String WX_AppSecret;
		private String QQ_APPID;
		private String QQ_APPKEY;
		private String UMENG_APPKEY;
		private String UMENG_CHANNEL;
		private String request_news;
		private String request_user;
		private String push_api_key;
		private AppConfig(Context context) {
			
			try {
				ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
				Bundle bundle = ai.metaData;
				WX_APPID = bundle.getString("WX_APPID");
				WX_AppSecret = bundle.getString("WX_AppSecret");
				QQ_APPID = bundle.getString("QQ_APPID");
				QQ_APPKEY = bundle.getString("QQ_APPKEY");
				UMENG_APPKEY = bundle.getString("UMENG_APPKEY");
				UMENG_CHANNEL = bundle.getString("UMENG_CHANNEL");
				request_news = bundle.getString("request_news");
				request_user = bundle.getString("request_user");
				push_api_key = bundle.getString("push_api_key");
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		public String getPush_api_key() {
			return push_api_key;
		}
		public String getRequest_news() {
			return request_news;
		}
		public String getRequest_user() {
			return request_user;
		}
		public String getQQ_APPID() {
			return QQ_APPID;
		}
		public String getQQ_APPKEY() {
			return QQ_APPKEY;
		}
		public String getUMENG_APPKEY() {
			return UMENG_APPKEY;
		}
		public String getUMENG_CHANNEL() {
			return UMENG_CHANNEL;
		}
		public String getWX_APPID() {
			return WX_APPID;
		}
		public String getWX_AppSecret() {
			return WX_AppSecret;
		}
		
		public String getImage(String filename,String format){
			
			return appConfig.getRequest_user()+"get.mvc/getTheImage?fileName="+filename+format+"&appid=1217";
			
		}
		
		public String getSharecontent(String id){
			return request_news+"ContentsShares/ContentsShare?contentid="+id;
		}
		
		public  String getShareForumcontent(String id){
			return request_news+"ForumtopicShares/ForumtopicShare?topicid="+id;
		}
		
	}
	
	public static Session getSession() {
		return session;
	}
	
	private void initPreference() {
		
		session = new Session(this);
	}


	public static APP getInstance() {
		return instance;
	}

	public static ImageLoader mImageLoader = ImageLoader.getInstance();

}
