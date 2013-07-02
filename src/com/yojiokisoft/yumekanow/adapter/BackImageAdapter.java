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

import java.util.List;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.yojiokisoft.yumekanow.entity.BackImageEntity;

/**
 * 背景画像アダプタークラス
 */
public class BackImageAdapter extends BaseAdapter {
	private Context mContext;
	private List<BackImageEntity> mItems;

	/**
	 * コンストラクタ.
	 * 
	 * @param context
	 * @param items
	 */
	public BackImageAdapter(Context context, List<BackImageEntity> items) {
		super();
		mContext = context;
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
		return mItems.get(pos).resouceId;
	}

	/**
	 * @see BaseAdapter#getView(int, View, ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LinearLayout layout = new LinearLayout(mContext);
			layout.setPadding(5, 5, 5, 5);
			layout.setOrientation(LinearLayout.VERTICAL);
			layout.setGravity(Gravity.CENTER);
			convertView = layout;

			ImageView view = new ImageView(mContext);
			view.setTag("image");
			view.setLayoutParams(new LinearLayout.LayoutParams(80, 120));
			layout.addView(view);
		}

		BackImageEntity item = mItems.get(position);
		ImageView imageView = (ImageView) convertView.findViewWithTag("image");
		if (item.resouceId == 0) {
			imageView.setImageBitmap(BitmapFactory.decodeFile(item.bitmapPath));
		} else {
			imageView.setImageResource(item.resouceId);
		}

		return convertView;
	}

	/**
	 * データのセット（コンストラクタでセットしたデータを総入れ替え）.
	 * 
	 * @param items
	 */
	public void setData(List<BackImageEntity> items) {
		if (mItems != null) {
			mItems = null;
		}
		mItems = items;
	}
}