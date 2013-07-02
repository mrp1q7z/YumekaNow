/*
 * Copyright (C) 2013 YojiokiSoft
 * 
 * This program is free software; you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 3 of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this
 * program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.yojiokisoft.yumekanow.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.yojiokisoft.yumekanow.YumekaNowService;
import com.yojiokisoft.yumekanow.activity.WakeUpActivity_;
import com.yojiokisoft.yumekanow.db.SettingDao;

/**
 * アラームマネージャー
 */
public class MyAlarmManager {
	/**
	 * 設定画面の表示インターバルで設定されている間隔で、YumekaNowServiceをアラームにセットする.
	 * 
	 * @param context
	 */
	public static void setStartTimer(Context context) {
		long now = System.currentTimeMillis();
		SettingDao settingDao = SettingDao.getInstance();
		long interval = settingDao.getDispInterval() * 60 * 1000;
		setStartTimer(context, now + interval, interval);
	}

	/**
	 * YumekaNowServiceを繰り返しアラームにセットする.
	 * セットした時間を保存し、getStartTimeで取り出せる.
	 * @see AlarmManager#setRepeating(int, long, long, PendingIntent)
	 * 
	 * @param context
	 * @param triggerAtTime
	 * @param interval
	 */
	public static void setStartTimer(Context context, long triggerAtTime, long interval) {
		Intent intent = new Intent(context, YumekaNowService.class);
		PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.setRepeating(AlarmManager.RTC, triggerAtTime, interval, pendingIntent);

		SettingDao settingDao = SettingDao.getInstance();
		settingDao.setNextStartTime(triggerAtTime);
	}

	/**
	 * YumekaNowServiceのアラームを解除する.
	 * 
	 * @param context
	 */
	public static void cancelStartTimer(Context context) {
		Intent intent = new Intent(context, YumekaNowService.class);
		PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(pendingIntent);

		SettingDao settingDao = SettingDao.getInstance();
		settingDao.setNextStartTime(0);
	}

	/**
	 * @return YumekaNowServiceの設定中のアラーム時間
	 */
	public static long getStartTime() {
		SettingDao settingDao = SettingDao.getInstance();
		return settingDao.getNextStartTime();
	}

	/**
	 * WackeUpActivityを単発アラームにセットする.
	 * 
	 * @param context
	 * @param triggerAtTime
	 */
	public static void setWakuUpTimer(Context context, long triggerAtTime) {
		Intent intent = new Intent(context, WakeUpActivity_.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC, triggerAtTime, pendingIntent);

		SettingDao settingDao = SettingDao.getInstance();
		settingDao.setWakeUpTime(triggerAtTime);
	}

	/**
	 * WakeUpActivityのアラームを解除する.
	 * 
	 * @param context
	 */
	public static void cancelWakeUpTimer(Context context) {
		Intent intent = new Intent(context, WakeUpActivity_.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(pendingIntent);

		SettingDao settingDao = SettingDao.getInstance();
		settingDao.setWakeUpTime(0);
	}

	/**
	 * @return WakeUpActivityの設定中のアラーム時間
	 */
	public static long getWakeUpTime() {
		SettingDao settingDao = SettingDao.getInstance();
		return settingDao.getWakeUpTime();
	}
}
