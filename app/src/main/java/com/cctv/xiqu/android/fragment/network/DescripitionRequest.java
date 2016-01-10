package com.cctv.xiqu.android.fragment.network;


import java.util.Date;

import org.apache.http.Header;

import android.content.Context;

import com.cctv.xiqu.android.APP;
import com.cctv.xiqu.android.SpecialDetailActivity;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;

public class DescripitionRequest extends BaseClient{

	public static class Result {
		private String contentsid;
		private String contentstitle;
		private String poemauthor;
		private String contentsdate;
		private String description;
		private String videositeurl;
		private String commentcount;
		public SpecialDetailActivity.Model toDetailModel(){
			return new SpecialDetailActivity.Model(description);
		}
	}
	
	
	private String contentsid;
	

	public DescripitionRequest(Context context, String contentsid) {
		super(context);
		this.contentsid = contentsid;
	}

	@Override
	public Object onSuccess(String str) {
		return new Gson().fromJson(str, Result.class);

	}

	

	@Override
	protected RequestParams getParams() {
		RequestParams params = new RequestParams();
		params.add("contentsid", contentsid);
		params.add("method", "description");
		return params;
	}

	@Override
	protected String getURL() {
		// TODO Auto-generated method stub
		return APP.getAppConfig().getRequest_news()+"cctv11/description";
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
