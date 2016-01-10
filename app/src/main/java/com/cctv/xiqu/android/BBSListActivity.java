package com.cctv.xiqu.android;

import java.util.ArrayList;
import java.util.List;

import com.cctv.xiqu.android.adapter.BBSListAdapter;
import com.cctv.xiqu.android.adapter.BBSListAdapter.OnDelClickListener;
import com.cctv.xiqu.android.fragment.network.BaseClient;
import com.cctv.xiqu.android.fragment.network.BaseClient.RequestHandler;
import com.cctv.xiqu.android.fragment.network.GetTopicRequest;
import com.cctv.xiqu.android.fragment.network.UpdateTopicRequest;
import com.cctv.xiqu.android.fragment.network.GetTopicRequest.Result;
import com.cctv.xiqu.android.utils.LoadingPopup;
import com.cctv.xiqu.android.utils.Preferences.Session;
import com.cctv.xiqu.android.widget.BaseListView;
import com.cctv.xiqu.android.widget.BaseListView.OnLoadListener;
import com.cctv.xiqu.android.widget.BaseListView.Type;

import com.cctv.xiqu.android.R;
import com.mengle.lib.utils.Utils;
import com.mengle.lib.wiget.ConfirmDialog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class BBSListActivity extends BaseActivity implements OnClickListener,OnLoadListener,OnItemClickListener,OnDelClickListener{
	
	public static final int TYPE_PUBLISH = 0;
	
	public static final int TYPE_REPLY = 1;
	
	public static void open(Context context,int type){
		
		Intent intent = new Intent(context, BBSListActivity.class);
		
		intent.putExtra("type", type);
		
		context.startActivity(intent);
		
	}
	
	private int type;
	
	private List<BBSListAdapter.Model> list = new ArrayList<BBSListAdapter.Model>();
	
	private BBSListAdapter adapter;
	
	private Session session;
	
	private View edit;
	
	private View editOk;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		session = APP.getSession();
		type = getIntent().getIntExtra("type",0);
		String title = this.type == 1 ? "回复的帖子":"发表的帖子";
		setContentView(R.layout.bbs_list_layout);
		View navRight= findViewById(R.id.nav_right);
		edit = findViewById(R.id.edit);
		edit.setOnClickListener(this);
		editOk = findViewById(R.id.edit_ok);
		editOk.setOnClickListener(this);
		if(this.type != 1){
			navRight.setVisibility(View.VISIBLE);
		}else{
			navRight.setVisibility(View.GONE);
		}
		((TextView)findViewById(R.id.title)).setText(title);
		findViewById(R.id.back).setOnClickListener(this);
		adapter = new BBSListAdapter(this, list);
		adapter.setOnDelClickListener(this);
		BaseListView listView = (BaseListView) findViewById(R.id.list);
		listView.setOnLoadListener(this);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
		listView.load(true);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.edit:
			onedit();
			break;
		case R.id.edit_ok:
			oneditOK();
			break;
		default:
			break;
		}
		
	}

	private void oneditOK() {
		edit.setVisibility(View.VISIBLE);
		editOk.setVisibility(View.GONE);
		adapter.setEdit(false);
	}

	private void onedit() {
		edit.setVisibility(View.GONE);
		editOk.setVisibility(View.VISIBLE);
		adapter.setEdit(true);
	}

	@Override
	public BaseClient onLoad(int offset, int limit) {
		GetTopicRequest request = new GetTopicRequest(this, new GetTopicRequest.Params(offset, limit,session.getSid(),type));
		return request;
	}

	@Override
	public boolean onLoadSuccess(Object object, int offset, int limit) {
		Result result = (Result) object;
		if(offset == 1){
			list.clear();
		}
		List<BBSListAdapter.Model> results = result.toModels();
		list.addAll(results);
		adapter.notifyDataSetChanged();
		return results.size()>=limit?true:false;
	}

	@Override
	public void onError(String error) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Type getRequestType() {
		// TODO Auto-generated method stub
		return Type.PAGE;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		if(adapter.isEdit()){
			ondel(position-1);
		}else{
			BBSDetailActivity.open(this, list.get(position-1).toModel());
		}
		 
		
	}
	
	private void ondel(int pos){
		final BBSListAdapter.Model model = list.get(pos);
		ConfirmDialog.open(this, "提示", "确认要删除该帖子吗?", new ConfirmDialog.OnClickListener() {
			
			@Override
			public void onPositiveClick() {
				UpdateTopicRequest request = new UpdateTopicRequest(BBSListActivity.this, 
						new UpdateTopicRequest.Params(APP.getSession().getSid(), APP.getSession().getPkey(), model.getId()));
				LoadingPopup.show(BBSListActivity.this);
				request.request(new RequestHandler() {
					
					@Override
					public void onSuccess(Object object) {
						list.remove(model);
						adapter.notifyDataSetChanged();
						Utils.tip(BBSListActivity.this, "删除成功");
					}
					
					@Override
					public void onError(int error, String msg) {
						Utils.tip(BBSListActivity.this, "删除失败");
						
					}
					
					@Override
					public void onComplete() {
						LoadingPopup.hide(BBSListActivity.this);
						
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
	public void ondelclick(int pos) {
		ondel(pos);
	}
}
