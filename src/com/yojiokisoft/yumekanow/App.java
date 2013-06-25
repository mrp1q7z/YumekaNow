package com.yojiokisoft.yumekanow;

import android.app.Application;
import android.content.Context;

public class App extends Application {
	public static final String NAME = "YourAppName";

	// コンテキスト
	private static App sInstance;

	// アプリケーションコンテキスト
	private static Context sAppContext;

	public App() {
		sInstance = this;
	}

	public static App getInstance() {
		if (sAppContext == null) {
			sAppContext = sInstance.getApplicationContext();
		}
		return sInstance;
	}

	public Context getAppContext() {
		return sAppContext;
	}
	
	public String getAppDataPath() {
		return sAppContext.getFilesDir().toString();
	}
}
