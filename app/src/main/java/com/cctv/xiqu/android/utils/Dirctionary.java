package com.cctv.xiqu.android.utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.nostra13.universalimageloader.utils.StorageUtils;

import android.content.Context;
import android.os.Environment;

public class Dirctionary {

	private static File pictureDir;
	
	private static File userObjectFile;
	
	private static File cateObjectFile;
	
	private static File relationObjectFile;
	
	private static File shareAccountFile;
	
	private static File dataDir;
	
	public static void init(Context context){
		initPicture(context);
		initDataDir(context);
		initUserObjectFile(context);
		initCateObjectFile(context);
		initRelationObjectFile(context);
		initShareAccountFile(context);
	}
	
	private static void initDataDir(Context context){
		dataDir = new File(context.getCacheDir()+"/data");
		if(!dataDir.exists()){
			dataDir.mkdirs();
		}
	}
	
	public static File getShareAccountFile() {
		return shareAccountFile;
	}
	
	public static File getPictureDir() {
		return pictureDir;
	}
	
	private static void initShareAccountFile(Context context){
//		userObjectFile = new File(context.getCacheDir(),"user.class");
		shareAccountFile = new File(dataDir,"share_account.data");
		if(!shareAccountFile.exists()){
			try {
				shareAccountFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	private static void initUserObjectFile(Context context){
//		userObjectFile = new File(context.getCacheDir(),"user.class");
		userObjectFile = new File(dataDir,"user.class");
		if(!userObjectFile.exists()){
			try {
				userObjectFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private static void initCateObjectFile(Context context){
		cateObjectFile = new File(dataDir,"cat.class");
		if(!cateObjectFile.exists()){
			try {
				cateObjectFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private static void initRelationObjectFile(Context context){
		relationObjectFile = new File(dataDir,"relation.class");
		if(!relationObjectFile.exists()){
			try {
				relationObjectFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	public static File getUserObjectFile() {
		return userObjectFile;
	}
	
	public static File getCateObjectFile() {
		return cateObjectFile;
	}
	
	public static File getRelationObjectFile(){
		return relationObjectFile;
	}
	
	private static void initPicture(Context context){
		 File cameraFolder = new File(Environment.getExternalStorageDirectory() ,"cctv11_picture");

         if(!cameraFolder.exists()){
        	 cameraFolder.mkdirs();
         }
         pictureDir = cameraFolder;
	}
	
	public static File creatPicture(){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss",Locale.CHINA);
        String timeStamp = dateFormat.format(new Date());
        String imageFileName = "picture_" + timeStamp + ".jpg";
        return new File(Dirctionary.getPictureDir(),imageFileName);
	}
	
	
	
}
