package com.cctv.xiqu.android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

public class VideoHeadRelativeLayout extends RelativeLayout {


	public VideoHeadRelativeLayout(Context context) {
		super(context);
	}

	public VideoHeadRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	public VideoHeadRelativeLayout(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);

	
	}

	// **overrides**

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int originalWidth = MeasureSpec.getSize(widthMeasureSpec);


		int calculatedHeight = originalWidth /4*3;

		

		super.onMeasure(
				MeasureSpec.makeMeasureSpec(originalWidth, MeasureSpec.EXACTLY),
				MeasureSpec.makeMeasureSpec(calculatedHeight, MeasureSpec.EXACTLY));
	}
}