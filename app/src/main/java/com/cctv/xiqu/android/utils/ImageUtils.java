package com.cctv.xiqu.android.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.cctv.xiqu.android.APP;
import com.cctv.xiqu.android.fragment.network.BaseClient;

import android.content.ContentValues;
import android.content.Context;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;


public class ImageUtils {

	public static class Size{
		private Integer width;
		private Integer height;
		public Size(Integer width, Integer height) {
			super();
			this.width = width;
			this.height = height;
		}
		public Integer getWidth() {
			return width;
		}
		public Integer getHeight() {
			return height;
		}
		
		
	}
	
	public static void addImageToGallery(final String filePath, final Context context) {

	    ContentValues values = new ContentValues();

	    values.put(Images.Media.DATE_TAKEN, System.currentTimeMillis());
	    values.put(Images.Media.MIME_TYPE, "image/jpeg");
	    values.put(MediaStore.MediaColumns.DATA, filePath);

	    context.getContentResolver().insert(Images.Media.EXTERNAL_CONTENT_URI, values);
	}
	
	public static String getTheImage(String filename,Size size){
		List<String> params = new ArrayList<String>();
		params.add("fileName="+filename);
		if(size != null){
			if(size.width != null){
				params.add("width="+size.width);
			}
			if(size.height != null){
				params.add("height="+size.height);
			}
		}
		String param = StringUtils.join(params,"&");
		return APP.getAppConfig().getRequest_news()+"cctv11/getTheImage?"+param;
		
	}
	
}
