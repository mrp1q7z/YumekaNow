package com.yojiokisoft.yumekanow.fragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

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

import com.yojiokisoft.yumekanow.R;
import com.yojiokisoft.yumekanow.model.DayCnt;
import com.yojiokisoft.yumekanow.widget.MyProgress;

public class StateFragment extends Fragment {
	//	private ArrayAdapter<DayCnt> adapter;
	private BaseAdapter adapter;
	private View view;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = null;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_state, container, false);

		MyProgress progress = (MyProgress) view.findViewById(R.id.totalProgress);
		progress.setShowPsersent(true);
		progress.setDescription("345/999");
		ProgressBar p = progress.getProgressBar();
		p.setMax(100);
		p.setProgress(37);

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		if (adapter == null) {
			ArrayList<DayCnt> list = (ArrayList<DayCnt>) getArguments().getSerializable("DTO");
			adapter = new MyListArrayAdapter(getActivity(), list);
		}
		ListView listView = (ListView) view.findViewById(R.id.dayToDayList);
		listView.setAdapter(adapter);
	}

	/**
	 * アダプタークラス
	 */
	private class MyListArrayAdapter extends BaseAdapter {
		private Activity mActivity;
		private ArrayList<DayCnt> mItems;

		MyListArrayAdapter(Activity activity, ArrayList<DayCnt> items) {
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
			DayCnt item = mItems.get(pos);
			// レイアウトの生成
			if (convertView == null) {
				convertView = mActivity.getLayoutInflater().inflate(R.layout.row_state_list, null);
			}
			// 値の設定
			String s;
			TextView nday = (TextView) convertView.findViewById(R.id.nday);
			s = String.format("%02d", item.getDay()) + "日目";
			nday.setText(s);
			TextView date = (TextView) convertView.findViewById(R.id.date);
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd' ('E')'", Locale.JAPAN);
			s = format.format(item.getDate());
			date.setText(s);
			TextView myCnt = (TextView) convertView.findViewById(R.id.cnt);
			s = String.format("%2d", item.getDay()) + "回";
			myCnt.setText(s);
			// TODO: ここでパーセンテージを表示（１０％ごとに）

			return convertView;
		}
	}

}
