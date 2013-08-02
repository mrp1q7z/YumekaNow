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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.widget.ImageView;

import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EBean;
import com.yojiokisoft.yumekanow.App;
import com.yojiokisoft.yumekanow.entity.BackImageEntity;
import com.yojiokisoft.yumekanow.entity.CardEntity;

/**
 * イメージに関するユーティリティ集
 */
@EBean
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
			imageView.setImageDrawable(new BitmapDrawable(card.backImagePath));
		} else {
			int resId = MyResource.getResourceIdByName(card.backImageResourceName);
			imageView.setImageResource(resId);
		}
	}

	/**
	 * イメージビューにサムネイル背景画像のセットする.
	 * 
	 * @param imageView
	 * @param card
	 */
	public static void setImageThum(ImageView imageView, CardEntity card) {
		if (card.backImageType == BackImageEntity.IT_BITMAP) {
			String fileName = getThumFileName(card.backImagePath);
			BitmapDrawable drawable = new BitmapDrawable(fileName);
			if (drawable.getBitmap() == null) {
				drawable = new BitmapDrawable(card.backImagePath);
				File file = new File(fileName);
				if (!file.exists()) {
					MyImage_ myImage = MyImage_.getInstance_(App.getInstance().getAppContext());
					myImage.ReductionImage(card.backImagePath);
				}
			}
			imageView.setImageDrawable(drawable);
		} else {
			String resName = getThumFileName(card.backImageResourceName);
			int resId = MyResource.getResourceIdByName(resName);
			imageView.setImageResource(resId);
		}
	}

	/**
	 * サムネイルファイル名の取得
	 * @param fileName
	 * @return
	 */
	public static String getThumFileName(String fileName) {
		String thumFileName;
		File file = new File(fileName);
		String path = file.getParent();
		if (path == null) {
			thumFileName = "s_" + fileName;
		} else {
			thumFileName = path + "/s_" + file.getName();
		}
		return thumFileName;
	}

	/**
	 * イメージビューに背景画像のセットする.
	 * 
	 * @param imageView
	 * @param card
	 */
	public static void setImage(ImageView imageView, BackImageEntity backImage) {
		if (backImage.type == BackImageEntity.IT_BITMAP) {
			imageView.setImageDrawable(new BitmapDrawable(backImage.bitmapPath));
		} else {
			int resId = MyResource.getResourceIdByName(backImage.resourceName);
			imageView.setImageResource(resId);
		}
	}

	/**
	 * 表示サイズから縮小率を求める.
	 * 
	 * @param options 画像のサイズ
	 * @param reqWidth 表示サイズ（幅）
	 * @param reqHeight 表示サイズ（高さ）
	 * @return 縮小サイズ
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			int inSampleSize1 = (int) Math.floor((float) height / (float) reqHeight);
			int inSampleSize2 = (int) Math.floor((float) width / (float) reqWidth);
			inSampleSize = Math.max(inSampleSize1, inSampleSize2);
		}
		return inSampleSize;
	}

	/**
	 * 画像の縮小をバックグラウンドで行う
	 * @param fileName
	 */
	@Background
	public void ReductionImage(String fileName) {
		File file = new File(fileName);
		InputStream inputStream = null;

		// 画像サイズ情報を取得する
		BitmapFactory.Options imageOptions = new BitmapFactory.Options();
		try {
			inputStream = new FileInputStream(file);

			imageOptions.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(inputStream, null, imageOptions);

			if (inputStream != null) {
				inputStream.close();
			}
		} catch (IOException e) {
			try {
				MyLog.writeStackTrace(MyConst.BUG_CAUGHT_FILE, e);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
		} finally {
			MyFile.closeQuietly(inputStream);
		}

		// 画像を縮小する
		Bitmap bitmap = null;
		inputStream = null;
		try {
			inputStream = new FileInputStream(file);
			int imageSizeMax = 158;
			float imageScaleWidth = (float) imageOptions.outWidth / imageSizeMax;

			// もしも、縮小できるサイズならば、縮小して読み込む
			if (imageScaleWidth > 2) {
				// 縮小するスケールを合わせる
				int imageScale = (int) Math.floor(imageScaleWidth);
				imageOptions.inSampleSize = imageScale;
				imageOptions.inJustDecodeBounds = false;

				bitmap = BitmapFactory.decodeStream(inputStream, null, imageOptions);
			}

			if (inputStream != null) {
				inputStream.close();
			}
		} catch (IOException e) {
			try {
				MyLog.writeStackTrace(MyConst.BUG_CAUGHT_FILE, e);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
		} finally {
			MyFile.closeQuietly(inputStream);
		}

		// 縮小した画像を保存する
		if (bitmap == null) {
			return;
		}
		File thumFile = new File(getThumFileName(fileName));
		try {
			saveImage(thumFile, bitmap);
		} catch (IOException e) {
			try {
				MyLog.writeStackTrace(MyConst.BUG_CAUGHT_FILE, e);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
		}
	}
}
