package com.cctv.xiqu.android.fragment.network;



import org.apache.http.Header;

import android.content.Context;


import com.cctv.xiqu.android.APP;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;

public class InsertCommentRequest extends BaseClient {

	public static class Result {
		
	}
	
	public static class Params {
		private String contentsid;
		private String iscommentid;
		private String issid;
		private String sid;
		private String remark;
		private String pkey;
		public Params(String contentsid, String iscommentid, String issid,
				String sid, String remark,String pkey) {
			super();
			this.contentsid = contentsid;
			this.iscommentid = iscommentid;
			this.issid = issid;
			this.sid = sid;
			this.remark = remark;
			this.pkey = pkey;
		}

	}
	
	private Params params;
	

	

	public InsertCommentRequest(Context context, Params params) {
		super(context);
		this.params = params;
	}

	@Override
	public Object onSuccess(String str) {
		return new Gson().fromJson(str, Result.class);

	}
	
	@Override
	protected RequestParams getParams() {
		RequestParams params = new RequestParams();
		params.add("method", "insertcomment");
		params.add("contentsid", this.params.contentsid);
		params.add("iscommentid", this.params.iscommentid);
		params.add("issid", this.params.issid);
		params.add("sid", this.params.sid);
		params.add("remark", this.params.remark);
		params.add("pkey", this.params.pkey);
		return params;
	}

	@Override
	protected String getURL() {
		
		return APP.getAppConfig().getRequest_news()+"cctv11/insertcomment";
	}

	@Override
	protected Method getMethod() {
		
		return Method.POST;
	}

	@Override
	public void onError(int error, String msg) {
		// TODO Auto-generated method stub
		
	}
}
