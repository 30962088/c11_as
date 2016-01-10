package com.cctv.xiqu.android.widget;

import com.viewpagerindicator.TabPageIndicator.OnTabScrollListener;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;

public class MyHorizontalScrollView extends HorizontalScrollView{

	public MyHorizontalScrollView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public MyHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MyHorizontalScrollView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public static interface OnTabScrollListener{
    	public void isLastVisible(boolean visible);
    }
    
    private OnTabScrollListener onTabScrollListener;
    
    public void setOnTabScrollListener(OnTabScrollListener onTabScrollListener) {
		this.onTabScrollListener = onTabScrollListener;
	}
	
	@Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        View mTabLayout = getChildAt(0);
        if(onTabScrollListener != null){
//        	int w = mTabLayout.getChildAt(mTabLayout.getChildCount()-1).getMeasuredWidth();
            if(x+getMeasuredWidth()>= mTabLayout.getMeasuredWidth()){
            	onTabScrollListener.isLastVisible(true);
            }else{
            	onTabScrollListener.isLastVisible(false);
            }
        }
        
        
//        System.out.println("zzm:"+x+","+y+","+oldx+","+oldy+","+getMeasuredWidth()+","+mTabLayout.getMeasuredWidth()+","+w);
    }
}
