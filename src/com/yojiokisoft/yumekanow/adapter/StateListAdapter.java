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

package com.yojiokisoft.yumekanow.adapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yojiokisoft.yumekanow.R;
import com.yojiokisoft.yumekanow.entity.DayCntEntity;

/**
 * 状態一覧のアダプター
 */
public class StateListAdapter extends BaseAdapter {
	private Activity mActivity;
	private List<DayCntEntity> mItems;

	/**
	 * コンストラクタ.
	 * 
	 * @param activity
	 * @param items
	 */
	public StateListAdapter(Activity activity, List<DayCntEntity> items) {
		super();
		mActivity = activity;
		mItems = items;
	}

	/**
	 * アイテムの取得.
	 * 
	 * @return
	 */
	public List<DayCntEntity> getItems() {
		return mItems;
	}

	/**
	 * アイテムのセット.
	 * 
	 * @param items
	 */
	public void setItems(List<DayCntEntity> items) {
		if (mItems != null) {
			mItems = null;
		}
		mItems = items;
	}

	/**
	 * @see BaseAdapter#getCount()
	 */
	@Override
	public int getCount() {
		return mItems.size();
	}

	/**
	 * @see BaseAdapter#getItem(int)
	 */
	@Override
	public Object getItem(int pos) {
		return mItems.get(pos);
	}

	/**
	 * @see BaseAdapter#getItemId(int)
	 */
	@Override
	public long getItemId(int pos) {
		return pos;
	}

	/**
	 * @see BaseAdapter#getView(int, View, ViewGroup)
	 */
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

	/**
	 * ビューを扱いやすくするためのラッパー.
	 */
	private class ViewHolder {
		TextView nday;
		TextView date;
		TextView cnt;
		TextView comment;
	}
}