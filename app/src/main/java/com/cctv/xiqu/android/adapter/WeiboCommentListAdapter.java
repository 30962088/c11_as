package com.cctv.xiqu.android.adapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.cctv.xiqu.android.WebViewActivity;
import com.cctv.xiqu.android.APP.DisplayOptions;
import com.cctv.xiqu.android.adapter.WeiboCommentListAdapter.TitleItem.Current;
import com.cctv.xiqu.android.utils.WeiboUtils;
import com.cctv.xiqu.android.utils.WeiboUtils.OnSymbolClickLisenter;
import com.cctv.xiqu.android.utils.WeiboUtils.Synbol;
import com.cctv.xiqu.android.utils.WeiboUtils.WeiboSymboResult;
import com.cctv.xiqu.android.utils.WeiboUtils.WeiboSymbol;
import com.hb.views.PinnedSectionListView.PinnedSectionListAdapter;
import com.mengle.lib.utils.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;

import com.cctv.xiqu.android.R;

import android.content.Context;
import android.graphics.Rect;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SectionIndexer;

import android.widget.TextView;

public class WeiboCommentListAdapter extends BaseAdapter implements
		PinnedSectionListAdapter {

	public static final int ITEM = 0;

	public static final int SECTION = 1;

	public static class CommentItem implements Serializable {

		private String avatar;

		private String name;

		private String content;

		private String time;

		private SpannableString spannableString;

		public CommentItem(String avatar, String name, String content,
				String time) {
			super();
			this.avatar = avatar;
			this.name = name;
			this.content = content;
			this.time = time;
		}
		
		private transient Context context;
		
		public void setContext(Context context) {
			this.context = context;
		}

		public SpannableString getSpannableString() {
			if (spannableString == null) {
				ArrayList<WeiboSymboResult> list = WeiboUtils.build(content,
						new OnSymbolClickLisenter() {

							@Override
							public void OnSymbolClick(WeiboSymbol symbol) {
								if(symbol.getSymbol() == Synbol.URL){
									WebViewActivity.open(context, symbol.getText());
								}

							}
						});
				this.spannableString = new SpannableString(content);
				for (WeiboSymboResult result : list) {
					this.spannableString.setSpan(result.getClickableString(),
							result.getStart(), result.getEnd(),
							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
			}
			return spannableString;
		}

	}

	public static interface OnTitleClickListener {
		public void onCommentClick();

		public void onShareClick();
	}

	public static class TitleItem implements Serializable {

		public enum Current {
			Share, Comment
		}

		private long share;

		private long comment;

		private Current current = Current.Comment;

		public void setShare(long share) {
			this.share = share;
		}

		public void setComment(long comment) {
			this.comment = comment;
		}

	}

	public static class Model implements Serializable {
		private TitleItem titleItem;
		private List<CommentItem> list;

		public Model(TitleItem titleItem, List<CommentItem> list) {
			super();
			this.titleItem = titleItem;
			this.list = list;
		}

		private List<Object> toDatas() {
			List<Object> datas = new ArrayList<Object>();
			datas.add(titleItem);
			datas.addAll(list);
			return datas;
		}

	}

	private OnTitleClickListener onTitleClickListener;

	private Context context;

	private List<Object> datas;

	private Model model;

	public WeiboCommentListAdapter(Context context, Model model,
			OnTitleClickListener onTitleClickListener) {
		super();
		this.onTitleClickListener = onTitleClickListener;
		this.context = context;
		this.model = model;
		this.datas = model.toDatas();
	}

	@Override
	public void notifyDataSetChanged() {
		this.datas = model.toDatas();
		super.notifyDataSetChanged();
	}

	@Override
	public int getCount() {

		return datas.size();
	}

	@Override
	public Object getItem(int position) {

		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	private Current current;
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			View commentItem = LayoutInflater.from(context).inflate(
					R.layout.comment_item, null);
			commentItem.findViewById(R.id.comment_btn).setVisibility(View.GONE);
			holder.commentItemHolder = new CommentItemHolder(commentItem);
			holder.commentItemHolder.container.setTag(holder);
			View titleItem = LayoutInflater.from(context).inflate(
					R.layout.comment_header, null);

			holder.titleItemHolder = new TitleItemHolder(titleItem);

			holder.titleItemHolder.comment
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

							model.titleItem.current = Current.Comment;
							
							onTitleClickListener.onCommentClick();

						}
					});
			holder.titleItemHolder.share
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							model.titleItem.current = Current.Share;
							
							onTitleClickListener.onShareClick();

						}
					});
			holder.titleItemHolder.container.setTag(holder);
			if (isItemViewTypePinned(getItemViewType(position))) {
				convertView = holder.titleItemHolder.container;
			} else {
				convertView = holder.commentItemHolder.container;
			}

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (isItemViewTypePinned(getItemViewType(position))) {
			final TitleItem item = (TitleItem) datas.get(position);

			
			
			

			holder.titleItemHolder.comment.setText("评论" + item.comment);

			holder.titleItemHolder.share.setText("转发" + item.share);
			

			if(current != item.current){
				Direct direct = null;
				
				if (item.current == Current.Comment) {
					direct = Direct.Right;
					holder.titleItemHolder.comment.setSelected(true);
					holder.titleItemHolder.share.setSelected(false);
				} else {
					direct = Direct.Left;
					holder.titleItemHolder.comment.setSelected(false);
					holder.titleItemHolder.share.setSelected(true);
				}
				
				new HeaderAnimation(holder.titleItemHolder, direct).start();
			}

		} else {
			CommentItem item = (CommentItem) datas.get(position);

			item.setContext(context);
			
			holder.commentItemHolder.name.setText(item.name);

			holder.commentItemHolder.content.setText(item.getSpannableString());

			holder.commentItemHolder.time.setText(item.time);

			ImageLoader.getInstance().displayImage(item.avatar,
					holder.commentItemHolder.avatar,
					DisplayOptions.IMG.getOptions());

		}

		return convertView;
	}

	public static class ViewHolder {
		private CommentItemHolder commentItemHolder;
		private TitleItemHolder titleItemHolder;

	}

	public static class CommentItemHolder {

		private ImageView avatar;

		private TextView name;

		private TextView content;

		private TextView time;

		private View container;

		public CommentItemHolder(View view) {
			container = view;
			name = (TextView) view.findViewById(R.id.name);
			avatar = (ImageView) view.findViewById(R.id.avatar);
			content = (TextView) view.findViewById(R.id.content);
			time = (TextView) view.findViewById(R.id.time);

		}

	}

	public static class TitleItemHolder {
		private TextView share;
		private TextView comment;
		private View container;
		private View tag;

		public TitleItemHolder(View view) {
			container = view;
			share = (TextView) view.findViewById(R.id.share);
			comment = (TextView) view.findViewById(R.id.comment);
			tag = view.findViewById(R.id.tag);
		}

	}

	@Override
	public boolean isItemViewTypePinned(int viewType) {
		// TODO Auto-generated method stub
		return viewType == SECTION;
	}

	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public int getItemViewType(int position) {
		Object object = datas.get(position);
		int type = 0;
		if (object instanceof TitleItem) {
			type = SECTION;
		} else {
			type = ITEM;
		}
		return type;
	}

	public enum Direct{
		Left,Right
	}
	
	public  class HeaderAnimation implements AnimationListener{
		

		
		private View tag;
		
		private Animation animation;
		
		private Direct direct;
		
		private TitleItemHolder holder;
		
		public HeaderAnimation(TitleItemHolder holder,Direct direct) {
			this.direct = direct;
			this.holder = holder;
			tag = holder.tag;
			
			holder.comment.measure(View.MeasureSpec.UNSPECIFIED,
					View.MeasureSpec.UNSPECIFIED);
			holder.share.measure(View.MeasureSpec.UNSPECIFIED,
					View.MeasureSpec.UNSPECIFIED);
			
			
			
			
			LayoutParams params = (LayoutParams) tag.getLayoutParams();
			if(direct == Direct.Right){
				params.leftMargin = Utils.dpToPx(tag.getContext(), 32)+holder.share.getMeasuredWidth() +holder.comment.getMeasuredWidth()/2;
//				tag.setLeft(holder.comment.getLeft()+holder.comment.getMeasuredWidth()/2);
			}else{
				params.leftMargin = Utils.dpToPx(tag.getContext(), 22)+holder.share.getMeasuredWidth()/2;
			}
			tag.setLayoutParams(params);
//			tag.layout(params.leftMargin, tag.getTop(), tag.getRight(), tag.getBottom());
			
			/*holder.container.measure(View.MeasureSpec.UNSPECIFIED,
					View.MeasureSpec.UNSPECIFIED);
			holder.container.requestLayout();
			holder.container.invalidate();*/
//			tag.setLayoutParams(params);
			
			/*int distance = holder.comment.getLeft()+holder.comment.getMeasuredWidth() - holder.share.getLeft()-holder.share.getMeasuredWidth();
			
			if(direct == Direct.Left){
				distance = -distance;
			}
			
			animation = new TranslateAnimation(0, distance, 0, 0);
			animation.setFillBefore(false);
			animation.setDuration(200);
			animation.setFillAfter(true);
			animation.setAnimationListener(this);*/
		}
		
		public void start(){
//			tag.startAnimation(animation);
		}

//		@Override
		public void onAnimationStart(Animation animation) {
			// TODO Auto-generated method stub
			
		}

//		@Override
		public void onAnimationEnd(Animation animation) {
			LayoutParams params = (LayoutParams) tag.getLayoutParams();
			if(direct == Direct.Right){
				params.leftMargin = holder.comment.getLeft()+holder.comment.getMeasuredWidth()/2;
//				tag.setLeft(holder.comment.getLeft()+holder.comment.getMeasuredWidth()/2);
			}else{
				params.leftMargin = holder.share.getLeft()+holder.share.getMeasuredWidth()/2;
			}
			
			tag.setLayoutParams(params);
		}

//		@Override
		public void onAnimationRepeat(Animation animation) {
			// TODO Auto-generated method stub
			
		}
		
		
		
	}


}
