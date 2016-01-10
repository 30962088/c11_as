package com.cctv.xiqu.android.fragment.network;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.Header;

import android.content.Context;

import com.cctv.xiqu.android.APP;
import com.cctv.xiqu.android.adapter.StageListAdapter;
import com.cctv.xiqu.android.adapter.TabsAdapter;
import com.cctv.xiqu.android.adapter.StageListAdapter.StageItem;
import com.cctv.xiqu.android.adapter.TabsAdapter.Pager;
import com.cctv.xiqu.android.fragment.NewsFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.mengle.lib.utils.Utils;

public class StageRequest extends BaseClient {

	public static class DateCount{
		private Date date;
		private int count;
		public DateCount(Date date, int count) {
			super();
			this.date = date;
			this.count = count;
		}
		public int getCount() {
			return count;
		}
		public Date getDate() {
			return date;
		}
		
	}
	
	
	
	public static class Stage implements Serializable{
		private String stageid;
		private String stagetitle;
		private String stageurl;
		private String stagecity;
		private String stagetheater;
		private String endtime;
		private String starttime;
		public StageItem toModel(){
			return new StageItem(stagetitle, stageurl, stagecity+"-"+stagetheater, DATE_FORMAT.format(getStartDate()));
		}
		
		private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm");
		
		private Date getEndDate(){
			long count = Long.parseLong(endtime.replaceAll("\\/Date\\((.*)\\)\\/", "$1"));
			return new Date(count);
		}
		private Date getStartDate(){
			long count = Long.parseLong(starttime.replaceAll("\\/Date\\((.*)\\)\\/", "$1"));
			return new Date(count);
		}
	}
	
	public static class StageGroup implements Serializable{
		private int datecount;
		private String showdatetime;
		private ArrayList<Stage> stagelist;
		private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy年MM月dd日");
		
		public Date getDate(){
			long count = Long.parseLong(showdatetime.replaceAll("\\/Date\\((.*)\\)\\/", "$1"));
			return new Date(count);
		}
		
		public List<StageItem> toList(){
			List<StageItem> list = new ArrayList<StageItem>();
			for(Stage stage : stagelist){
				list.add(stage.toModel());
			}
			return list;
		}
		
	}

	public static class Result implements Serializable{
		private ArrayList<StageGroup> countlist;
		private int result;
		public int getResult() {
			return result;
		}
		public List<DateCount> getDateCounts(){
			List<DateCount> list = new ArrayList<DateCount>();
			for(StageGroup group : countlist){
				list.add(new DateCount(group.getDate(), group.datecount));
			}
			return list;
		}
		public ArrayList<StageGroup> getGrouplist() {
			return countlist;
		}
	}
	
	public static class Params{
		private Date start;
		private Date end;
		public Params(Date start, Date end) {
			super();
			this.start = start;
			this.end = end;
		}
		
	}
	
	private Params params;
	

	

	public StageRequest(Context context, Params params) {
		super(context);
		this.params = params;
	}

	@Override
	public Object onSuccess(String str) {
		return new Gson().fromJson(str, Result.class);

	}
	
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd");

	@Override
	protected RequestParams getParams() {
		RequestParams params = new RequestParams();
		params.add("method", "stagecount");
		params.add("startdate", DATE_FORMAT.format(this.params.start));
		params.add("enddate", DATE_FORMAT.format(this.params.end));
	/*	params.add("startdate", "2014/01/01");
		params.add("enddate", "2015/01/01");*/
		return params;
	}

	@Override
	protected String getURL() {
		// TODO Auto-generated method stub
		return APP.getAppConfig().getRequest_news()+"cctv11/stagecount";
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
