package com.cctv.xiqu.android;

import com.cctv.xiqu.android.R;

import com.cctv.xiqu.android.fragment.network.FeedbackRequest;
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

public class FeedBackActivity extends BaseActivity implements OnClickListener{

	public static void open(Context context){
		context.startActivity(new Intent(context, FeedBackActivity.class));
	}
	
	private EditText titleText;
	
	private EditText contentText;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.feedback_layout);
		titleText = (EditText) findViewById(R.id.title);
		contentText = (EditText) findViewById(R.id.content);
		findViewById(R.id.publish_btn).setOnClickListener(this);
		findViewById(R.id.back).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
			
		case R.id.publish_btn:
			onpublish();
			break;

		default:
			break;
		}
		
	}

	private void onpublish() {
		String title = titleText.getText().toString();
		
		String content = contentText.getText().toString();
		
		if(TextUtils.isEmpty(title)){
			Utils.tip(this, "请输入标题");
			return;
		}
		if(TextUtils.isEmpty(content)){
			Utils.tip(this, "请输入内容");
			return;
		}
		
		String sid = APP.getSession().getSid();
		
		FeedbackRequest request = new FeedbackRequest(this, new FeedbackRequest.Params(sid, title, content,APP.getSession().getPkey()));
		LoadingPopup.show(this);
		request.request(new SimpleRequestHandler(){
			@Override
			public void onSuccess(Object object) {
			
				Utils.tip(FeedBackActivity.this, "反馈成功");
				finish();
				
			}
			@Override
			public void onComplete() {
				LoadingPopup.show(FeedBackActivity.this);
			}
		});
		
		
	}
	
}
