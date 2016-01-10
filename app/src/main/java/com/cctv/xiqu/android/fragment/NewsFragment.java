package com.cctv.xiqu.android.fragment;

import java.util.ArrayList;
import java.util.List;

import com.cctv.xiqu.android.SpecialDetailActivity;
import com.cctv.xiqu.android.VideoCommentActivity;
import com.cctv.xiqu.android.adapter.NewsListAdapter;
import com.cctv.xiqu.android.adapter.NewsListAdapter.Model;
import com.cctv.xiqu.android.fragment.SliderFragment.OnSliderItemClickListener;
import com.cctv.xiqu.android.fragment.network.BaseClient;
import com.cctv.xiqu.android.fragment.network.ContentsRequest;
import com.cctv.xiqu.android.fragment.network.ContentsRequest.News;
import com.cctv.xiqu.android.fragment.network.ContentsRequest.Result;
import com.cctv.xiqu.android.widget.BaseListView;
import com.cctv.xiqu.android.widget.BaseListView.OnLoadListener;
import com.cctv.xiqu.android.widget.BaseListView.RequestResult;
import com.cctv.xiqu.android.widget.BaseListView.Type;



import com.cctv.xiqu.android.R;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class NewsFragment extends BaseFragment implements OnLoadListener,OnSliderItemClickListener,OnItemClickListener{
	
	public static NewsFragment newInstance(int categoryId,String categoryName){
		NewsFragment fragment = new NewsFragment();
		Bundle args = new Bundle();
		args.putInt("categoryId", categoryId);
		args.putString("categoryName", categoryName);
		fragment.setArguments(args);
		return fragment;
	}
	
	private int categoryId;
	
	private String categoryName;
	
	public NewsFragment() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		categoryName = getArguments().getString("categoryName");
		categoryId = getArguments().getInt("categoryId");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.common_list, null);
	}
	
	private ArrayList<Model> list = new ArrayList<Model>();
	
	private NewsListAdapter adapter;
	
	private View listHeader;
	private View listHeaderInner;
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		BaseListView listView = (BaseListView) view.findViewById(R.id.list);
		listView.setOnLoadListener(this);
		listHeader = LayoutInflater.from(getActivity()).inflate(R.layout.list_header, null);
		listView.getRefreshableView().addHeaderView(listHeader);
		listView.setOnItemClickListener(this);
		listHeaderInner = listHeader.findViewById(R.id.list_header);
		boolean showCategory = false;
		boolean bold = false;
		if(TextUtils.equals(categoryName, "头条")){
			showCategory = true;
			bold = true;
		}
		adapter = new NewsListAdapter(getActivity(), list,showCategory,bold);
		listView.setAdapter(adapter);
		listView.load(true);
	}
	

	private int offset;
	
	private int size;
	
	



	@Override
	public void onError(String error) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public BaseClient onLoad(int offset, int limit) {
		// TODO Auto-generated method stub
		return new ContentsRequest(getActivity(), new ContentsRequest.Params( categoryId, offset, limit));
	}

	@Override
	public boolean onLoadSuccess(Object object, int offset, int limit) {
		Result result = (Result) object;
		ArrayList<News> list = result.getList();
		if(offset == 1){
			this.list.clear();
			ArrayList<SliderFragment.Model> sliderList =  News.toSliderList(result.getLunbolist());
			if(sliderList.size()>0 && !TextUtils.equals("专栏", categoryName)){
				listHeaderInner.setVisibility(View.VISIBLE);
				getChildFragmentManager().beginTransaction().replace(R.id.list_header,SliderFragment.newInstance(this,sliderList)).commit();
			}else{
				listHeaderInner.setVisibility(View.GONE);
			}
			
		}
		
		
		this.list.addAll( News.toNewsList(list));
		adapter.notifyDataSetChanged();
		
		return list.size()>=limit?true:false;
	}

	@Override
	public void OnSliderItemClick(
			com.cctv.xiqu.android.fragment.SliderFragment.Model model) {
		if(TextUtils.equals("视频", model.getCategoryName()) ){
			VideoCommentActivity.open(getActivity(), model.toCommentModel());
		}else{
			SpecialDetailActivity.open(getActivity(), model.toDetailParams());
		}
		
		
	}

	@Override
	public Type getRequestType() {
		// TODO Auto-generated method stub
		return Type.PAGE;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
		Model model = (Model) adapter.getItem(position-2);
		if(TextUtils.equals("视频", model.getCategory().getText()) ){
			VideoCommentActivity.open(getActivity(), model.toCommentModel());
		}else{
			SpecialDetailActivity.open(getActivity(), model.toDetailParams());
		}
		
		
		
		
	}
	
	

}
