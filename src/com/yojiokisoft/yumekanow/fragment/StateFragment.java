package com.yojiokisoft.yumekanow.fragment;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ViewById;
import com.yojiokisoft.yumekanow.R;
import com.yojiokisoft.yumekanow.entity.DayCntEntity;
import com.yojiokisoft.yumekanow.exception.MyUncaughtExceptionHandler;
import com.yojiokisoft.yumekanow.model.CounterDao;
import com.yojiokisoft.yumekanow.model.SettingDao;
import com.yojiokisoft.yumekanow.mycomponent.MyProgress;

@EFragment(R.layout.fragment_state)
public class StateFragment extends Fragment {
	private Activity mActivity;

	@ViewById(R.id.totalProgress)
	MyProgress mProgress;

	@ViewById(R.id.dayToDayList)
	ListView mListView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return null;
	}

	@AfterViews
	void setProgress() {
		SettingDao settingDao = SettingDao.getInstance(mActivity);
		CounterDao counter;
		int okCnt = 0;
		try {
			counter = new CounterDao(mActivity);
			okCnt = counter.getOkCnt();
		} catch (SQLException e) {
			MyUncaughtExceptionHandler.sendBugReport(mActivity, e);
		}

		int goalCnt = settingDao.getGoalCnt();

		mProgress.setShowPercent(true);
		mProgress.setDescription(okCnt + "/" + goalCnt);
		mProgress.setMax(100);
		mProgress.setProgress(okCnt * 100 / goalCnt);
	}

	@AfterViews
	void setAdapter() {
		CounterDao counter;
		List<DayCntEntity> list = null;
		try {
			counter = new CounterDao(mActivity);
			list = counter.getDayCnt();
		} catch (SQLException e) {
			MyUncaughtExceptionHandler.sendBugReport(mActivity, e);
		}
		if (list == null) {
			return;
		}
		BaseAdapter adapter = new MyListArrayAdapter(mActivity, list);
		mListView.setAdapter(adapter);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = activity;
	}

	@Override
	public void onResume() {
		super.onResume();
		setProgress();
	}

	/**
	 * アダプタークラス
	 */
	private class MyListArrayAdapter extends BaseAdapter {
		private Activity mActivity;
		private List<DayCntEntity> mItems;

		MyListArrayAdapter(Activity activity, List<DayCntEntity> items) {
			super();
			mActivity = activity;
			mItems = items;
		}

		@Override
		public int getCount() {
			return mItems.size();
		}

		@Override
		public Object getItem(int pos) {
			return mItems.get(pos);
		}

		@Override
		public long getItemId(int pos) {
			return pos;
		}

		@Override
		public View getView(int pos, View convertView, ViewGroup parent) {
			ViewHolder holder;
			DayCntEntity item = mItems.get(pos);
			// レイアウトの生成
			if (convertView == null) {
				convertView = mActivity.getLayoutInflater().inflate(R.layout.row_state_list, null);
				TextView nday = (TextView) convertView.findViewById(R.id.nday);
				TextView date = (TextView) convertView.findViewById(R.id.date);
				TextView cnt = (TextView) convertView.findViewById(R.id.cnt);
				TextView percent = (TextView) convertView.findViewById(R.id.percent);
				holder = new ViewHolder();
				holder.nday = nday;
				holder.date = date;
				holder.cnt = cnt;
				holder.comment = percent;
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			// 値の設定
			String s;
			s = String.format("%02d", item.day) + "日目";
			holder.nday.setText(s);
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd' ('E')'", Locale.JAPAN);
			s = format.format(item.date.getTime());
			holder.date.setText(s);
			s = String.format("%2d", item.okCnt) + "回";
			holder.cnt.setText(s);
			holder.comment.setText(item.encouragmentMsg);

			return convertView;
		}

		class ViewHolder {
			TextView nday;
			TextView date;
			TextView cnt;
			TextView comment;
		}
	}
}
