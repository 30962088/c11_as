package com.cctv.xiqu.android.fragment;

import java.io.Serializable;

import com.umeng.analytics.MobclickAgent;

import android.support.v4.app.Fragment;

public class BaseFragment extends Fragment implements Serializable{
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart("fragment:"+getClass().getSimpleName() );	
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd("fragment:"+getClass().getSimpleName() );
	}
	
}
