package com.cctv.xiqu.android.widget;

import java.util.List;

import com.cctv.xiqu.android.R;




import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.PopupWindow;
import android.widget.TextView;

public class IOSPopupWindow implements OnClickListener{

	public static interface OnIOSItemClickListener{
		public void oniositemclick(int pos, String text);
	}
	
	public static class Params{
		private String title;
		private List<String> list;
		private OnIOSItemClickListener onIOSItemClickListener;
		public Params(List<String> list,
				OnIOSItemClickListener onIOSItemClickListener) {
			super();
			this.list = list;
			this.onIOSItemClickListener = onIOSItemClickListener;
		}
		public Params(String title, List<String> list,
				OnIOSItemClickListener onIOSItemClickListener) {
			super();
			this.title = title;
			this.list = list;
			this.onIOSItemClickListener = onIOSItemClickListener;
		}
		
		
	}
	
	private PopupWindow mPopupWindow;
	
	
	
	public IOSPopupWindow(Context context,final Params params) {
		View view = LayoutInflater.from(context).inflate(R.layout.ios_popup,null);
		View titleContainer = view.findViewById(R.id.titleContainer);
		TextView titleView = (TextView) view.findViewById(R.id.title);
		if(params.title == null){
			titleContainer.setVisibility(View.GONE);
		}else{
			titleContainer.setVisibility(View.VISIBLE);
			titleView.setText(params.title);
		}
		ViewGroup container = (ViewGroup) view.findViewById(R.id.container);
		for(int i = 0;i<params.list.size();i++){
			View item = LayoutInflater.from(context).inflate(R.layout.ios_popup_item,null);
			TextView textView = (TextView) item.findViewById(R.id.text);
			final String text = params.list.get(i);
			if(TextUtils.equals("删除", text)){
				textView.setTextColor(Color.parseColor("#e82321"));
			}
			textView.setText(text);
			View sep = item.findViewById(R.id.sep);
			if(i != params.list.size() -1){
				sep.setVisibility(View.VISIBLE);
			}else{
				sep.setVisibility(View.GONE);
			}
			container.addView(item);
			final int index = i; 
			item.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					params.onIOSItemClickListener.oniositemclick(index, text);
					mPopupWindow.dismiss();
				}
			});
		}
		view.setOnClickListener(this);
		mPopupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
		mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#4D000000")));
		mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);
        Animation rotation = AnimationUtils.loadAnimation(context, R.anim.slide_in_from_bottom);
        
        View bottomBar = view.findViewById(R.id.popup);
        bottomBar.setOnClickListener(this);
        bottomBar.startAnimation(rotation);
        mPopupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        view.findViewById(R.id.close).setOnClickListener(this);
	}
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.popupWindow:
			mPopupWindow.dismiss();
			break;
		case R.id.close:
			mPopupWindow.dismiss();
			break;
		default:
			break;
		}
		
	}
	
	
	
	

	
}
