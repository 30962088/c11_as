package com.cctv.xiqu.android.adapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.cctv.xiqu.android.APP;
import com.cctv.xiqu.android.APP.DisplayOptions;
import com.cctv.xiqu.android.adapter.BBSListAdapter.ViewHolder;
import com.cctv.xiqu.android.fragment.network.InsertcommentreportRequest;
import com.cctv.xiqu.android.fragment.network.BaseClient.RequestHandler;
import com.cctv.xiqu.android.fragment.network.InsertcommentreportRequest.Params;
import com.cctv.xiqu.android.widget.IOSPopupWindow;
import com.hb.views.PinnedSectionListView.PinnedSectionListAdapter;
import com.mengle.lib.utils.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;

import com.cctv.xiqu.android.R;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;

import android.widget.TextView;

public class NewsCommentListAdapter extends BaseAdapter implements PinnedSectionListAdapter {


	public static class Model implements Serializable {

		private String id;
		
		private String avatar;

		private String name;

		private String content;

		private String time;
		
		private String userid;
		
		private int reportType;

		public Model(String id,String avatar, String name, String content, String time,
				String replynickname,String userid,int reportType) {
			super();
			this.id = id;
			this.avatar = avatar;
			this.name = name;
			this.content = content;
			this.time = time;
			this.userid = userid;
			this.reportType = reportType;
			if(!TextUtils.isEmpty(replynickname)){
				this.content = "回复<font color='#0b92c3'>"+replynickname+"</font>"+" "+this.content;
			}
			
		}
		public String getId() {
			return id;
		}
		
		public String getUserid() {
			return userid;
		}

		public int getReportType() {
			return reportType;
		}
		
		public String getAvatar() {
			return avatar;
		}
		public String getContent() {
			return content;
		}
		public String getName() {
			return name;
		}
		public String getTime() {
			return time;
		}
		

	}

	

	public static interface OnCommentBtnClickListener{
		public void onCommentBtnClick(Model model);
	}

	private Context context;

	private List<Model> list;
	
	private OnCommentBtnClickListener onCommentBtnClickListener;

	
	public NewsCommentListAdapter(Context context, List<Model> list,OnCommentBtnClickListener onCommentBtnClickListener) {
		super();
		this.context = context;
		this.list = list;
		this.onCommentBtnClickListener = onCommentBtnClickListener;
	}

	@Override
	public int getCount() {

		return list.size();
	}

	@Override
	public Object getItem(int position) {

		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final Model model = list.get(position);
		
		ViewHolder holder = null;
		
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.new_comment_item, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
			
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.content.setText(Html.fromHtml(model.content));
		
		holder.name.setText(model.name);
		
		holder.time.setText(model.time);
		
		holder.commentBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onCommentBtnClickListener.onCommentBtnClick(model);
				
			}
		});
		
		ImageLoader.getInstance().displayImage(model.avatar, holder.avatar,DisplayOptions.IMG.getOptions());
		
		holder.jubaoBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onjubao(model);
				
			}

			
		});

		return convertView;
	}
	
	
	private void onjubao(final Model model) {
		new IOSPopupWindow(context, new IOSPopupWindow.Params("请输入举报理由", new ArrayList<String>(){{
			add("营销诈骗");
			add("淫秽色情");
			add("地域攻击");
			add("其他理由");
		}},new IOSPopupWindow.OnIOSItemClickListener(){

			@Override
			public void oniositemclick(int pos, String text) {
				InsertcommentreportRequest request = new InsertcommentreportRequest(context,
						new InsertcommentreportRequest.Params(text, model.id, model.reportType, APP.getSession().getSid(), APP.getSession().getPkey()));
				request.request(new RequestHandler() {
					
					@Override
					public void onSuccess(Object object) {
						Utils.tip(context, "举报成功");
						
					}
					
					@Override
					public void onError(int error, String msg) {
						Utils.tip(context, "举报失败");
						
					}
					
					@Override
					public void onComplete() {
						// TODO Auto-generated method stub
						
					}
				});
			}
			
		}));
		
	}
	

	public static class ViewHolder {

		private ImageView avatar;

		private TextView name;

		private TextView content;

		private TextView time;
		
		private View commentBtn;
		
		private View jubaoBtn;

		public ViewHolder(View view) {
			name = (TextView) view.findViewById(R.id.name);
			avatar = (ImageView) view.findViewById(R.id.avatar);
			content = (TextView) view.findViewById(R.id.content);
			time = (TextView) view.findViewById(R.id.time);
			commentBtn = view.findViewById(R.id.comment_btn);
			jubaoBtn = view.findViewById(R.id.jubao_btn);
		}

	}

	
	

	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public boolean isItemViewTypePinned(int viewType) {
		// TODO Auto-generated method stub
		return false;
	}

}
