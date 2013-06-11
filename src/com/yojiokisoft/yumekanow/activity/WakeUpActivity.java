package com.yojiokisoft.yumekanow.activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;

import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.yojiokisoft.yumekanow.R;
import com.yojiokisoft.yumekanow.model.SettingDao;
import com.yojiokisoft.yumekanow.service.MyWidgetService;

@EActivity(R.layout.activity_wake_up)
public class WakeUpActivity extends Activity {
	private Activity mActivity;
	private Ringtone mRingtone = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity = this;
		SettingDao settingDao = SettingDao.getInstance(this);
		String url = settingDao.getSleepAlarmUrl();
		if (url != null) {
			mRingtone = RingtoneManager.getRingtone(this, Uri.parse(url));
			mRingtone.play();
		}
	}

	@Click(R.id.okButton)
	void okButtonClicked() {
		Intent intent = new Intent(mActivity, MyWidgetService.class);
		PendingIntent pendingIntent = PendingIntent.getService(mActivity, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager alarmManager = (AlarmManager) mActivity.getSystemService(Context.ALARM_SERVICE);
		long now = System.currentTimeMillis();
		SettingDao settingDao = SettingDao.getInstance(mActivity);
		long interval = settingDao.getDispInterval() * 60 * 1000;
		alarmManager.setRepeating(AlarmManager.RTC, now, interval, pendingIntent);

		finish();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mRingtone != null) {
			mRingtone.stop();
			mRingtone = null;
		}
	}
}
