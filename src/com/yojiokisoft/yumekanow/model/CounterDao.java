package com.yojiokisoft.yumekanow.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.yojiokisoft.yumekanow.db.DatabaseHelper;
import com.yojiokisoft.yumekanow.entity.CounterEntity;
import com.yojiokisoft.yumekanow.entity.DayCntEntity;
import com.yojiokisoft.yumekanow.entity.EncouragementMsgEntity;
import com.yojiokisoft.yumekanow.utils.MyConst;

public class CounterDao {
	private Dao<CounterEntity, Integer> mCounterDao = null;
	private int mCurrentCardId;
	private Context mContext;

	public CounterDao(Context context) throws SQLException {
		mContext = context;
		DatabaseHelper helper = DatabaseHelper.getInstance(context);
		mCounterDao = helper.getDao(CounterEntity.class);

		mCurrentCardId = SettingDao.getInstance(context).getUseCard();
	}

	public int getCurrentCardId() {
		return mCurrentCardId;
	}

	/**
	 * 成長レベル(1〜100)の取得
	 * @return
	 * @throws SQLException 
	 */
	/*
	public int getGrowLevel() throws SQLException {
		GenericRawResults<String[]> rawResults = null;
		int ret = 0;
		rawResults = mCounterDao
				.queryRaw(
				"select case when sum(okCnt) is null then 0 else sum(okCnt) end as okCntSum from counter where cardId = "
						+ mCurrentCardId);
		List<String[]> results = rawResults.getResults();
		String[] resultArray = results.get(0);
		ret = Integer.parseInt(resultArray[0]);

		return ret;
	}
	*/

	/**
	 * OKカウントの取得
	 * @return
	 * @throws SQLException 
	 */
	public int getOkCnt() throws SQLException {
		GenericRawResults<String[]> rawResults = null;
		int ret = 0;
		rawResults = mCounterDao
				.queryRaw(
				"select case when sum(okCnt) is null then 0 else sum(okCnt) end as okCntSum from counter where cardId = "
						+ mCurrentCardId);
		List<String[]> results = rawResults.getResults();
		String[] resultArray = results.get(0);
		ret = Integer.parseInt(resultArray[0]);

		return ret;
	}

	/**
	 * 日々カウントの取得
	 * @return
	 * @throws SQLException 
	 */
	public List<DayCntEntity> getDayCnt() throws SQLException {
		GenericRawResults<String[]> rawResults = null;
		List<DayCntEntity> ret = new ArrayList<DayCntEntity>();
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
		setEncouragementMsg(ret, false);

		// debug >>>
		/*
		List<DayCntEntity> ret = new ArrayList<DayCntEntity>();
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2013);
		cal.set(Calendar.MONTH, 4 - 1);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		int totalOkCnt = 0;
		int totalNgCnt = 0;
		Random rand = new Random();
		for (int i = 0; i < 116; i++) {
			DayCntEntity dayCnt = new DayCntEntity();
			dayCnt.day = i + 1;
			dayCnt.date = cal;
			dayCnt.okCnt = rand.nextInt(8) + 9;
			dayCnt.ngCnt = rand.nextInt(3) + 0;
			totalOkCnt += dayCnt.okCnt;
			totalNgCnt += dayCnt.ngCnt;
			dayCnt.totalOkCnt = totalOkCnt;
			dayCnt.totalNgCnt = totalNgCnt;
			ret.add(dayCnt);
			cal.add(Calendar.DAY_OF_MONTH, 1);
		}
		setEncouragementMsg(ret, false);
		*/
		// debug <<<

		return ret;
	}

	public void setEncouragementMsg(List<DayCntEntity> list) {
		setEncouragementMsg(list, true);
	}

	private void setEncouragementMsg(List<DayCntEntity> list, boolean clearFlag) {
		DayCntEntity item;
		int size = list.size();
		if (size <= 0) {
			return;
		}
		if (clearFlag) {
			for (int i = 0; i < size; i++) {
				item = list.get(i);
				if (item.encouragmentMsg != null) {
					item.encouragmentMsg = null;
					list.set(i, item);
				}
			}
		}

		int[] percent = new int[size];
		SettingDao settingDao = SettingDao.getInstance(mContext);
		int goalCnt = settingDao.getGoalCnt();
		for (int i = 0; i < size; i++) {
			percent[i] = list.get(i).totalOkCnt * 100 / goalCnt;
		}

		EncouragementMsgEntity[] msg = MyConst.getEncouragementMsg(mContext);
		int len = msg.length;
		size = list.size();
		int max;
		int startIndex = 0;
		for (int i = 0; i < len; i++) {
			if (i >= len - 1) {
				max = Integer.MAX_VALUE;
			} else {
				max = msg[i + 1].percent - 1;
			}
			if (msg[i].day != 0) {
				if (size >= msg[i].day) {
					item = list.get(msg[i].day - 1);
					item.encouragmentMsg = msg[i].message;
					list.set(msg[i].day - 1, item);
				}
			} else {
				for (int j = startIndex; j < size; j++) {
					item = list.get(j);
					if (item.encouragmentMsg != null) {
						continue;
					}
					if (msg[i].percent <= percent[j] && percent[j] <= max) {
						item.encouragmentMsg = msg[i].message;
						list.set(j, item);
						startIndex = j + 1;
						break;
					}
				}
			}
		}
	}

	public void setCounter(CounterEntity counter) throws SQLException {
		mCounterDao.create(counter);
	}
}
