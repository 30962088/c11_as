package com.cctv.xiqu.android.fragment;

import java.util.ArrayList;
import java.util.List;

import com.cctv.xiqu.android.SpecialDetailActivity;
import com.cctv.xiqu.android.VideoCommentActivity;
import com.cctv.xiqu.android.adapter.NewsListAdapter;
import com.cctv.xiqu.android.adapter.VideoListAdapter;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class VideoListFragment extends BaseFragment implements OnLoadListener,
		OnSliderItemClickListener, OnItemClickListener {

	public static VideoListFragment newInstance(int categoryId) {
		VideoListFragment fragment = new VideoListFragment();
		Bundle args = new Bundle();
		args.putInt("categoryId", categoryId);
		fragment.setArguments(args);
		return fragment;
	}

	private int categoryId;

	public VideoListFragment() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		categoryId = getArguments().getInt("categoryId");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.videolist_layout, null);
	}

	private ArrayList<VideoListAdapter.Model> list = new ArrayList<VideoListAdapter.Model>();

	private VideoListAdapter adapter;

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		BaseListView listView = (BaseListView) view.findViewById(R.id.list);
		listView.setOnLoadListener(this);

		listView.setOnItemClickListener(this);
		adapter = new VideoListAdapter(getActivity(), list);
		listView.setAdapter(adapter);
		listView.load(true);
	}

	@Override
	public void onError(String error) {
		// TODO Auto-generated method stub

	}

	@Override
	public BaseClient onLoad(int offset, int limit) {
		// TODO Auto-generated method stub
		return new ContentsRequest(getActivity(), new ContentsRequest.Params(
				categoryId, offset, limit));
	}

	@Override
	public boolean onLoadSuccess(Object object, int offset, int limit) {
		Result result = (Result) object;
		ArrayList<News> list = result.getList();
		if(offset == 1){
			this.list.clear();
			this.list.addAll(News.toVideoList(result.getLunbolist()));
		}
		this.list.addAll(News.toVideoList(list));
		adapter.notifyDataSetChanged();

		return list.size() >= limit ? true : false;
	}

	@Override
	public void OnSliderItemClick(
			SliderFragment.Model model) {
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

		VideoListAdapter.Model model = (VideoListAdapter.Model) adapter.getItem(position - 1);
		
		VideoCommentActivity.open(getActivity(), model.toCommentModel());

	}

}
