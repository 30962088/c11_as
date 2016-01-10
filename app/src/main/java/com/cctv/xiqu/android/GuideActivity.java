package com.cctv.xiqu.android;

import java.util.ArrayList;
import java.util.List;

import com.cctv.xiqu.android.adapter.TabsAdapter;
import com.cctv.xiqu.android.adapter.TabsAdapter.Pager;
import com.cctv.xiqu.android.fragment.BaseFragment;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.viewpagerindicator.CirclePageIndicator;

import com.cctv.xiqu.android.R;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

public class GuideActivity extends BaseActivity{
	
	public static void open(Context context){
		context.startActivity(new Intent(context, GuideActivity.class));
	}
	
	public static class ButtonFragment extends BaseFragment{
		
		public static ButtonFragment newInstance(String image){
			ButtonFragment fragment = new ButtonFragment();
			Bundle args = new Bundle();
			args.putString("uri", image.toString());
			fragment.setArguments(args);
			return fragment;
		}
		private String url;
		
		private OnClickListener onClickListener;
		
		public void setOnClickListener(OnClickListener onClickListener) {
			this.onClickListener = onClickListener;
		}
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			url = getArguments().getString("uri");
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			return inflater.inflate(R.layout.guide_item2, null);
		}
		
		@Override
		public void onViewCreated(View view, Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onViewCreated(view, savedInstanceState);
			ImageView imageView = (ImageView) view.findViewById(R.id.img);
			ImageLoader.getInstance().displayImage(url, imageView,APP.DisplayOptions.IMG.getOptions());
			view.findViewById(R.id.enter).setOnClickListener(onClickListener);
		}
		
	}
	
	public static class ImageFragment extends BaseFragment{
		
		public static ImageFragment newInstance(String image){
			ImageFragment fragment = new ImageFragment();
			Bundle args = new Bundle();
			args.putString("uri", image.toString());
			fragment.setArguments(args);
			return fragment;
		}
		private String url;
		@Override
		public void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			url = getArguments().getString("uri");
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			return inflater.inflate(R.layout.guide_item, null);
		}
		
		@Override
		public void onViewCreated(View view, Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onViewCreated(view, savedInstanceState);
			ImageView imageView = (ImageView) view.findViewById(R.id.img);
			ImageLoader.getInstance().displayImage(url, imageView,APP.DisplayOptions.IMG.getOptions());
		}
		
	}
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.guide_layout);
		ViewPager pager = (ViewPager) findViewById(R.id.viewPager);
		pager.setOffscreenPageLimit(5);
		CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.indicator);
		List<Pager> list = new ArrayList<Pager>(){{
			add(new Pager("", ImageFragment.newInstance("drawable://"+R.drawable.guide_1)));
			add(new Pager("", ImageFragment.newInstance("drawable://"+R.drawable.guide_2)));
			ButtonFragment fragment = ButtonFragment.newInstance("drawable://"+R.drawable.guide_3);
			fragment.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					finish();
				}
			});
			add(new Pager("", fragment));
		}};
		pager.setAdapter(new TabsAdapter(getSupportFragmentManager(),list));
		indicator.setViewPager(pager);
	}

}
