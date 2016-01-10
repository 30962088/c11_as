package com.cctv.xiqu.android;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;

import com.cctv.xiqu.android.adapter.StageListAdapter;
import com.cctv.xiqu.android.adapter.TabsAdapter;
import com.cctv.xiqu.android.adapter.StageListAdapter.StageItem;
import com.cctv.xiqu.android.adapter.TabsAdapter.Pager;
import com.cctv.xiqu.android.fragment.StageFragment;
import com.cctv.xiqu.android.fragment.network.StageRequest.Result;
import com.cctv.xiqu.android.fragment.network.StageRequest.StageGroup;
import com.cctv.xiqu.android.widget.HIndicator;

import com.cctv.xiqu.android.R;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;

public class StageActivity extends BaseActivity implements OnClickListener{

	public static class Model implements Serializable {
		private Date start;
		private Date end;
		private Result result;
		private Date selected;
		public Model(Date start, Date end, Result result, Date selected) {
			super();
			this.start = start;
			this.end = end;
			this.result = result;
			this.selected = selected;
		}

		

	}

	public static void open(Context context, Model model) {

		Intent intent = new Intent(context, StageActivity.class);

		intent.putExtra("model", model);

		context.startActivity(intent);

	}

	private Model model;

	private ViewPager pager;

	private HIndicator indicator;
	

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		model = (Model) getIntent().getSerializableExtra("model");
		setContentView(R.layout.stage_layout);
		findViewById(R.id.back).setOnClickListener(this);
		pager = (ViewPager) findViewById(R.id.pager);
		indicator = (HIndicator) findViewById(R.id.indicator);
		init();
	}

	private void init(){
		
		Date start = model.start,end = model.end;
		int length = (int) ((end.getTime() -start.getTime())/(1000*3600*24));
		List<Pager> pagers = new ArrayList<Pager>();
		int position = 0;
		for(int i = 0;i<length;i++){
			
			Calendar c = DateUtils.toCalendar(start);
			c.setTime(start);
			c.add(Calendar.DATE, i);
			Date date = c.getTime();
			if(DateUtils.isSameDay(date, model.selected)){
				position = i;
			}
			ArrayList<StageItem> list = new ArrayList<StageItem>();
			for(StageGroup group: model.result.getGrouplist()){
				if(DateUtils.isSameDay(date, group.getDate())){
					list.addAll(group.toList());
					break;
				}
			}
			pagers.add(new Pager(com.cctv.xiqu.android.utils.DateUtils.getPageTitle(date)
					, StageFragment.newInstance(new StageFragment.Model(date, list))));
			
		}
		pager.setAdapter(new TabsAdapter(getSupportFragmentManager(), pagers));
		indicator.setViewPager(pager);
		pager.setCurrentItem(position);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;

		default:
			break;
		}
		
	}
}
