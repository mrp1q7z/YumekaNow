package com.yojiokisoft.yumekanow.utils;

import android.util.Log;

import com.yojiokisoft.yumekanow.BuildConfig;

public class MyLog {
	private static final String TAG = "MyLog";
	private static final int MAX_TAB_COUNT = 8;
	private static final int TAB_SIZE = 4;
	private static final int INDEX = 3;

	/*
	* DEBUG
	*/
	public static void d(String message) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, message + getPadding(message) + getAdditionalLog());
		}
	}

	public static void d(String tag, String message) {
		if (BuildConfig.DEBUG) {
			Log.d(tag, message + getPadding(message) + getAdditionalLog());
		}
	}

	public static void d(String message, Throwable throwable) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, message + getPadding(message) + getAdditionalLog(), throwable);
		}
	}

	public static void d(String tag, String message, Throwable throwable) {
		if (BuildConfig.DEBUG) {
			Log.d(tag, message + getPadding(message) + getAdditionalLog(), throwable);
		}
	}

	private static String getPadding(String message) {
		String padding = "";
		int tabCount = MAX_TAB_COUNT - (message.length() / TAB_SIZE);
		if (tabCount <= 0) {
			return "\t";
		}
		for (int i = 0; i < tabCount; i++) {
			padding += "\t";
		}
		return padding;
	}

	private static String getAdditionalLog() {
		String additionalLog = " at " + getClassName() + "#" + getMethodName()
				+ "(" + getFileName() + ":" + getLineNumber() + ")";
		return additionalLog;
	}

	private static String getClassName() {
		String className = new Throwable().getStackTrace()[INDEX].getClassName();
		className = className.substring(className.lastIndexOf(".") + 1);
		return className;
	}

	private static String getMethodName() {
		return new Throwable().getStackTrace()[INDEX].getMethodName();
	}

	private static String getFileName() {
		return new Throwable().getStackTrace()[INDEX].getFileName();
	}

	private static String getLineNumber() {
		return String.valueOf(new Throwable().getStackTrace()[INDEX].getLineNumber());
	}
}
