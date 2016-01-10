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
package com.handmark.pulltorefresh.library.internal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView.ScaleType;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Orientation;

@SuppressLint("ViewConstructor")
public class FlipLoadingLayout extends LoadingLayout {

	static final int FLIP_ANIMATION_DURATION = 150;

	public FlipLoadingLayout(Context context, Mode mode,
			Orientation scrollDirection, TypedArray attrs) {
		super(context, mode, scrollDirection, attrs);

		/*
		 * final int rotateAngle = mode == Mode.PULL_FROM_START ? -180 : 180;
		 * 
		 * mRotateAnimation = new RotateAnimation(0, rotateAngle,
		 * Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		 * mRotateAnimation.setInterpolator(ANIMATION_INTERPOLATOR);
		 * mRotateAnimation.setDuration(FLIP_ANIMATION_DURATION);
		 * mRotateAnimation.setFillAfter(true);
		 * 
		 * mResetRotateAnimation = new RotateAnimation(rotateAngle, 0,
		 * Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		 * mResetRotateAnimation.setInterpolator(ANIMATION_INTERPOLATOR);
		 * mResetRotateAnimation.setDuration(FLIP_ANIMATION_DURATION);
		 * mResetRotateAnimation.setFillAfter(true);
		 */
	}

	@Override
	protected void onLoadingDrawableSet(Drawable imageDrawable) {
		switch (mMode) {
		case PULL_FROM_END:
			mHeaderImage.setSelected(true);
			break;

		case PULL_FROM_START:
			mHeaderImage.setSelected(false);
			break;

		default:
			break;
		}
		/*
		 * if (null != imageDrawable) { final int dHeight =
		 * imageDrawable.getIntrinsicHeight(); final int dWidth =
		 * imageDrawable.getIntrinsicWidth();
		 * 
		 * 
		 * ViewGroup.LayoutParams lp = mHeaderImage.getLayoutParams(); lp.width
		 * = lp.height = Math.max(dHeight, dWidth);
		 * mHeaderImage.requestLayout();
		 * 
		 * 
		 * mHeaderImage.setScaleType(ScaleType.MATRIX); Matrix matrix = new
		 * Matrix(); matrix.postTranslate((lp.width - dWidth) / 2f, (lp.height -
		 * dHeight) / 2f); matrix.postRotate(getDrawableRotationAngle(),
		 * lp.width / 2f, lp.height / 2f); mHeaderImage.setImageMatrix(matrix);
		 * }
		 */
	}

	@Override
	protected void onPullImpl(float scaleOfLayout) {
		// NO-OP
	}

	@Override
	protected void pullToRefreshImpl() {
		/*
		 * // Only start reset Animation, we've previously show the rotate anim
		 * if (mRotateAnimation == mHeaderImage.getAnimation()) {
		 * mHeaderImage.startAnimation(mResetRotateAnimation); }
		 */
		mHeaderImage.setSelected(false);
	}

	@Override
	protected void refreshingImpl() {
		/*// mHeaderImage.clearAnimation();
		mHeaderImage.setVisibility(View.INVISIBLE);*/
		mHeaderImage.setSelected(true);
	}

	@Override
	protected void releaseToRefreshImpl() {
		mHeaderImage.setSelected(true);
	}

	@Override
	protected void resetImpl() {
	/*	// mHeaderImage.clearAnimation();
		mHeaderImage.setVisibility(View.VISIBLE);*/
		mHeaderImage.setSelected(false);
	}

	
}
