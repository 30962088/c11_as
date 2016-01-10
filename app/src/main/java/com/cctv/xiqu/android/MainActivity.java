package com.cctv.xiqu.android;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.cctv.xiqu.android.fragment.MainFragment1;
import com.cctv.xiqu.android.fragment.MainFragment2;
import com.cctv.xiqu.android.fragment.MainFragment4;
import com.cctv.xiqu.android.fragment.MemberFragment;
import com.cctv.xiqu.android.fragment.StageCalendarFragment;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

import com.cctv.xiqu.android.R;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class MainActivity extends BaseActivity implements OnClickListener {

	public static void open(Context context) {
		Intent intent = new Intent(context, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
				| Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
		context.startActivity(intent);
	}
	
	private View tab5;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initPush();
		UmengUpdateAgent.update(this);
		MobclickAgent.updateOnlineConfig(this);
		openGuide();
		setContentView(R.layout.activity_main);
		fragments = new Fragment[] { MainFragment1.newInstance(),
				MainFragment2.newInstance(),
				StageCalendarFragment.newInstance(),
				MainFragment4.newInstance(), MemberFragment.newInstance() };
		findViewById(R.id.tab1).setOnClickListener(this);
		findViewById(R.id.tab2).setOnClickListener(this);
		findViewById(R.id.tab3).setOnClickListener(this);
		findViewById(R.id.tab4).setOnClickListener(this);
		tab5 = findViewById(R.id.tab5);
		tab5.setOnClickListener(this);
		findViewById(R.id.tab1).performClick();
	}

	private Fragment[] fragments;

	private Fragment lastFragment;

	public void switchFragment(Fragment fragment) {
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();
		if (lastFragment != null) {
			transaction.hide(lastFragment);
		}
		if (!fragment.isAdded()) {
			transaction.add(R.id.fragment_container, fragment);
		} else {
			transaction.show(fragment);
			fragment.onResume();
		}
		transaction.commitAllowingStateLoss();
		lastFragment = fragment;
	}

	private View lastView;
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
		
	}

	@Override
	public void onClick(View v) {
		if (lastView != null) {
			lastView.setSelected(false);
		}
		v.setSelected(true);
		switch (v.getId()) {
		case R.id.tab1:
			switchFragment(fragments[0]);
			break;
		case R.id.tab2:
			switchFragment(fragments[1]);
			break;
		case R.id.tab3:
			switchFragment(fragments[2]);
			break;
		case R.id.tab4:
			switchFragment(fragments[3]);
			break;
		case R.id.tab5:
			switchFragment(fragments[4]);
			break;
		}
		lastView = v;
	}

	long waitTime = 2000;
	long touchTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN
				&& KeyEvent.KEYCODE_BACK == keyCode) {
			long currentTime = System.currentTimeMillis();
			if ((currentTime - touchTime) >= waitTime) {
				Toast.makeText(this, "再按一次退出央视戏曲", Toast.LENGTH_SHORT).show();
				touchTime = currentTime;
			} else {
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	public static final String ACTION_TOLOGIN = "action_tologin";
	
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		if(TextUtils.equals(ACTION_TOLOGIN, intent.getAction())){
			tab5.performClick();
		}
	}
	
	private void initPush(){
		if(APP.getSession().getNewsPush()){
			PushManager.startWork(getApplicationContext(),
	                PushConstants.LOGIN_TYPE_API_KEY,
	                APP.getAppConfig().getPush_api_key());
		}
		if(APP.getSession().getVoice()){
			PushManager.setNoDisturbMode(this, -1 , -1, -1, -1);
		}else{
			PushManager.setNoDisturbMode(this, 0, 0, 23, 59);
		}
		
	}

}
