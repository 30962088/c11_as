package com.cctv.xiqu.android.fragment.network;

import org.apache.http.Header;


import com.cctv.xiqu.android.APP;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;

import android.content.Context;

public class GetProvinceCityRequest extends BaseClient{

	
	public static class Result{
		private int result;
		private String provincecity;
		public int getResult() {
			return result;
		}
		public String getProvincecity() {
			return provincecity;
		}
	}
	
	public static class Params{
		private double axisY;
		private double axisX;
		public Params(double axisX,double axisY) {
			this.axisY = axisY;
			this.axisX = axisX;
		}
		public double getAxisX() {
			return axisX;
		}
		public double getAxisY() {
			return axisY;
		}
		
		
	}
	
	private Params params;
	
	public GetProvinceCityRequest(Context context, Params params) {
		super(context);
		this.params = params;
	}


	@Override
	public Object onSuccess(String str) {
		// TODO Auto-generated method stub
		return new Gson().fromJson(str, Result.class);
	}



	@Override
	protected RequestParams getParams() {
		RequestParams params = new RequestParams();
		params.add("axisX", ""+this.params.axisX);
		params.add("axisY", ""+this.params.axisY);
		params.add("appid", "1217");
		return params;
	}


	@Override
	protected String getURL() {
		// TODO Auto-generated method stub
		return APP.getAppConfig().getRequest_user()+"get.mvc/getprovincecity";
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
