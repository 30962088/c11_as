package com.cctv.xiqu.android;

import java.io.Serializable;

import com.cctv.xiqu.android.R;

import com.cctv.xiqu.android.fragment.network.ReportRequest;
import com.cctv.xiqu.android.fragment.network.BaseClient.SimpleRequestHandler;
import com.cctv.xiqu.android.utils.LoadingPopup;
import com.mengle.lib.utils.Utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class ReportActivity extends BaseActivity implements OnClickListener{

	public static class Model implements Serializable{
		private String topicid;

		public Model(String topicid) {
			super();
			this.topicid = topicid;
		}

	}
	
	public static void open(Context context,Model model){
		Intent intent = new Intent(context, ReportActivity.class);
		intent.putExtra("model", model);
		context.startActivity(intent);
	}
	
	private EditText contentView;
	
	private View statusView;
	
	private EditText nameView;
	
	private EditText phoneView;
	
	private Model model;
	
	
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		model = (Model) getIntent().getSerializableExtra("model");
		setContentView(R.layout.report_layout);
		contentView  = (EditText) findViewById(R.id.content);
		nameView = (EditText) findViewById(R.id.name);
		phoneView = (EditText) findViewById(R.id.phone);
		statusView = findViewById(R.id.status);
		if(APP.getSession().isLogin()){
			statusView.setVisibility(View.GONE);
		}else{
			statusView.setVisibility(View.VISIBLE);
		}
		findViewById(R.id.publish_btn).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.publish_btn:
			onpublish();
			break;

		default:
			break;
		}
		
	}

	private void onpublish() {
		String content = contentView.getText().toString();
		String phone = phoneView.getText().toString();
		String name = nameView.getText().toString();
		if(!APP.getSession().isLogin()){
			if(TextUtils.isEmpty(name)){
				Utils.tip(this, "请输入姓名");
				return;
			}
			if(TextUtils.isEmpty(phone)){
				Utils.tip(this, "请输入手机号码");
				return;
			}
			if(phone.length() < 11){
				Utils.tip(this, "请输入正确的手机号码");
				return;
			}
		}
		
		if(TextUtils.isEmpty(content)){
			Utils.tip(this, "请输入举报内容");
			return;
		}
		
		ReportRequest.Params params = null;
		
		if(APP.getSession().isLogin()){
			params = new ReportRequest.Params(APP.getSession().getSid(), content, model.topicid, APP.getSession().getPkey(),true);
		}else{
			params = new ReportRequest.Params(content, model.topicid, name, phone);
		}
		ReportRequest request = new ReportRequest(this, params);
		LoadingPopup.show(this);
		request.request(new SimpleRequestHandler(){
			@Override
			public void onSuccess(Object object) {
				Utils.tip(ReportActivity.this, "谢谢您的反馈");
				finish();
			}
			@Override
			public void onError(int error, String msg) {
				Utils.tip(ReportActivity.this, "举报失败");
			}
			@Override
			public void onComplete() {
				LoadingPopup.hide(ReportActivity.this);
			}
		});
		
		
	}
	
}
