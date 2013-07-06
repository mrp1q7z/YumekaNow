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

package com.yojiokisoft.yumekanow.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;

import com.yojiokisoft.yumekanow.App;
import com.yojiokisoft.yumekanow.R;
import com.yojiokisoft.yumekanow.utils.MyConst;

/**
 * 設定情報のデータアクセス
 */
public class SettingDao {
	private static SettingDao mInstance = null;
	private static SharedPreferences mSharedPref = null;
	private static Context mContext;
	private static String[] mDispIntervalKey;
	private static String[] mDispIntervalVal;
	private static String[] mInquiryKey;
	private static String[] mInquiryVal;

	/**
	 * コンストラクタは公開しない
	 * インスタンスを取得する場合は、getInstanceを使用する.
	 */
	private SettingDao() {
	}

	/**
	 * インスタンスの取得.
	 * 
	 * @return SettingDao
	 */
	public static SettingDao getInstance() {
		if (mInstance == null) {
			mInstance = new SettingDao();
			mContext = App.getInstance().getAppContext();
			mSharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
			mDispIntervalKey = mContext.getResources().getStringArray(R.array.disp_interval_key);
			mDispIntervalVal = mContext.getResources().getStringArray(R.array.disp_interval_val);
			mInquiryKey = mContext.getResources().getStringArray(R.array.inquiry_key);
			mInquiryVal = mContext.getResources().getStringArray(R.array.inquiry_val);
		}
		return mInstance;
	}

	/**
	 * @return 表示間隔
	 */
	public int getDispInterval() {
		String val = mSharedPref.getString(MyConst.PK_DISP_INTERVAL, mContext.getString(R.string.defaultDispInterval));
		return Integer.parseInt(val);
	}

	/**
	 * 表示間隔を値からキーに変換
	 * 例：90 -> 1時間30分
	 * 
	 * @param val
	 * @return
	 */
	public String dispIntervalVal2Key(String val) {
		String ret = null;

		for (int i = 0; i < mDispIntervalVal.length; i++) {
			if (mDispIntervalVal[i].equals(val)) {
				ret = mDispIntervalKey[i];
				break;
			}
		}
		return ret;
	}

	/**
	 * バイブレータを値からキーに変換
	 * 例：true -> オン
	 * 
	 * @param val
	 * @return
	 */
	public String vibratorVal2Key(boolean isChecked) {
		String key;

		if (isChecked) {
			key = mContext.getString(R.string.vibrator_on);
		} else {
			key = mContext.getString(R.string.vibrator_off);
		}
		return key;
	}

	/**
	 * お問い合わせを値からキーに変換
	 * 例：questions -> ご質問
	 * 
	 * @param val
	 * @return
	 */
	public String inquiryVal2Key(String val) {
		String key = null;

		for (int i = 0; i < mInquiryVal.length; i++) {
			if (mInquiryVal[i].equals(val)) {
				key = mInquiryKey[i];
				break;
			}
		}
		return key;
	}

	/**
	 * @return 表示間隔を文字列で（例：１時間３０分）
	 */
	public String getDispIntervalString() {
		String val = mSharedPref.getString(MyConst.PK_DISP_INTERVAL, mContext.getString(R.string.defaultDispInterval));
		return dispIntervalVal2Key(val);
	}

	/**
	 * @return 目標回数
	 */
	public int getGoalCnt() {
		return Integer
				.parseInt(mSharedPref.getString(MyConst.PK_GOAL_CNT, mContext.getString(R.string.defaultGoalCnt)));
	}

	/**
	 * @return バイブレータのON/OFF状態
	 */
	public boolean getVibrator() {
		return mSharedPref.getBoolean(MyConst.PK_VIBRATOR, false);
	}

	/**
	 * @return バイルレータのON/OFF状態を文字列で
	 */
	public String getVibratorString() {
		return vibratorVal2Key(mSharedPref.getBoolean(MyConst.PK_VIBRATOR, false));
	}

	/**
	 * @return アラーム音のURL
	 */
	public String getAlarmUrl() {
		String url = mSharedPref.getString(MyConst.PK_ALARM, "");
		String ret = null;
		if (url.length() > 0) {
			ret = url;
		}
		return ret;
	}

	/**
	 * @return アラーム音のタイトル
	 */
	public String getAlarm() {
		String url = mSharedPref.getString(MyConst.PK_ALARM, "");
		String ret = null;
		if (url.length() <= 0) {
			ret = "サイレント";
		} else {
			Ringtone rm = RingtoneManager.getRingtone(mContext, Uri.parse(url));
			ret = rm.getTitle(mContext);
		}
		return ret;
	}

	/**
	 * @return 目覚まし音のURL
	 */
	public String getSleepAlarmUrl() {
		String url = mSharedPref.getString(MyConst.PK_SLEEP_ALARM, "");
		String ret = null;
		if (url.length() > 0) {
			ret = url;
		}
		return ret;
	}

	/**
	 * @return 目覚まし音のタイトル
	 */
	public String getSleepAlarm() {
		String url = mSharedPref.getString(MyConst.PK_SLEEP_ALARM, "");
		String ret = null;
		if (url.length() <= 0) {
			ret = "サイレント";
		} else {
			Ringtone rm = RingtoneManager.getRingtone(mContext, Uri.parse(url));
			ret = rm.getTitle(mContext);
		}
		return ret;
	}

	/**
	 * @return 使用中のカードID
	 */
	public int getUseCard() {
		return mSharedPref.getInt("UseCard", -1);
	}

	/**
	 * 使用中のカードIDをセットする.
	 * 
	 * @param cardId
	 */
	public void setUseCard(int cardId) {
		mSharedPref.edit().putInt("UseCard", cardId).commit();
	}

	/**
	 * @return スリープ時間
	 */
	public String getSleepJikan() {
		return mSharedPref.getString(MyConst.PK_SLEEP_JIKAN, "");
	}

	/**
	 * スリープ時間のセット.
	 * 
	 * @param jikan
	 */
	public void setSleepJikan(String jikan) {
		mSharedPref.edit().putString(MyConst.PK_SLEEP_JIKAN, jikan).commit();
	}

	/**
	 * @return 何分後タイマーの時間
	 */
	public String getSleepTimer() {
		return mSharedPref.getString(MyConst.PK_SLEEP_TIMER, "");
	}

	/**
	 * 何分後タイマーの時間をセットする.
	 * 
	 * @param timer
	 */
	public void setSleepTimer(String timer) {
		mSharedPref.edit().putString(MyConst.PK_SLEEP_TIMER, timer).commit();
	}

	/**
	 * @return 次回開始時間
	 */
	public long getNextStartTime() {
		return mSharedPref.getLong(MyConst.PK_NEXT_START_TIME, 0);
	}

	/**
	 * 次回開始時間のセット.
	 * 
	 * @param time
	 */
	public void setNextStartTime(long time) {
		mSharedPref.edit().putLong(MyConst.PK_NEXT_START_TIME, time).commit();
	}

	/**
	 * @return 目覚まし時間
	 */
	public long getWakeUpTime() {
		return mSharedPref.getLong(MyConst.PK_WAKE_UP_TIME, 0);
	}

	/**
	 * 目覚まし時間のセット.
	 * 
	 * @param time
	 */
	public void setWakeUpTime(long time) {
		mSharedPref.edit().putLong(MyConst.PK_WAKE_UP_TIME, time).commit();
	}
}
