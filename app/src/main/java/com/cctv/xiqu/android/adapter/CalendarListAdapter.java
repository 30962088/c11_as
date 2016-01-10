package com.cctv.xiqu.android.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.cctv.xiqu.android.utils.CalendarUtils.CalendarDate;
import com.mengle.lib.utils.Utils;

import com.cctv.xiqu.android.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

public class CalendarListAdapter extends BaseAdapter{
	public static interface OnCalendarGridItemClickListener{
		public void OnCalendarGridItemClick(Date date);
	}
	
	
	public static class Model{
		private Date date;
		private List<CalendarDate> list;
		public Model(Date date, List<CalendarDate> list) {
			super();
			this.date = date;
			this.list = list;
		}
		public String getDateString(){
			return new SimpleDateFormat("yyyy年MM月").format(date);
		}
	}
	
	private Context context;
	
	
	private List<Model> list;
	
	private OnCalendarGridItemClickListener calendarGridItemClickListener;
	

	

	public CalendarListAdapter(Context context, List<Model> list,
			OnCalendarGridItemClickListener calendarGridItemClickListener) {
		super();
		this.context = context;
		this.list = list;
		this.calendarGridItemClickListener = calendarGridItemClickListener;
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
	
	
	private CalendarDate last;
	
	public void setLast(CalendarDate last) {
		this.last = last;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		final Model model = list.get(position);
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.calendar_layout, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.title.setText(model.getDateString());
		holder.gridview.setAdapter(new CalendarGridAdapter(context, model.list));
		
		holder.gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				CalendarDate calendarDate = model.list.get(position);
				if(calendarDate.isEnable()){
					
					calendarGridItemClickListener.OnCalendarGridItemClick(calendarDate.getDate());
					if(last != null){
						last.setSelected(false);
					}
					last = calendarDate;
					calendarDate.setSelected(true);
					notifyDataSetChanged();
				}
				
				
			}
			
		});
		
//		Utils.setGridViewHeightBasedOnChildren(holder.gridview);
		
		return convertView;
	}
	
	public static class ViewHolder {

		private TextView title;
		
		private GridView gridview;

		public ViewHolder(View view) {
			title = (TextView) view.findViewById(R.id.title);
			gridview = (GridView) view.findViewById(R.id.gridview);
		}

	}

	
	
}
