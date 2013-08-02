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

package com.yojiokisoft.yumekanow.fragment;

import java.sql.SQLException;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ViewById;
import com.yojiokisoft.yumekanow.R;
import com.yojiokisoft.yumekanow.adapter.StateListAdapter;
import com.yojiokisoft.yumekanow.db.CounterDao;
import com.yojiokisoft.yumekanow.db.SettingDao;
import com.yojiokisoft.yumekanow.entity.DayCntEntity;
import com.yojiokisoft.yumekanow.exception.MyUncaughtExceptionHandler;

/**
 * 状態フラグメント
 */
@EFragment(R.layout.fragment_state)
public class StateFragment extends Fragment {
	@ViewById(R.id.totalProgress)
	/*package*/ProgressBar mProgress;

	@ViewById(R.id.percent)
	/*package*/TextView mPercent;

	@ViewById(R.id.description)
	/*package*/TextView mDescription;

	@ViewById(R.id.dayToDayList)
	/*package*/ListView mListView;

	private Activity mActivity;
	private CounterDao mCounter;
	private int mGoalCnt;

	/**
	 * フラグメントがアクティビティにアタッチされたときに呼ばれる.
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = activity;
		try {
			mCounter = new CounterDao();
		} catch (SQLException e) {
			MyUncaughtExceptionHandler.sendBugReport(mActivity, e);
		}
	}

	/**
	 * フラグメント用のビューを作成
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// nullを返すことで @EFragment(xxx) で定義している xxx がインフレートされる
		return null;
	}

	/**
	 * プログレスバーのセット
	 */
	@AfterViews
	/*package*/void setProgress() {
		SettingDao settingDao = SettingDao.getInstance();
		int okCnt = 0;
		try {
			okCnt = mCounter.getOkCnt();
		} catch (SQLException e) {
			MyUncaughtExceptionHandler.sendBugReport(mActivity, e);
		}

		int goalCnt = settingDao.getGoalCnt();

		int percent = okCnt * 100 / goalCnt;
		mPercent.setText(percent + "%");
		mDescription.setText(okCnt + "/" + goalCnt);
		mProgress.setMax(100);
		mProgress.setProgress(percent);
	}

	/**
	 * リストビューのアダプターをセット
	 */
	@AfterViews
	/*package*/void setAdapter() {
		List<DayCntEntity> list = null;
		try {
			list = mCounter.getDayCnt();
		} catch (SQLException e) {
			MyUncaughtExceptionHandler.sendBugReport(mActivity, e);
		}
		if (list == null) {
			return;
		}
		BaseAdapter adapter = new StateListAdapter(mActivity, list);
		mListView.setAdapter(adapter);

		SettingDao settingDao = SettingDao.getInstance();
		mGoalCnt = settingDao.getGoalCnt();
	}

	/**
	 * 前面に表示された.
	 */
	@Override
	public void onResume() {
		super.onResume();

		SettingDao settingDao = SettingDao.getInstance();
		if (mGoalCnt != settingDao.getGoalCnt()) {
			mGoalCnt = settingDao.getGoalCnt();
			setProgress();
			List<DayCntEntity> list = ((StateListAdapter) mListView.getAdapter()).getItems();
			mCounter.setEncouragementMsg(list);
			((StateListAdapter) mListView.getAdapter()).setItems(list);
			mListView.invalidateViews();
		}
	}
}
