package com.cctv.xiqu.android.fragment.network;

import org.apache.http.Header;


import com.cctv.xiqu.android.APP;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;

import android.content.Context;

public class SearchRequest extends BaseClient{

	public static class Params{
		private String contentstitle;
		private String categoryid;
		private int pageno;
		private int pagesize;
		public Params(String contentstitle, String categoryid, int pageno,
				int pagesize) {
			super();
			this.contentstitle = contentstitle;
			this.categoryid = categoryid;
			this.pageno = pageno;
			this.pagesize = pagesize;
		}
	}
	
	private Params params;

	public SearchRequest(Context context, Params params) {
		super(context);
		this.params = params;
	}

	@Override
	public Object onSuccess(String str) {
		// TODO Auto-generated method stub
		return new Gson().fromJson(str, ContentsRequest.Result.class);
	}

	@Override
	protected RequestParams getParams() {
		RequestParams params = new RequestParams();
		params.add("method", "searchcontentsbykeyword");
		params.add("contentstitle",""+this.params.contentstitle);
		params.add("categoryid",""+this.params.categoryid);
		params.add("pageno",""+this.params.pageno);
		params.add("pagesize",""+this.params.pagesize);
		return params;
	}

	@Override
	protected String getURL() {
		return APP.getAppConfig().getRequest_news()+"cctv11/searchcontentsbykeyword";
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
