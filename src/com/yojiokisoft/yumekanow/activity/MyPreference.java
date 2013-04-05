package com.yojiokisoft.yumekanow.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

import com.yojiokisoft.yumekanow.MyWidgetService;
import com.yojiokisoft.yumekanow.R;

public class MyPreference extends PreferenceActivity implements OnSharedPreferenceChangeListener {
	private final static String BR = System.getProperty("line.separator");
	private static String[] mDispIntervalKey;
	private static String[] mDispIntervalVal;
	private static String[] mAnimationKey;
	private static String[] mAnimationVal;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);

		mDispIntervalKey = getResources().getStringArray(R.array.disp_interval_key);
		mDispIntervalVal = getResources().getStringArray(R.array.disp_interval_val);
		mAnimationKey = getResources().getStringArray(R.array.animation_key);
		mAnimationVal = getResources().getStringArray(R.array.animation_val);

		setSummary(null);
	}

	private String indexOfBr(String str) {
		int find = str.indexOf(BR);
		if (find == -1) {
			return str;
		}
		return str.substring(0, find);
	}

	private String dispIntervalVal2Key(String val) {
		String ret = null;

		for (int i = 0; i < mDispIntervalVal.length; i++) {
			if (mDispIntervalVal[i].equals(val)) {
				ret = mDispIntervalKey[i];
				break;
			}
		}

		return ret;
	}

	private String animationVal2Key(String val) {
		String key = null;

		for (int i = 0; i < mAnimationVal.length; i++) {
			if (mAnimationVal[i].equals(val)) {
				key = mAnimationKey[i];
				break;
			}
		}

		return key;
	}

	private void setSummary(String key) {
		String summary;
		if (key == null || "DISP_INTERVAL".equals(key)) {
			ListPreference prefDispInterval = (ListPreference) getPreferenceScreen().findPreference("DISP_INTERVAL");
			summary = indexOfBr(prefDispInterval.getSummary().toString());
			summary += BR + "現在値：" + dispIntervalVal2Key(prefDispInterval.getValue());
			/*
			SpannableString nowVal;
			nowVal = new SpannableString("現在地：" + preDispInterval.getValue());
			nowVal.setSpan(new ForegroundColorSpan(Color.RED), 0, nowVal.length(), 0);
			preDispInterval.setSummary(nowVal);
			*/
			prefDispInterval.setSummary(summary);
		}
		if (key == null || "GOAL_CNT".equals(key)) {
			EditTextPreference prefGoalCnt = (EditTextPreference) getPreferenceScreen().findPreference("GOAL_CNT");
			summary = indexOfBr(prefGoalCnt.getSummary().toString());
			summary += BR + "現在値：" + prefGoalCnt.getText();
			prefGoalCnt.setSummary(summary);
		}
		if (key == null || "ANIMATION".equals(key)) {
			ListPreference prefAnimation = (ListPreference) getPreferenceScreen().findPreference("ANIMATION");
			summary = indexOfBr(prefAnimation.getSummary().toString());
			summary += BR + "現在値：" + animationVal2Key(prefAnimation.getValue());
			prefAnimation.setSummary(summary);
		}
	}

	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		setSummary(key);
		if ("DISP_INTERVAL".equals(key)) {
			Intent intent = new Intent(this, MyWidgetService.class);
			PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent,
					PendingIntent.FLAG_UPDATE_CURRENT);
			AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
			alarmManager.cancel(pendingIntent);

			SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
			String dispInterval = sp.getString("DISP_INTERVAL", "60");
Log.d("taoka", "onSharedPreferrnceChanged:dispInterval=" + dispInterval);
			long interval = Integer.parseInt(dispInterval) * 60 * 1000;
			alarmManager.setRepeating(AlarmManager.RTC, interval, interval, pendingIntent);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
	}
}
