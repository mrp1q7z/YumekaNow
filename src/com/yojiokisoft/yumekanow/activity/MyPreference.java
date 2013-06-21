package com.yojiokisoft.yumekanow.activity;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.res.StringArrayRes;
import com.yojiokisoft.yumekanow.R;
import com.yojiokisoft.yumekanow.dialog.VersionDialogPreference;
import com.yojiokisoft.yumekanow.model.SettingDao;
import com.yojiokisoft.yumekanow.service.TimerManager;
import com.yojiokisoft.yumekanow.utils.MyConst;
import com.yojiokisoft.yumekanow.utils.MyLog;
import com.yojiokisoft.yumekanow.utils.MyMail;

@EActivity
public class MyPreference extends PreferenceActivity implements OnSharedPreferenceChangeListener,
		OnPreferenceChangeListener {
	private final static String BR = System.getProperty("line.separator");

	@StringArrayRes(R.array.disp_interval_key)
	String[] mDispIntervalKey;

	@StringArrayRes(R.array.disp_interval_val)
	String[] mDispIntervalVal;

	@StringArrayRes(R.array.inquiry_key)
	String[] mInquiryKey;

	@StringArrayRes(R.array.inquiry_val)
	String[] mInquiryVal;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);

		setSummary(null);

		clearPreference(MyConst.INQUIRY);
	}

	private void clearPreference(String key) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.remove(key);
		editor.commit();
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

	private String vibratorVal2Key(boolean isChecked) {
		String key;

		if (isChecked) {
			key = getString(R.string.vibrator_on);
		} else {
			key = getString(R.string.vibrator_off);
		}
		return key;
	}

	private void setSummary(String key) {
		String summary;
		if (key == null || MyConst.DISP_INTERVAL.equals(key)) {
			ListPreference prefDispInterval = (ListPreference) getPreferenceScreen().findPreference(
					MyConst.DISP_INTERVAL);
			summary = indexOfBr(prefDispInterval.getSummary().toString());
			summary += BR + getString(R.string.now_setting) + dispIntervalVal2Key(prefDispInterval.getValue());

			SpannableString nowVal;
			nowVal = new SpannableString(summary);
			nowVal.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.theme_headerTextColor)), 0,
					nowVal.length(), 0);
			prefDispInterval.setSummary(nowVal);
		}
		if (key == null || MyConst.GOAL_CNT.equals(key)) {
			EditTextPreference prefGoalCnt = (EditTextPreference) getPreferenceScreen()
					.findPreference(MyConst.GOAL_CNT);
			summary = indexOfBr(prefGoalCnt.getSummary().toString());
			summary += BR + getString(R.string.now_setting) + prefGoalCnt.getText();

			SpannableString nowVal;
			nowVal = new SpannableString(summary);
			nowVal.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.theme_headerTextColor)), 0,
					nowVal.length(), 0);
			prefGoalCnt.setSummary(nowVal);
		}
		if (key == null || MyConst.VIBRATOR.equals(key)) {
			CheckBoxPreference prefVibrator = (CheckBoxPreference) getPreferenceScreen().findPreference(
					MyConst.VIBRATOR);
			summary = indexOfBr(prefVibrator.getSummary().toString());
			summary += BR + getString(R.string.now_setting) + vibratorVal2Key(prefVibrator.isChecked());

			SpannableString nowVal;
			nowVal = new SpannableString(summary);
			nowVal.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.theme_headerTextColor)), 0,
					nowVal.length(), 0);
			prefVibrator.setSummary(nowVal);
		}
		if (key == null || MyConst.ALARM.equals(key)) {
			RingtonePreference prefAlarm = (RingtonePreference) getPreferenceScreen().findPreference(MyConst.ALARM);
			summary = indexOfBr(prefAlarm.getSummary().toString());
			SettingDao settingDao = SettingDao.getInstance(this);
			summary += BR + getString(R.string.now_setting) + settingDao.getAlarm();

			SpannableString nowVal;
			nowVal = new SpannableString(summary);
			nowVal.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.theme_headerTextColor)), 0,
					nowVal.length(), 0);
			prefAlarm.setSummary(nowVal);
		}
		if (key == null || MyConst.SLEEP_ALARM.equals(key)) {
			RingtonePreference prefSleepAlarm = (RingtonePreference) getPreferenceScreen().findPreference(
					MyConst.SLEEP_ALARM);
			summary = indexOfBr(prefSleepAlarm.getSummary().toString());
			SettingDao settingDao = SettingDao.getInstance(this);
			summary += BR + getString(R.string.now_setting) + settingDao.getSleepAlarm();

			SpannableString nowVal;
			nowVal = new SpannableString(summary);
			nowVal.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.theme_headerTextColor)), 0,
					nowVal.length(), 0);
			prefSleepAlarm.setSummary(nowVal);
		}
		if (key == null || MyConst.VERSION.equals(key)) {
			VersionDialogPreference prefVersion = (VersionDialogPreference) getPreferenceScreen().findPreference(
					MyConst.VERSION);
			summary = indexOfBr(prefVersion.getSummary().toString());

			SpannableString nowVal;
			nowVal = new SpannableString(summary);
			nowVal.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.theme_headerTextColor)), 0,
					nowVal.length(), 0);
			prefVersion.setSummary(nowVal);
		}
		if (key == null || MyConst.INQUIRY.equals(key)) {
			ListPreference prefInquiry = (ListPreference) getPreferenceScreen().findPreference(MyConst.INQUIRY);
			summary = indexOfBr(prefInquiry.getSummary().toString());

			SpannableString nowVal;
			nowVal = new SpannableString(summary);
			nowVal.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.theme_headerTextColor)), 0,
					nowVal.length(), 0);
			prefInquiry.setSummary(nowVal);
		}
	}

	private String inquiryVal2Key(String val) {
		String key = null;

		for (int i = 0; i < mInquiryVal.length; i++) {
			if (mInquiryVal[i].equals(val)) {
				key = mInquiryKey[i];
				break;
			}
		}

		return key;
	}

	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		setSummary(key);
		if (MyConst.DISP_INTERVAL.equals(key)) {
			TimerManager.cancelTimer(this);
			TimerManager.setTimer(this);
		}
		if (MyConst.INQUIRY.equals(key)) {
			String inquiry = sharedPreferences.getString(key, "");
			if (!"".equals(inquiry)) {
				String inquiryKey = inquiryVal2Key(inquiry);
				String subject = "[" + inquiryKey + "]" + getString(R.string.app_name);
				MyLog.d("Inquiry=" + inquiry);
				// メール送信
				MyMail.Builder.newInstance(getApplicationContext())
						.setTo(getString(R.string.developer_email))
						.setSubject(subject)
						.send();
				clearPreference(MyConst.INQUIRY);
			}
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
		RingtonePreference pref = (RingtonePreference) findPreference(MyConst.ALARM);
		pref.setOnPreferenceChangeListener(this);
		RingtonePreference prefSleepAlarm = (RingtonePreference) findPreference(MyConst.SLEEP_ALARM);
		prefSleepAlarm.setOnPreferenceChangeListener(this);
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		RingtonePreference prefAlarm = (RingtonePreference) preference;
		Ringtone rm = RingtoneManager.getRingtone(this, Uri.parse((String) newValue));
		String summary = indexOfBr(prefAlarm.getSummary().toString());
		if (((String) newValue).length() <= 0) {
			summary += BR + getString(R.string.now_setting) + getString(R.string.silent);
		} else {
			summary += BR + getString(R.string.now_setting) + rm.getTitle(this);
		}
		SpannableString nowVal;
		nowVal = new SpannableString(summary);
		nowVal.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.theme_headerTextColor)), 0,
				nowVal.length(), 0);
		prefAlarm.setSummary(nowVal);
		return true;
	}
}
