package com.cctv.xiqu.android.adapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.cctv.xiqu.android.APP;
import com.cctv.xiqu.android.APP.DisplayOptions;
import com.cctv.xiqu.android.adapter.BBSListAdapter.ViewHolder;
import com.cctv.xiqu.android.adapter.NewsCommentListAdapter.Model;
import com.cctv.xiqu.android.adapter.NewsCommentListAdapter.OnCommentBtnClickListener;
import com.cctv.xiqu.android.fragment.network.InsertcommentreportRequest;
import com.cctv.xiqu.android.fragment.network.BaseClient.RequestHandler;
import com.cctv.xiqu.android.fragment.network.InsertcommentreportRequest.Params;
import com.cctv.xiqu.android.widget.BBSDetailHeaderView;
import com.cctv.xiqu.android.widget.BBSHeaderView;
import com.cctv.xiqu.android.widget.IOSPopupWindow;
import com.hb.views.PinnedSectionListView.PinnedSectionListAdapter;
import com.mengle.lib.utils.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

import com.cctv.xiqu.android.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;

import android.widget.TextView;

public class BBSCommentListAdapter extends BaseAdapter implements
		PinnedSectionListAdapter {

	private Context context;

	private List<Object> list;

	private OnCommentBtnClickListener onCommentBtnClickListener;

	public BBSCommentListAdapter(Context context, List<Object> list,
			OnCommentBtnClickListener onCommentBtnClickListener) {
		super();
		this.context = context;
		this.list = list;
		this.onCommentBtnClickListener = onCommentBtnClickListener;
	}

	@Override
	public int getCount() {

		return list.size();
	}

	@Override
	public Object getItem(int position) {

		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final Object obj = list.get(position);

		if (obj instanceof BBSDetailHeaderView.Model) {
			final BBSDetailHeaderView.Model headerModel = (BBSDetailHeaderView.Model) obj;
			convertView = LayoutInflater.from(context).inflate(
					R.layout.bbs_detail_header, null);
			TextView titleView = (TextView) convertView
					.findViewById(R.id.title);
			ImageView avatarView = (ImageView) convertView
					.findViewById(R.id.avatar);
			TextView nameView = (TextView) convertView.findViewById(R.id.name);
			final ImageView imgView = (ImageView) convertView
					.findViewById(R.id.img);
			TextView timeView = (TextView) convertView.findViewById(R.id.time);
			TextView contentView = (TextView) convertView
					.findViewById(R.id.content);
			titleView.setText(headerModel.getTitle());
			ImageLoader.getInstance().displayImage(headerModel.getAvatar(),
					avatarView, DisplayOptions.IMG.getOptions());
			nameView.setText(headerModel.getName());
			timeView.setText(headerModel.getTime());
			contentView.setText(headerModel.getContent());
			imgView.setVisibility(View.GONE);
			if (!TextUtils.isEmpty(headerModel.getImg())) {

				ImageLoader.getInstance().loadImage(headerModel.getImg(),APP.DisplayOptions.IMG.getOptions(),
						new ImageLoadingListener() {

							@Override
							public void onLoadingStarted(String imageUri,
									View view) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onLoadingFailed(String imageUri,
									View view, FailReason failReason) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onLoadingComplete(String imageUri,
									View view, Bitmap loadedImage) {
								imgView.setVisibility(View.VISIBLE);
								imgView.setImageBitmap(loadedImage);

							}

							@Override
							public void onLoadingCancelled(String imageUri,
									View view) {
								// TODO Auto-generated method stub

							}
						});
			}

		} else if (obj instanceof NewsCommentListAdapter.Model) {
			final NewsCommentListAdapter.Model model = (NewsCommentListAdapter.Model) obj;

			convertView = LayoutInflater.from(context).inflate(
					R.layout.new_comment_item, null);

			ViewHolder holder = new ViewHolder(convertView);

			holder.content.setText(Html.fromHtml(model.getContent()));

			holder.name.setText(model.getName());

			holder.time.setText(model.getTime());

			holder.commentBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					onCommentBtnClickListener.onCommentBtnClick(model);

				}
			});

			ImageLoader.getInstance().displayImage(model.getAvatar(),
					holder.avatar, DisplayOptions.IMG.getOptions());

			holder.jubaoBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					onjubao(model);

				}

			});
		}

		return convertView;
	}

	private void onjubao(final Model model) {
		new IOSPopupWindow(context, new IOSPopupWindow.Params("请输入举报理由",
				new ArrayList<String>() {
					{
						add("营销诈骗");
						add("淫秽色情");
						add("地域攻击");
						add("其他理由");
					}
				}, new IOSPopupWindow.OnIOSItemClickListener() {

					@Override
					public void oniositemclick(int pos, String text) {
						InsertcommentreportRequest request = new InsertcommentreportRequest(
								context, new InsertcommentreportRequest.Params(
										text, model.getId(), model
												.getReportType(), APP
												.getSession().getSid(), APP
												.getSession().getPkey()));
						request.request(new RequestHandler() {

							@Override
							public void onSuccess(Object object) {
								Utils.tip(context, "举报成功");

							}

							@Override
							public void onError(int error, String msg) {
								Utils.tip(context, "举报失败");

							}

							@Override
							public void onComplete() {
								// TODO Auto-generated method stub

							}
						});
					}

				}));

	}

	public static class ViewHolder {

		private ImageView avatar;

		private TextView name;

		private TextView content;

		private TextView time;

		private View commentBtn;

		private View jubaoBtn;

		public ViewHolder(View view) {
			name = (TextView) view.findViewById(R.id.name);
			avatar = (ImageView) view.findViewById(R.id.avatar);
			content = (TextView) view.findViewById(R.id.content);
			time = (TextView) view.findViewById(R.id.time);
			commentBtn = view.findViewById(R.id.comment_btn);
			jubaoBtn = view.findViewById(R.id.jubao_btn);
		}

	}

	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public boolean isItemViewTypePinned(int viewType) {
		// TODO Auto-generated method stub
		return false;
	}

}
