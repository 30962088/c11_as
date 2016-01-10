package com.cctv.xiqu.android.fragment.network;


import android.content.Context;


import com.cctv.xiqu.android.APP;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;

public class InsertForumRequest extends BaseClient {

	public static class Result {
		
	}

	public static class Params {
		private String topicid;
		private String iscommentid = "0";
		private String issid = "0";
		private String sid;
		private String remark;
		private String pkey;

		public Params(String topicid, String iscommentid, String issid,
				String sid, String remark,String pkey) {
			super();
			this.topicid = topicid;
			this.iscommentid = iscommentid;
			this.issid = issid;
			this.sid = sid;
			this.remark = remark;
			this.pkey = pkey;
		}

		public Params(String topicid, String sid, String remark,String pkey) {
			super();
			this.topicid = topicid;
			this.sid = sid;
			this.remark = remark;
			this.pkey = pkey;
		}

	}

	private Params params;

	public InsertForumRequest(Context context, Params params) {
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
		params.add("method", "insertforumcomment");
		params.add("topicid", this.params.topicid);
		params.add("iscommentid", this.params.iscommentid);
		params.add("issid", this.params.issid);
		params.add("sid", this.params.sid);
		params.add("remark", this.params.remark);
		params.add("pkey", this.params.pkey);
		return params;
	}

	@Override
	protected String getURL() {

		return APP.getAppConfig().getRequest_news()+"cctv11/insertforumcomment";
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
