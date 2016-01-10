package com.cctv.xiqu.android;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.cctv.xiqu.android.adapter.NewsListAdapter;
import com.cctv.xiqu.android.adapter.VideoListAdapter;
import com.cctv.xiqu.android.fragment.network.ContentsRequest;
import com.cctv.xiqu.android.fragment.network.SearchRequest;
import com.cctv.xiqu.android.fragment.network.BaseClient.SimpleRequestHandler;
import com.cctv.xiqu.android.fragment.network.CategoryRequest.Category;
import com.cctv.xiqu.android.fragment.network.ContentsRequest.News;
import com.cctv.xiqu.android.utils.LoadingPopup;
import com.cctv.xiqu.android.widget.MyHorizontalScrollView;
import com.cctv.xiqu.android.widget.MyHorizontalScrollView.OnTabScrollListener;

import com.cctv.xiqu.android.R;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView.OnEditorActionListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class SearchActivity extends BaseActivity implements OnClickListener,
		TextWatcher, OnScrollListener, OnEditorActionListener,OnItemClickListener,OnTabScrollListener{

	public static <T> ArrayList<T> copyList(List<T> source) {
		ArrayList<T> dest = new ArrayList<T>();
	    for (T item : source) { dest.add(item); }
	    return dest;
	}
	
	public static class Model implements Serializable {
		private ArrayList<Category> categories;

		public Model(ArrayList<Category> categories) {
			super();
			categories = copyList(categories);
			categories.add(0, new Category(0, "全部"));
			this.categories = categories;
		}

	}

	public static void open(Context context, Model model) {

		Intent intent = new Intent(context, SearchActivity.class);

		intent.putExtra("model", model);

		context.startActivity(intent);

	}

	private Model model;

	private ViewGroup tabs;

	private Category category;

	private EditText editText;

	private View clearView;

	private ListView listView;

	private View mFooterLoading;

	private View lastView;
	
	private View arrowView;
	
	private View bgView;
	
	private MyHorizontalScrollView scrollView;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		model = (Model) getIntent().getSerializableExtra("model");
		category = model.categories.get(0);
		setContentView(R.layout.search_layout);
		bgView = findViewById(R.id.bg);
		arrowView = findViewById(R.id.arrow);
		scrollView = (MyHorizontalScrollView) findViewById(R.id.scrollView);
//		scrollView.setOnTabScrollListener(this);
		findViewById(R.id.back).setOnClickListener(this);
		
		listView = (ListView) findViewById(R.id.listview);
		listView.setOnItemClickListener(this);
		
		View footer = LayoutInflater.from(this).inflate(
				R.layout.footer_loading, null);
		listView.addFooterView(footer);
		mFooterLoading = footer.findViewById(R.id.layout_checkmore);
		mFooterLoading.setVisibility(View.GONE);
		tabs = (ViewGroup) findViewById(R.id.tabs);
		editText = (EditText) findViewById(R.id.edit);
		editText.setOnEditorActionListener(this);
		editText.addTextChangedListener(this);
		clearView = findViewById(R.id.clear);
		clearView.setOnClickListener(this);
		initTabs();
		listView.setOnScrollListener(this);
	}

	private void initTabs() {
		LayoutInflater inflater = LayoutInflater.from(this);
		int i = 0;
		for (Category category : model.categories) {
			TextView tab = (TextView) inflater.inflate(
					R.layout.search_tab_template, null);
			tab.setText(category.getCategoryname());
			tab.setTag(category);
			tab.setOnClickListener(this);
			if (i == 0) {
				tab.performClick();
			}
			tabs.addView(tab);
			i++;
		}

	}

	private int pageno = 1;

	private int pagesize = 10;

	private List<NewsListAdapter.Model> list1;

	private List<VideoListAdapter.Model> list2;
	
	private BaseAdapter adapter;

	private void request() {
		String content = editText.getText().toString();
		if (TextUtils.isEmpty(content)) {
			return;
		}
		if(pageno == 1){
			if(category.getCategoryname().equals("视频")){
				list2 = new ArrayList<VideoListAdapter.Model>();
				adapter = new VideoListAdapter(this, list2);
			}else{
				list1 = new ArrayList<NewsListAdapter.Model>();
				adapter = new NewsListAdapter(this, list1,true,false);
			}
			
			listView.setAdapter(adapter);
			adapter.notifyDataSetChanged();
		}
		mFooterLoading.setVisibility(View.GONE);
		SearchRequest request = new SearchRequest(this,
				new SearchRequest.Params(content,
						"" + category.getCategoryid(), pageno, pagesize));
		if(pageno == 1){
			LoadingPopup.show(this);
		}
		request.request(new SimpleRequestHandler() {
			@Override
			public void onSuccess(Object object) {
				ContentsRequest.Result result = (ContentsRequest.Result) object;
				if(result.getResult() != 1000){
					mFooterLoading.setVisibility(View.GONE);
					return;
				}
				List<?> models = null;
				if(category.getCategoryname().equals("视频")){
					models = News.toVideoList(result.getList());
					list2.addAll((List<VideoListAdapter.Model>)models);
				}else{
					models = News.toNewsList(result.getList());
					list1.addAll((List<NewsListAdapter.Model>)models);
				}
				if((list2 !=null && list2.size()>0) || (list1!=null && list1.size() > 0)){
					bgView.setVisibility(View.GONE);
				}else{
					bgView.setVisibility(View.VISIBLE);
				}
				if (models.size() >= pagesize) {
					mFooterLoading.setVisibility(View.VISIBLE);
				} else {
					mFooterLoading.setVisibility(View.GONE);
				}
				adapter.notifyDataSetChanged();

			}
			@Override
			public void onComplete() {
				LoadingPopup.hide(SearchActivity.this);
			}
		});

	}

	@Override
	public void onClick(View v) {
		if (v.getTag() instanceof Category) {
			if (lastView != null) {
				lastView.setSelected(false);
			}
			lastView = v;
			v.setSelected(true);
			category = ((Category) v.getTag());
			pageno = 1;
			request();

		}else if(v.getId() == R.id.clear){
			
			editText.setText("");
			
			clearView.setVisibility(View.GONE);
			
		}else if(v.getId() == R.id.back){
			finish();
		}

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterTextChanged(Editable s) {
		String text = s.toString();
		if (!TextUtils.isEmpty(text)) {
			clearView.setVisibility(View.VISIBLE);
		} else {
			clearView.setVisibility(View.INVISIBLE);
		}

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		final int lastItem = firstVisibleItem + visibleItemCount;
		if (lastItem == totalItemCount) {
			if (mFooterLoading.getVisibility() == View.VISIBLE) {
				pageno++;
				request();
			}
		}

	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (actionId == EditorInfo.IME_ACTION_SEARCH) {
			pageno=1;
			request();
			return true;
		}
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
		
		
		if(TextUtils.equals(category.getCategoryname(),"视频")){
			VideoCommentActivity.open(this, list2.get(position).toCommentModel());
		}else{
			NewsListAdapter.Model model = list1.get(position);
			if(TextUtils.equals("视频", model.getCategory().getText()) ){
				VideoCommentActivity.open(this, model.toCommentModel());
			}else{
				SpecialDetailActivity.open(this, model.toDetailParams());
			}
		
		}
		
	}

	@Override
	public void isLastVisible(boolean visible) {
		arrowView.setVisibility(visible?View.GONE:View.VISIBLE);
		
	}

	
	
}
