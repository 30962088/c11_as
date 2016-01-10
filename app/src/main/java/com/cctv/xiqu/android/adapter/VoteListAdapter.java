package com.cctv.xiqu.android.adapter;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.cctv.xiqu.android.APP.DisplayOptions;
import com.cctv.xiqu.android.SpecialDetailActivity.Params;
import com.cctv.xiqu.android.utils.RelativeDateFormat;
import com.cctv.xiqu.android.widget.BBSDetailHeaderView;
import com.hb.views.PinnedSectionListView.PinnedSectionListAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import com.cctv.xiqu.android.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import android.widget.TextView;

public class VoteListAdapter extends BaseAdapter implements Serializable,PinnedSectionListAdapter {

	public enum Status{
		DOWNLOAD,OPEN
	}
	public static class Model implements Serializable {
		private String img;
		private String title;
		private String date;
		private int count;
		private String url;
		private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		public Model(String img, String title, Date date, int count,String url) {
			super();
			this.img = img;
			this.title = title;
			this.date = format.format(date);
			this.count = count;
			this.url = url;
		}
		public String getImg() {
			return img;
		}
		public String getUrl() {
			return url;
		}
		public String getTitle() {
			return title;
		}
		
		
	}
	public static interface OnBtnClickListener{
		public void onBtnClick(Model model);
	}

	private Context context;

	private List<Model> list;
	
	

	public VoteListAdapter(Context context, List<Model> list) {
		super();
		this.context = context;
		this.list = list;
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
		final Model model = list.get(position);
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.vote_item, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.title.setText(model.title);
		holder.date.setText(model.date);
		holder.count.setText(model.count+"人参与");
		ImageLoader.getInstance().displayImage(model.img, holder.img,DisplayOptions.IMG.getOptions());
		
		
		return convertView;
	}

	public static class ViewHolder {

		private ImageView img;

		private TextView title;
		
		private TextView date;
		
		private TextView count;
		
		public ViewHolder(View view) {
			img = (ImageView) view.findViewById(R.id.img);
			title = (TextView) view.findViewById(R.id.title);
			date = (TextView) view.findViewById(R.id.date);
			count = (TextView) view.findViewById(R.id.count);
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
