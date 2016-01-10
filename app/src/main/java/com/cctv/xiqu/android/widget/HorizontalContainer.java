package com.cctv.xiqu.android.widget;
import com.cctv.xiqu.android.R;

import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.View.OnClickListener;;

public class HorizontalContainer extends LinearLayout implements OnClickListener{
	
	public static interface OnItemClickListener2{
		public void onitemclick(int position, View view);
	}
	
	public HorizontalContainer(Context context) {
		super(context);
		_init();
	}
	
	@SuppressLint("NewApi") public HorizontalContainer(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		_init();
	}
	
	public HorizontalContainer(Context context, AttributeSet attrs) {
		super(context, attrs);
		_init();
	}

	private int width;
	
	private int index;
	
	public int getSWidth() {
		return width;
	}
	
	private OnItemClickListener2 onItemClickListener2;
	
	public void setOnItemClickListener2(
			OnItemClickListener2 onItemClickListener2) {
		this.onItemClickListener2 = onItemClickListener2;
	}
	
	private void _init() {
		WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		width = display.getWidth()/7;
	}
	
	public void init(){
		RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) getLayoutParams();
		lp.leftMargin = width*3;
		setLayoutParams(lp);
	}
	
	public void addView(CharSequence sequence){
		TextView textView = new TextView(getContext());
		textView.setLayoutParams(new LayoutParams(width, LayoutParams.MATCH_PARENT));
		textView.setTextColor(Color.parseColor("#8d8d8d"));
		textView.setGravity(Gravity.CENTER);
		textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
		textView.setText(sequence);
		textView.setOnClickListener(this);
		textView.setTag(index);
		addView(textView);
		index++;
	}
	
	private TextView lastView;
	
	public void translateTo(int index){
		if(lastView != null){
			lastView.setTextColor(Color.parseColor("#8d8d8d"));
		}
		TextView view = (TextView) getChildAt(index);
		view.setTextColor(Color.parseColor("#ffffff"));
		lastView = view;
		RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) getLayoutParams();
		ValueAnimator varl = ValueAnimator.ofInt(lp.leftMargin,width*(3-index));
		varl.setDuration(200);
		varl.addUpdateListener(new AnimatorUpdateListener() {

		    @Override
		    public void onAnimationUpdate(ValueAnimator animation) {
		        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) getLayoutParams();
		        lp.setMargins((Integer) animation.getAnimatedValue(), 0, 0, 0);
		        setLayoutParams(lp);      
		    }
		});
		varl.start();
		
	}

	@Override
	public void onClick(View v) {
		int index = (Integer)v.getTag();
		onItemClickListener2.onitemclick(index, v);
		
	}

	
	

}
