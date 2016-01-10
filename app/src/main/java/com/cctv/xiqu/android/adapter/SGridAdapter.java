package com.cctv.xiqu.android.adapter;

import java.io.Serializable;
import java.util.List;

import com.hb.views.PinnedSectionListView.PinnedSectionListAdapter;

import com.cctv.xiqu.android.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import android.widget.TextView;

public class SGridAdapter extends BaseAdapter implements Serializable,PinnedSectionListAdapter {

	
	public static class Model implements Serializable {
		private int icon;
		private String label;
		public Model(int icon, String label) {
			super();
			this.icon = icon;
			this.label = label;
		}
		
		
		
	}
	
	private Context context;

	private List<Model> list;
	
	

	public SGridAdapter(Context context, List<Model> list) {
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
					R.layout.s_item, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.icon.setImageResource(model.icon);
		
		holder.label.setText(model.label);
		
		return convertView;
	}

	public static class ViewHolder {

		private ImageView icon;
		
		private TextView label;
		
		public ViewHolder(View view) {
			icon = (ImageView) view.findViewById(R.id.icon);
			label = (TextView) view.findViewById(R.id.label);
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
