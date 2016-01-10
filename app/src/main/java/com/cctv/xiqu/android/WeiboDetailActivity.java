package com.cctv.xiqu.android;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import com.cctv.xiqu.android.adapter.WeiboCommentListAdapter;
import com.cctv.xiqu.android.adapter.WeiboCommentListAdapter.CommentItem;
import com.cctv.xiqu.android.adapter.WeiboCommentListAdapter.Model;
import com.cctv.xiqu.android.adapter.WeiboCommentListAdapter.OnTitleClickListener;
import com.cctv.xiqu.android.adapter.WeiboCommentListAdapter.TitleItem;
import com.cctv.xiqu.android.fragment.network.BaseClient;
import com.cctv.xiqu.android.fragment.network.WeiboCommentRequest;
import com.cctv.xiqu.android.fragment.network.WeiboCountRequest;
import com.cctv.xiqu.android.fragment.network.WeiboReportRequest;
import com.cctv.xiqu.android.fragment.network.BaseClient.RequestHandler;
import com.cctv.xiqu.android.widget.WeiboItemView;
import com.cctv.xiqu.android.widget.WeiboItemView.OnWeiboItemClickListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshPinnedSectionListView;

import com.cctv.xiqu.android.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

public class WeiboDetailActivity extends BaseActivity implements
		OnWeiboItemClickListener, OnClickListener, OnLastItemVisibleListener,
		OnRefreshListener<ListView>, OnScrollListener, OnTitleClickListener {
	public static void open(Context context, WeiboItemView.Model model) {
		Intent intent = new Intent(context, WeiboDetailActivity.class);
		intent.putExtra("params", model);
		context.startActivity(intent);
	}

	public static interface WeiboResult {
		public DataSource toDataSource();
	}

	public static interface WeiboRequest {
		public WeiboResult getWeiboResult();
	}

	public static class DataSource {
		private Long total;
		private List<CommentItem> list;

		public DataSource(Long total, List<CommentItem> list) {
			super();
			this.total = total;
			this.list = list;
		}
	}

	private enum WeiboType {
		Comment, Share
	}

	public class WeiboDataSource implements RequestHandler {
		private List<CommentItem> list = new ArrayList<CommentItem>();
		private int current;
		private int page = 1;
		private WeiboType type;

		public WeiboDataSource(WeiboType type) {
			super();
			this.type = type;
		}

		public void init() {
			if (page == 1) {
				request();
			}
		}

		public void add(DataSource dataSource) {

			list.addAll(dataSource.list);
		}

		public void reset() {
			mFooterLoading.setVisibility(View.VISIBLE);
			list.clear();
			current = 0;
			page = 1;

		}

		public void setCurrent(int current) {
			this.current = current;
		}

		public int getCurrent() {
			return current;
		}

		public void request() {
			
			if (access_token != null) {
				BaseClient client = null;
				if (type == WeiboType.Comment) {
					client = new WeiboCommentRequest(WeiboDetailActivity.this,
							new WeiboCommentRequest.Params(access_token,
									params.getId(), 30, page));
				} else {
					client = new WeiboReportRequest(WeiboDetailActivity.this,
							new WeiboReportRequest.Params(access_token,
									params.getId(), 30, page));
				}
				client.request(this);
			}

		}

		@Override
		public void onSuccess(Object object) {
			WeiboResult result = (WeiboResult) object;
			DataSource dataSource = result.toDataSource();
			add(dataSource);
			page++;

			if (list.size() < dataSource.total) {
				mFooterLoading.setVisibility(View.VISIBLE);
			} else {
				mFooterLoading.setVisibility(View.GONE);
			}
			// baseListView.onRefreshComplete();
			adapter.notifyDataSetChanged();

		}


		@Override
		public void onComplete() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onError(int error, String msg) {
			// TODO Auto-generated method stub
			
		}
	}

	private WeiboDataSource commentDatasouce;

	private WeiboDataSource shareDataSource;

	private WeiboDataSource currentdaDataSource;

	private WeiboItemView itemView;

	private TitleItem titleItem = new TitleItem();

	private List<CommentItem> list = new ArrayList<CommentItem>();

	private WeiboItemView.Model params;

	private PullToRefreshPinnedSectionListView baseListView;

	private WeiboCommentListAdapter adapter;

	private View mFooterLoading;

	private int currentItem;
	
	private String access_token;

	public void setCurrentdaDataSource(WeiboDataSource currentdaDataSource) {
		this.currentdaDataSource = currentdaDataSource;
		list = currentdaDataSource.list;
		currentdaDataSource.init();
		adapter = new WeiboCommentListAdapter(this, new Model(titleItem, list),
				this);
		baseListView.setAdapter(adapter);
		baseListView.getRefreshableView().setSelection(currentItem);

	}

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		access_token = APP.getSession().getWeiboAccessToken();
		params = (WeiboItemView.Model) getIntent()
				.getSerializableExtra("params");
		commentDatasouce = new WeiboDataSource(WeiboType.Comment);
		shareDataSource = new WeiboDataSource(WeiboType.Share);
		setContentView(R.layout.activity_weibo);
		findViewById(R.id.back).setOnClickListener(this);
		itemView = new WeiboItemView(this);
		itemView.setOnWeiboItemClickListener(this);
		itemView.setModel(params);
		baseListView = (PullToRefreshPinnedSectionListView) findViewById(R.id.listview);
		baseListView.getRefreshableView().addHeaderView(itemView);

		setCurrentdaDataSource(commentDatasouce);
		if(access_token != null){
			View footer = LayoutInflater.from(this).inflate(
					R.layout.footer_loading, null);
			mFooterLoading = footer.findViewById(R.id.layout_checkmore);
			mFooterLoading.setVisibility(View.GONE);
			baseListView.getRefreshableView().addFooterView(footer);
			baseListView.setOnLastItemVisibleListener(this);
			baseListView.setOnRefreshListener(this);
			baseListView.setRefreshing(true);
			baseListView.setOnScrollListener(this);
		}
		
		
	}

	@Override
	public void onLastItemVisible() {
		if (mFooterLoading.getVisibility() == View.VISIBLE) {
			currentdaDataSource.request();
		}

	}

	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {

		
		if (access_token != null) {

			WeiboCountRequest request = new WeiboCountRequest(this,
					new WeiboCountRequest.Params(access_token, params.getId()));
			request.request(new RequestHandler() {

				@Override
				public void onSuccess(Object object) {
					WeiboCountRequest.Result result = (WeiboCountRequest.Result) object;
					titleItem.setComment(result.getComments());
					titleItem.setShare(result.getReposts());
					baseListView.onRefreshComplete();
					adapter.notifyDataSetChanged();

				}

				

				@Override
				public void onComplete() {
					// TODO Auto-generated method stub
					
				}



				@Override
				public void onError(int error, String msg) {
					// TODO Auto-generated method stub
					
				}
			});
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		currentItem = firstVisibleItem;
		currentdaDataSource.setCurrent(visibleItemCount);

	}

	@Override
	public void onCommentClick() {
		setCurrentdaDataSource(commentDatasouce);

	}

	@Override
	public void onShareClick() {
		setCurrentdaDataSource(shareDataSource);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;

		default:
			break;
		}

	}

	@Override
	public void onCommentClick(
			WeiboItemView.Model model) {
		WeiboPublishActivity.open(this, model.getId());

	}

	@Override
	public void onItemClick(
			WeiboItemView.Model model) {
		// TODO Auto-generated method stub

	}

}
