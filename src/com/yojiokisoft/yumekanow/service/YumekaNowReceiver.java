package com.yojiokisoft.yumekanow.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.yojiokisoft.yumekanow.model.SettingDao;
import com.yojiokisoft.yumekanow.utils.MyLog;

public class YumekaNowReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// debug >>>
		String debug = intent.getAction();
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd(E) HH:mm:ss", Locale.JAPANESE);
		// debug <<<
		SettingDao.getInstance(context.getApplicationContext());
		long now = System.currentTimeMillis();
		long startTime = TimerManager.getStartTime();
		if (startTime != 0) {
			SettingDao settingDao = SettingDao.getInstance(context);
			long interval = settingDao.getDispInterval() * 60 * 1000;
			if (startTime < now) {
				int x = (int) ((now - startTime) / interval) + 1;
				startTime += x * interval;
			}
			TimerManager.setStartTimer(context, startTime, interval);
			// debug >>>
			cal.setTimeInMillis(startTime);
			debug += "\nstartTime=" + sdf.format(cal.getTime());
			// debug <<<
		}

		long wakeUpTime = TimerManager.getWakeUpTime();
		if (wakeUpTime != 0) {
			TimerManager.setWakuUpTimer(context, wakeUpTime);
			// debug >>>
			cal.setTimeInMillis(wakeUpTime);
			debug += "\nwakeUpTime=" + sdf.format(cal.getTime());
			// debug <<<
		}
		// debug >>>
		Toast.makeText(context, debug, Toast.LENGTH_LONG).show();
		MyLog.d("debug=" + debug);
		// debug <<<
	}
}
