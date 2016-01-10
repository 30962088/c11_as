package com.cctv.xiqu.android.fragment.network;


import com.cctv.xiqu.android.APP;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;

import android.content.Context;

public class ReportRequest extends BaseClient{

	public static class Params{
		private String sid;
		private String contents;
		private String topicid;
		private String pkey;
		private String username;
		private String phone;
		public Params(String sid, String contents, String topicid, String pkey,boolean login) {
			super();
			this.sid = sid;
			this.contents = contents;
			this.topicid = topicid;
			this.pkey = pkey;
		}
		public Params(String contents, String topicid, String username,
				String phone) {
			super();
			this.contents = contents;
			this.topicid = topicid;
			this.username = username;
			this.phone = phone;
		}
	}
	
	public static class Result{
		private int result;
		public int getResult() {
			return result;
		}
	}
	
	private Params params;

	public ReportRequest(Context context, Params params) {
		super(context);
		this.params = params;
	}

	@Override
	public Object onSuccess(String str) {
		
		return new Gson().fromJson(str, Result.class);
	}

	@Override
	public void onError(int error, String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected RequestParams getParams() {
		RequestParams params = new RequestParams();
		params.add("method", "report");
		if(this.params.sid != null){
			params.add("sid", ""+this.params.sid);
		}
		if(this.params.pkey != null){
			params.add("pkey", ""+this.params.pkey);
		}
		if(this.params.phone != null){
			params.add("phone", ""+this.params.phone);
		}
		if(this.params.username != null){
			params.add("username", ""+this.params.username);
		}
		params.add("contents", ""+this.params.contents);
		params.add("topicid", ""+this.params.topicid);
		
		return params;
	}

	@Override
	protected String getURL() {
		// TODO Auto-generated method stub
		return APP.getAppConfig().getRequest_news()+"cctv11/report";
	}

	@Override
	protected Method getMethod() {
		// TODO Auto-generated method stub
		return Method.POST;
	}
	
	
	
}
