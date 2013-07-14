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
import android.content.SharedPreferences;
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
import android.preference.PreferenceScreen;
import android.preference.RingtonePreference;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

import com.googlecode.androidannotations.annotations.EActivity;
import com.yojiokisoft.yumekanow.R;
import com.yojiokisoft.yumekanow.db.SettingDao;
import com.yojiokisoft.yumekanow.utils.MyAlarmManager;
import com.yojiokisoft.yumekanow.utils.MyConst;
import com.yojiokisoft.yumekanow.utils.MyMail;

/**
 * 設定アクティビティ
 */
@EActivity
public class MyPreference extends PreferenceActivity {
	private final static String BR = System.getProperty("line.separator");
	private Activity mActivity;
	private SettingDao mSettingDao;

	/**
	 * アクティビティの初期化
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity = this;
		mSettingDao = SettingDao.getInstance();
		addPreferencesFromResource(R.xml.settings);

		setSummary();

		// お問い合わせの設定値をクリア
		// 既に選択中のものを選択してもイベントが発生しないから
		clearPreference(MyConst.PK_INQUIRY);

		// 寄付をクリックしたときアクティビティを開くようにする
		PreferenceScreen prefScreen = (PreferenceScreen) findPreference(MyConst.PK_DONATION);
		Intent intent = new Intent(this, BillingActivity_.class);
		prefScreen.setIntent(intent);

		// 設定値が変更された時のイベントリスナーを登録
		ListPreference prefDispInterval = (ListPreference) findPreference(MyConst.PK_DISP_INTERVAL);
		prefDispInterval.setOnPreferenceChangeListener(mDispIntervalChanged);

		EditTextPreference prefGoalCnt = (EditTextPreference) getPreferenceScreen().findPreference(MyConst.PK_GOAL_CNT);
		prefGoalCnt.setOnPreferenceChangeListener(mGoalCntChanged);

		CheckBoxPreference prefVibrator = (CheckBoxPreference) getPreferenceScreen()
				.findPreference(MyConst.PK_VIBRATOR);
		prefVibrator.setOnPreferenceChangeListener(mVibratorChanged);

		RingtonePreference prefAlarm = (RingtonePreference) findPreference(MyConst.PK_ALARM);
		prefAlarm.setOnPreferenceChangeListener(mAlarmChanged);

		RingtonePreference prefSleepAlarm = (RingtonePreference) findPreference(MyConst.PK_SLEEP_ALARM);
		prefSleepAlarm.setOnPreferenceChangeListener(mAlarmChanged);

		ListPreference prefInquiry = (ListPreference) getPreferenceScreen().findPreference(MyConst.PK_INQUIRY);
		prefInquiry.setOnPreferenceChangeListener(mInquiryChanged);
	}

	/**
	 * 設定値のクリア.
	 * 
	 * @param key
	 */
	private void clearPreference(String key) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.remove(key);
		editor.commit();
	}

	/**
	 * 文字列をBRまででカットする.
	 * 
	 * @param str 対象の文字列
	 * @return BRまでの文字列
	 */
	private String indexOfBr(String str) {
		int find = str.indexOf(BR);
		if (find == -1) {
			return str;
		}
		return str.substring(0, find);
	}

	/**
	 * サマリーに現在の設定値をセットする.
	 */
	private void setSummary() {
		String[] prefKeys = { MyConst.PK_DISP_INTERVAL, MyConst.PK_GOAL_CNT, MyConst.PK_VIBRATOR, MyConst.PK_ALARM,
				MyConst.PK_SLEEP_ALARM, MyConst.PK_VERSION, MyConst.PK_INQUIRY
		};
		Preference pref;
		String summary;
		String nowVal;
		for (int i = 0; i < prefKeys.length; i++) {
			pref = getPreferenceScreen().findPreference(prefKeys[i]);
			summary = indexOfBr(pref.getSummary().toString());
			nowVal = getNowValue(prefKeys[i]);
			if (nowVal != null) {
				summary += BR + getString(R.string.now_setting) + nowVal;
			}
			pref.setSummary(getSummarySpannableString(summary));
		}
	}

	/**
	 * 現在値の取得.
	 * 
	 * @param key
	 * @return
	 */
	private String getNowValue(String key) {
		if (MyConst.PK_DISP_INTERVAL.equals(key)) {
			return mSettingDao.getDispIntervalString();
		}
		if (key == null || MyConst.PK_GOAL_CNT.equals(key)) {
			return String.valueOf(mSettingDao.getGoalCnt());
		}
		if (key == null || MyConst.PK_VIBRATOR.equals(key)) {
			return mSettingDao.getVibratorString();
		}
		if (key == null || MyConst.PK_ALARM.equals(key)) {
			return mSettingDao.getAlarm();
		}
		if (key == null || MyConst.PK_SLEEP_ALARM.equals(key)) {
			return mSettingDao.getSleepAlarm();
		} else {
			return null;
		}
	}

	/**
	 * サマリー用のマークアップ可能な文字列の取得
	 * 
	 * @param summary
	 * @return
	 */
	private SpannableString getSummarySpannableString(String summary) {
		SpannableString span;
		span = new SpannableString(summary);
		span.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.theme_headerTextColor)), 0,
				span.length(), 0);
		return span;
	}

	/**
	 * 表示間隔が変更された.
	 */
	private final OnPreferenceChangeListener mDispIntervalChanged = new OnPreferenceChangeListener() {
		@Override
		public boolean onPreferenceChange(Preference preference, Object newValue) {
			MyAlarmManager.cancelStartTimer(mActivity);
			MyAlarmManager.setStartTimer(mActivity);

			ListPreference pref = (ListPreference) preference;
			String summary = indexOfBr(pref.getSummary().toString());
			summary += BR + getString(R.string.now_setting) + mSettingDao.dispIntervalVal2Key((String) newValue);
			pref.setSummary(getSummarySpannableString(summary));

			return true;
		}
	};

	/**
	 * 目標回数が変更された.
	 */
	private final OnPreferenceChangeListener mGoalCntChanged = new OnPreferenceChangeListener() {
		@Override
		public boolean onPreferenceChange(Preference preference, Object newValue) {
			EditTextPreference pref = (EditTextPreference) preference;
			String summary = indexOfBr(pref.getSummary().toString());
			summary += BR + getString(R.string.now_setting) + (String) newValue;
			pref.setSummary(getSummarySpannableString(summary));

			return true;
		}
	};

	/**
	 * バイブレータが変更された.
	 */
	private final OnPreferenceChangeListener mVibratorChanged = new OnPreferenceChangeListener() {
		@Override
		public boolean onPreferenceChange(Preference preference, Object newValue) {
			CheckBoxPreference pref = (CheckBoxPreference) preference;
			String summary = indexOfBr(pref.getSummary().toString());
			summary += BR + getString(R.string.now_setting) + mSettingDao.vibratorVal2Key((Boolean) newValue);
			pref.setSummary(getSummarySpannableString(summary));
			return true;
		}
	};

	/**
	 * アラーム音が変更された.
	 */
	private final OnPreferenceChangeListener mAlarmChanged = new OnPreferenceChangeListener() {
		@Override
		public boolean onPreferenceChange(Preference preference, Object newValue) {
			RingtonePreference pref = (RingtonePreference) preference;
			Ringtone rm = RingtoneManager.getRingtone(mActivity, Uri.parse((String) newValue));
			String summary = indexOfBr(pref.getSummary().toString());
			if (((String) newValue).length() <= 0) {
				summary += BR + getString(R.string.now_setting) + getString(R.string.silent);
			} else {
				summary += BR + getString(R.string.now_setting) + rm.getTitle(mActivity);
			}
			pref.setSummary(getSummarySpannableString(summary));

			return true;
		}
	};

	/**
	 * 目覚まし音が変更された.
	 */
	private final OnPreferenceChangeListener mInquiryChanged = new OnPreferenceChangeListener() {
		@Override
		public boolean onPreferenceChange(Preference preference, Object newValue) {
			String inquiry = (String) newValue;
			if (!"".equals(inquiry)) {
				String inquiryKey = mSettingDao.inquiryVal2Key(inquiry);
				String subject = "[" + inquiryKey + "]" + getString(R.string.app_name);
				// メール送信
				MyMail.Builder.newInstance(getApplicationContext())
						.setTo(getString(R.string.developer_email))
						.setSubject(subject)
						.send();
			}

			return false; // データの変更はしない
		}
	};
}
