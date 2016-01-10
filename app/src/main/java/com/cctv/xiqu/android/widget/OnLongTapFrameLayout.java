package com.cctv.xiqu.android.widget;

import com.cctv.xiqu.android.PhotoViewActivity;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.widget.FrameLayout;

public class OnLongTapFrameLayout extends FrameLayout{

	public OnLongTapFrameLayout(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public OnLongTapFrameLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public OnLongTapFrameLayout(Context context) {
		super(context);
		init();
	}

	private void init() {
		gestureDetector = new GestureDetector(getContext(),
				new SingleTapConfirm());
		
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (gestureDetector.onTouchEvent(ev)) {
			return true;
		} else {
			return false;
		}
	}
	
	public OnLongClickListener onLongClickListener1;
	
	public void setOnLongClickListener1(OnLongClickListener onLongClickListener1) {
		this.onLongClickListener1 = onLongClickListener1;
	}
	
	private GestureDetector gestureDetector;
	
	
	
	class SingleTapConfirm extends SimpleOnGestureListener {

		@Override
		public void onLongPress(MotionEvent e) {
			if(onLongClickListener1 != null){
				onLongClickListener1.onLongClick(OnLongTapFrameLayout.this);
			}
		}
	}
	
}
