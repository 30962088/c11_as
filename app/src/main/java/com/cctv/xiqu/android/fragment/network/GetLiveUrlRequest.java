package com.cctv.xiqu.android.fragment.network;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.http.Header;

import android.content.Context;
import android.text.TextUtils;


import com.cctv.xiqu.android.adapter.LiveListAdapter;
import com.cctv.xiqu.android.adapter.LiveListAdapter.Model.State;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;

public class GetLiveUrlRequest extends BaseClient {

	public static class HLSURL {
		private String hls1;
		private String hls2;
		private String hls3;
		private String hls4;
		private String hls5;

		public String[] toList() {
			return new String[] { hls4, hls3, hls5, hls2, hls1 };
		}
	}

	public static class HDSURL {
		private String hds1;
		private String hds2;
		private String hds3;
		private String hds4;
		private String hds5;

		public String[] toList() {
			return new String[] { hds1, hds2, hds3, hds4, hds5 };
		}
	}

	public static class Result {
		private HLSURL hls_url;
		private HDSURL hds_url;


		public List<String> getUrlList() {
			List<String> list = new ArrayList<String>();
			if(hls_url != null){
				list.addAll(Arrays.asList(hls_url.toList()));
			}
			if(hds_url != null){
				list.addAll(Arrays.asList(hds_url.toList()));
			}
			List<String> res = new ArrayList<String>();
			for(String url : list){
				if(!TextUtils.isEmpty(url)){
					res.add(url);
				}
			}
			return res;
			
		}

	}


	public GetLiveUrlRequest(Context context) {
		super(context);
	}

	@Override
	public Object onSuccess(String str) {
		return new Gson().fromJson(str, Result.class);

	}


	@Override
	protected RequestParams getParams() {
		RequestParams params = new RequestParams();
		params.add("client", "androidapp");
		params.add("channel", "pa://cctv_p2p_hdcctv11");
		return params;
	}

	@Override
	protected String getURL() {
		// TODO Auto-generated method stub
		return "http://vdn.live.cntv.cn/api2/live.do";
	}

	@Override
	protected Method getMethod() {
		// TODO Auto-generated method stub
		return Method.GET;
	}

	@Override
	public void onError(int error, String msg) {
		// TODO Auto-generated method stub
		
	}
}
