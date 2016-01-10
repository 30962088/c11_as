package com.cctv.xiqu.android;


import com.cctv.xiqu.android.R;

import com.cctv.xiqu.android.adapter.InfoListAdapter;
import com.cctv.xiqu.android.adapter.InfoListAdapter.InfoItem;
import com.cctv.xiqu.android.adapter.InfoListAdapter.OnInfoClickListener;
import com.cctv.xiqu.android.fragment.network.GetPushInfoRequest;
import com.cctv.xiqu.android.fragment.network.BaseClient.SimpleRequestHandler;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshPinnedSectionListView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

public class InfoListActivity extends BaseActivity implements OnClickListener,OnRefreshListener<ListView>,OnInfoClickListener{

	public static void open(Context context){
		context.startActivity(new Intent(context, InfoListActivity.class));
	}
	
	private PullToRefreshPinnedSectionListView listView;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_info_list);
		findViewById(R.id.back).setOnClickListener(this);
		listView = (PullToRefreshPinnedSectionListView) findViewById(R.id.listView);
		listView.setOnRefreshListener(this);
		GetPushInfoRequest.Result result = GetPushInfoRequest.getResult();
		if(result != null){
			listView.setAdapter(new InfoListAdapter(this,result.toInfoGroups(),this));
		}
		request();
		listView.setRefreshing(true);
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
	
	private void request(){
		GetPushInfoRequest request = new GetPushInfoRequest(this);
		request.request(new SimpleRequestHandler(){
			@Override
			public void onSuccess(Object object) {
				GetPushInfoRequest.Result result = (GetPushInfoRequest.Result)object;
				listView.setAdapter(new InfoListAdapter(InfoListActivity.this, result.toInfoGroups(),InfoListActivity.this));
			}
			
			@Override
			public void onComplete() {
				listView.onRefreshComplete();
			}
			
		});
	}
	
	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		request();
		
	}
	
	@Override
	public void finish() {
		MainActivity.open(this);
		super.finish();
	}

	@Override
	public void oninfoclick(InfoItem item) {
		InfoDetailActivity.open(this, item);
		
	}
	
}
