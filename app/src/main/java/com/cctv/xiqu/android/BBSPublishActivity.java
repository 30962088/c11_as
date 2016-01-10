package com.cctv.xiqu.android;

import java.io.File;

import com.cctv.xiqu.android.BaseActivity.OnGallerySelectionListener;
import com.cctv.xiqu.android.fragment.network.BaseClient;
import com.cctv.xiqu.android.fragment.network.InsertTopicRequest;
import com.cctv.xiqu.android.utils.AliyunUtils;
import com.cctv.xiqu.android.utils.CropImageUtils;
import com.cctv.xiqu.android.utils.LoadingPopup;
import com.cctv.xiqu.android.utils.AliyunUtils.UploadListener;
import com.cctv.xiqu.android.utils.AliyunUtils.UploadResult;
import com.mengle.lib.utils.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.cctv.xiqu.android.R;

public class BBSPublishActivity extends BaseActivity implements
		OnClickListener, OnGallerySelectionListener, OnFocusChangeListener,
		TextWatcher {

	public static void open(Context context) {
		context.startActivity(new Intent(context, BBSPublishActivity.class));
	}

	private EditText titleText;

	private EditText contentText;

	private ImageView imageView;

	private View delView;

	private TextView tipView;

	private int defaultImg = R.drawable.icon_gallery;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.bbs_publish_layout);
		titleText = (EditText) findViewById(R.id.title);
		titleText.addTextChangedListener(this);
		titleText.setOnFocusChangeListener(this);
		contentText = (EditText) findViewById(R.id.content);
		contentText.addTextChangedListener(this);
		contentText.setOnFocusChangeListener(this);
		imageView = (ImageView) findViewById(R.id.img);
		delView = findViewById(R.id.del);
		delView.setVisibility(View.GONE);
		tipView = (TextView) findViewById(R.id.tip);
		findViewById(R.id.back).setOnClickListener(this);
		findViewById(R.id.publish_btn).setOnClickListener(this);
		imageView.setOnClickListener(this);
		imageView.setImageResource(defaultImg);
		delView.setOnClickListener(this);
		titleText.requestFocus();
		titleWord();
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
		case R.id.img:
			onselect();
			break;
		case R.id.del:
			ondel();
			break;
		default:
			break;
		}

	}

	private void ondel() {
		imageView.setImageResource(defaultImg);
		imageView.setTag(null);
		delView.setVisibility(View.GONE);

	}

	private void onselect() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(currentEdit.getWindowToken(), 0);
		getPhoto(this);

	}

	private void onpublish() {
		final String title = titleText.getText().toString();
		final String content = contentText.getText().toString();
		if (TextUtils.isEmpty(title)) {
			Utils.tip(this, "请输入帖子标题");
			return;
		}
		if (TextUtils.isEmpty(content)) {
			Utils.tip(this, "请输入有效的帖子内容");
			return;
		}
		String filepath = (String) imageView.getTag();
		LoadingPopup.show(this);
		if (filepath != null) {

			AliyunUtils.getInstance().upload(
					CropImageUtils.thumnailFile(filepath, 800),"cctv11newscdn",
					new UploadListener() {

						@Override
						public void onsuccess(UploadResult result) {
							send(new InsertTopicRequest.Params(content, APP
									.getSession().getSid(),APP.getSession().getPkey(), title, result
									.getGuid(), result.getExt()));

						}
					});
		} else {
			send(new InsertTopicRequest.Params(content, APP.getSession()
					.getSid(),APP.getSession().getPkey(), title));
		}

	}

	private void send(InsertTopicRequest.Params params) {

		InsertTopicRequest request = new InsertTopicRequest(this, params);

		request.request(new BaseClient.SimpleRequestHandler() {
			@Override
			public void onSuccess(Object object) {

				Utils.tip(BBSPublishActivity.this, "发帖成功");
				finish();

			}

			@Override
			public void onComplete() {
				LoadingPopup.hide(BBSPublishActivity.this);
			}

			@Override
			public void onError(int error, String msg) {
				if(error == 1011){
					Utils.tip(BBSPublishActivity.this, "发帖频率过于频繁");
				}else{
					Utils.tip(BBSPublishActivity.this, "发帖失败");
				}
			}

		});

	}

	private EditText currentEdit;

	private void contentWord() {

		currentEdit = contentText;
		tipView.setText("" + (1000 - contentText.getText().toString().length()));

	}

	private void titleWord() {

		currentEdit = titleText;
		tipView.setText("" + (30 - titleText.getText().toString().length()));

	}

	@Override
	public void onGallerySelection(File file) {
		imageView.setTag(file.toString());
		ImageLoader.getInstance().displayImage(Uri.fromFile(file).toString(),
				imageView, APP.DisplayOptions.IMG.getOptions());
		delView.setVisibility(View.VISIBLE);
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {

		if (hasFocus) {
			switch (v.getId()) {
			case R.id.title:
				titleWord();
				break;
			case R.id.content:
				contentWord();
				break;
			default:
				break;
			}
		}

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterTextChanged(Editable s) {
		switch (currentEdit.getId()) {
		case R.id.title:
			titleWord();
			break;
		case R.id.content:
			contentWord();
			break;
		default:
			break;
		}

	}

}
