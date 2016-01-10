package com.cctv.xiqu.android.fragment.network;

import java.util.Map;

import com.loopj.android.http.RequestParams;

public class EscapeRequestParams extends RequestParams{

	public EscapeRequestParams() {
		super();
		// TODO Auto-generated constructor stub
	}

	public EscapeRequestParams(Map<String, String> source) {
		super(source);
		// TODO Auto-generated constructor stub
	}

	public EscapeRequestParams(Object... keysAndValues) {
		super(keysAndValues);
		// TODO Auto-generated constructor stub
	}

	public EscapeRequestParams(String key, String value) {
		super(key, value);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void add(String key, String value) {
		value = encode(value);
		super.add(key, value);
	}
	public static String encode(String s) {
	    StringBuilder sb = new StringBuilder(s.length() * 3);
	    for (char c : s.toCharArray()) {
	        if (c < 256) {
	            sb.append(c);
	        } else {
	            sb.append("\\u");
	            sb.append(Character.forDigit((c >>> 12) & 0xf, 16));
	            sb.append(Character.forDigit((c >>> 8) & 0xf, 16));
	            sb.append(Character.forDigit((c >>> 4) & 0xf, 16));
	            sb.append(Character.forDigit((c) & 0xf, 16));
	        }
	    }
	    return sb.toString();
	}
}
