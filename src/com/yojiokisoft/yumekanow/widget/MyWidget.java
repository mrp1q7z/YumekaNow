package com.yojiokisoft.yumekanow.widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

import com.yojiokisoft.yumekanow.exception.MyUncaughtExceptionHandler;
import com.yojiokisoft.yumekanow.model.SettingDao;
import com.yojiokisoft.yumekanow.service.MyWidgetService;

public class MyWidget extends AppWidgetProvider {
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		//キャッチされない例外により、スレッドが突然終了したときや、  
		//このスレッドに対してほかにハンドラが定義されていないときに  
		//呼び出されるデフォルトのハンドラを設定します。  
		Context appCtx = context.getApplicationContext();
		Thread.setDefaultUncaughtExceptionHandler(new MyUncaughtExceptionHandler(appCtx));

		Intent intent = new Intent(context, MyWidgetService.class);
		PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		long now = System.currentTimeMillis();
		SettingDao settingDao = SettingDao.getInstance(context);
		long interval = settingDao.getDispInterval() * 60 * 1000;
		alarmManager.setRepeating(AlarmManager.RTC, now, interval, pendingIntent);

		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}
}
