package com.cctv.xiqu.android;

import org.apache.http.Header;

import com.cctv.xiqu.android.R;

import com.cctv.xiqu.android.fragment.network.BaseClient;
import com.cctv.xiqu.android.fragment.network.WeiboCommentPostRequest;
import com.cctv.xiqu.android.fragment.network.WeiboReportPostRequest;
import com.cctv.xiqu.android.fragment.network.BaseClient.RequestHandler;
import com.cctv.xiqu.android.utils.LoadingPopup;
import com.mengle.lib.utils.Utils;
import com.umeng.socialize.utils.LoadingDialog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;

public class WeiboPublishActivity extends BaseActivity implements
		OnClickListener {

	private static final long serialVersionUID = -7321740010713099613L;

	public static void open(Context context, String id) {
		Intent intent = new Intent(context, WeiboPublishActivity.class);
		intent.putExtra("id", id);
		context.startActivity(intent);
	}

	private CheckBox reportBox;

	private EditText contentText;
	
	private String id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		id = getIntent().getStringExtra("id");
		setContentView(R.layout.weibocomment_publish_layout);
		reportBox = (CheckBox) findViewById(R.id.report);
		contentText = (EditText) findViewById(R.id.content);
		findViewById(R.id.back).setOnClickListener(this);
		findViewById(R.id.publish_btn).setOnClickListener(this);
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
		String content = contentText.getText().toString();
		
		if(getWordCount(content)>140){
			Utils.tip(this, "评论不能超过140个汉字 ");
			return;
		}
		BaseClient client = null;
		String token = APP.getSession().getWeiboAccessToken();
		if(reportBox.isChecked()){
			client = new WeiboReportPostRequest(this, 
					new WeiboReportPostRequest.Params(token, id, content,1));
		}else{
			client = new WeiboCommentPostRequest(this, 
					new WeiboCommentPostRequest.Params(token, id, content));
		}
		
		LoadingPopup.show(this);
		
		client.request(new BaseClient.SimpleRequestHandler(){
			@Override
			public void onComplete() {
				LoadingPopup.hide(WeiboPublishActivity.this);
			}
			@Override
			public void onSuccess(Object object) {
				Utils.tip(WeiboPublishActivity.this, "评论成功");
				finish();
			}
			
			@Override
			public void onError(int error, String msg) {
				Utils.tip(WeiboPublishActivity.this, "评论失败");
			}
			
		});
		
		
	}

	public int getWordCount(String s) {
		int length = 0;
		for (int i = 0; i < s.length(); i++) {
			int ascii = Character.codePointAt(s, i);
			if (ascii >= 0 && ascii <= 255)
				length++;
			else
				length += 2;

		}
		return length;

	}

}
