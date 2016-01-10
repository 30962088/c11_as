package com.cctv.xiqu.android.fragment.network;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.Header;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.TextUtils;



import com.cctv.xiqu.android.APP;
import com.cctv.xiqu.android.adapter.BBSListAdapter;
import com.cctv.xiqu.android.adapter.WeiboListAdapter;
import com.cctv.xiqu.android.widget.WeiboItemView.Content;
import com.cctv.xiqu.android.widget.WeiboItemView.Count;
import com.cctv.xiqu.android.widget.WeiboItemView.Model;
import com.cctv.xiqu.android.widget.WeiboItemView.Photo;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;

public class GetTopicRequest extends BaseClient {

	public static class Topic implements Serializable{
		private String topicid;
		private String userid;
		private String username;
		private String topictitle;
		private String topiccontent;
		private String datetime;
		private String commentid;
		private String userimgguid;
		private String userimgformat;
		private String uploadimgguid;
		private String uploadimgurl;
		private String uploadimgformat;
		private String userimgurl;
		private int totopstatus;
		private int commentcount;
		private String colstatus;
		public BBSListAdapter.Model toModel(){
			return new BBSListAdapter.Model(topicid,topictitle,topiccontent,userimgurl, username, getDate(), commentcount,userid,commentid,getUploadImg(),totopstatus==1?true:false);
		}
		
		private String getUploadImg(){
			
			return uploadimgurl;
		}
		
		private Date getDate(){
			long count = Long.parseLong(datetime.replaceAll("\\/Date\\((.*)\\)\\/", "$1"));
			return new Date(count);
		}
		
	}
	public static class Result{
		private List<Topic> topiclist;
		
		public List<BBSListAdapter.Model> toModels(){
			List<BBSListAdapter.Model> list = new ArrayList<BBSListAdapter.Model>();
			for(Topic topic : topiclist){
				list.add(topic.toModel());
			}
			return list;
		}
		
		
	}

	public static class Params {
		private int pageno;
		private int pagesize;
		private String sid;
		private Integer type;

		public Params(int pageno, int pagesize) {
			super();
			this.pageno = pageno;
			this.pagesize = pagesize;
		}

		public Params(int pageno, int pagesize, String sid, Integer type) {
			super();
			this.pageno = pageno;
			this.pagesize = pagesize;
			this.sid = sid;
			this.type = type;
		}
		
		

	}

	

	private Params params;

	public GetTopicRequest(Context context, Params params) {
		super(context);
		this.params = params;
	}

	@Override
	public Object onSuccess(String str) {
		return new Gson().fromJson(str, Result.class);

	}

	@Override
	protected RequestParams getParams() {
		RequestParams params = new RequestParams();
		params.add("method", "topic");
		params.add("pageno", "" + this.params.pageno);
		params.add("pagesize", "" + this.params.pagesize);
		if(this.params.sid != null){
			params.add("sid", "" + this.params.sid);
		}
		if(this.params.type != null){
			params.add("type", "" + this.params.type);
		}
		try {
			PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			params.add("versiontype", ""+pInfo.versionCode);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return params;
	}

	@Override
	protected String getURL() {
		// TODO Auto-generated method stub
		return APP.getAppConfig().getRequest_news()+"cctv11/topic";
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
