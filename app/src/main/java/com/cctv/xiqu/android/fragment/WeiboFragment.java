package com.cctv.xiqu.android.fragment;

import java.util.ArrayList;
import java.util.List;

import com.cctv.xiqu.android.WeiboDetailActivity;
import com.cctv.xiqu.android.WeiboPublishActivity;
import com.cctv.xiqu.android.adapter.WeiboListAdapter;
import com.cctv.xiqu.android.adapter.WeiboCommentListAdapter.CommentItem;
import com.cctv.xiqu.android.fragment.network.BaseClient;
import com.cctv.xiqu.android.fragment.network.GetWeiboRequest;
import com.cctv.xiqu.android.fragment.network.GetWeiboRequest.Result;
import com.cctv.xiqu.android.widget.BaseListView;
import com.cctv.xiqu.android.widget.WeiboItemView;
import com.cctv.xiqu.android.widget.BaseListView.OnLoadListener;
import com.cctv.xiqu.android.widget.BaseListView.Type;
import com.cctv.xiqu.android.widget.WeiboItemView.Model;
import com.cctv.xiqu.android.widget.WeiboItemView.OnWeiboItemClickListener;

import com.cctv.xiqu.android.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class WeiboFragment extends BaseFragment implements OnLoadListener,OnItemClickListener,OnWeiboItemClickListener{
	
	
	
	
	public static WeiboFragment newInstance(){
		return new WeiboFragment();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.weibo_layout, null);
	}
	
	
	private List<Model> list = new ArrayList<Model>();
	private WeiboListAdapter adapter;
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		BaseListView listView = (BaseListView) view.findViewById(R.id.listview);
		listView.setOnItemClickListener(this);
		adapter = new WeiboListAdapter(getActivity(), list,this);
		listView.setAdapter(adapter);
		listView.setOnLoadListener(this);
		listView.load(true);
	}

	@Override
	public BaseClient onLoad(int offset, int limit) {
		GetWeiboRequest.Params params = new GetWeiboRequest.Params("0", offset, limit);
		return new GetWeiboRequest(getActivity(), params);
	}

	@Override
	public boolean onLoadSuccess(Object object, int offset, int limit) {
		List<Model> results = ((Result)object).toModelList();
		if(offset == 1){
			list.clear();
		}
		list.addAll(results);
		adapter.notifyDataSetChanged();
		return results.size()>=limit?true:false;
	}

	@Override
	public void onError(String error) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Type getRequestType() {
		// TODO Auto-generated method stub
		return Type.PAGE;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
		Model model = list.get(position);
		WeiboDetailActivity.open(getActivity(), model);
	}

	@Override
	public void onCommentClick(Model model) {
		WeiboPublishActivity.open(getActivity(), model.getId());
		
	}

	@Override
	public void onItemClick(Model model) {
		WeiboDetailActivity.open(getActivity(), model);
		
	}

	

}
