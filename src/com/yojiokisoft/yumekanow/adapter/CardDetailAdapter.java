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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.yojiokisoft.yumekanow.R;
import com.yojiokisoft.yumekanow.db.CardDao;
import com.yojiokisoft.yumekanow.entity.CardEntity;
import com.yojiokisoft.yumekanow.exception.MyUncaughtExceptionHandler;

/**
 * カード詳細アダプター
 */
public class CardDetailAdapter extends PagerAdapter {
	private TextView mTextView;
	private ImageView mImageView;
	private CardDao mCardDao = null;
	private Context mContext;
	private ArrayList<Integer> mList;
	private LayoutInflater mInflter;

	/**
	 * コンストラクタ.
	 * 
	 * @param context
	 */
	public CardDetailAdapter(Context context) {
		mContext = context;
		mInflter = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mList = new ArrayList<Integer>();

		try {
			mCardDao = new CardDao();
		} catch (SQLException e) {
			MyUncaughtExceptionHandler.sendBugReport((Activity) mContext, e);
		}
	}

	/**
	 * リストにアイテムを追加する.
	 * 
	 * @param item アイテム
	 */
	public void add(Integer item) {
		mList.add(item);
	}

	/**
	 * @see PagerAdapter#instantiateItem(ViewGroup, int)
	 */
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		FrameLayout layout = (FrameLayout) this.mInflter.inflate(R.layout.card_detail, null);

		mTextView = (TextView) layout.findViewById(R.id.affirmationText);
		mImageView = (ImageView) layout.findViewById(R.id.backImage);

		// リストから取得
		if (mCardDao != null) {
			Integer cardId = mList.get(position);
			try {
				List<CardEntity> list = mCardDao.queryForEq("id", cardId);
				printCard(list.get(0));
			} catch (SQLException e) {
				MyUncaughtExceptionHandler.sendBugReport((Activity) mContext, e);
			}
		}

		// コンテナに追加
		container.addView(layout);

		return layout;
	}

	/**
	 * @see PagerAdapter#destroyItem(ViewGroup, int, Object)
	 */
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// コンテナから View を削除
		container.removeView((View) object);
	}

	/**
	 * @see PagerAdapter#getCount()
	 */
	@Override
	public int getCount() {
		// リストのアイテム数を返す
		return mList.size();
	}

	/**
	 * @see PagerAdapter#isViewFromObject(View, Object)
	 */
	@Override
	public boolean isViewFromObject(View view, Object object) {
		// Object 内に View が存在するか判定する
		return view == (FrameLayout) object;
	}

	/**
	 * カードの表示.
	 * 
	 * @param card
	 */
	private void printCard(CardEntity card) {
		mTextView.setText(card.affirmationText);
		mTextView.setTextColor(card.textColor);
		mTextView.setShadowLayer(1.5f, 1.5f, 1.5f, card.shadowColor);
		mTextView.setTextSize(card.textSize);
		MarginLayoutParams params = (MarginLayoutParams) mTextView.getLayoutParams();
		params.leftMargin = card.marginLeft;
		params.topMargin = card.marginTop;
		mTextView.setLayoutParams(params);

		if (card.backImageResourceId == 0) {
			mImageView.setImageURI(Uri.parse("file:///" + card.backImagePath));
		} else {
			mImageView.setImageResource(card.backImageResourceId);
		}
	}

	/**
	 * カードの取得.
	 * 
	 * @param position
	 * @return
	 */
	public CardEntity getCard(int position) {
		if (mCardDao != null) {
			int cardId = mList.get(position);
			try {
				List<CardEntity> list = mCardDao.queryForEq("id", cardId);
				return list.get(0);
			} catch (SQLException e) {
				MyUncaughtExceptionHandler.sendBugReport((Activity) mContext, e);
			}
		}
		return null;
	}
}