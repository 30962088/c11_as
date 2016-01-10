package com.cctv.xiqu.android.adapter;

import java.io.Serializable;
import java.util.List;

import com.cctv.xiqu.android.APP.DisplayOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import com.cctv.xiqu.android.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class WeiboImgAdapter extends BaseAdapter implements Serializable {

	public static class Model implements Serializable {

		private String img;

		public Model(String img) {
			super();
			this.img = img;
		}

	}

	private Context context;

	private List<Model> list;

	public WeiboImgAdapter(Context context, List<Model> list) {
		super();
		this.context = context;
		this.list = list;
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

		Model model = list.get(position);

		if (convertView == null) {
			holder = new ViewHolder();
			View img1view = LayoutInflater.from(context).inflate(
					R.layout.weibo_image1, null);
			holder.image1Holder = new Image1Holder(img1view);
			holder.image1Holder.container.setTag(holder);
			View img2view = LayoutInflater.from(context).inflate(
					R.layout.weibo_image2, null);
			holder.image2Holder = new Image2Holder(img2view);
			holder.image2Holder.container.setTag(holder);
			if (list.size() == 1) {
				convertView = holder.image1Holder.container;
			} else {
				convertView = holder.image2Holder.container;
			}

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		ImageView imageView = null;

		if (list.size() == 1) {

			imageView = holder.image1Holder.img;

		} else {
			imageView = holder.image2Holder.img;
		}

		ImageLoader.getInstance().displayImage(model.img, imageView,
				DisplayOptions.IMG.getOptions());

		return convertView;
	}

	public static class ViewHolder {
		private Image1Holder image1Holder;
		private Image2Holder image2Holder;

	}

	public static class Image1Holder {

		private ImageView img;

		private View container;

		public Image1Holder(View view) {
			img = (ImageView) view.findViewById(R.id.img);
			container = view;
		}

	}

	public static class Image2Holder {

		private ImageView img;

		private View container;

		public Image2Holder(View view) {
			img = (ImageView) view.findViewById(R.id.img);
			container = view;
		}

	}

}
