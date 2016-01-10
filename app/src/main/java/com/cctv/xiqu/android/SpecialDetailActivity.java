package com.cctv.xiqu.android;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.apache.commons.lang3.ArrayUtils;

import com.cctv.xiqu.android.APP.DisplayOptions;
import com.cctv.xiqu.android.adapter.SGridAdapter;
import com.cctv.xiqu.android.fragment.network.BaseClient;
import com.cctv.xiqu.android.fragment.network.DescripitionRequest;
import com.cctv.xiqu.android.fragment.network.InsertCommentRequest;
import com.cctv.xiqu.android.fragment.network.BaseClient.RequestHandler;
import com.cctv.xiqu.android.fragment.network.DescripitionRequest.Result;
import com.cctv.xiqu.android.utils.HtmlUtils;
import com.cctv.xiqu.android.utils.LoadingPopup;
import com.cctv.xiqu.android.utils.ShareUtils;
import com.cctv.xiqu.android.widget.IOSPopupWindow;
import com.cctv.xiqu.android.widget.SPopupWindow;
import com.cctv.xiqu.android.widget.IOSPopupWindow.OnIOSItemClickListener;
import com.mengle.lib.utils.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.socialize.bean.SHARE_MEDIA;

import com.cctv.xiqu.android.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Layout;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewStub;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class SpecialDetailActivity extends BaseActivity implements
		OnClickListener {

	public static void open(Context context, Params params) {
		Intent intent = new Intent(context, SpecialDetailActivity.class);
		intent.putExtra("params", params);
		context.startActivity(intent);
	}

	public static class Params implements Serializable {
		private String contentId;
		private String title;

		private String subTitle;

		private String cover;

		private int comment;

		public Params(String contentId, String title, String subTitle,
				String cover, int comment) {
			super();
			this.contentId = contentId;
			this.title = title;
			this.subTitle = subTitle;
			this.cover = cover;
			this.comment = comment;
		}

		private Model toModel() {
			return new Model(title, subTitle, cover, comment);
		}
	}

	public static class Model implements Serializable {

		private String title;

		private String subTitle;

		private String cover;

		private String html;

		private Integer comment;

		public Model(String title, String subTitle, String cover,
				Integer comment) {
			super();
			this.title = title;
			this.subTitle = subTitle;
			this.cover = cover;
			this.comment = comment;
		}

		public Model(String html) {
			super();
			this.html = html;
		}

	}

	private class ViewHolder {
		private WebView webView;

		private ImageView coverView;

		private TextView title;

		private TextView subTitle;

		private TextView comment;

		private EditText editText;

		private WebSettings settings;

		private int fontSize = APP.getSession().getFontSize();

		private int fonts[] = new int[] { 10, 14, 18, 22, 26 };

		private int fontIndex = ArrayUtils.indexOf(fonts, fontSize);

		@SuppressLint("NewApi")
		public ViewHolder() {
			findViewById(R.id.back).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					SpecialDetailActivity.this.finish();

				}
			});
			findViewById(R.id.scaleDown).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							scaleDown();

						}
					});
			findViewById(R.id.scaleUp).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							scaleUp();

						}
					});
			webView = (WebView) findViewById(R.id.webView);
			webView.setBackgroundColor(Color.WHITE);
			/*if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
				webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
			}*/

//			webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
			webView.getSettings().setRenderPriority(
					WebSettings.RenderPriority.HIGH);
			webView.getSettings().setAppCacheEnabled(true);
			settings = webView.getSettings();
			settings.setJavaScriptEnabled(true);

			coverView = (ImageView) findViewById(R.id.cover);
			title = (TextView) findViewById(R.id.title);

			subTitle = (TextView) findViewById(R.id.subtitle);
			comment = (TextView) findViewById(R.id.comment);
			editText = (EditText) findViewById(R.id.edit);
		}

		private String html;

		private void setModel(final Model model) {
			if (model.title != null) {
				title.setText(model.title);
				ViewTreeObserver vto = title.getViewTreeObserver();
				vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
				    @Override
				    public void onGlobalLayout() {
				       Layout l = title.getLayout();
				       if ( l != null){
				          int lines = l.getLineCount();
				          if ( lines > 0){
				        	  if ( l.getEllipsisCount(lines-1) > 0){
				        		  title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
				              }
				          }
				              
				            	  
				       }  
				    }
				});
				
			}

			if (model.subTitle != null) {
				subTitle.setText(model.subTitle);
			}

			if (model.comment != null) {
				comment.setText("" + model.comment + " 跟帖");
			}

			if (model.cover != null) {
				ImageLoader.getInstance().displayImage(model.cover, coverView,
						DisplayOptions.IMG.getOptions());
			}
			if (model.html != null) {

				try {
					html = HtmlUtils.getHtml(SpecialDetailActivity.this,
							"desc_template.html",
							new HashMap<String, String>() {
								{
									put("content", model.html);
									put("font-size", "" + fontSize);
								}
							});
					webView.loadDataWithBaseURL(null, html, "text/html",
							"utf-8", null);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		private void setFontSize(int fontSize) {
			APP.getSession().setFontSize(fontSize);
			webView.loadUrl("javascript:setFontSize(" + fontSize + ")");
		}

		private void scaleUp() {
			if (fontIndex < fonts.length - 1) {
				fontIndex++;
			}
			setFontSize(fonts[fontIndex]);
		}

		private void scaleDown() {
			if (fontIndex > 0) {
				fontIndex--;
			}
			setFontSize(fonts[fontIndex]);
		}

	}

	private ViewHolder holder;

	private View notLoginView;

	private Params params;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.specical_detail_layout);
		findViewById(R.id.share).setOnClickListener(this);
		notLoginView = findViewById(R.id.not_login_view);
		notLoginView.setOnClickListener(this);
		if (APP.getSession().isLogin()) {
			notLoginView.setVisibility(View.GONE);
		} else {
			notLoginView.setVisibility(View.VISIBLE);
		}
		findViewById(R.id.sendBtn).setOnClickListener(this);
		findViewById(R.id.comment_btn).setOnClickListener(this);
		params = (Params) getIntent().getSerializableExtra("params");
		holder = new ViewHolder();
		holder.setModel(params.toModel());
		request();
	}

	private void request() {
		DescripitionRequest request = new DescripitionRequest(this,
				params.contentId);
		request.request(new RequestHandler() {

			@Override
			public void onSuccess(Object object) {
				Result result = (Result) object;
				holder.setModel(result.toDetailModel());

			}

			@Override
			public void onComplete() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onError(int error, String msg) {
				// TODO Auto-generated method stub

			}
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.share:
			onshare();
			break;
		case R.id.not_login_view:
			toLogin();
			break;
		case R.id.comment_btn:
			NewsCommentActivity.open(this, new NewsCommentActivity.Model(
					params.contentId, params.comment, params.title));
			break;
		case R.id.sendBtn:
			LoadingPopup.show(this);
			String content = holder.editText.getText().toString();
			new InsertCommentRequest(this, new InsertCommentRequest.Params(
					params.contentId, "0", "0", APP.getSession().getSid(),
					content, APP.getSession().getPkey()))
					.request(new RequestHandler() {

						@Override
						public void onSuccess(Object object) {
							Utils.tip(SpecialDetailActivity.this, "评论成功");
							holder.editText.setText("");
							params.comment++;
							holder.comment.setText((params.comment) + " 跟帖");

						}

						@Override
						public void onError(int error, String msg) {
							if(error == 1011){
								Utils.tip(SpecialDetailActivity.this, "评论频率过于频繁");
							}else{
								Utils.tip(SpecialDetailActivity.this, "评论失败");
							}

						}

						@Override
						public void onComplete() {
							LoadingPopup.hide(SpecialDetailActivity.this);

						}
					});
			break;
		default:
			break;
		}

	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		holder.webView.loadUrl("about:blank");
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
				File bitmapFile = ImageLoader.getInstance().getDiscCache().get(params.cover);	
				ShareUtils.shareWebsite(SpecialDetailActivity.this,
						media, params.title, APP.getAppConfig()
								.getSharecontent(params.contentId),bitmapFile);
				
			}
		});
		

	}

}
