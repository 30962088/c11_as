package com.cctv.xiqu.android.fragment.network;

import java.util.List;

import org.apache.http.Header;


import com.cctv.xiqu.android.APP;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;

import android.content.Context;

public class GetProgramRequest extends BaseClient{
	
	public static class Program{
		private String programcontent;
	}
	
	public static class Result{
		private int result;
		private List<Program> programlist;
		public String getContent(){
			return programlist.get(0).programcontent;
		}
		public int getResult() {
			return result;
		}
	}

	public GetProgramRequest(Context context) {
		super(context);
	}

	@Override
	public Object onSuccess(String str) {
		// TODO Auto-generated method stub
		return new Gson().fromJson(str, Result.class);
	}

	@Override
	protected RequestParams getParams() {
		RequestParams params = new RequestParams();
		params.add("pageno", "1");
		params.add("pagesize", "1");
		params.add("method", "program");
		return params;
	}

	@Override
	protected String getURL() {
		// TODO Auto-generated method stub
		return APP.getAppConfig().getRequest_news()+"cctv11/program";
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
