package com.cctv.xiqu.android.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsAdapter extends FragmentPagerAdapter{

	public static class Pager{
		private String title;
		private Fragment fragment;
		public Pager(String title, Fragment fragment) {
			super();
			this.title = title;
			this.fragment = fragment;
		}
		
	}
	
	private List<Pager> list;
	
	
	public TabsAdapter(FragmentManager fm,List<Pager> list) {
		super(fm);
		this.list = list;
	}

	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position).fragment;
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
		// TODO Auto-generated method stub
		return list.get(position).title;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

}
