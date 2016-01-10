package com.cctv.xiqu.android;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

import com.cctv.xiqu.android.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;

public class AboutActivity extends BaseActivity implements OnClickListener{
	
	public static void open(Context context){
		context.startActivity(new Intent(context, AboutActivity.class));
	}
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_about);
		WebView webView = (WebView) findViewById(R.id.webview);
		webView.getSettings().setJavaScriptEnabled(true);
		findViewById(R.id.back).setOnClickListener(this);
		try {
			String html = IOUtils.toString(getAssets()
					.open("about.html"));
			webView.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
	
}
