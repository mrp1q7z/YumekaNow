package com.yojiokisoft.yumekanow.activity;

import android.app.Activity;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;

import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.yojiokisoft.yumekanow.R;
import com.yojiokisoft.yumekanow.model.SettingDao;
import com.yojiokisoft.yumekanow.service.TimerManager;

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
		TimerManager.cancelWakeUpTimer(mActivity);
		
		long now = System.currentTimeMillis();
		SettingDao settingDao = SettingDao.getInstance(mActivity);
		long interval = settingDao.getDispInterval() * 60 * 1000;
		TimerManager.setTimer(mActivity, now, interval);

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
