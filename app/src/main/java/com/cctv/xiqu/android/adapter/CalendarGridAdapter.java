package com.cctv.xiqu.android.adapter;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;

import com.cctv.xiqu.android.utils.CalendarUtils.CalendarDate;

import com.cctv.xiqu.android.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CalendarGridAdapter extends BaseAdapter{


	
	private Context context;
	
	private List<CalendarDate> list;
	

	

	public CalendarGridAdapter(Context context, List<CalendarDate> list) {
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
		final CalendarDate model = list.get(position);
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.day_item, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.day.setText(""+model.getDay());
		holder.day.setEnabled(model.isEnable());
		
		
		if(model.getTotal() > 0){
			holder.popup.setVisibility(View.VISIBLE);
			holder.popup.setText(""+model.getTotal());
		}else{
			holder.popup.setVisibility(View.GONE);
		}
		
		holder.selected.setVisibility(model.isSelected()&&model.isEnable()?View.VISIBLE:View.GONE);
		holder.selected2.setVisibility(View.GONE);
		if(holder.selected.getVisibility() != View.VISIBLE){
			if(DateUtils.isSameDay(model.getDate(), new Date())&&model.isEnable()){
				holder.selected2.setVisibility(View.VISIBLE);
			}
		}
		
		return convertView;
	}
	
	public static class ViewHolder {

		private TextView day;

		private TextView popup;
		
		private View selected;
		
		private View selected2;

		public ViewHolder(View view) {
			day = (TextView) view.findViewById(R.id.day);
			popup = (TextView) view.findViewById(R.id.popup);
			selected =  view.findViewById(R.id.selected);
			selected2 =  view.findViewById(R.id.selected2);
		}

	}

	
	
}
