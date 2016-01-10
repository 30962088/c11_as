package com.cctv.xiqu.android.fragment.network;

import com.cctv.xiqu.android.APP;
import com.loopj.android.http.RequestParams;

import android.content.Context;

public class IsHaveSingerName extends BaseClient{

	private String singername;
	
	

	public IsHaveSingerName(Context context, String singername) {
		super(context);
		this.singername = singername;
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
		params.add("singername", singername);
		return params;
	}

	@Override
	protected String getURL() {
		// TODO Auto-generated method stub
		return APP.getAppConfig().getRequest_user()+"get.mvc/isHaveSingerName";
	}

	@Override
	protected Method getMethod() {
		// TODO Auto-generated method stub
		return Method.POST;
	}

}
