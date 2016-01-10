package com.cctv.xiqu.android.adapter;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hb.views.PinnedSectionListView.PinnedSectionListAdapter;
import com.mengle.lib.utils.Utils;

import com.cctv.xiqu.android.R;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;

import android.widget.TextView;

public class InfoListAdapter extends BaseAdapter implements
		PinnedSectionListAdapter{

	public static final int ITEM = 0;
	
	public static final int SECTION = 1;
	
	public static class InfoItem implements Serializable {

		private String title;

		private String desc;
		
		private Date date;
		
		private int bg;
		
		private boolean sep;
		
		private int marginTop;

		public InfoItem(String title, String desc,Date date) {
			super();
			this.title = title;
			this.desc = desc;
			this.date = date;
		}
		public String getTitle() {
			return title;
		}
		public Date getDate() {
			return date;
		}
		public String getDesc() {
			return desc;
		}
		

		

	}

	public static class DateItem {
		private String date;
		private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy/MM/dd");
		public DateItem(Date date) {
			super();
			this.date = FORMAT.format(date);
		}

	}

	public static class InfoGroup {
		private Date date;
		private List<InfoItem> list;

		public InfoGroup(Date date, List<InfoItem> list) {
			super();
			this.date = date;
			this.list = list;
		}
		
		public List<InfoItem> getList() {
			return list;
		}

	}
	
	public static interface OnInfoClickListener{
		public void oninfoclick(InfoItem item);
	}

	private Context context;

	private List<InfoGroup> list;

	private List<Object> datas;

	private OnInfoClickListener onInfoClickListener;
	
	public InfoListAdapter(Context context, List<InfoGroup> list,OnInfoClickListener onInfoClickListener) {
		super();
		this.context = context;
		this.list = list;
		this.onInfoClickListener = onInfoClickListener;
		this.datas = getDatas();

	}

	private List<Object> getDatas() {
		List<Object> datas = new ArrayList<Object>();
		for (InfoGroup group : list) {
			datas.add(new DateItem(group.date));
			for(int i = 0;i<group.list.size();i++){
				InfoItem item = group.list.get(i);
				if(group.list.size() == 1){
					item.sep = false;
					item.bg = R.drawable.i_t;
					item.marginTop = Utils.dpToPx(context, 18);
				}else{
					if(i == 0){
						item.marginTop = Utils.dpToPx(context, 18);
						item.sep = true;
						item.bg = R.drawable.i_t_1;
					}else if(i>0&&i<list.size()-1){
						item.sep = true;
						item.bg = R.drawable.i_t_2;
					}else{
						item.sep = false;
						item.bg = R.drawable.i_t_3;
					}
				}
				datas.add(item);
				
			}
		}
		return datas;
	}

	@Override
	public int getCount() {

		return datas.size();
	}

	@Override
	public Object getItem(int position) {

		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null; 
		if(convertView == null){
			holder = new ViewHolder();
			View stageview = LayoutInflater.from(context).inflate(
					R.layout.info_item, null);
			holder.stageViewHolder = new StageViewHolder(stageview);
			holder.stageViewHolder.container.setTag(holder);
			View headerview = LayoutInflater.from(context).inflate(
					R.layout.info_bottom, null);
			holder.headerViewHolder = new HeaderViewHolder(headerview);
			holder.headerViewHolder.container.setTag(holder);
			if(isItemViewTypePinned(getItemViewType(position))){
				convertView = holder.headerViewHolder.container;
			}else{
				convertView = holder.stageViewHolder.container;
			}
			
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		if(isItemViewTypePinned(getItemViewType(position))){
			
			DateItem item = (DateItem) datas.get(position);
			
			holder.headerViewHolder.text.setText(item.date);
			
		}else{
			final InfoItem item = (InfoItem) datas.get(position);
			
			
			
			holder.stageViewHolder.title.setText(item.title);

			holder.stageViewHolder.desc.setText(item.desc);

			holder.stageViewHolder.container1.setBackgroundResource(item.bg);
			
			holder.stageViewHolder.container1.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					onInfoClickListener.oninfoclick(item);
					
				}
			});
			
			/*FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) holder.stageViewHolder.container1.getLayoutParams();
			
			params.topMargin = item.marginTop;
			
			holder.stageViewHolder.container1.setLayoutParams(params);*/
			
			holder.stageViewHolder.sep.setVisibility(item.sep?View.VISIBLE:View.GONE);
			
			

		}

		return convertView;
	}
	
	public static class ViewHolder{
		private StageViewHolder stageViewHolder;
		private HeaderViewHolder headerViewHolder;
		
	}

	public static class StageViewHolder {

		private TextView title;

		private TextView desc;
		
		private View container;
		private View sep;
		
		private View container1;
		public StageViewHolder(View view) {
			container = view;
			sep = view.findViewById(R.id.sep);
			container1 = view.findViewById(R.id.container);
			title = (TextView) view.findViewById(R.id.title);
			desc = (TextView) view.findViewById(R.id.desc);
			
		}

	}

	public static class HeaderViewHolder {
		private TextView text;
		private View container;
		public HeaderViewHolder(View view) {
			container = view;
			text = (TextView) view.findViewById(R.id.text);
		}

	}

	@Override
	public boolean isItemViewTypePinned(int viewType) {
		// TODO Auto-generated method stub
		return viewType == SECTION;
	}
	
	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 2;
	}
	@Override
	public int getItemViewType(int position) {
		Object object = datas.get(position);
		int type = 0;
		if(object instanceof DateItem){
			type = SECTION;
		}else{
			type = ITEM;
		}
		return type;
	}
}

