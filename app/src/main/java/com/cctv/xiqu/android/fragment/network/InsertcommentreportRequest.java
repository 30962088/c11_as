package com.cctv.xiqu.android.fragment.network;


import com.cctv.xiqu.android.APP;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;

import android.content.Context;

public class InsertcommentreportRequest extends BaseClient{

	public static class Params{
		private String reporttype;
		private String commentid;
		private int typeid;
		private String sid;
		private String pkey;
		public Params(String reporttype, String commentid, int typeid) {
			super();
			this.reporttype = reporttype;
			this.commentid = commentid;
			this.typeid = typeid;
		}
		public Params(String reporttype, String commentid, int typeid,
				String sid, String pkey) {
			super();
			this.reporttype = reporttype;
			this.commentid = commentid;
			this.typeid = typeid;
			this.sid = sid;
			this.pkey = pkey;
		}
		
		
	}
	
	private Params params;

	public InsertcommentreportRequest(Context context, Params params) {
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
	public void onError(int error, String msg) {
		
		
	}

	@Override
	protected RequestParams getParams() {
		RequestParams params = new RequestParams();
		params.add("commentid", ""+this.params.commentid);
		params.add("reporttype", ""+this.params.reporttype);
		params.add("typeid", ""+this.params.typeid);
		if(this.params.pkey != null){
			params.add("pkey", ""+this.params.pkey);
		}
		if(this.params.sid != null){
			params.add("sid", ""+this.params.sid);
		}
		return params;
	}

	@Override
	protected String getURL() {
		// TODO Auto-generated method stub
		return APP.getAppConfig().getRequest_news()+ "cctv11/Insertcommentreport";
	}

	@Override
	protected Method getMethod() {
		// TODO Auto-generated method stub
		return Method.POST;
	}
	
	
	
}
