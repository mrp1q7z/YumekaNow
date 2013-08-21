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
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yojiokisoft.yumekanow.entity.BackImageEntity;
import com.yojiokisoft.yumekanow.utils.MyImage;

/**
 * 背景画像のページャーアダプター
 */
public class BackImagePagerAdapter extends PagerAdapter {
	private ImageView mImageView;
	private List<BackImageEntity> mList;
	private Context mContext;

	/**
	 * コンストラクタ.
	 * 
	 * @param context
	 */
	public BackImagePagerAdapter(Context context, List<BackImageEntity> list) {
		mContext = context;
		mList = list;
	}

	/**
	 * @see PagerAdapter#instantiateItem(ViewGroup, int)
	 */
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		mImageView = new ImageView(mContext);
		mImageView.setPadding(0, 5, 0, 0);
		mImageView.setBackgroundColor(0xFF000000);
		mImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

		BackImageEntity item = mList.get(position);
		MyImage.setImage(mImageView, item);

		// コンテナに追加
		container.addView(mImageView);

		return mImageView;
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
		return view == (ImageView) object;
	}
	
	/**
	 * @see PagerAdapter#getItemPosition(Object)
	 */
	@Override
	public int getItemPosition(Object object) {
	    return POSITION_NONE;
	}

	/**
	 * 背景画像の取得.
	 * 
	 * @param position
	 * @return
	 */
	public BackImageEntity getBackImage(int position) {
		return mList.get(position);
	}

	/**
	 * アイテムのセット
	 * 
	 * @param list
	 */
	public void setItems(List<BackImageEntity> list) {
		if (mList != null) {
			mList = null;
		}
		mList = list;
	}
}