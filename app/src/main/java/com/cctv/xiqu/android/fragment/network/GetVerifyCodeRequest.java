package com.cctv.xiqu.android.fragment.network;



import com.cctv.xiqu.android.APP;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;

import android.content.Context;

public class GetVerifyCodeRequest extends BaseClient{

	public static class Params{
		private String phone;
		private int type;
		public Params(String phone, int type) {
			super();
			this.phone = phone;
			this.type = type;
		}
		
	}
	
	public static class Result{
		private int result;
		private String code;
		public int getResult() {
			return result;
		}
		public String getCode() {
			return code;
		}
	}
	
	private Params params;
	
	

	public GetVerifyCodeRequest(Context context, Params params) {
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
		params.add("appid", "1217");
		params.add("phone", this.params.phone);
		params.add("type", ""+this.params.type);
		return params;
	}

	@Override
	protected String getURL() {
		// TODO Auto-generated method stub
		return APP.getAppConfig().getRequest_user()+"get.mvc/getVerifyCode";
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
