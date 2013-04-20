package com.yojiokisoft.yumekanow.model;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.yojiokisoft.yumekanow.db.DatabaseHelper;
import com.yojiokisoft.yumekanow.entity.CounterEntity;

public class CounterDao {
	private Dao<CounterEntity, Integer> mCounterDao = null;
	private int mCurrentCardId;

	private CounterDao() {
	}

	public CounterDao(Context context) {
		DatabaseHelper helper = DatabaseHelper.getInstance(context);
		try {
			mCounterDao = helper.getDao(CounterEntity.class);
			// 明示的にonCreateを呼ばないとテーブルが作られない（onCreateはDBが存在しない最初だけしか呼ばれない）
			//helper.onCreate(helper.getWritableDatabase(),helper.getConnectionSource());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		mCurrentCardId = sp.getInt("UseCard", -1);
		Log.d("taoka", "CounterDao.constractor:UseCard=" + mCurrentCardId);
	}

	public int getCurrentCardId() {
		return mCurrentCardId;
	}

	/**
	 * 成長レベル(1〜100)の取得
	 * @return
	 */
	public int getGrowLevel() {
		GenericRawResults<String[]> rawResults = null;
		int ret = 0;
		try {
			rawResults = mCounterDao
					.queryRaw(
					"select case when sum(okCnt) is null then 0 else sum(okCnt) end as okCntSum from counter where cardId = "
							+ mCurrentCardId);
			List<String[]> results = rawResults.getResults();
			String[] resultArray = results.get(0);
			Log.d("taoka", "getGrowLevel=" + resultArray[0]);
			ret = Integer.parseInt(resultArray[0]);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ret;
	}

	public void setCounter(CounterEntity counter) {
		try {
			mCounterDao.create(counter);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
