package com.yojiokisoft.yumekanow.activity;

import android.app.Activity;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;

import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.yojiokisoft.yumekanow.R;
import com.yojiokisoft.yumekanow.db.SettingDao;
import com.yojiokisoft.yumekanow.utils.TimerManager;

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
		TimerManager.setStartTimer(mActivity);

		Intent intent = new Intent(getApplicationContext(), MainActivity_.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);

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
