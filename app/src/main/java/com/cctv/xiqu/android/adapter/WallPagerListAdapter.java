package com.cctv.xiqu.android.adapter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.cctv.xiqu.android.WallPagerActivity;
import com.cctv.xiqu.android.APP.DisplayOptions;
import com.cctv.xiqu.android.SpecialDetailActivity.Params;
import com.cctv.xiqu.android.utils.RelativeDateFormat;
import com.hb.views.PinnedSectionListView.PinnedSectionListAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import com.cctv.xiqu.android.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import android.widget.TextView;

public class WallPagerListAdapter extends BaseAdapter implements Serializable,PinnedSectionListAdapter {

	public static class Model implements Serializable {

		private String img;
		private String name;
		private String size;
		private String origin;
		public Model(String img, String name, String size, String origin) {
			super();
			this.img = img;
			this.name = name;
			this.size = size;
			this.origin = origin;
		}
		
		public WallPagerActivity.Model toModel(){
			return new WallPagerActivity.Model(img, name, origin);
		}
		
		

	}

	private Context context;

	private List<Model> list;

	public WallPagerListAdapter(Context context, List<Model> list) {
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
		Model model = list.get(position);
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.wallpaper_item, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}



		holder.name.setText(model.name);

		holder.size.setText("大小:"+model.size);
		
		ImageLoader.getInstance().displayImage(model.img, holder.img, DisplayOptions.IMG.getOptions());
		
		
		return convertView;
	}

	public static class ViewHolder {

		private ImageView img ;

		private TextView name;
		
		private TextView size;

		public ViewHolder(View view) {
			img = (ImageView) view.findViewById(R.id.img);
			name = (TextView) view.findViewById(R.id.name);
			size =  (TextView) view.findViewById(R.id.size);
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
