package com.cctv.xiqu.android.fragment.network;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;


import com.cctv.xiqu.android.APP;
import com.cctv.xiqu.android.adapter.VoteListAdapter;
import com.cctv.xiqu.android.utils.DateUtils;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;

import android.content.Context;

public class GetVoteListRequest extends BaseClient{

	public static class Params{
		private int pageno;
		private int pagesize;
		public Params(int pageno, int pagesize) {
			super();
			this.pageno = pageno;
			this.pagesize = pagesize;
		}
		
	}
	
	public static class Attachment{
		private String attachmentimgurl;
	}
	
	public static class Vote{
		private String voteid;
		private String votetitle;
		private String datetime;
		private String endtime;
		private boolean isover;
		private int voteusercount;
		private String votecontent;
		private Attachment attachment;
		public VoteListAdapter.Model toModel(){
			return new VoteListAdapter.Model(attachment.attachmentimgurl, 
					votetitle, DateUtils.getDate(datetime), voteusercount,APP.getAppConfig().getRequest_news()+ "votepage/index?voteid="+voteid);
		}
		
		
	}
	
	public static class Result{
		private List<Vote> votelist;
		
		public List<VoteListAdapter.Model> toList(){
			List<VoteListAdapter.Model> models = new ArrayList<VoteListAdapter.Model>();
			for(Vote vote : votelist){
				models.add(vote.toModel());
			}
			return models;
		}
		
	}
	
	private Params params;

	public GetVoteListRequest(Context context, Params params) {
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
		params.add("method", "vote");
		params.add("pageno", ""+this.params.pageno);
		params.add("pagesize", ""+this.params.pagesize);
		return params;
	}

	@Override
	protected String getURL() {
		// TODO Auto-generated method stub
		return APP.getAppConfig().getRequest_news()+"cctv11/vote";
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
