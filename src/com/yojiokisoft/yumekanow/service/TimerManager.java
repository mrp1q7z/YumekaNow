package com.yojiokisoft.yumekanow.service;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.yojiokisoft.yumekanow.activity.WakeUpActivity_;

public class TimerManager {
	private static Calendar mCalendar;
	
	static {
		mCalendar = Calendar.getInstance();
		mCalendar.setTimeInMillis(0);
	}
	
	public static void setTimer(Context context, long triggerAtTime, long interval) {
		Intent intent = new Intent(context, MyWidgetService.class);
		PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.setRepeating(AlarmManager.RTC, triggerAtTime, interval, pendingIntent);
	}

	public static void cancelTimer(Context context) {
		Intent intent = new Intent(context, MyWidgetService.class);
		PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(pendingIntent);
	}

	public static void setWakuUpTimer(Context context, long triggerAtTime) {
		Intent intent = new Intent(context, WakeUpActivity_.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC, triggerAtTime, pendingIntent);
		mCalendar.setTimeInMillis(triggerAtTime);
	}

	public static void cancelWakeUpTimer(Context context) {
		Intent intent = new Intent(context, WakeUpActivity_.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(pendingIntent);
		mCalendar.setTimeInMillis(0);
	}
	
	public static Calendar getCurrentTimer() {
		return mCalendar;
	}
}
