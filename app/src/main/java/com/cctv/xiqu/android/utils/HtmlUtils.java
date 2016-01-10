package com.cctv.xiqu.android.utils;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import android.content.Context;

public class HtmlUtils {

	public static String getHtml(Context context, String templateUrl,
			Map<String, String> map) throws IOException {
		String template = IOUtils.toString(context.getAssets()
				.open(templateUrl));
		for (Map.Entry<String, String> entry : map.entrySet()) {
			String key = entry.getKey(),value = entry.getValue();
			template = template.replace("{{"+key+"}}", value);
		}
		return template;
	}

}
