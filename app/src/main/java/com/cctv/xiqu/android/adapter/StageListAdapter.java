package com.cctv.xiqu.android.adapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.cctv.xiqu.android.adapter.CalendarGridAdapter.ViewHolder;
import com.hb.views.PinnedSectionListView.PinnedSectionListAdapter;

import com.cctv.xiqu.android.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;

import android.widget.TextView;

public class StageListAdapter extends BaseAdapter implements
		PinnedSectionListAdapter {

	public static class StageItem implements Serializable {

		private String name;

		private String link;

		private String loc;

		private String time;

		public StageItem(String name, String link, String loc, String time) {
			super();
			this.name = name;
			this.link = link;
			this.loc = loc;
			this.time = time;
		}
		public String getLink() {
			return link;
		}

	}

	private Context context;

	private List<StageItem> list;

	public StageListAdapter(Context context, List<StageItem> list) {
		super();
		this.context = context;
		this.list = list;
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

		StageViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.stage_item, null);
			holder = new StageViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (StageViewHolder) convertView.getTag();
		}

		StageItem item = (StageItem) list.get(position);

		holder.name.setText(item.name);

		holder.link.setText(item.link);

		holder.loc.setText(item.loc);

		holder.time.setText(item.time);

		return convertView;
	}

	public static class StageViewHolder {

		private TextView name;

		private TextView link;

		private TextView loc;

		private TextView time;

		public StageViewHolder(View view) {
			name = (TextView) view.findViewById(R.id.name);
			link = (TextView) view.findViewById(R.id.link);
			loc = (TextView) view.findViewById(R.id.loc);
			time = (TextView) view.findViewById(R.id.time);
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
