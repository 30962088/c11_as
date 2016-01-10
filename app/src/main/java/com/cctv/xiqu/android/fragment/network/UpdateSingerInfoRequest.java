package com.cctv.xiqu.android.fragment.network;



import com.cctv.xiqu.android.APP;
import com.loopj.android.http.RequestParams;

import android.content.Context;

public class UpdateSingerInfoRequest extends BaseClient{

	public static class Singername{
		private String sid;
		private String name;
		public Singername(String sid, String name) {
			super();
			this.sid = sid;
			this.name = name;
		}
	}
	
	public static class Params{
		private String sid;
		private String singerimgguid;
		private String singerimgformat;
		private String address;
		private String singername;
		public Params(String sid, String singerimgguid, String singerimgformat) {
			super();
			this.sid = sid;
			this.singerimgguid = singerimgguid;
			this.singerimgformat = singerimgformat;
		}
		public Params(String sid, String address) {
			super();
			this.sid = sid;
			this.address = address;
		}
		public Params(Singername singername) {
			super();
			this.sid = singername.sid;
			this.singername = singername.name;
		}
		
		
	}
	
	
	private Params params;

	public UpdateSingerInfoRequest(Context context, Params params) {
		super(context);
		this.params = params;
	}

	@Override
	public Object onSuccess(String str) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	protected RequestParams getParams() {
		RequestParams params = new RequestParams();
		params.add("sid", this.params.sid);
		if(this.params.singerimgguid != null){
			params.add("singerimgguid", ""+this.params.singerimgguid);
		}
		if(this.params.singerimgformat != null){
			params.add("singerimgformat", this.params.singerimgformat);
		}
		if(this.params.address != null){
			params.add("address", this.params.address);
		}
		if(this.params.singername != null){
			params.add("singername", this.params.singername);
		}
		params.add("appid", "1217");
		
		
		return params;
	}

	@Override
	protected String getURL() {
		// TODO Auto-generated method stub
		return APP.getAppConfig().getRequest_user()+"get.mvc/updateSingerInfo";
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
