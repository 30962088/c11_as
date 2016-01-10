/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.cctv.xiqu.android;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.cctv.xiqu.android.R;
import com.cctv.xiqu.android.R.drawable;


import com.cctv.xiqu.android.APP.DisplayOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.DisplayImageOptions.Builder;
import com.nostra13.universalimageloader.core.assist.DiscCacheUtil;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.sample.HackyViewPager;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Lock/Unlock button is added to the ActionBar. Use it to temporarily disable
 * ViewPager navigation in order to correctly interact with ImageView by
 * gestures. Lock/Unlock state of ViewPager is saved and restored on
 * configuration changes.
 * 
 * Julia Zudikova
 */

public class PhotoViewActivity extends BaseActivity {

	public static class Photo implements Serializable{
		private String thumbnail;
		private String origin;

		public Photo(String thumbnail, String origin) {
			super();
			this.thumbnail = thumbnail;
			this.origin = origin;
		}
	}

	private class Container extends FrameLayout {
		private GestureDetector gestureDetector;

		public Container() {
			super(PhotoViewActivity.this);
			gestureDetector = new GestureDetector(PhotoViewActivity.this,
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

	}

	class SingleTapConfirm extends SimpleOnGestureListener {

		@Override
		public boolean onSingleTapConfirmed(MotionEvent event) {
			finish();
			return true;
		}
	}

	public static void open(Context context, ArrayList<Photo> photos) {
		Intent intent = new Intent(context, PhotoViewActivity.class);
		intent.putExtra("photos", photos);
		context.startActivity(intent);
		((Activity) context).overridePendingTransition(R.anim.fade_in,
				R.anim.fade_out);
	}

	private static final String ISLOCKED_ARG = "isLocked";

	private ViewPager mViewPager;

	private ArrayList<Photo> photos;

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		photos = (ArrayList<Photo>) getIntent().getSerializableExtra("photos");
		mViewPager = (HackyViewPager) LayoutInflater.from(this).inflate(
				R.layout.photoview_layout, null);
		Container container = new Container();
		container.addView(mViewPager);
		setContentView(container);
		mViewPager.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});
		mViewPager.setAdapter(new SamplePagerAdapter(photos));

		if (savedInstanceState != null) {
			boolean isLocked = savedInstanceState.getBoolean(ISLOCKED_ARG,
					false);
			((HackyViewPager) mViewPager).setLocked(isLocked);
		}
	}

	static class SamplePagerAdapter extends PagerAdapter {

		public List<Photo> photos;

		public SamplePagerAdapter(List<Photo> photos) {
			super();
			this.photos = photos;
		}

		@Override
		public int getCount() {
			return photos.size();
		}

		@Override
		public View instantiateItem(ViewGroup container, int position) {
			Photo photo = photos.get(position);
			View view = LayoutInflater.from(container.getContext()).inflate(
					R.layout.photoview_item, null);
			final PhotoView photoView = (PhotoView) view.findViewById(R.id.photoview);
			photoView.setVisibility(View.GONE);
			final View loading = view.findViewById(R.id.loading);
			loading.setVisibility(View.VISIBLE);
			final ImageView thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
			ImageLoader.getInstance().displayImage(photo.thumbnail, thumbnail,DisplayOptions.IMG.getOptions());
			ImageLoader.getInstance().displayImage(
					photo.origin,
					photoView,
					DisplayOptions.IMG.getOptions(),
					new SimpleImageLoadingListener() {
						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {
							loading.setVisibility(View.GONE);
							thumbnail.setVisibility(View.GONE);
							photoView.setVisibility(View.VISIBLE);
						}
					});

			// Now just add PhotoView to ViewPager and return it
			container.addView(photoView, LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);

			return photoView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return true;
	}

	private boolean isViewPagerActive() {
		return (mViewPager != null && mViewPager instanceof HackyViewPager);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		if (isViewPagerActive()) {
			outState.putBoolean(ISLOCKED_ARG,
					((HackyViewPager) mViewPager).isLocked());
		}
		super.onSaveInstanceState(outState);
	}

}
