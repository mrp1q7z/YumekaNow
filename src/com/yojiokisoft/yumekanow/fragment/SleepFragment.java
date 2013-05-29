package com.yojiokisoft.yumekanow.fragment;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ViewById;
import com.yojiokisoft.yumekanow.R;
import com.yojiokisoft.yumekanow.activity.MainActivity;
import com.yojiokisoft.yumekanow.activity.WakeUpActivity_;
import com.yojiokisoft.yumekanow.service.MyWidgetService;

@EFragment(R.layout.fragment_sleep)
public class SleepFragment extends Fragment {
	private Activity mActivity;

	@ViewById(R.id.wakeUpTime)
	TimePicker mWakeUpTime;

	@ViewById(R.id.timeKind)
	RadioGroup mTimeKind;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return null;
	}

	@AfterViews
	void printWakuUpTime() {
		mWakeUpTime.setIs24HourView(true);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = activity;
	}

	@Click(R.id.setTimerButton)
	void setTimerButtonClicked() {
		// アファーメーションアラームの解除
		Intent serviceIntent = new Intent(mActivity, MyWidgetService.class);
		PendingIntent pendingIntent = PendingIntent.getService(mActivity, 0, serviceIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager alarmManager = (AlarmManager) mActivity.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(pendingIntent);

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
		Intent wakeUpIntent = new Intent(mActivity, WakeUpActivity_.class);
		pendingIntent = PendingIntent.getActivity(mActivity, 0, wakeUpIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		Log.d("taoka", "MainActivity.setTimerButtonOnClick : calendar=" + calendar.toString());
		// 前回設定したタイマーは上書きされるのでキャンセルする必要はない
		alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);

		Toast toast = Toast.makeText(mActivity, "おやすみなさい", Toast.LENGTH_LONG);
		toast.show();

		try {
			((MainActivity) mActivity).closeActivity();
		} catch (ClassCastException e) {
			throw new ClassCastException("activity が closeActivity を実装していません.");
		}
	}
}
