package com.cctv.xiqu.android.fragment;

import java.util.ArrayList;
import java.util.List;

import com.cctv.xiqu.android.adapter.TabsAdapter;
import com.cctv.xiqu.android.adapter.TabsAdapter.Pager;
import com.viewpagerindicator.TabPageIndicator;

import com.cctv.xiqu.android.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MainFragment2 extends BaseFragment{
	
	
	public static MainFragment2 newInstance(){
		return new MainFragment2();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment_tabs2, null);
	}
	
	private TabPageIndicator indicator;
	
	private ViewPager pager;
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		pager = (ViewPager) view.findViewById(R.id.pager);
		indicator = (TabPageIndicator) view.findViewById(R.id.indicator);
		List<Pager> list = new ArrayList<Pager>();
		
		list.add(new Pager("直播间", LiveFragment.newInstance()));
		
		list.add(new Pager("节目单", ProgramFragment.newInstance()));
		
		pager.setAdapter(new TabsAdapter(getChildFragmentManager(), list));
		indicator.setViewPager(pager);
	}

}
