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
import com.yojiokisoft.yumekanow.model.SettingDao;
import com.yojiokisoft.yumekanow.service.TimerManager;

@EFragment(R.layout.fragment_sleep)
public class SleepFragment extends Fragment {
	private Activity mActivity;

	@ViewById(R.id.wakeUpTime)
	TimePicker mWakeUpTime;

	@ViewById(R.id.timeKind)
	RadioGroup mTimeKind;

	@ViewById(R.id.currentTimer)
	TextView mCurrentTimer;

	@ViewById(R.id.cancelTimerButton)
	Button mCancelTimerButton;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return null;
	}

	@AfterViews
	void initActivity() {
		mWakeUpTime.setIs24HourView(true);
		setCurrentTime();
		printCurrentTimer();
		mTimeKind.setOnCheckedChangeListener(mTimeKindChanged);
	}

	private void printCurrentTimer() {
		Calendar calendar = TimerManager.getCurrentTimer();
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

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = activity;
	}

	private void setCurrentTime() {
		SettingDao settingDao = SettingDao.getInstance(mActivity);
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

	private OnCheckedChangeListener mTimeKindChanged = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			setCurrentTime();
		}
	};

	@Click(R.id.setTimerButton)
	void setTimerButtonClicked() {
		// アファーメーションアラームの解除
		TimerManager.cancelStartTimer(mActivity);

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
		TimerManager.setWakuUpTimer(mActivity, calendar.getTimeInMillis());

		Toast toast = Toast.makeText(mActivity, getString(R.string.good_night), Toast.LENGTH_LONG);
		toast.show();

		try {
			((MainActivity) mActivity).closeActivity();
		} catch (RuntimeException e) {
			throw new RuntimeException("activity が closeActivity を実装していません.");
		}

		// セットされた時間を記憶しておく
		SettingDao settingDao = SettingDao.getInstance(mActivity);
		String time = String.format(Locale.JAPANESE, "%02d%02d", hour, min);
		if (mTimeKind.getCheckedRadioButtonId() == R.id.jikan) {
			settingDao.setSleepJikan(time);
		} else {
			settingDao.setSleepTimer(time);
		}
	}

	@Click(R.id.cancelTimerButton)
	void cancelTimerButtonClicked() {
		TimerManager.cancelWakeUpTimer(mActivity);
		printCurrentTimer();

		TimerManager.setStartTimer(mActivity);
	}
}
