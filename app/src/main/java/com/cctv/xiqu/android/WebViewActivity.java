package com.cctv.xiqu.android;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import com.cctv.xiqu.android.R;
import com.cctv.xiqu.android.adapter.SGridAdapter;
import com.cctv.xiqu.android.utils.ShareUtils;
import com.cctv.xiqu.android.widget.IOSPopupWindow;
import com.cctv.xiqu.android.widget.SPopupWindow;
import com.cctv.xiqu.android.widget.IOSPopupWindow.OnIOSItemClickListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.socialize.bean.SHARE_MEDIA;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class WebViewActivity extends BaseActivity implements OnClickListener{

	public static final String PARAM_URL = "url";
	
	public static final String PARAM_TITLE = "title";
	
	public static final String PARAM_IMG = "img";
	
	private WebView webView;
	
	private TextView titleView;
	
	private String title;
	
	private String img;
	
	@Override
	protected void onCreate(Bundle bundle) {
		// TODO Auto-generated method stub
		super.onCreate(bundle);
		title = getIntent().getStringExtra(PARAM_TITLE);
		img = getIntent().getStringExtra(PARAM_IMG);
		
		setContentView(R.layout.webview_layout);
		View share = findViewById(R.id.share);
		share.setOnClickListener(this);
		if(img != null){
			share.setVisibility(View.VISIBLE);
			
		}else{
			share.setVisibility(View.GONE);
		}
		titleView = (TextView) findViewById(R.id.title);
		if(title != null){
			titleView.setText(title);
		}
		webView = (WebView) findViewById(R.id.webview);
		final View loading = findViewById(R.id.loading);
		webView.getSettings().setJavaScriptEnabled(true);
		if(img != null){
			webView.getSettings().setLoadWithOverviewMode(true);
			webView.getSettings().setUseWideViewPort(true);
		}
		webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		webView.loadUrl(getIntent().getStringExtra(PARAM_URL));
		webView.setWebViewClient(new WebViewClient() {
	        @Override
	        public void onPageFinished(WebView view, String url) {
	        	loading.setVisibility(View.GONE);
	        	if(title == null){
	        		titleView.setText(view.getTitle());
	        	}
	        	
	        }
	        @Override
	        public void onPageStarted(WebView view, String url, Bitmap favicon) {
	        	loading.setVisibility(View.VISIBLE);
	        	super.onPageStarted(view, url, favicon);
	        }
	    });
		findViewById(R.id.back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
	}
	
	
	
	public static void open(Context context,String title,String url,String img){
		Intent intent = new Intent(context, WebViewActivity.class);
		intent.putExtra(PARAM_TITLE, title);
		intent.putExtra(WebViewActivity.PARAM_URL,url);
		intent.putExtra(WebViewActivity.PARAM_IMG,img);
		context.startActivity(intent);
	}
	
	public static void open(Context context,String url){
		open(context, null, url,null);
	}
	
	public static void open(Context context,String title,String url){
		open(context, title, url,null);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		webView.loadUrl("about:blank");
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(webView.canGoBack()){
				webView.goBack();
			}else{
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}



	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.share:
			onshare();
			break;

		default:
			break;
		}
		
	}



	private void onshare() {
		new SPopupWindow(this, new ArrayList<SGridAdapter.Model>(){{
			add(new SGridAdapter.Model(R.drawable.s_qq, "QQ好友"));
			add(new SGridAdapter.Model(R.drawable.s_qzone, "QQ空间"));
			add(new SGridAdapter.Model(R.drawable.s_weixin, "微信好友"));
			add(new SGridAdapter.Model(R.drawable.s_timeline, "微信朋友圈"));
			add(new SGridAdapter.Model(R.drawable.s_sina, "新浪微博"));
		}}, new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				SHARE_MEDIA media = new SHARE_MEDIA[] { SHARE_MEDIA.QQ,
						SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN,
						SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA }[position];
				String url = webView.getUrl();
				File bitmapFile = ImageLoader.getInstance().getDiscCache().get(img);	
				ShareUtils.shareWebsite(WebViewActivity.this,
						media, title, url,bitmapFile);
				
			}
		});
		
	}

	

	
}
