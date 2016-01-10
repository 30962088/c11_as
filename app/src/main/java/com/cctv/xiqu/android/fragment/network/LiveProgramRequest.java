package com.cctv.xiqu.android.fragment.network;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;

import android.content.Context;


import com.cctv.xiqu.android.adapter.LiveListAdapter;
import com.cctv.xiqu.android.adapter.LiveListAdapter.Model.State;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;

public class LiveProgramRequest extends BaseClient {

	public static class Program {
		private String t;
		private long st;
		private long et;
		private String showTime;
		private long duration;

	}

	public static class CCTV11 {
		private String isLive;
		private long liveSt;
		private String channelName;
		private List<Program> program;
	}

	public static class Result {
		private CCTV11 cctv11;

		public List<LiveListAdapter.Model> toLiveList() {
			List<LiveListAdapter.Model> list = new ArrayList<LiveListAdapter.Model>();
			for (Program program : cctv11.program) {
				State state = null;
				if (program.st == cctv11.liveSt) {
					state = State.CURRENT;
				} else if (program.et <= cctv11.liveSt) {
					state = State.OUTTIME;
				} else {
					state = State.INTIME;
				}
				list.add(new LiveListAdapter.Model(program.showTime, program.t,
						state));
			}
			return list;
		}
	}

	public LiveProgramRequest(Context context) {
		super(context);
	}

	@Override
	public Object onSuccess(String str) {
		Object object = null;
		Matcher matcher = Pattern.compile("\\{\"cctv11\".*").matcher(str);
		if(matcher.find()){
			str = matcher.group();
			object = new Gson().fromJson(str, Result.class);
		}
		return object;

	}


	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
			"yyyyMMdd");

	@Override
	protected RequestParams getParams() {
		RequestParams params = new RequestParams();
		params.add("serviceId", "cbox");
		params.add("c", "cctv11");
		params.add("d", DATE_FORMAT.format(new Date()));
		return params;
	}

	@Override
	protected String getURL() {
		// TODO Auto-generated method stub
		return "http://api.cntv.cn/epg/epginfo";
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
