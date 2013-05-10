package com.yojiokisoft.yumekanow.fragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.yojiokisoft.yumekanow.R;
import com.yojiokisoft.yumekanow.entity.DayCntEntity;
import com.yojiokisoft.yumekanow.model.CounterDao;
import com.yojiokisoft.yumekanow.model.SettingDao;
import com.yojiokisoft.yumekanow.mycomponent.MyProgress;

public class StateFragment extends Fragment {
	private BaseAdapter mAdapter;
	private View mView;
	private Activity mActivity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mView = null;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d("taoka", "onCreateView");
		mView = inflater.inflate(R.layout.fragment_state, container, false);

		setProgress();

		return mView;
	}

	private void setProgress() {
		SettingDao settingDao = SettingDao.getInstance(mActivity);
		CounterDao counter = new CounterDao(mActivity);

		int goalCnt = settingDao.getGoalCnt();
		int okCnt = counter.getOkCnt();

		MyProgress progress = (MyProgress) mView.findViewById(R.id.totalProgress);
		progress.setShowPercent(true);
		progress.setDescription(okCnt + "/" + goalCnt);
		progress.setMax(100);
		progress.setProgress(okCnt * 100 / goalCnt);
	}

	@Override
	public void onAttach(Activity activity) {
		Log.d("taoka", "onAttach");
		super.onAttach(activity);
		mActivity = activity;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		Log.d("taoka", "onActivityCreated");
		super.onActivityCreated(savedInstanceState);

		if (mAdapter == null) {
			CounterDao counter = new CounterDao(mActivity);
			List<DayCntEntity> list = counter.getDayCnt();
			mAdapter = new MyListArrayAdapter(mActivity, list);
		}
		ListView listView = (ListView) mView.findViewById(R.id.dayToDayList);
		listView.setAdapter(mAdapter);
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
			DayCntEntity item = mItems.get(pos);
			// レイアウトの生成
			if (convertView == null) {
				convertView = mActivity.getLayoutInflater().inflate(R.layout.row_state_list, null);
			}
			// 値の設定
			String s;
			TextView nday = (TextView) convertView.findViewById(R.id.nday);
			s = String.format("%02d", item.day) + "日目";
			nday.setText(s);
			TextView date = (TextView) convertView.findViewById(R.id.date);
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd' ('E')'", Locale.JAPAN);
			s = format.format(item.date.getTime());
			date.setText(s);
			TextView myCnt = (TextView) convertView.findViewById(R.id.cnt);
			s = String.format("%2d", item.okCnt) + "回";
			myCnt.setText(s);
			// TODO: ここでパーセンテージを表示（１０％ごとに）
			TextView percent = (TextView) convertView.findViewById(R.id.percent);
			s = getComment(item);
			if (s != null) {
				percent.setText(s);
			}

			return convertView;
		}
	}

	private String getComment(DayCntEntity item) {
		if (item.day == 1) {
			return "スタート";
		}
		if (item.totalOkCnt > 10) {
			return "この調子";
		}
		if (item.totalOkCnt > 900) {
			return "あと少し";
		}
		//		return null;
		return String.valueOf(item.totalOkCnt);
	}

	@Override
	public void onResume() {
		super.onResume();
		setProgress();
	}
}
