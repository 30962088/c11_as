package com.cctv.xiqu.android.fragment.network;

import java.security.Signer;

import org.apache.http.Header;


import com.cctv.xiqu.android.APP;
import com.cctv.xiqu.android.fragment.UserSettingFragment;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;

import android.content.Context;
import android.text.TextUtils;

public class GetSingerInfoRequest extends BaseClient{

	public static class Params{
		private String sid;

		public Params(String sid) {
			super();
			this.sid = sid;
		}
		
	}
	
	public static class Model{
		private String pkey;
		private String sid;
		private String singername;
		private String qqnum;
		private String sex;
		private String address;
		private String weiboid;
		private String singerimgguid;
		private String singerimgformat;
		private String datetime;
		private String singerimgurl;
		
		public String getSingername() {
			return singername;
		}
		public String getSingerimgurl() {
			return APP.getAppConfig().getImage(singerimgguid, singerimgformat);
		}
		
		public String getAddress() {
			return address;
		}
		
		
	}
	
	public static class Result{
		private Model models;
		public Model getModels() {
			return models;
		}
		public boolean isPhoneLogin(){
			if(TextUtils.isEmpty(models.qqnum) && TextUtils.isEmpty(models.weiboid)){
				return true;
			}
			return false;
		}
	}
	
	private Params params;

	public GetSingerInfoRequest(Context context, Params params) {
		super(context);
		this.params = params;
	}

	@Override
	public Object onSuccess(String str) {
		// TODO Auto-generated method stub
		return new Gson().fromJson(str, Result.class);
	}

	@Override
	protected RequestParams getParams() {
		RequestParams params = new RequestParams();
		params.add("sid", this.params.sid);
		params.add("appid", "1217");
		return params;
	}

	@Override
	protected String getURL() {
		// TODO Auto-generated method stub
		return APP.getAppConfig().getRequest_user()+"get.mvc/getSingerInfo";
	}

	@Override
	protected Method getMethod() {
		// TODO Auto-generated method stub
		return Method.GET;
	}

	@Override
	public void onError(int error, String msg) {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
