package com.cctv.xiqu.android.fragment.network;

import java.util.List;


import com.cctv.xiqu.android.APP;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;

import android.content.Context;
import android.text.TextUtils;

public class AdvertisementRequest extends BaseClient{

	public static class Advertisement{
		private String devicetype;
		private String linkurl;
		private String imageurl;
		public String getLinkurl() {
			return linkurl;
		}
		public String getImageurl() {
			return imageurl;
		}
	}
	
	public static class Result{
		private List<Advertisement> advertisementlist;
		public Advertisement getAdvertisement(){
			Advertisement android = getAdvertisement("android");
			if(android == null){
				android = getAdvertisement("iphone5");
			}
			return android;
		}
		private Advertisement getAdvertisement(String devicetype){
			Advertisement result = null;
			for(Advertisement advertisement : advertisementlist){
				if(TextUtils.equals(devicetype, advertisement.devicetype)){
					result = advertisement;
					break;
				}
			}
			return result;
		}
		
	}
	
	public AdvertisementRequest(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object onSuccess(String str) {
		// TODO Auto-generated method stub
		return new Gson().fromJson(str, Result.class);
	}

	@Override
	public void onError(int error, String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected RequestParams getParams() {
		RequestParams params = new RequestParams();
		params.add("method", "advertisement");
		return params;
	}

	@Override
	protected String getURL() {
		// TODO Auto-generated method stub
		return APP.getAppConfig().getRequest_news()+"cctv11/advertisement";
	}

	@Override
	protected Method getMethod() {
		// TODO Auto-generated method stub
		return Method.POST;
	}

	
	
}
