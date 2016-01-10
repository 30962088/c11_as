package com.umeng.socialize.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import java.util.Locale;

public class DeviceConfig {
	protected static final String LOG_TAG = DeviceConfig.class.getName();
	protected static final String UNKNOW = "Unknown";
	private static final String MOBILE_NETWORK = "2G/3G";
	private static final String WIFI = "Wi-Fi";
	public static final int DEFAULT_TIMEZONE = 8;

	public static boolean isAppInstalled(String paramString,
			Context paramContext) {
		if (paramContext == null) {
			return false;
		}
		PackageManager localPackageManager = paramContext.getPackageManager();
		boolean bool = false;
		try {
			localPackageManager.getPackageInfo(paramString, 1);
			bool = true;
		} catch (NameNotFoundException localNameNotFoundException) {
			bool = false;
		}
		return bool;
	}

	public static String getAppVersion(String paramString, Context paramContext) {
		try {
			PackageManager localPackageManager = paramContext
					.getPackageManager();
			PackageInfo localPackageInfo = localPackageManager.getPackageInfo(
					paramString, 0);
			return localPackageInfo.versionName;
		} catch (Exception localException) {
		}
		return null;
	}

	public static boolean isChinese(Context paramContext) {
		Locale localLocale = paramContext.getResources().getConfiguration().locale;
		return localLocale.toString().equals(Locale.CHINA.toString());
	}

	public static boolean checkPermission(Context paramContext,
			String paramString) {
		if (paramContext == null) {
			return false;
		}
		PackageManager localPackageManager = paramContext.getPackageManager();
		if (localPackageManager.checkPermission(paramString,
				paramContext.getPackageName()) != 0) {
			return false;
		}
		return true;
	}

	public static String getDeviceId(Context paramContext) {
		if (paramContext == null) {
			return "deviceNull";
		}
		TelephonyManager localTelephonyManager = (TelephonyManager) paramContext
				.getSystemService("phone");
		if (localTelephonyManager == null) {
			Log.w(LOG_TAG, "No IMEI.");
		}
		String str = "";
		try {
			if (checkPermission(paramContext,
					"android.permission.READ_PHONE_STATE")) {
				str = localTelephonyManager.getDeviceId();
			}
		} catch (Exception localException) {
			Log.w(LOG_TAG, "No IMEI.", localException);
		}
		if (TextUtils.isEmpty(str)) {
			Log.w(LOG_TAG, "No IMEI.");
			str = getMac(paramContext);
			if (TextUtils.isEmpty(str)) {
				Log.w(LOG_TAG,
						"Failed to take mac as IMEI. Try to use Secure.ANDROID_ID instead.");
				str = Secure.getString(
						paramContext.getContentResolver(), "android_id");
				Log.w(LOG_TAG, "getDeviceId: Secure.ANDROID_ID: " + str);
				return str;
			}
		}
		return str;
	}

	public static String[] getNetworkAccessMode(Context paramContext) {
		String[] arrayOfString = { "Unknown", "Unknown" };
		PackageManager localPackageManager = paramContext.getPackageManager();
		if (localPackageManager.checkPermission(
				"android.permission.ACCESS_NETWORK_STATE",
				paramContext.getPackageName()) != 0) {
			arrayOfString[0] = "Unknown";
			return arrayOfString;
		}
		ConnectivityManager localConnectivityManager = (ConnectivityManager) paramContext
				.getSystemService("connectivity");
		if (localConnectivityManager == null) {
			arrayOfString[0] = "Unknown";
			return arrayOfString;
		}
		NetworkInfo localNetworkInfo1 = localConnectivityManager
				.getNetworkInfo(1);
		if (localNetworkInfo1.getState() == State.CONNECTED) {
			arrayOfString[0] = "Wi-Fi";
			return arrayOfString;
		}
		NetworkInfo localNetworkInfo2 = localConnectivityManager
				.getNetworkInfo(0);
		if (localNetworkInfo2.getState() == State.CONNECTED) {
			arrayOfString[0] = "2G/3G";
			arrayOfString[1] = localNetworkInfo2.getSubtypeName();
			return arrayOfString;
		}
		return arrayOfString;
	}

	public static boolean isWiFiAvailable(Context paramContext) {
		return "Wi-Fi".equals(getNetworkAccessMode(paramContext)[0]);
	}

	public static boolean isOnline(Context paramContext) {
		try {
			ConnectivityManager localConnectivityManager = (ConnectivityManager) paramContext
					.getSystemService("connectivity");
			NetworkInfo localNetworkInfo = localConnectivityManager
					.getActiveNetworkInfo();
			if (localNetworkInfo != null) {
				return localNetworkInfo.isConnectedOrConnecting();
			}
			return false;
		} catch (Exception localException) {
		}
		return true;
	}

	public static boolean isNetworkAvailable(Context paramContext) {
		if ((checkPermission(paramContext,
				"android.permission.ACCESS_NETWORK_STATE"))
				&& (isOnline(paramContext))) {
			return true;
		}
		return false;
	}

	public static boolean isSdCardWrittenable() {
		if (Environment.getExternalStorageState().equals("mounted")) {
			return true;
		}
		return false;
	}

	public static String getMac(Context paramContext) {
		try {
			WifiManager localWifiManager = (WifiManager) paramContext
					.getSystemService("wifi");
			if (checkPermission(paramContext,
					"android.permission.ACCESS_WIFI_STATE")) {
				WifiInfo localWifiInfo = localWifiManager.getConnectionInfo();
				return localWifiInfo.getMacAddress();
			}
			Log.w(LOG_TAG,
					"Could not get mac address.[no permission android.permission.ACCESS_WIFI_STATE");
		} catch (Exception localException) {
			Log.w(LOG_TAG,
					"Could not get mac address." + localException.toString());
		}
		return "";
	}

	public static String getPackageName(Context paramContext) {
		return paramContext.getPackageName();
	}
}
