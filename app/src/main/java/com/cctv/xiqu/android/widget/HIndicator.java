package com.cctv.xiqu.android.widget;

import com.cctv.xiqu.android.widget.HorizontalContainer.OnItemClickListener2;
import com.mengle.lib.utils.Utils;

import com.cctv.xiqu.android.R;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

public class HIndicator extends FrameLayout implements OnPageChangeListener,OnItemClickListener2{

	public HIndicator(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public HIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public HIndicator(Context context) {
		super(context);
		init();
	}

	private HorizontalContainer container;
	
	private View pop;
	
	private void init() {
		LayoutInflater.from(getContext()).inflate(R.layout.hindicator, this);
		pop = findViewById(R.id.pop);
		container = (HorizontalContainer) findViewById(R.id.container);
		container.setOnItemClickListener2(this);
		container.init();
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) (container.getSWidth()*1.5), LayoutParams.MATCH_PARENT);
		params.topMargin = Utils.dpToPx(getContext(), 5);
		params.bottomMargin = Utils.dpToPx(getContext(), 5);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		pop.setLayoutParams(params);
	}
	
	private ViewPager viewPager;
	
	public void setViewPager(ViewPager viewPager){
		this.viewPager = viewPager;
		PagerAdapter adapter = viewPager.getAdapter();
		
		for(int i = 0;i<adapter.getCount();i++){
			CharSequence title = adapter.getPageTitle(i);
			container.addView(title);
		}
		
		viewPager.setOnPageChangeListener(this);
		
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageSelected(int position) {
		container.translateTo(position);
		
	}

	@Override
	public void onitemclick(int position, View view) {
		viewPager.setCurrentItem(position);
		
	}

	
	
}
