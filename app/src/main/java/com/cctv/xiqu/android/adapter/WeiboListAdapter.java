package com.cctv.xiqu.android.adapter;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cctv.xiqu.android.PhotoViewActivity;
import com.cctv.xiqu.android.WeiboDetailActivity;
import com.cctv.xiqu.android.APP.DisplayOptions;
import com.cctv.xiqu.android.SpecialDetailActivity.Params;
import com.cctv.xiqu.android.utils.WeiboUtils;
import com.cctv.xiqu.android.utils.WeiboUtils.OnSymbolClickLisenter;
import com.cctv.xiqu.android.utils.WeiboUtils.Synbol;
import com.cctv.xiqu.android.utils.WeiboUtils.WeiboSymboResult;
import com.cctv.xiqu.android.utils.WeiboUtils.WeiboSymbol;
import com.cctv.xiqu.android.utils.WeiboUtils.WeiboText;
import com.cctv.xiqu.android.widget.WeiboItemView;
import com.cctv.xiqu.android.widget.WeiboItemView.Model;
import com.cctv.xiqu.android.widget.WeiboItemView.OnWeiboItemClickListener;
import com.cctv.xiqu.android.widget.WeiboItemView.ViewHolder;
import com.hb.views.PinnedSectionListView.PinnedSectionListAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.DiscCacheUtil;

import com.cctv.xiqu.android.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import android.widget.TextView;

public class WeiboListAdapter extends BaseAdapter implements Serializable,PinnedSectionListAdapter {

	

	private Context context;

	private List<Model> list;
	
	private OnWeiboItemClickListener onWeiboItemClickListener;

	public WeiboListAdapter(Context context, List<Model> list,OnWeiboItemClickListener onWeiboItemClickListener) {
		super();
		this.context = context;
		this.list = list;
		this.onWeiboItemClickListener = onWeiboItemClickListener;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		final Model model = list.get(position);
		if (convertView == null) {
			WeiboItemView itemView = new WeiboItemView(context);
			itemView.setOnWeiboItemClickListener(onWeiboItemClickListener);
			convertView = itemView;
			
		}
		
		WeiboItemView itemView = (WeiboItemView) convertView;
		/*itemView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				WeiboDetailActivity.open(context, model);
				
			}
		});*/
		itemView.setModel(model);


		return convertView;
	}

	@Override
	public boolean isItemViewTypePinned(int viewType) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 2;
	}

	


	
}
