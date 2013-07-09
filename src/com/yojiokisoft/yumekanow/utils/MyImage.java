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

package com.yojiokisoft.yumekanow.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.widget.ImageView;

import com.yojiokisoft.yumekanow.entity.BackImageEntity;
import com.yojiokisoft.yumekanow.entity.CardEntity;

/**
 * イメージに関するユーティリティ集
 */
public class MyImage {
	/**
	 * ビットマップをファイルに保存する.
	 * 
	 * @param file ファイル
	 * @param bitmap ビットマップ
	 * @throws IOException
	 */
	public static void saveImage(File file, Bitmap bitmap) throws IOException {
		FileOutputStream out = null;

		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdir();
		}

		try {
			out = new FileOutputStream(file);
			bitmap.compress(CompressFormat.JPEG, 100, out);
			out.flush();
			out.close();
		} finally {
			MyFile.closeQuietly(out);
		}
	}

	/**
	 * 画面の幅と高さを取得する.
	 * 
	 * @param activity
	 * @return 画面の幅(=first)と高さ(=second)
	 */
	public static Pair<Integer, Integer> getScreenWidthAndHeight(Activity activity) {
		// 画面サイズを取得する
		DisplayMetrics metrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int screenWidth = (Integer) metrics.widthPixels;
		int screenHeight = (Integer) metrics.heightPixels;
		Pair<Integer, Integer> size = new Pair<Integer, Integer>(screenWidth, screenHeight);
		return size;
	}

	/**
	 * イメージビューに背景画像のセットする.
	 * 
	 * @param imageView
	 * @param card
	 */
	public static void setImage(ImageView imageView, CardEntity card) {
		if (card.backImageType == BackImageEntity.IT_BITMAP) {
			imageView.setImageURI(Uri.parse("file:///" + card.backImagePath));
		} else {
			int resId = MyResource.getResourceIdByName(card.backImageResourceName);
			imageView.setImageResource(resId);
		}
	}

	/**
	 * イメージビューに背景画像のセットする.
	 * 
	 * @param imageView
	 * @param card
	 */
	public static void setImage(ImageView imageView, BackImageEntity backImage) {
		if (backImage.type == BackImageEntity.IT_BITMAP) {
			imageView.setImageURI(Uri.parse("file:///" + backImage.bitmapPath));
		} else {
			int resId = MyResource.getResourceIdByName(backImage.resourceName);
			imageView.setImageResource(resId);
		}
	}
}
