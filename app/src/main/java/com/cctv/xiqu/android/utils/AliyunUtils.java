package com.cctv.xiqu.android.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import com.aliyun.android.oss.OSSClient;

import android.os.AsyncTask;

public class AliyunUtils {

	private static final String ACCESS_ID = "LYk8ljJ9J2fhZ4XX";
	private static final String ACCESS_KEY = "yPRp9E5reO4aO3YtRkrgAlaAWe34YA";

	private static final String OSS_ENDPOINT = "http://oss-cn-qingdao.aliyuncs.com/";

	public static AliyunUtils instance;

	public static AliyunUtils getInstance() {
		if (instance == null) {
			instance = new AliyunUtils();
		}
		return instance;
	}
	private OSSClient client = null;
	private AliyunUtils() {
		client = new OSSClient();
		client.setAccessId(ACCESS_ID);
		client.setAccessKey(ACCESS_KEY);
	}

	public void upload(File file,String bucketName, UploadListener uploadListener) {
		new UploadTask(file,bucketName,uploadListener).execute();
	}

	private static UploadResult getFilenameBySha1(File file) {
		UploadResult result = null;
		try {
			String sha1 = byteArrayToHexString(createSha1(file));
			result = new UploadResult(sha1, "."
					+ FilenameUtils.getExtension(file.getName()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	private static String byteArrayToHexString(byte[] b) {
		String result = "";
		for (int i = 0; i < b.length; i++) {
			result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
		}
		return result;
	}
	private static byte[] createSha1(File file) throws Exception {
		MessageDigest digest = MessageDigest.getInstance("SHA-1");
		InputStream fis = new FileInputStream(file);

		try {
			int n = 0;
			byte[] buffer = new byte[8192];
			while (n != -1) {
				n = fis.read(buffer);
				if (n > 0) {
					digest.update(buffer, 0, n);
				}
			}
		} finally {
			IOUtils.closeQuietly(fis);
		}

		return digest.digest();
	}

	public static class UploadResult {
		private String guid;
		private String ext;
		private String filename;
		private String url;

		public UploadResult(String guid, String ext) {
			super();
			this.guid = guid;
			this.ext = ext;
			filename = guid + ext;
		}

		public String getExt() {
			return ext;
		}

		public String getGuid() {
			return guid;
		}
		
		public String getUrl() {
			return url;
		}

	}

	public static interface UploadListener {
		public void onsuccess(UploadResult result);
	}

	private class UploadTask extends AsyncTask<Void, Void, UploadResult> {

		private File file;
		
		private String bucketName;

		private UploadListener uploadListener;

		public UploadTask(File file,String bucketName, UploadListener uploadListener) {
			super();
			this.file = file;
			this.bucketName = bucketName;
			this.uploadListener = uploadListener;
		}

		@Override
		protected UploadResult doInBackground(Void... params) {
			UploadResult result = getFilenameBySha1(file);
			try {
				client.uploadObject(bucketName, result.filename, FileUtils.readFileToByteArray(file));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(UploadResult result) {
			result.url = OSS_ENDPOINT+bucketName+"/"+result.filename;
			uploadListener.onsuccess(result);
		}

	}

}
