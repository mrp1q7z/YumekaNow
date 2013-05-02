package com.yojiokisoft.yumekanow.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.yojiokisoft.yumekanow.db.DatabaseHelper;
import com.yojiokisoft.yumekanow.entity.CounterEntity;
import com.yojiokisoft.yumekanow.entity.DayCntEntity;

public class CounterDao {
	private Dao<CounterEntity, Integer> mCounterDao = null;
	private int mCurrentCardId;

	private CounterDao() {
	}

	public CounterDao(Context context) {
		DatabaseHelper helper = DatabaseHelper.getInstance(context);
		try {
			mCounterDao = helper.getDao(CounterEntity.class);
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

	/**
	 * OKカウントの取得
	 * @return
	 */
	public int getOkCnt() {
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

	/**
	 * 日々カウントの取得
	 * @return
	 */
	public List<DayCntEntity> getDayCnt() {
		GenericRawResults<String[]> rawResults = null;
		List<DayCntEntity> ret = new ArrayList<DayCntEntity>();
		try {
			rawResults = mCounterDao
					.queryRaw(
					"select procDay, sum(okCnt), sum(ngCnt) from counter where cardId = "
							+ mCurrentCardId + " group by procDay");
			List<String[]> results = rawResults.getResults();
			int day = 0;
			int totalOkCnt = 0;
			int totalNgCnt = 0;
			for (String[] resultArray : results) {
				day++;
				DayCntEntity dayCnt = new DayCntEntity();
				dayCnt.day = day;
				String yyyymmdd = resultArray[0];
				Calendar date = Calendar.getInstance();
				date.set(Calendar.YEAR, Integer.parseInt(yyyymmdd.substring(0, 4)));
				date.set(Calendar.MONTH, Integer.parseInt(yyyymmdd.substring(4, 6)) - 1);
				date.set(Calendar.DAY_OF_MONTH, Integer.parseInt(yyyymmdd.substring(6, 8)));
				dayCnt.date = date;
				dayCnt.okCnt = Integer.parseInt(resultArray[1]);
				dayCnt.ngCnt = Integer.parseInt(resultArray[2]);
				totalOkCnt += dayCnt.okCnt;
				totalNgCnt += dayCnt.ngCnt;
				dayCnt.totalOkCnt = totalOkCnt;
				dayCnt.totalNgCnt = totalNgCnt;
				ret.add(dayCnt);
			}
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
