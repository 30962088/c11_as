package com.cctv.xiqu.android.widget;

import java.util.List;
import java.util.zip.Inflater;

import com.cctv.xiqu.android.R;




import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

public class PhotoSelectPopupWindow implements OnClickListener{

	public static interface OnItemClickListener{
		public void onItemClick(int id);
	}
	
	private PopupWindow mPopupWindow;
	
	private OnItemClickListener onItemClickListener;
	
	
	
	public PhotoSelectPopupWindow(Context context,OnItemClickListener onItemClickListener,String title) {
		this.onItemClickListener = onItemClickListener;
		View view = LayoutInflater.from(context).inflate(R.layout.photo_select_popup,null);
		view.setOnClickListener(this);
		mPopupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
		mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#4D000000")));
		mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);
        ((TextView)view.findViewById(R.id.title)).setText(title);
        Animation rotation = AnimationUtils.loadAnimation(context, R.anim.slide_in_from_bottom);
        
        View bottomBar = view.findViewById(R.id.popup);
        bottomBar.setOnClickListener(this);
        bottomBar.startAnimation(rotation);
        mPopupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        
        view.findViewById(R.id.take_photo).setOnClickListener(this);
        view.findViewById(R.id.read_photo).setOnClickListener(this);
        view.findViewById(R.id.close).setOnClickListener(this);
	}
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.popupWindow:
			mPopupWindow.dismiss();
			break;
		case R.id.close:
		case R.id.take_photo:
		case R.id.read_photo:
			if(onItemClickListener != null){
				onItemClickListener.onItemClick(v.getId());
			}
			mPopupWindow.dismiss();
			break;
		default:
			break;
		}
		
	}
	
	
	
	

	
}
