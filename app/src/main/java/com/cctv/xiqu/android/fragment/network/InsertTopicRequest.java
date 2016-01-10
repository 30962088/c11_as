package com.cctv.xiqu.android.fragment.network;

import org.apache.http.Header;


import com.cctv.xiqu.android.APP;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;

import android.content.Context;

public class InsertTopicRequest extends BaseClient{
	public static class Params{
		private String topiccontent;
		private String sid;
		private String pkey;
		private String topictitle;
		private String uploadimgguid;
		private String uploadimgformat;
		public Params(String topiccontent, String sid,String pkey, String topictitle,
				String uploadimgguid, String uploadimgformat) {
			super();
			this.topiccontent = topiccontent;
			this.sid = sid;
			this.pkey = pkey;
			this.topictitle = topictitle;
			this.uploadimgguid = uploadimgguid;
			this.uploadimgformat = uploadimgformat;
		}
		public Params(String topiccontent, String sid,String pkey, String topictitle) {
			super();
			this.topiccontent = topiccontent;
			this.sid = sid;
			this.pkey = pkey;
			this.topictitle = topictitle;
		}
		
	}
	
	private Params params;

	public InsertTopicRequest(Context context, Params params) {
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
		RequestParams params = new EscapeRequestParams();
		params.add("topiccontent", ""+this.params.topiccontent);
		params.add("sid", ""+this.params.sid);
		params.add("pkey", ""+this.params.pkey);
		params.add("topictitle", ""+this.params.topictitle);
		if(this.params.uploadimgguid != null){
			params.add("uploadimgguid", ""+this.params.uploadimgguid);
		}
		if(this.params.uploadimgformat != null){
			params.add("uploadimgformat", ""+this.params.uploadimgformat);
		}
		return params;
	}

	@Override
	protected String getURL() {
		// TODO Auto-generated method stub
		return APP.getAppConfig().getRequest_news()+"cctv11/inserttopic";
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
