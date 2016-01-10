package com.cctv.xiqu.android.fragment.network;

import org.apache.http.Header;


import com.cctv.xiqu.android.APP;
import com.cctv.xiqu.android.fragment.FillInfoFragment.Sex;
import com.cctv.xiqu.android.utils.Preferences.Session;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;

import android.content.Context;

public class SetSingerRequest extends BaseClient{

	
	public static class Params{
		private String wbqqid;
		private int type;
		private String singername;
		private Sex sex;
		private String singerimgguid;
		private String singerimgformat;
		private String address;
		public Params(String wbqqid, int type, String singername, Sex sex,
				String singerimgguid, String singerimgformat, String address) {
			super();
			this.wbqqid = wbqqid;
			this.type = type;
			this.singername = singername;
			this.sex = sex;
			this.singerimgguid = singerimgguid;
			this.singerimgformat = singerimgformat;
			this.address = address;
		}
		
	}
	
	public static class Result{
		private String pkey;
		private int result;
		private String sid;
		public void login(Context context){
			if(result == 1000){
				Session session = new Session(context);
				session.login(sid, pkey);
			}
			
		}
	}
	
	private Params params;
	
	
	public SetSingerRequest(Context context, Params params) {
		super(context);
		this.params = params;
	}

	@Override
	public Object onSuccess(String str) {
		Result result = new Gson().fromJson(str, Result.class);
		result.login(context);
		return result;
	}


	@Override
	protected RequestParams getParams() {
		RequestParams params = new RequestParams();
		params.add("wbqqid", this.params.wbqqid);
		params.add("type", ""+this.params.type);
		params.add("singername", this.params.singername);
		params.add("sex", this.params.sex.getCode());
		params.add("singerimgguid", this.params.singerimgguid);
		params.add("singerimgformat", this.params.singerimgformat);
		params.add("address", this.params.address);
		params.add("appid", "1217");
		return params;
	}

	@Override
	protected String getURL() {
		// TODO Auto-generated method stub
		return APP.getAppConfig().getRequest_user()+"get.mvc/setSinger";
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
