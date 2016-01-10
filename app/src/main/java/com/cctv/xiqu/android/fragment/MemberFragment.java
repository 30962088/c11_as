package com.cctv.xiqu.android.fragment;

import com.cctv.xiqu.android.APP;

import com.cctv.xiqu.android.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;

public class MemberFragment extends BaseFragment {
	public static MemberFragment newInstance() {
		return new MemberFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.member_layout, null);
	}

	public void backFragment() {
		getChildFragmentManager().popBackStack(null,
				FragmentManager.POP_BACK_STACK_INCLUSIVE);
	}

	public void fragment(Fragment fragment) {
		getChildFragmentManager().beginTransaction()
				.replace(R.id.fragment_container, fragment).commit();
	}

	public void initFragment(Fragment fragment) {
		FragmentManager fm = getChildFragmentManager();
		for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
			fm.popBackStack();
		}
		getChildFragmentManager().beginTransaction()
				.replace(R.id.fragment_container, fragment).commit();
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		Fragment fragment = null;
		if (APP.getSession().isLogin()) {
			fragment = UserSettingFragment.newInstance(APP.getSession()
					.getSid());
		} else {
			fragment = LoginFragment.newInstance();
		}
		getChildFragmentManager().beginTransaction()
				.replace(R.id.fragment_container, fragment).commit();
		view.setFocusableInTouchMode(true);
		view.requestFocus();
		view.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					backFragment();
					return true;
				} else {
					return false;
				}
			}
		});
	}

}
