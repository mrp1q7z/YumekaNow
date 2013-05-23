package com.yojiokisoft.yumekanow.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;

import com.yojiokisoft.yumekanow.R;

public class SettingDao {
	private static SettingDao mInstance = null;
	private static SharedPreferences mSharedPref = null;
	private static Context mContext;
	private static String[] mDispIntervalKey;
	private static String[] mDispIntervalVal;
	private static String[] mAnimationKey;
	private static String[] mAnimationVal;

	private SettingDao() {
	}

	public static SettingDao getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new SettingDao();
			mSharedPref = PreferenceManager.getDefaultSharedPreferences(context);
			mContext = context;
			mDispIntervalKey = mContext.getResources().getStringArray(R.array.disp_interval_key);
			mDispIntervalVal = mContext.getResources().getStringArray(R.array.disp_interval_val);
			mAnimationKey = mContext.getResources().getStringArray(R.array.animation_key);
			mAnimationVal = mContext.getResources().getStringArray(R.array.animation_val);
		}
		return mInstance;
	}

	public int getDispInterval() {
		String val = mSharedPref.getString("DISP_INTERVAL", "");
		return Integer.parseInt(val);
	}

	public String getDispIntervalString() {
		String val = mSharedPref.getString("DISP_INTERVAL", "");
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
		return Integer.parseInt(mSharedPref.getString("GOAL_CNT", mContext.getString(R.string.goalCnt)));
	}

	public String getAnimation() {
		String val = mSharedPref.getString("ANIMATION", "");
		String ret = null;
		for (int i = 0; i < mAnimationVal.length; i++) {
			if (mAnimationVal[i].equals(val)) {
				ret = mAnimationKey[i];
				break;
			}
		}
		return ret;
	}

	public String getAnimationId() {
		return mSharedPref.getString("ANIMATION", "");
	}
	
	public boolean getVibrator() {
		return mSharedPref.getBoolean("Vibrator", false);
	}

	public String getAlarmUrl() {
		String url = mSharedPref.getString("Alarm", "");
		String ret = null;
		if (url.length() > 0) {
			ret = url;
		}
		return ret;
	}
	
	public String getAlarm() {
		String url = mSharedPref.getString("Alarm", "");
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
}
