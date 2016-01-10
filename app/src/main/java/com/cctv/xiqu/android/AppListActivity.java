package com.cctv.xiqu.android;

import java.util.ArrayList;

import java.util.List;

import com.cctv.xiqu.android.adapter.AppListAdapter;
import com.cctv.xiqu.android.fragment.network.BaseClient;
import com.cctv.xiqu.android.widget.BaseListView;
import com.cctv.xiqu.android.widget.BaseListView.OnLoadListener;
import com.cctv.xiqu.android.widget.BaseListView.Type;

import com.cctv.xiqu.android.R;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

public class AppListActivity extends BaseActivity{
	
	public static void open(Context context) {
		context.startActivity(new Intent(context, AppListActivity.class));
	}
	
	public static class App{
		private String img;
		private String title;
		private String desc;
		private String url;
		private String packageName;
		
	}
	
	private ListView listView;
	
	private List<AppListAdapter.Model> list = new ArrayList<AppListAdapter.Model>();
	
	private AppListAdapter adapter;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.app_list_layout);
		adapter = new AppListAdapter(this, list);
		listView = (ListView) findViewById(R.id.listview);
		listView.setAdapter(adapter);
	}

	
	
}
