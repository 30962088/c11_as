package com.cctv.xiqu.android.fragment.network;

import org.apache.http.Header;

import com.cctv.xiqu.android.APP;
import com.cctv.xiqu.android.utils.Preferences.Session;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;

import android.content.Context;

public class SetSingerUserRequest extends BaseClient{
	
	
	public static class Params{
		private String singername;
		private String sex;
		private String singerimgguid;
		private String singerimgformat;
		private String address;
		private String phone;
		private String password;
		public Params(String singername, String sex, String singerimgguid,
				String singerimgformat, String address, String phone,
				String password) {
			super();
			this.singername = singername;
			this.sex = sex;
			this.singerimgguid = singerimgguid;
			this.singerimgformat = singerimgformat;
			this.address = address;
			this.phone = phone;
			this.password = password;
		}
		
	}
	
	public static class Result{
		private String pkey;
		private int result;
		private String sid;
		
		public void login(Context context){
			if(result == 1000){
				Session session = new Session(context);
				session.login(sid, pkey);
			}
			
		}
	}
	
	private Params params;

	public SetSingerUserRequest(Context context, Params params) {
		super(context);
		this.params = params;
	}

	@Override
	public Object onSuccess(String str) {
		Result result = new Gson().fromJson(str, Result.class);
		result.login(context);
		return result;
	}


	@Override
	protected RequestParams getParams() {
		RequestParams params = new RequestParams();
		params.add("singername", this.params.singername);
		params.add("sex", this.params.sex);
		params.add("singerimgguid", "" + this.params.singerimgguid);
		params.add("singerimgformat", "" + this.params.singerimgformat);
		params.add("address", "" + this.params.address);
		params.add("phone", "" + this.params.phone);
		params.add("password", "" + this.params.password);
		params.add("appid", "1217");
		return params;
	}
	

	@Override
	protected String getURL() {
		// TODO Auto-generated method stub
		return APP.getAppConfig().getRequest_user()+"get.mvc/setSingerUser";
	}

	@Override
	protected Method getMethod() {
		// TODO Auto-generated method stub
		return Method.POST;
	}

	@Override
	public void onError(int error, String msg) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
}
