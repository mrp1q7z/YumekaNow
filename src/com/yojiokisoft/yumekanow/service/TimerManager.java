package com.yojiokisoft.yumekanow.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.yojiokisoft.yumekanow.activity.WakeUpActivity_;
import com.yojiokisoft.yumekanow.model.SettingDao;

public class TimerManager {
	public static void setStartTimer(Context context) {
		long now = System.currentTimeMillis();
		SettingDao settingDao = SettingDao.getInstance(context);
		long interval = settingDao.getDispInterval() * 60 * 1000;
		setStartTimer(context, now + interval, interval);
	}

	public static void setStartTimer(Context context, long triggerAtTime, long interval) {
		Intent intent = new Intent(context, YumekaNowService.class);
		PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.setRepeating(AlarmManager.RTC, triggerAtTime, interval, pendingIntent);

		SettingDao settingDao = SettingDao.getInstance();
		settingDao.setNextStartTime(triggerAtTime);
	}

	public static void cancelStartTimer(Context context) {
		Intent intent = new Intent(context, YumekaNowService.class);
		PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(pendingIntent);

		SettingDao settingDao = SettingDao.getInstance();
		settingDao.setNextStartTime(0);
	}

	public static long getStartTime() {
		SettingDao settingDao = SettingDao.getInstance();
		return settingDao.getNextStartTime();
	}

	public static void setWakuUpTimer(Context context, long triggerAtTime) {
		Intent intent = new Intent(context, WakeUpActivity_.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC, triggerAtTime, pendingIntent);

		SettingDao settingDao = SettingDao.getInstance();
		settingDao.setWakeUpTime(triggerAtTime);
	}

	public static void cancelWakeUpTimer(Context context) {
		Intent intent = new Intent(context, WakeUpActivity_.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(pendingIntent);

		SettingDao settingDao = SettingDao.getInstance();
		settingDao.setWakeUpTime(0);
	}

	public static long getWakeUpTime() {
		SettingDao settingDao = SettingDao.getInstance();
		return settingDao.getWakeUpTime();
	}
}
