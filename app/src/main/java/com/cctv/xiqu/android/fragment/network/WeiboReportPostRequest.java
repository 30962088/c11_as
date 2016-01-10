package com.cctv.xiqu.android.fragment.network;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.Header;

import com.loopj.android.http.RequestParams;

import android.content.Context;

public class WeiboReportPostRequest extends BaseClient{
	
	public static class Params{
		private String access_token;
		
		private String status;
		
		private String id;
		
		private int is_comment;

		public Params(String access_token, String id, String status,int is_comment) {
			super();
			this.access_token = access_token;
			try {
				this.status = URLEncoder.encode(status,"utf-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.id = id;
			this.is_comment = is_comment;
		}
		
	}
	
	private Params params;

	public WeiboReportPostRequest(Context context, Params params) {
		super(context);
		this.params = params;
	}

	@Override
	public Object onSuccess(String str) {
		
		return null;
	}

	@Override
	protected RequestParams getParams() {

		RequestParams params = new RequestParams();
		params.add("access_token", "" + this.params.access_token);
		params.add("status", "" + this.params.status);
		params.add("id", "" + this.params.id);
		params.add("is_comment", "" + this.params.is_comment);
		return params;
	}

	@Override
	protected String getURL() {
		// TODO Auto-generated method stub
		return "https://api.weibo.com/2/statuses/repost.json";
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
