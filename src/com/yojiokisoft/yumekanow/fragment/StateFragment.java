package com.yojiokisoft.yumekanow.fragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
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
				LinearLayout layout = new LinearLayout(mActivity);
				layout.setPadding(10, 10, 10, 10);
				convertView = layout;
				
				// ｎ日目
				TextView nday = new TextView(mActivity);
				nday.setTag("nday");
				nday.setTextColor(Color.rgb(0, 255, 128));
				nday.setPadding(0, 0, 10, 0);
				layout.addView(nday);
				
				// 日付
				TextView date = new TextView(mActivity);
				date.setTag("date");
				date.setTextColor(Color.rgb(0, 128, 255));
				date.setPadding(0, 0, 10, 0);
				layout.addView(date);
				
				// OKカウント
				TextView cnt = new TextView(mActivity);
				cnt.setTag("cnt");
				cnt.setTextColor(Color.rgb(255, 128, 128));
				cnt.setPadding(0, 0, 10, 0);
				layout.addView(cnt);
			}
			// 値の設定
			String s;
			TextView nday = (TextView)convertView.findViewWithTag("nday");
			s = String.format("%02d", item.getDay()) + "日目";
			nday.setText(s);
			TextView date = (TextView)convertView.findViewWithTag("date");
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd'('E')'", Locale.JAPAN);
			s = format.format(item.getDate());
			date.setText(s);
			TextView myCnt = (TextView)convertView.findViewWithTag("cnt");
			s = String.format("%2d", item.getDay()) + "回";
			myCnt.setText(s);
			
			return convertView;
		}
	}

}
