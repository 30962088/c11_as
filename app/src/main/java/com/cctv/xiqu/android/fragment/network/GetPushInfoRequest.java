package com.cctv.xiqu.android.fragment.network;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.Header;


import com.cctv.xiqu.android.APP;
import com.cctv.xiqu.android.adapter.InfoListAdapter.InfoGroup;
import com.cctv.xiqu.android.adapter.InfoListAdapter.InfoItem;
import com.cctv.xiqu.android.utils.DateUtils;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;

import android.content.Context;

public class GetPushInfoRequest extends BaseClient{

	public static class PushInfo{
		private String pid;
		private String title;
		private String description;
		private String datetime;
		private String sendtime;
		private String colstatus;
		public Date getDatetime() {
			return DateUtils.getDate(datetime);
		}
		
		public InfoItem toItem(){
			return new InfoItem(title, description,getDatetime());
		}
	}
	
	private static Result result;
	
	public static Result getResult() {
		return result;
	}
	
	public static class Result{
		private int result;
		private List<PushInfo> pushinfolist;
		public int getResult() {
			return result;
		}
		public List<PushInfo> getPushinfolist() {
			return pushinfolist;
		}
		
		public List<InfoGroup> toInfoGroups(){
			List<InfoGroup> list = new ArrayList<InfoGroup>();
			Date last = null;
			InfoGroup group = null;
			for(PushInfo info : pushinfolist){
				if(last == null || !org.apache.commons.lang3.time.DateUtils.isSameDay(last, info.getDatetime())){
					last = info.getDatetime();
					group = new InfoGroup(last, new ArrayList<InfoItem>());
					list.add(group);
				}
				group.getList().add(info.toItem());
			}
			return list;
		}
		
	}
	
	public GetPushInfoRequest(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object onSuccess(String str) {
		result = new Gson().fromJson(str, Result.class); 
		return result;
	}

	@Override
	protected RequestParams getParams() {
		RequestParams params = new RequestParams();
		params.add("method", "pushinfo");
		return params;
	}

	@Override
	protected String getURL() {
		// TODO Auto-generated method stub
		return APP.getAppConfig().getRequest_news()+"/cctv11/pushinfo";
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
