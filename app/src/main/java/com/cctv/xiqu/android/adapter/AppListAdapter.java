package com.cctv.xiqu.android.adapter;

import java.io.Serializable;
import java.util.List;

import com.cctv.xiqu.android.APP.DisplayOptions;
import com.hb.views.PinnedSectionListView.PinnedSectionListAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import com.cctv.xiqu.android.R;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import android.widget.TextView;

public class AppListAdapter extends BaseAdapter implements Serializable,PinnedSectionListAdapter {

	public enum Status{
		DOWNLOAD(R.drawable.border_red,"下载",Color.parseColor("#4b5187")),
		INSTALLOPEN(R.drawable.border_pink,"打开",Color.parseColor("#ef3d23")),
		OPEN(R.drawable.border_pink,"打开",Color.parseColor("#ef3d23"));
		
		private int background;
		private String text;
		private int textColor;
		private Status(int background, String text, int textColor) {
			this.background = background;
			this.text = text;
			this.textColor = textColor;
		}
		public int getBackground() {
			return background;
		}
		public String getText() {
			return text;
		}
		public int getTextColor() {
			return textColor;
		}
	}
	
	public static class Model implements Serializable {
		private String img;
		private String title;
		private String desc;
		private Status status;
		public Model(String img, String title, String desc, Status status) {
			super();
			this.img = img;
			this.title = title;
			this.desc = desc;
			this.status = status;
		}
		
	}
	public static interface OnBtnClickListener{
		public void onBtnClick(Model model);
	}

	private Context context;

	private List<Model> list;
	
	

	public AppListAdapter(Context context, List<Model> list) {
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
					R.layout.app_item, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.title.setText(model.title);
		holder.desc.setText(model.desc);
		ImageLoader.getInstance().displayImage(model.img, holder.img,DisplayOptions.IMG.getOptions());
		holder.btn.setBackgroundResource(model.status.getBackground());
		holder.btn.setText(model.status.getText());
		holder.btn.setTextColor(model.status.getTextColor());
		
		return convertView;
	}

	public static class ViewHolder {

		private ImageView img;

		private TextView title;
		
		private TextView desc;
		
		private TextView btn;

		public ViewHolder(View view) {
			img = (ImageView) view.findViewById(R.id.img);
			title = (TextView) view.findViewById(R.id.title);
			desc = (TextView) view.findViewById(R.id.desc);
			btn = (TextView) view.findViewById(R.id.btn);
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
