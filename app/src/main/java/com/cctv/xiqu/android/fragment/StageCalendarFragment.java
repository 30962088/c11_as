package com.cctv.xiqu.android.fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.http.Header;

import com.cctv.xiqu.android.StageActivity;
import com.cctv.xiqu.android.adapter.CalendarListAdapter;
import com.cctv.xiqu.android.adapter.CalendarListAdapter.OnCalendarGridItemClickListener;
import com.cctv.xiqu.android.fragment.network.BaseClient;
import com.cctv.xiqu.android.fragment.network.StageRequest;
import com.cctv.xiqu.android.fragment.network.StageRequest.DateCount;
import com.cctv.xiqu.android.fragment.network.StageRequest.Result;
import com.cctv.xiqu.android.utils.CalendarUtils;
import com.cctv.xiqu.android.utils.CalendarUtils.CalendarDate;
import com.cctv.xiqu.android.utils.CalendarUtils.CurrentCalendarList;
import com.cctv.xiqu.android.widget.NoResultView;
import com.cctv.xiqu.android.widget.NoResultView.OnRefreshClickListener;
import com.mengle.lib.utils.Utils;

import com.cctv.xiqu.android.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class StageCalendarFragment extends BaseFragment implements
		OnCalendarGridItemClickListener, OnRefreshClickListener {

	public static StageCalendarFragment newInstance() {
		StageCalendarFragment fragment = new StageCalendarFragment();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.stage_calendar_layout, null);
	}

	private ListView listView;

	private CalendarListAdapter adapter;

	private NoResultView noResultView;
	
	private View loadingView;

	private List<CalendarListAdapter.Model> list = new ArrayList<CalendarListAdapter.Model>();

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		loadingView = view.findViewById(R.id.loading);
		loadingView.setVisibility(View.VISIBLE);
		listView = (ListView) view.findViewById(R.id.listview);
		adapter = new CalendarListAdapter(getActivity(), list, this);
		listView.setAdapter(adapter);
		noResultView = (NoResultView) view.findViewById(R.id.no_result);
		noResultView.setOnRefreshClickListener(this);
		request();
	}

	private Date getRelativeMonth(int d) {
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, d);
		return calendar.getTime();
	}

	private Date getStartDate() {
		return getRelativeMonth(-1);
	}

	private Date getEndDate() {
		return getRelativeMonth(3);
	}

	private CalendarDate current;
	
	private Integer currentMonth;

	private List<CalendarListAdapter.Model> getModels(Date start, Date end,
			List<DateCount> dateCounts) {
		int sy = DateUtils.toCalendar(start).get(Calendar.YEAR), ey = DateUtils
				.toCalendar(end).get(Calendar.YEAR), ly = ey - sy;
		int sm = DateUtils.toCalendar(start).get(Calendar.MONTH), em = DateUtils
				.toCalendar(end).get(Calendar.MONTH), length = ly * 12 + em
				- sm;
		int monthNow = DateUtils.toCalendar(new Date()).get(Calendar.MONTH);
		
		
		
		List<CalendarListAdapter.Model> list = new ArrayList<CalendarListAdapter.Model>();
		for (int i = 0; i <= length; i++) {
			Calendar calendar = DateUtils.toCalendar(start);
			calendar.add(Calendar.MONTH, i);
			int year = calendar.get(Calendar.YEAR), month = calendar
					.get(Calendar.MONTH) + 1;
			if(monthNow ==  month -1){
				currentMonth = i;
			}
			CurrentCalendarList model = CalendarUtils.getDay(year, month,
					dateCounts);
			if (model.getCurrent() != null) {
				current = model.getCurrent();
			}

			list.add(new CalendarListAdapter.Model(calendar.getTime(), model
					.getList()));
		}

		return list;

	}
	
	public void setCurrent(){
		if(currentMonth != null){
			listView.setSelection(currentMonth);
		}
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setCurrent();
	}

	private Date startDate;

	private Date endDate;

	private Result result;

	private void request() {

		startDate = getStartDate();
		endDate = getEndDate();
		StageRequest request = new StageRequest(getActivity(),
				new StageRequest.Params(startDate, endDate));

		request.request(new BaseClient.SimpleRequestHandler() {
			@Override
			public void onSuccess(Object object) {
				result = (Result)object;
				list.addAll(getModels(startDate, endDate,
						result.getDateCounts()));
				adapter.setLast(current);
				adapter.notifyDataSetChanged();
				listView.setVisibility(View.VISIBLE);
				noResultView.setVisibility(View.GONE);
				setCurrent();
			}

			@Override
			public void onError(int error, String msg) {
				Utils.tip(getActivity(), msg);
				listView.setVisibility(View.GONE);
				noResultView.setVisibility(View.VISIBLE);
			}
			
			@Override
			public void onComplete() {
				loadingView.setVisibility(View.GONE);
			}

		});

	}

	@Override
	public void OnCalendarGridItemClick(Date date) {
		StageActivity.open(getActivity(), new StageActivity.Model(startDate,
				endDate, result, date));

	}

	@Override
	public void onrefreshclick() {
		request();

	}

}
