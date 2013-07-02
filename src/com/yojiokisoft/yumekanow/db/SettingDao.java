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

public class SettingDao {
	private static SettingDao mInstance = null;
	private static SharedPreferences mSharedPref = null;
	private static Context mContext;
	private static String[] mDispIntervalKey;
	private static String[] mDispIntervalVal;

	private SettingDao() {
	}

	public static SettingDao getInstance() {
		if (mInstance == null) {
			mInstance = new SettingDao();
			mContext = App.getInstance().getAppContext();
			mSharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
			mDispIntervalKey = mContext.getResources().getStringArray(R.array.disp_interval_key);
			mDispIntervalVal = mContext.getResources().getStringArray(R.array.disp_interval_val);
		}
		return mInstance;
	}

	public int getDispInterval() {
		String val = mSharedPref.getString(MyConst.PK_DISP_INTERVAL, mContext.getString(R.string.defaultDispInterval));
		return Integer.parseInt(val);
	}

	public String getDispIntervalString() {
		String val = mSharedPref.getString(MyConst.PK_DISP_INTERVAL, mContext.getString(R.string.defaultDispInterval));
		String ret = null;
		for (int i = 0; i < mDispIntervalVal.length; i++) {
			if (mDispIntervalVal[i].equals(val)) {
				ret = mDispIntervalKey[i];
				break;
			}
		}
		return ret;
	}

	public int getGoalCnt() {
		return Integer.parseInt(mSharedPref.getString(MyConst.PK_GOAL_CNT, mContext.getString(R.string.defaultGoalCnt)));
	}

	public boolean getVibrator() {
		return mSharedPref.getBoolean(MyConst.PK_VIBRATOR, false);
	}

	public String getAlarmUrl() {
		String url = mSharedPref.getString(MyConst.PK_ALARM, "");
		String ret = null;
		if (url.length() > 0) {
			ret = url;
		}
		return ret;
	}

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

	public String getSleepAlarmUrl() {
		String url = mSharedPref.getString(MyConst.PK_SLEEP_ALARM, "");
		String ret = null;
		if (url.length() > 0) {
			ret = url;
		}
		return ret;
	}

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

	public int getUseCard() {
		return mSharedPref.getInt("UseCard", -1);
	}

	public void setUseCard(int cardId) {
		mSharedPref.edit().putInt("UseCard", cardId).commit();
	}

	public String getSleepJikan() {
		return mSharedPref.getString(MyConst.PK_SLEEP_JIKAN, "");
	}

	public void setSleepJikan(String jikan) {
		mSharedPref.edit().putString(MyConst.PK_SLEEP_JIKAN, jikan).commit();
	}

	public String getSleepTimer() {
		return mSharedPref.getString(MyConst.PK_SLEEP_TIMER, "");
	}

	public void setSleepTimer(String timer) {
		mSharedPref.edit().putString(MyConst.PK_SLEEP_TIMER, timer).commit();
	}
	
	public long getNextStartTime() {
		return mSharedPref.getLong(MyConst.PK_NEXT_START_TIME, 0);
	}
	
	public void setNextStartTime(long time) {
		mSharedPref.edit().putLong(MyConst.PK_NEXT_START_TIME, time).commit();
	}
	
	public long getWakeUpTime() {
		return mSharedPref.getLong(MyConst.PK_WAKE_UP_TIME, 0);
	}
	
	public void setWakeUpTime(long time) {
		mSharedPref.edit().putLong(MyConst.PK_WAKE_UP_TIME, time).commit();
	}
}
