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

package com.yojiokisoft.yumekanow;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.yojiokisoft.yumekanow.db.SettingDao;
import com.yojiokisoft.yumekanow.utils.MyAlarmManager;

/**
 * sendBroadcastによって送信されたインテントを受信する
 */
public class YumekaNowReceiver extends BroadcastReceiver {
	/**
	 * ブロードキャストの受信
	 * @see BroadcastReceiver#onReceive(Context, Intent)1
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		long now = System.currentTimeMillis();
		long startTime = MyAlarmManager.getStartTime();
		if (startTime != 0) {
			SettingDao settingDao = SettingDao.getInstance();
			long interval = settingDao.getDispInterval() * 60 * 1000;
			if (startTime < now) {
				int x = (int) ((now - startTime) / interval) + 1;
				startTime += x * interval;
			}
			MyAlarmManager.setStartTimer(context, startTime, interval);
		}

		long wakeUpTime = MyAlarmManager.getWakeUpTime();
		if (wakeUpTime != 0) {
			MyAlarmManager.setWakuUpTimer(context, wakeUpTime);
		}
	}
}
