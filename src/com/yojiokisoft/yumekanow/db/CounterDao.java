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

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.yojiokisoft.yumekanow.entity.CounterEntity;
import com.yojiokisoft.yumekanow.entity.DayCntEntity;
import com.yojiokisoft.yumekanow.entity.EncouragementMsgEntity;
import com.yojiokisoft.yumekanow.utils.MyConst;

/**
 * アファーメーションを唱えた回数のデータアクセス
 */
public class CounterDao {
	private Dao<CounterEntity, Integer> mCounterDao = null;
	private int mCurrentCardId;

	/**
	 * コンストラクタ.
	 * 
	 * @throws SQLException
	 */
	public CounterDao() throws SQLException {
		DatabaseHelper helper = DatabaseHelper.getInstance();
		mCounterDao = helper.getDao(CounterEntity.class);

		mCurrentCardId = SettingDao.getInstance().getUseCard();
	}

	/**
	 * @return 現在使用中のカードID
	 */
	public int getCurrentCardId() {
		return mCurrentCardId;
	}

	/**
	 * @return OKカウント
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
	 * @return 日々カウント
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
		Calendar startDate = Calendar.getInstance();
		startDate.set(Calendar.YEAR, 2013);
		startDate.set(Calendar.MONTH, 4 - 1);
		startDate.set(Calendar.DAY_OF_MONTH, 1);
		int totalOkCnt = 0;
		int totalNgCnt = 0;
		Random rand = new Random();
		for (int i = 0; i < 116; i++) {
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.YEAR, startDate.get(Calendar.YEAR));
			cal.set(Calendar.MONTH, startDate.get(Calendar.MONTH));
			cal.set(Calendar.DAY_OF_MONTH, startDate.get(Calendar.DAY_OF_MONTH));

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

			startDate.add(Calendar.DAY_OF_MONTH, 1);
		}
		setEncouragementMsg(ret, false);
		*/
		// debug <<<

		return ret;
	}

	/**
	 * 日々カウントに励ましメッセージをセット.
	 * 
	 * @param list 日々カウント
	 */
	public void setEncouragementMsg(List<DayCntEntity> list) {
		setEncouragementMsg(list, true);
	}

	/**
	 * 日々カウントに励ましメッセージをセット.
	 * 
	 * @param list 日々カウント
	 * @param clearFlag 最初に励ましメッセージをクリアするかどうか
	 */
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
		SettingDao settingDao = SettingDao.getInstance();
		int goalCnt = settingDao.getGoalCnt();
		for (int i = 0; i < size; i++) {
			percent[i] = list.get(i).totalOkCnt * 100 / goalCnt;
		}

		EncouragementMsgEntity[] msg = MyConst.getEncouragementMsg();
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

	/**
	 * 回数データの登録
	 * 
	 * @param counter
	 * @return データベース内で更新された行数。これは1でなければなりません。
	 * @throws SQLException
	 */
	public int setCounter(CounterEntity counter) throws SQLException {
		return mCounterDao.create(counter);
	}

	/**
	 * 唱えた回数を登録する.
	 * 
	 * @param advocated アファーメーションを唱えた場合はtrue
	 * @throws SQLException 
	 */
	public void setCounter(boolean advocated) throws SQLException {
		int currentCardId = getCurrentCardId();

		CounterEntity cnt = new CounterEntity();
		cnt.cardId = currentCardId;
		cnt.procTime = System.currentTimeMillis();
		if (advocated) {
			cnt.okCnt = 1;
			cnt.ngCnt = 0;
		} else {
			cnt.okCnt = 0;
			cnt.ngCnt = 1;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.JAPANESE);
		cnt.procDay = sdf.format(new Date(cnt.procTime));
		setCounter(cnt);
	}
}
