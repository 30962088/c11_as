package com.cctv.xiqu.android.fragment.network;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.Header;

import com.loopj.android.http.RequestParams;

import android.content.Context;

public class WeiboCommentPostRequest extends BaseClient{
	
	public static class Params{
		private String access_token;
		
		private String comment;
		
		private String id;

		public Params(String access_token, String id, String comment) {
			super();
			this.access_token = access_token;
			try {
				this.comment = URLEncoder.encode(comment,"utf-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.id = id;
		}
		
	}
	
	private Params params;

	public WeiboCommentPostRequest(Context context, Params params) {
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
		params.add("comment", "" + this.params.comment);
		params.add("id", "" + this.params.id);
		return params;
	}

	@Override
	protected String getURL() {
		// TODO Auto-generated method stub
		return "https://api.weibo.com/2/comments/create.json";
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
