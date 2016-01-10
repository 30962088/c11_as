package com.cctv.xiqu.android.fragment.network;


import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;


import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;

public class WeiboCountRequest extends BaseClient {

	public static class Params {
		private String access_token;
		private String id;
		public Params(String access_token, String id) {
			super();
			this.access_token = access_token;
			this.id = id;
		}

		

	}

	public static class Result{
		private String id;
		private long comments;
		private long reposts;
		public long getComments() {
			return comments;
		}
		public long getReposts() {
			return reposts;
		}
		public String getId() {
			return id;
		}
	}

	private Params params;

	public WeiboCountRequest(Context context, Params params) {
		super(context);
		this.params = params;
	}

	@Override
	public Object onSuccess(String str) {
		Result result = new Result();
		
		try {
			JSONArray array = new JSONArray(str);
			if(array.length()>0){
				result = new Gson().fromJson(array.get(0).toString(), Result.class);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	@Override
	protected RequestParams getParams() {
		
		RequestParams params = new RequestParams();
		params.add("access_token", "" + this.params.access_token);
		params.add("ids", "" + this.params.id);
		return params;
	}

	@Override
	protected String getURL() {
		// TODO Auto-generated method stub
		return "https://api.weibo.com/2/statuses/count.json";
	}

	@Override
	protected Method getMethod() {
		// TODO Auto-generated method stub
		return Method.GET;
	}

	@Override
	public void onError(int error, String msg) {
		// TODO Auto-generated method stub
		
	}
	
	

	

}
