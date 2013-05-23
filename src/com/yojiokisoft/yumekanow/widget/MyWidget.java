package com.yojiokisoft.yumekanow.widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

import com.yojiokisoft.yumekanow.model.SettingDao;
import com.yojiokisoft.yumekanow.service.MyWidgetService;
import com.yojiokisoft.yumekanow.utils.MyLog;

public class MyWidget extends AppWidgetProvider {
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		MyLog.d("MyWidget.onUpdate : bigin");

		Intent intent = new Intent(context, MyWidgetService.class);
		PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		long now = System.currentTimeMillis();
		SettingDao settingDao = SettingDao.getInstance(context);
		long interval = settingDao.getDispInterval() * 60 * 1000;
		alarmManager.setRepeating(AlarmManager.RTC, now, interval, pendingIntent);

		super.onUpdate(context, appWidgetManager, appWidgetIds);
		MyLog.d("MyWidget.onUpdate : end");
	}
}
