package com.cctv.xiqu.android.fragment.network;

import org.apache.http.Header;


import com.cctv.xiqu.android.APP;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;

import android.content.Context;

public class FeedbackRequest extends BaseClient{

	public static class Params{
		private String sid;
		private String feedbackhelptitle;
		private String feedbackhelpcontent;
		private String pkey;
		public Params(String sid, String feedbackhelptitle,
				String feedbackhelpcontent,String pkey) {
			super();
			this.sid = sid;
			this.feedbackhelptitle = feedbackhelptitle;
			this.feedbackhelpcontent = feedbackhelpcontent;
			this.pkey = pkey;
		}
	}
	
	private Params params;

	public FeedbackRequest(Context context, Params params) {
		super(context);
		this.params = params;
	}
	
	public static class Result{
		private int result;
		public int getResult() {
			return result;
		}
	}

	@Override
	public Object onSuccess(String str) {
		// TODO Auto-generated method stub
		return new Gson().fromJson(str, Result.class);
	}


	@Override
	protected RequestParams getParams() {
		RequestParams params = new RequestParams();
		if(this.params.sid != null){
			params.add("sid", this.params.sid);
		}
		if(this.params.pkey != null){
			params.add("pkey", this.params.pkey);
		}
		params.add("method", "InsertFeedbackhelp");
		params.add("feedbackhelptitle", this.params.feedbackhelptitle);
		params.add("feedbackhelpcontent", this.params.feedbackhelpcontent);
		return params;
	}

	@Override
	protected String getURL() {
		// TODO Auto-generated method stub
		return APP.getAppConfig().getRequest_news()+"cctv11/InsertFeedbackhelp";
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
