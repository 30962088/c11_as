package com.cctv.xiqu.android.widget;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.cctv.xiqu.android.APP;
import com.cctv.xiqu.android.PhotoViewActivity;
import com.cctv.xiqu.android.WebViewActivity;
import com.cctv.xiqu.android.APP.DisplayOptions;
import com.cctv.xiqu.android.adapter.WeiboImgAdapter;
import com.cctv.xiqu.android.utils.WeiboUtils;
import com.cctv.xiqu.android.utils.WeiboUtils.OnSymbolClickLisenter;
import com.cctv.xiqu.android.utils.WeiboUtils.Synbol;
import com.cctv.xiqu.android.utils.WeiboUtils.WeiboSymboResult;
import com.cctv.xiqu.android.utils.WeiboUtils.WeiboSymbol;
import com.mengle.lib.utils.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;

import com.cctv.xiqu.android.R;
import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class WeiboItemView extends FrameLayout{

	public static interface OnWeiboItemClickListener{
		public void onCommentClick(Model model);
		public void onItemClick(Model model);
	}
	
	public WeiboItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public WeiboItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public WeiboItemView(Context context) {
		super(context);
		init();
	}

	private OnWeiboItemClickListener onWeiboItemClickListener;
	
	public void setOnWeiboItemClickListener(
			OnWeiboItemClickListener onWeiboItemClickListener) {
		this.onWeiboItemClickListener = onWeiboItemClickListener;
	}
	
	public static class Count implements Serializable{
		private int share;
		private int comment;
		public Count(int share, int comment) {
			super();
			this.share = share;
			this.comment = comment;
		}
		public int getShare() {
			return share;
		}
		public int getComment() {
			return comment;
		}
	}
	
	public static class Model implements Serializable {
		private String id;
		private String avatar;
		private String name;
		private String time;
		private Content content;
		private Content retweetedContent;
		public Model(String id,String avatar, String name, String time, Content content,
				Content retweetedContent) {
			super();
			this.id = id;
			this.avatar = avatar;
			this.name = name;
			this.time = time;
			this.content = content;
			this.retweetedContent = retweetedContent;
		}
		
		public Content getContent() {
			return content;
		}
		public String getId() {
			return id;
		}

		

	}

	private ViewHolder holder;

	private void init() {
		LayoutInflater.from(getContext()).inflate(R.layout.weibo_item, this);
		
		holder = new ViewHolder();

	}
	
	private static class MyClickSpan extends ClickableSpan{

		private OnClickListener onClickListener;
		
		public MyClickSpan(OnClickListener onClickListener) {
			super();
			this.onClickListener = onClickListener;
		}

		@Override
		public void onClick(View widget) {
			onClickListener.onClick(widget);
		}
		@Override
		public void updateDrawState(TextPaint ds) {
			/*// TODO Auto-generated method stub
			super.updateDrawState(ds);
			ds.setColor(Color.BLACK);
			ds.setUnderlineText(false);*/
		}
		
	}

	public void setModel(final Model model) {
		
		
		
		holder.commentBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(APP.getSession().getWeiboAccessToken() == null){
					Utils.tip(getContext(), "请绑定新浪微博");
					return;
				}
				if(onWeiboItemClickListener != null){
					onWeiboItemClickListener.onCommentClick(model);
				}
				
			}
		});
		
		holder.container.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(onWeiboItemClickListener != null){
					onWeiboItemClickListener.onItemClick(model);
				}
			}
		});
		
		holder.name.setText(model.name);

		holder.time.setText(model.time);

		ImageLoader.getInstance().displayImage(model.avatar, holder.avatar,
				DisplayOptions.IMG.getOptions());
		model.content.setContext(getContext());
		SpannableString spannableString = model.content.getSpannableString();
		spannableString.setSpan(new MyClickSpan(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(onWeiboItemClickListener != null){
					onWeiboItemClickListener.onItemClick(model);
				}
				
			}
		}), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		holder.text.setText(spannableString);
		
		
		holder.retweetedPhotogrid.setVisibility(View.GONE);

		holder.photogrid.setVisibility(View.GONE);

		if (model.retweetedContent != null) {
			holder.photogrid.setVisibility(View.GONE);
			holder.retweeted.setVisibility(View.VISIBLE);
			model.retweetedContent.setContext(getContext());
			SpannableString spannableString1 = model.retweetedContent.getSpannableString();
			spannableString1.setSpan(new MyClickSpan(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(onWeiboItemClickListener != null){
						onWeiboItemClickListener.onItemClick(model);
					}
					
				}
			}), 0, spannableString1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			holder.retweetedText.setText(spannableString1);
//			holder.retweetedText.setText(model.retweetedContent.text);
			holder.retweetedCount.setText("转发（"+model.retweetedContent.count.share+"）| 评论（"+model.retweetedContent.count.comment+"）");
			if (model.retweetedContent.photos != null) {
				holder.retweetedPhotogrid.setVisibility(View.VISIBLE);
				holder.retweetedPhotogrid.setAdapter(new WeiboImgAdapter(
						getContext(), model.retweetedContent.toModel()));
				holder.retweetedPhotogrid
						.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {
								onImageClick(model.retweetedContent.getPhotos());

							}

						});
			}

		} else {
			holder.photogrid.setVisibility(View.VISIBLE);
			holder.retweeted.setVisibility(View.GONE);
			if (model.content.photos != null) {
				holder.photogrid
						.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {
								onImageClick(model.content.getPhotos());

							}

						});
				holder.photogrid.setAdapter(new WeiboImgAdapter(getContext(),
						model.content.toModel()));
			}
		}
	}

	public class ViewHolder {

		private View commentBtn;
		
		private TextView time;

		private TextView name;

		private ImageView avatar;

		private TextView text;

		private GridView photogrid;

		private View retweeted;

		private TextView retweetedText;

		private GridView retweetedPhotogrid;
		
		private TextView retweetedCount;
		
		private View container;

		public ViewHolder() {
			container = findViewById(R.id.container);
			commentBtn = findViewById(R.id.comment_btn);
			time = (TextView) findViewById(R.id.time);
			name = (TextView) findViewById(R.id.name);
			avatar = (ImageView) findViewById(R.id.avatar);
			text = (TextView) findViewById(R.id.text);
			text.setMovementMethod(LinkMovementMethod.getInstance());
			photogrid = (GridView) findViewById(R.id.photogrid);
			retweeted = findViewById(R.id.retweeted);
			retweetedText = (TextView) findViewById(R.id.retweetedText);
			retweetedText.setMovementMethod(LinkMovementMethod.getInstance());
			retweetedPhotogrid = (GridView) findViewById(R.id.retweetedPhotogrid);
			retweetedCount = (TextView) findViewById(R.id.retweetedCount);
		}

	}

	public static class Photo implements Serializable {
		private String url;
		private String real;

		public Photo(String url, String real) {
			super();
			this.url = url;
			this.real = real;
		}

		public WeiboImgAdapter.Model toModel() {
			return new WeiboImgAdapter.Model(url);
		}
	}


	public static class Content implements Serializable {
		private String text;
		private ArrayList<Photo> photos;
		private Count count;
		private transient SpannableString spannableString;
		public Count getCount() {
			return count;
		}
		public List<Photo> getPhotos() {

			return photos;
		}

		public Content(String text, ArrayList<Photo> photos,Count count) {
			super();
			this.text = text;
			this.photos = photos;
			this.count = count;
		}
		
		private transient Context context;
		
		public void setContext(Context context) {
			this.context = context;
		}
		
		public SpannableString getSpannableString() {
			if (spannableString == null) {
				ArrayList<WeiboSymboResult> list = WeiboUtils.build(text,
						new OnSymbolClickLisenter() {

							@Override
							public void OnSymbolClick(WeiboSymbol symbol) {
								if(symbol.getSymbol() == Synbol.URL){
									WebViewActivity.open(context, symbol.getText());
								}

							}
						});
				this.spannableString = new SpannableString(text);
				for (WeiboSymboResult result : list) {
					this.spannableString.setSpan(result.getClickableString(),
							result.getStart(), result.getEnd(),
							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
			}
			return spannableString;
		}

		public List<WeiboImgAdapter.Model> toModel() {
			List<WeiboImgAdapter.Model> list = new ArrayList<WeiboImgAdapter.Model>();
			for (Photo photo : photos) {
				list.add(photo.toModel());
			}
			return list;
		}
	}

	private void onImageClick(List<Photo> imgs) {
		ArrayList<PhotoViewActivity.Photo> list = new ArrayList<PhotoViewActivity.Photo>();

		for (Photo photo : imgs) {

			list.add(new PhotoViewActivity.Photo(photo.url, photo.real));
		}
		PhotoViewActivity.open(getContext(), list);
	}

	

}
