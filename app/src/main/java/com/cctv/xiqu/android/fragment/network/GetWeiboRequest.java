package com.cctv.xiqu.android.fragment.network;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.Header;

import android.content.Context;



import com.cctv.xiqu.android.APP;
import com.cctv.xiqu.android.adapter.WeiboListAdapter;
import com.cctv.xiqu.android.widget.WeiboItemView.Content;
import com.cctv.xiqu.android.widget.WeiboItemView.Count;
import com.cctv.xiqu.android.widget.WeiboItemView.Model;
import com.cctv.xiqu.android.widget.WeiboItemView.Photo;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;

public class GetWeiboRequest extends BaseClient {

	public static class Weibo implements Serializable{
		private String weibouserid;
		private String weibocontentid;
		private String created_at;
		private String id;
		private String text;
		private String thumbnail_pic;
		private String bmiddle_pic;
		private String original_pic;
		private int reposts_count;
		private int comments_count;
		private String mid;
		private boolean favorited;
		private String source;
		private User user;
		private Weibo retweeted_status;
		private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
				"EEE MMM dd HH:mm:ss Z yyyy");
		private static final SimpleDateFormat DATE_FORMAT2 = new SimpleDateFormat(
				"HH:mm");

		public Content toContent() {
			ArrayList<Photo> list = new ArrayList<Photo>() {
				{
					add(new Photo(bmiddle_pic,original_pic));
				}
			};
			return new Content(text, list,new Count(reposts_count, comments_count));
		}

		public Model toModel() throws ParseException {
//			Date date = DATE_FORMAT.parse(created_at);
			Date date = new Date();
			Content retweetedContent = null;
			if(retweeted_status != null){
				retweetedContent = retweeted_status.toContent();
				
			}
			return new Model(id,user.profile_image_url, user.name,
					DATE_FORMAT2.format(date), toContent(),
					retweetedContent);
		}
	}

	public static class User implements Serializable{
		private String screen_name;
		private String name;
		private String description;
		private String profile_image_url;
		private String avatar_large;
	}

	public static class Params {
		private String weibouserid;
		private int pageno;
		private int pagesize;

		public Params(String weibouserid, int pageno, int pagesize) {
			super();
			this.weibouserid = weibouserid;
			this.pageno = pageno;
			this.pagesize = pagesize;
		}

	}

	public static class Result {
		private List<Weibo> statuses;
		public List<Model> toModelList(){
			List<Model> list = new ArrayList<Model>();
			for(Weibo weibo:statuses){
				try {
					list.add(weibo.toModel());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return list;
		}
	}

	private Params params;

	public GetWeiboRequest(Context context, Params params) {
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
		params.add("method", "getweiboinfo");
		params.add("weibouserid", this.params.weibouserid);
		params.add("pageno", "" + this.params.pageno);
		params.add("pagesize", "" + this.params.pagesize);
		return params;
	}

	@Override
	protected String getURL() {
		// TODO Auto-generated method stub
		return "http://xiqu.1du1du.com/weibo/cctv11/getweiboinfo";
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
