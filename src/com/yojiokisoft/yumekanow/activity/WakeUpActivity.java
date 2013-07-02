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

package com.yojiokisoft.yumekanow.activity;

import android.app.Activity;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.yojiokisoft.yumekanow.R;
import com.yojiokisoft.yumekanow.db.SettingDao;
import com.yojiokisoft.yumekanow.utils.MyAlarmManager;

/**
 * 目覚ましアクティビティ
 */
@EActivity(R.layout.activity_wake_up)
public class WakeUpActivity extends Activity {
	private Ringtone mRingtone = null;

	/**
	 * アクティビティの初期化 (onCreateと同等のタイミングで呼ばれる）
	 */
	@AfterViews
	/*package*/void initActivity() {
		SettingDao settingDao = SettingDao.getInstance();
		String url = settingDao.getSleepAlarmUrl();
		if (url != null) {
			mRingtone = RingtoneManager.getRingtone(this, Uri.parse(url));
			mRingtone.play();
		}
	}

	/**
	 * OKボタンのクリック
	 */
	@Click(R.id.okButton)
	/*package*/void okButtonClicked() {
		MyAlarmManager.cancelWakeUpTimer(this);
		MyAlarmManager.setStartTimer(this);

		Intent intent = new Intent(getApplicationContext(), MainActivity_.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);

		finish();
	}

	/**
	 * 終了処理
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mRingtone != null) {
			mRingtone.stop();
			mRingtone = null;
		}
	}
}
