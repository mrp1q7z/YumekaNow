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

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yojiokisoft.yumekanow.R;
import com.yojiokisoft.yumekanow.entity.CardEntity;
import com.yojiokisoft.yumekanow.utils.MyImage;

/**
 * カード一覧アダプター
 */
public class CardListAdapter extends BaseAdapter {
	private Activity mActivity;
	private List<CardEntity> mItems;

	/**
	 * コンストラクタ.
	 * 
	 * @param activity
	 * @param items
	 */
	public CardListAdapter(Activity activity, List<CardEntity> items) {
		super();
		mActivity = activity;
		mItems = items;
	}

	/**
	 * アイテムのセット.
	 * 
	 * @param items
	 */
	public void setItems(List<CardEntity> items) {
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
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		ViewWrapper wrapper = null;

		if (view == null) {
			view = mActivity.getLayoutInflater().inflate(R.layout.row_card_list, null);
			wrapper = new ViewWrapper(view);
			view.setTag(wrapper);
		} else {
			wrapper = (ViewWrapper) view.getTag();
		}

		CardEntity item = mItems.get(position);
		MyImage.setImage(wrapper.image, item);
		wrapper.label.setText(item.affirmationText);

		return view;
	}

	/**
	 * ビューを扱いやすくするためのラッパー.
	 */
	private class ViewWrapper {
		public final ImageView image;
		public final TextView label;

		ViewWrapper(View view) {
			this.image = (ImageView) view.findViewById(R.id.item_row_img);
			this.label = (TextView) view.findViewById(R.id.item_row_txt);
		}
	}
}