package com.cctv.xiqu.android.widget;

import com.cctv.xiqu.android.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;

public class NoResultView extends FrameLayout implements OnClickListener{

	public static interface OnRefreshClickListener{
		public void onrefreshclick();
	}
	
	public NoResultView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public NoResultView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public NoResultView(Context context) {
		super(context);
		init();
	}

	private void init() {
		LayoutInflater.from(getContext()).inflate(R.layout.noresult_layout, this);
		findViewById(R.id.refresh_btn).setOnClickListener(this);
	}
	
	private OnRefreshClickListener onRefreshClickListener;
	
	public void setOnRefreshClickListener(
			OnRefreshClickListener onRefreshClickListener) {
		this.onRefreshClickListener = onRefreshClickListener;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.refresh_btn:
			if(onRefreshClickListener != null){
				onRefreshClickListener.onrefreshclick();
			}
			break;

		default:
			break;
		}
		
	}

	
	
}
