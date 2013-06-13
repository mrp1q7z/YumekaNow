package com.yojiokisoft.yumekanow.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;

import com.yojiokisoft.yumekanow.exception.MyUncaughtExceptionHandler;
import com.yojiokisoft.yumekanow.model.SettingDao;
import com.yojiokisoft.yumekanow.service.TimerManager;

public class MyWidget extends AppWidgetProvider {
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		//キャッチされない例外により、スレッドが突然終了したときや、  
		//このスレッドに対してほかにハンドラが定義されていないときに  
		//呼び出されるデフォルトのハンドラを設定します。  
		Context appCtx = context.getApplicationContext();
		Thread.setDefaultUncaughtExceptionHandler(new MyUncaughtExceptionHandler(appCtx));

		long now = System.currentTimeMillis();
		SettingDao settingDao = SettingDao.getInstance(context);
		long interval = settingDao.getDispInterval() * 60 * 1000;
		TimerManager.setTimer(context, now, interval);

		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}
}
