package com.cctv.xiqu.android.adapter;

import java.io.Serializable;
import java.util.List;

import com.cctv.xiqu.android.VideoCommentActivity;
import com.cctv.xiqu.android.APP.DisplayOptions;
import com.cctv.xiqu.android.SpecialDetailActivity.Params;
import com.hb.views.PinnedSectionListView.PinnedSectionListAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import com.cctv.xiqu.android.R;


import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import android.widget.TextView;

public class NewsListAdapter extends BaseAdapter implements Serializable,PinnedSectionListAdapter{

	public static class Model implements Serializable{

		public static class Category implements Serializable{
			public enum Background implements Serializable{
				GREEN(R.drawable.rect_green),PURPLE(R.drawable.rect_purple),RED(R.drawable.rect_red);
				private int background;
				Background(int background) {
					this.background = background;
				}
				
			}
			private Background background;
			private String text;
			public Category(Background background, String text) {
				super();
				this.background = background;
				this.text = text;
			}
			
			public String getText() {
				return text;
			}
			
			
		}
		
		private String id;

		private String img;

		private String title;

		private int comment;

		private boolean isNew;
		
		private Category category;
		
		private String subtitle;

		private boolean isZhuanlan;
		
		private String url;
		
		
		public VideoCommentActivity.Model toCommentModel(){
			return new VideoCommentActivity.Model(id, comment, title, img, url);
		}
		public Model(String id, String img, String title, int comment,
				boolean isNew, Category category, String subtitle,
				boolean isZhuanlan,String url) {
			super();
			this.id = id;
			this.img = img;
			this.title = title;
			this.comment = comment;
			this.isNew = isNew;
			this.category = category;
			this.subtitle = subtitle;
			this.isZhuanlan = isZhuanlan;
			this.url = url;
		}
		
		public Category getCategory() {
			return category;
		}

		public Params toDetailParams(){
			Params params = new Params(id, title, subtitle, img, comment);
			
			return params;
		}


		public boolean isZhuanlan() {
			return isZhuanlan;
		}
	
		
		
	}

	private Context context;

	private List<Model> list;
	
	private boolean showCategory;
	
	private boolean bold;

	public NewsListAdapter(Context context, List<Model> list,boolean showCategory,boolean bold) {
		super();
		this.context = context;
		this.list = list;
		this.showCategory = showCategory;
		this.bold = bold;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		Model model = list.get(position);
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.news_item, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		ImageLoader.getInstance().displayImage(model.img, holder.img,DisplayOptions.IMG.getOptions());
		if(bold){
			holder.title.setTypeface(null, Typeface.BOLD);
			holder.comment.setTypeface(null, Typeface.BOLD);
			holder.category.setTypeface(null, Typeface.BOLD);
		}
		holder.title.setText(model.title);
		
		holder.comment.setText("" + model.comment+"评论");

		holder.isNew.setVisibility(model.isNew ? View.VISIBLE : View.GONE);
		
		if(showCategory && model.category != null){
			holder.category.setVisibility(View.VISIBLE);
			holder.category.setText(model.category.text);
			holder.category.setBackgroundResource(model.category.background.background);
		}else{
			holder.category.setVisibility(View.GONE);
		}

		return convertView;
	}

	public static class ViewHolder {

		private ImageView img;

		private TextView title;

		private TextView comment;

		private View isNew;
		
		private TextView category;

		public ViewHolder(View view) {
			img = (ImageView) view.findViewById(R.id.img);
			title = (TextView) view.findViewById(R.id.title);
			comment = (TextView) view.findViewById(R.id.comment);
			isNew = view.findViewById(R.id.isNew);
			category = (TextView) view.findViewById(R.id.category);

		}

	}

	@Override
	public boolean isItemViewTypePinned(int viewType) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 2;
	}

}
