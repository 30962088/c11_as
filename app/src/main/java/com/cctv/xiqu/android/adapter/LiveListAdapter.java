package com.cctv.xiqu.android.adapter;

import java.io.Serializable;
import java.util.List;

import com.cctv.xiqu.android.APP.DisplayOptions;
import com.cctv.xiqu.android.SpecialDetailActivity.Params;
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

public class LiveListAdapter extends BaseAdapter implements Serializable,PinnedSectionListAdapter {

	public static class Model implements Serializable {

		public enum State {
			CURRENT, OUTTIME, INTIME
		}

		private String time;
		private String name;
		private State state;

		public Model(String time, String name, State state) {
			super();
			this.time = time;
			this.name = name;
			this.state = state;
		}
		public State getState() {
			return state;
		}

	}

	private Context context;

	private List<Model> list;

	public LiveListAdapter(Context context, List<Model> list) {
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
					R.layout.live_item, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}



		holder.name.setText(model.name);

		holder.time.setText(model.time);

		switch (model.state) {
		case CURRENT:
			holder.container.setSelected(true);
			holder.time.setEnabled(true);
			holder.name.setEnabled(true);
			break;
		case OUTTIME:
			holder.container.setSelected(false);
			holder.time.setEnabled(false);
			holder.name.setEnabled(false);
			break;
		case INTIME:
			holder.container.setSelected(false);
			holder.time.setEnabled(true);
			holder.name.setEnabled(true);
			break;
		default:
			break;
		}

		return convertView;
	}

	public static class ViewHolder {

		private TextView time;

		private TextView name;
		
		private View container;

		public ViewHolder(View view) {
			time = (TextView) view.findViewById(R.id.time);
			name = (TextView) view.findViewById(R.id.name);
			container =  view.findViewById(R.id.container);
		}

	}

	@Override
	public boolean isItemViewTypePinned(int viewType) {
		// TODO Auto-generated method stub
		return false;
	}

}
