package com.cctv.xiqu.android.widget;

import com.cctv.xiqu.android.APP;
import com.cctv.xiqu.android.BBSListActivity;
import com.cctv.xiqu.android.BBSPublishActivity;
import com.cctv.xiqu.android.BaseActivity;
import com.cctv.xiqu.android.SettingActivity;
import com.cctv.xiqu.android.utils.CacheManager;
import com.cctv.xiqu.android.utils.LoadingPopup;
import com.cctv.xiqu.android.utils.CacheManager.OnClearCacheListner;
import com.cctv.xiqu.android.utils.Preferences.Session;
import com.mengle.lib.wiget.ConfirmDialog;

import com.cctv.xiqu.android.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

public class BBSHeaderView extends FrameLayout{

	public BBSHeaderView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public BBSHeaderView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public BBSHeaderView(Context context) {
		super(context);
		init();
	}

	private ViewHolder holder;
	
	private void init() {
		LayoutInflater.from(getContext()).inflate(R.layout.bbs_header, this);
		holder = new ViewHolder();
	}
	
	private class ViewHolder implements OnClickListener{
		
		private TextView published;
		
		private TextView ipublish;
		
		public ViewHolder() {
			published = (TextView) findViewById(R.id.published);
			ipublish = (TextView) findViewById(R.id.ipublish);
			findViewById(R.id.publish_btn).setOnClickListener(this);
			findViewById(R.id.published_btn).setOnClickListener(this);
			findViewById(R.id.ipublish_btn).setOnClickListener(this);
		}
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.publish_btn:
				if(check("您需要登录才能发帖")){
					BBSPublishActivity.open(getContext());
				}
				break;
			case R.id.published_btn:
				if(check("您需要登录才能查看我发表的帖子")){
					BBSListActivity.open(getContext(), BBSListActivity.TYPE_PUBLISH);
					
				}
				
				break;
			case R.id.ipublish_btn:
				
				if(check("您需要登录才能查看我回复的帖子")){
					BBSListActivity.open(getContext(), BBSListActivity.TYPE_REPLY);
				}
				break;
			default:
				break;
			}
			
		}
		private boolean check(String title) {
			
			if(!APP.getSession().isLogin()){
				((BaseActivity)getContext()).toLogin(title);
				return false;
			}
			
			return true;
		}
		
	}
	
	/*public void setModel(Model model){
		holder.ipublish.setText("("+model.ipublish+")");
		holder.published.setText("("+model.published+")");
	}
	
	public static class Model{
		private int published;
		private int ipublish;
		public Model(int published, int ipublish) {
			super();
			this.published = published;
			this.ipublish = ipublish;
		}
		
	}*/
	
	

}
