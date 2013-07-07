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

package com.yojiokisoft.yumekanow.fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ViewById;
import com.yojiokisoft.yumekanow.R;
import com.yojiokisoft.yumekanow.activity.MainActivity;
import com.yojiokisoft.yumekanow.db.SettingDao;
import com.yojiokisoft.yumekanow.utils.MyAlarmManager;

/**
 * スリープフラグメント
 */
@EFragment(R.layout.fragment_sleep)
public class SleepFragment extends Fragment {
	@ViewById(R.id.wakeUpTime)
	/*package*/TimePicker mWakeUpTime;

	@ViewById(R.id.timeKind)
	/*package*/RadioGroup mTimeKind;

	@ViewById(R.id.currentTimer)
	/*package*/TextView mCurrentTimer;

	@ViewById(R.id.cancelTimerButton)
	/*package*/Button mCancelTimerButton;

	private Activity mActivity;

	/**
	 * フラグメントがアクティビティにアタッチされたときに呼ばれる.
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = activity;
	}

	/**
	 * フラグメント用のビューを作成
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// nullを返すことで @EFragment(xxx) で定義している xxx がインフレートされる
		return null;
	}

	/**
	 * フラグメントの初期化
	 */
	@AfterViews
	/*package*/void initFragment() {
		mWakeUpTime.setIs24HourView(true);
		setCurrentTime();
		printCurrentTimer();
		mTimeKind.setOnCheckedChangeListener(mTimeKindChanged);
	}

	/**
	 * セット中のタイマー時間を表示.
	 */
	private void printCurrentTimer() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(MyAlarmManager.getWakeUpTime());
		if (calendar.getTimeInMillis() == 0) {
			mCurrentTimer.setText(getString(R.string.timer_not_set));
			mCancelTimerButton.setVisibility(View.GONE);
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd HH:mm", Locale.JAPANESE);
			String time = sdf.format(calendar.getTime()) + getString(R.string.timer_setting_at);
			mCurrentTimer.setText(time);
			mCancelTimerButton.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 前回セットしていた時間をセット.
	 */
	private void setCurrentTime() {
		SettingDao settingDao = SettingDao.getInstance();
		String time;
		if (mTimeKind.getCheckedRadioButtonId() == R.id.jikan) {
			time = settingDao.getSleepJikan();
		} else {
			time = settingDao.getSleepTimer();
		}
		if (time != "") {
			int hour = Integer.parseInt(time.substring(0, 2));
			int min = Integer.parseInt(time.substring(2, 4));
			mWakeUpTime.setCurrentHour(hour);
			mWakeUpTime.setCurrentMinute(min);
		}
	}

	/**
	 * タイマーの種類（時間指定／タイマー指定）が変更された
	 */
	private OnCheckedChangeListener mTimeKindChanged = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			setCurrentTime();
		}
	};

	/**
	 * タイマーセットボタンのクリック
	 */
	@Click(R.id.setTimerButton)
	/*package*/void setTimerButtonClicked() {
		// アファーメーションアラームの解除
		MyAlarmManager.cancelStartTimer(mActivity);

		// タイマーのアラームをセット
		int hour = mWakeUpTime.getCurrentHour();
		int min = mWakeUpTime.getCurrentMinute();
		Calendar calendar = Calendar.getInstance(); // Calendar取得
		calendar.setTimeInMillis(System.currentTimeMillis()); // 現在時刻を取得
		if (mTimeKind.getCheckedRadioButtonId() == R.id.jikan) {
			// 時間指定
			if (hour < calendar.get(Calendar.HOUR_OF_DAY)
					|| (hour <= calendar.get(Calendar.HOUR_OF_DAY) && min < calendar.get(Calendar.MINUTE))) {
				calendar.add(Calendar.DAY_OF_MONTH, 1);
			}
			calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
					calendar.get(Calendar.DAY_OF_MONTH), hour, min, 0);
		} else {
			// タイマー指定
			calendar.add(Calendar.MINUTE, 60 * hour + min); // 現時刻 + 指定時間
		}
		MyAlarmManager.setWakuUpTimer(mActivity, calendar.getTimeInMillis());

		Toast toast = Toast.makeText(mActivity, getString(R.string.good_night), Toast.LENGTH_LONG);
		toast.show();

		try {
			((MainActivity) mActivity).closeActivity();
		} catch (RuntimeException e) {
			throw new RuntimeException("activity が closeActivity を実装していません.");
		}

		// セットされた時間を記憶しておく
		SettingDao settingDao = SettingDao.getInstance();
		String time = String.format(Locale.JAPANESE, "%02d%02d", hour, min);
		if (mTimeKind.getCheckedRadioButtonId() == R.id.jikan) {
			settingDao.setSleepJikan(time);
		} else {
			settingDao.setSleepTimer(time);
		}
	}

	/**
	 * タイマーキャンセルボタンのクリック
	 */
	@Click(R.id.cancelTimerButton)
	/*package*/void cancelTimerButtonClicked() {
		MyAlarmManager.cancelWakeUpTimer(mActivity);
		printCurrentTimer();

		MyAlarmManager.setStartTimer(mActivity);
	}
}
