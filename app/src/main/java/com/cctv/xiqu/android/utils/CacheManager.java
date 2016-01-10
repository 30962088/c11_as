package com.cctv.xiqu.android.utils;

import java.io.File;

import android.content.Context;
import android.os.AsyncTask;

public class CacheManager {



	private CacheManager() {

	}

	
	public static interface OnClearCacheListner{
		public void onclearSuccess();
	}
	
	public static void clearCache(final Context context,final OnClearCacheListner listner){
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				delete(context.getCacheDir());
				delete(context.getExternalCacheDir());
				Dirctionary.init(context);
				return null;
			}
			
			protected void onPostExecute(Void result) {
				if(listner != null){
					listner.onclearSuccess();
				}
			};
			
		}.execute();
		
	}
	
	private static void delete(File f) {
		if (f.isDirectory()) {
			for (File c : f.listFiles()) {
				delete(c);
			}

		}
		if(!f.getName().endsWith(".class")){
			f.delete();
		}
		

	}

	public static long folderSize(File directory) {
		long length = 0;
		if(directory != null){
			for (File file : directory.listFiles()) {
				if (file.isFile())
					length += file.length();
				else
					length += folderSize(file);
			}
		}
		
		return length;
	}
}
