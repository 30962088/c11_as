package com.cctv.xiqu.android.fragment;

import java.util.ArrayList;
import java.util.List;

import com.cctv.xiqu.android.WallPagerActivity;
import com.cctv.xiqu.android.adapter.BBSListAdapter;
import com.cctv.xiqu.android.adapter.WallPagerListAdapter;
import com.cctv.xiqu.android.fragment.network.BaseClient;
import com.cctv.xiqu.android.fragment.network.GetPagerRequest;
import com.cctv.xiqu.android.fragment.network.GetTopicRequest;
import com.cctv.xiqu.android.fragment.network.GetTopicRequest.Result;
import com.cctv.xiqu.android.utils.ImageUtils.Size;
import com.cctv.xiqu.android.widget.BBSHeaderView;
import com.cctv.xiqu.android.widget.BaseListView;
import com.cctv.xiqu.android.widget.BaseListView.OnLoadListener;
import com.cctv.xiqu.android.widget.BaseListView.Type;

import com.cctv.xiqu.android.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class WallPagerFragment extends BaseFragment implements OnLoadListener,OnItemClickListener{

	
	public static WallPagerFragment newInstance(){
		return new WallPagerFragment();
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
		return inflater.inflate(R.layout.wallpager_layout, null);
	}
	
	private WallPagerListAdapter adapter;
	
	private List<WallPagerListAdapter.Model> list = new ArrayList<WallPagerListAdapter.Model>();
	
	private Size size;
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		
		final BaseListView listView = (BaseListView) view.findViewById(R.id.listview);
		listView.post(new Runnable() {
			
			@Override
			public void run() {
				size = new Size(listView.getWidth(), null);
				
			}
		});
		adapter = new WallPagerListAdapter(getActivity(), list);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
		listView.setOnLoadListener(this);
		listView.load(true);
	}

	@Override
	public BaseClient onLoad(int offset, int limit) {
		GetPagerRequest request = new GetPagerRequest(getActivity(), new GetPagerRequest.Params(offset, limit));
		return request;
	}

	@Override
	public boolean onLoadSuccess(Object object, int offset, int limit) {
		GetPagerRequest.Result result = (GetPagerRequest.Result) object;
		if(offset == 1){
			list.clear();
		}
		List<WallPagerListAdapter.Model> results = result.toModels(size);
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
		WallPagerActivity.open(getActivity(), list.get(position-1).toModel()) ;
		
	}
	
}
