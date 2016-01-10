package com.cctv.xiqu.android.adapter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.cctv.xiqu.android.VideoCommentActivity;
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

public class VideoListAdapter extends BaseAdapter implements Serializable,PinnedSectionListAdapter {

	public enum Status{
		DOWNLOAD,OPEN
	}
	public static class Model implements Serializable {
		private String id;
		private int comment;
		private String img;
		private String name;
		private String url;
		public Model(String id, int comment, String img, String name, String url) {
			super();
			this.id = id;
			this.comment = comment;
			this.img = img;
			this.name = name;
			this.url = url;
		}
		public VideoCommentActivity.Model toCommentModel(){
			return new VideoCommentActivity.Model(id, comment, name, img, url);
		}
		
		
		
	}
	public static interface OnBtnClickListener{
		public void onBtnClick(Model model);
	}

	private Context context;

	private List<Model> list;
	
	

	public VideoListAdapter(Context context, List<Model> list) {
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
					R.layout.video_item, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.name.setText(model.name);
		ImageLoader.getInstance().displayImage(model.img, holder.img,DisplayOptions.IMG.getOptions());
		
		return convertView;
	}

	public static class ViewHolder {

		private ImageView img;

		private TextView name;

		public ViewHolder(View view) {
			img = (ImageView) view.findViewById(R.id.img);
			name = (TextView) view.findViewById(R.id.name);
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
