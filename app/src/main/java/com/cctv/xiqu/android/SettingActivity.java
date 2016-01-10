package com.cctv.xiqu.android;

import com.cctv.xiqu.android.utils.CacheManager;
import com.cctv.xiqu.android.utils.LoadingPopup;
import com.cctv.xiqu.android.utils.CacheManager.OnClearCacheListner;
import com.mengle.lib.wiget.ConfirmDialog;

import com.cctv.xiqu.android.R;
import de.ankri.views.Switch;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class SettingActivity extends BaseActivity implements OnClickListener,
		OnCheckedChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static void open(Context context) {
		context.startActivity(new Intent(context, SettingActivity.class));
	}

	private Switch voiceSwitch;

	private Switch newsSwitch;

	private TextView cacheView;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.setting_layout);
		voiceSwitch = (Switch) findViewById(R.id.voice);
		voiceSwitch.setOnCheckedChangeListener(this);
		newsSwitch = (Switch) findViewById(R.id.news);
		newsSwitch.setOnCheckedChangeListener(this);
		cacheView = (TextView) findViewById(R.id.cache);
		findViewById(R.id.back).setOnClickListener(this);
		findViewById(R.id.cache_btn).setOnClickListener(this);
		findViewById(R.id.feedback_btn).setOnClickListener(this);
		findViewById(R.id.app_btn).setOnClickListener(this);
		findViewById(R.id.about_btn).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.cache_btn:
			onclearcache();
			break;
		case R.id.feedback_btn:
			onfeedback();
			break;
		case R.id.app_btn:
			onapp();
			break;
		case R.id.about_btn:
			onabout();
			break;
		default:
			break;
		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		voiceSwitch.setChecked(APP.getSession().getVoice());
		newsSwitch.setChecked(APP.getSession().getNewsPush());
		getCacheSize();

	}

	private void getCacheSize() {

		new AsyncTask<Context, Void, Long>() {

			@Override
			protected Long doInBackground(Context... params) {
				// TODO Auto-generated method stub
				return CacheManager.folderSize(params[0].getCacheDir())
						+ CacheManager.folderSize(params[0]
								.getExternalCacheDir());
			}

			@Override
			protected void onPostExecute(Long result) {
				cacheView.setText(Math.round(result / 1024 / 1024 * 100)
						/ 100.0 + "M");
			}

		}.execute(this);
	}

	private void onabout() {
		AboutActivity.open(this);

	}

	private void onapp() {
		AppListActivity.open(this);

	}

	private void onfeedback() {
		FeedBackActivity.open(this);

	}

	private void onclearcache() {
		ConfirmDialog.open(this, "确认", "是否要清空缓存？",
				new ConfirmDialog.OnClickListener() {

					@Override
					public void onPositiveClick() {

						LoadingPopup.show(SettingActivity.this);
						CacheManager.clearCache(SettingActivity.this,
								new OnClearCacheListner() {

									@Override
									public void onclearSuccess() {
										LoadingPopup.hide(SettingActivity.this);
										onResume();
									}
								});
					}

					@Override
					public void onNegativeClick() {
						// TODO Auto-generated method stub

					}

				});

	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.voice:
			APP.getSession().setVoice(isChecked);
			break;

		case R.id.news:
			APP.getSession().setNewsPush(isChecked);
			break;
		}
		onResume();

	}
}
