package com.cctv.xiqu.android.fragment.network;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


import com.cctv.xiqu.android.APP;
import com.cctv.xiqu.android.BaseActivity;
import com.cctv.xiqu.android.MainActivity;
import com.loopj.android.http.*;
import com.mengle.lib.utils.Utils;

public abstract class BaseClient implements HttpResponseHandler {
	
	
	private static AsyncHttpClient client = new AsyncHttpClient();

	protected Context context;
	
	public BaseClient(Context context) {
		this.context = context;
	}
	
	static {
		client.setMaxConnections(10);
		client.setTimeout(10 * 1000);

	}

	private class ResponseHandler extends AsyncHttpResponseHandler {

		private HttpResponseHandler handler;

		private ResponseHandler(HttpResponseHandler handler) {

			this.handler = handler;

		}

		@Override
		public void onFailure(int arg0, Header[] arg1, byte[] arg2,
				Throwable arg3) {
			
			handler.onError(500, "请求失败");
			requestHandler.onError(500, "请求失败");
			requestHandler.onComplete();
		}

		@Override
		public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
			String str = new String(arg2);
			String dstr = decode1(str);
			
			if(getURL().indexOf(APP.getAppConfig().getRequest_news())!=-1 || getURL().indexOf(APP.getAppConfig().getRequest_user())!=-1){
				try {
					JSONObject object = new JSONObject(str);
					int result = object.getInt("result");
					if(result != 1000){
						requestHandler.onComplete();
						if((getURL().indexOf(APP.getAppConfig().getRequest_news()) != -1 && result == 1010)||(getURL().indexOf(APP.getAppConfig().getRequest_user()) != -1 && result == 1011)){
							
							Utils.tip(context, "登录过期，请重新登录");
							Intent intent = new Intent(context, MainActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
									| Intent.FLAG_ACTIVITY_CLEAR_TOP);
							intent.setAction(MainActivity.ACTION_TOLOGIN);
							context.startActivity(intent);
							APP.getSession().logout();
							
						}else{
							requestHandler.onError(result, "请求失败");
							handler.onError(result, "请求失败");
						}
						
						return;
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			requestHandler.onSuccess(handler.onSuccess(new String(dstr)));
			requestHandler.onComplete();
			/*String json = new String(arg2);
			if (json.startsWith("[")) {
				requestHandler.onSuccess(handler.onSuccess(json));
			} else {
				try {
					JSONObject object = new JSONObject(json);
					int error = object.getInt("err");
					if (error == 0) {
						String data = object.getString("data");
						requestHandler.onSuccess(handler.onSuccess(data));
						if (useOffline()) {
							OfflineDataField.create(new OfflineDataField(
									getOfflineHash(), data));
						}
					} else {
						handler.onError(error);
						requestHandler.onError(error);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}*/

		}

	}

	public enum Method {

		GET, POST
	}

	protected abstract RequestParams getParams();

	protected abstract String getURL();

	protected abstract Method getMethod();

	public static interface RequestHandler {
		
		public void onComplete();
		
		public void onSuccess(Object object);

		public void onError(int error, String msg);

	}
	
	

	public static class SimpleRequestHandler implements RequestHandler {

		@Override
		public void onSuccess(Object object) {
			// TODO Auto-generated method stub

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

	private RequestHandler requestHandler;

	public void request(RequestHandler requestHandler) {
		
		this.requestHandler = requestHandler;
		
		if(!isOnline(context)){
			onError(501, "未发现网络");
			requestHandler.onError(501, "未发现网络");
			return;
		}
		
		Method method = getMethod();
		if (method == Method.GET) {
			get(getURL(), getParams(), this);
		} else if (method == Method.POST) {
			post(getURL(), getParams(), this);
		}
	}
	
	public static boolean isOnline(Context context) {
	    ConnectivityManager cm =
	        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    return netInfo != null && netInfo.isConnectedOrConnecting();
	}

	private RequestHandle handle;

	private void get(String url, RequestParams params,
			HttpResponseHandler handler) {
		Header[] headers = fillHeaders();
		if(headers == null){
			handle = client.get(getURL(), params, new ResponseHandler(
					handler));
		}else{
			handle = client.get(context,getURL(),headers, params, new ResponseHandler(
					handler));
		}
		

	}

	private void post(String url, RequestParams params,
			HttpResponseHandler handler) {
		Header[] headers = fillHeaders();
		if(headers == null){
			handle = client.post(getURL(),params, new ResponseHandler(
					handler));
		}else{
			handle = client.post(context,getURL(),headers, params,contentType(), new ResponseHandler(
					handler));
		}
		
	}

	public void cancel(boolean mayInterruptIfRunning) {
		if (handle != null && !handle.isFinished()) {
			handle.cancel(mayInterruptIfRunning);
		}

	}
	
	protected Header[] fillHeaders(){
		return null;
	}
	
	
	protected String contentType(){
		return "application/json";
	}


	protected boolean useOffline() {
		return true;
	}
	
	static final Pattern reUnicode = Pattern.compile("\\\\\\\\u([0-9a-zA-Z]{4})");

	public static String decode1(String s) {
	    Matcher m = reUnicode.matcher(s);
	    StringBuffer sb = new StringBuffer(s.length());
	    while (m.find()) {
	        m.appendReplacement(sb,
	                Character.toString((char) Integer.parseInt(m.group(1), 16)));
	    }
	    m.appendTail(sb);
	    return sb.toString();
	}

}
