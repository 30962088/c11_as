package com.cctv.xiqu.android.fragment.network;


import com.cctv.xiqu.android.APP;
import com.loopj.android.http.RequestParams;

import android.content.Context;

public class ForgetpasswordRequest extends BaseClient{

	public static class Params{
		private String phone;
		private String password;
		public Params(String phone, String password) {
			super();
			this.phone = phone;
			this.password = password;
		}
		
	}
	
	private Params params;
	
	

	public ForgetpasswordRequest(Context context, Params params) {
		super(context);
		this.params = params;
	}

	@Override
	public Object onSuccess(String str) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onError(int error, String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected RequestParams getParams() {
		RequestParams params = new RequestParams();
		params.add("phone", this.params.phone);
		params.add("password", this.params.password);
		params.add("appid", "1217");
		return params;
	}

	@Override
	protected String getURL() {
		// TODO Auto-generated method stub
		return APP.getAppConfig().getRequest_user()+"get.mvc/forgetpassword";
	}

	@Override
	protected Method getMethod() {
		// TODO Auto-generated method stub
		return Method.POST;
	}

}
