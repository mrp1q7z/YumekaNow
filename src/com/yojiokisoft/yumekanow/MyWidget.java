package com.yojiokisoft.yumekanow;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class MyWidget extends AppWidgetProvider {
	@Override
	public void onEnabled(Context context) {
		Log.d("TAG", "MyWidget onEnabled");
		//		Intent intent = new Intent(context, MyWidgetService.class);
		//		PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		//		AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		//		long now = System.currentTimeMillis();
		//		long interval = 60 * 1000;
		//		alarmManager.setRepeating(AlarmManager.RTC, now, interval, pendingIntent);
		super.onEnabled(context);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		Log.d("taoka", "MyWidget.onUpdate : bigin");

		Intent intent = new Intent(context, MyWidgetService.class);
		PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		long now = System.currentTimeMillis();
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		String dispInterval = sp.getString("DISP_INTERVAL", "60");
		Log.d("taoka", "MyWidget.onUpdate : Interval=" + dispInterval);
		long interval = Integer.parseInt(dispInterval) * 60 * 1000;
		alarmManager.setRepeating(AlarmManager.RTC, now, interval, pendingIntent);

		// サービスが起動してないなら起動する

		super.onUpdate(context, appWidgetManager, appWidgetIds);
		Log.d("taoka", "MyWidget.onUpdate : end");
	}

}
