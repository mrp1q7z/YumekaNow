package com.yojiokisoft.yumekanow.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.Media;
import android.util.DisplayMetrics;
import android.util.Pair;

public class MyImage {
	public static void saveImage(File path, Bitmap bmp) throws Exception {
		FileOutputStream out = null;

		// 上の階層(アプリ名のディレクトリ)が存在しなかったら作成
		if (!path.getParentFile().exists()) {
			path.getParentFile().mkdir();
		}

		try {
			out = new FileOutputStream(path);
			bmp.compress(CompressFormat.JPEG, 100, out);
			out.flush();
		} catch (Exception e) {
			throw e;
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}
	}

	public static void addGarally(Activity activity, File path) throws Exception {
		try {
			ContentValues values = new ContentValues();
			ContentResolver contentResolver = activity.getContentResolver();
			values.put(Images.Media.MIME_TYPE, "image/jpeg");
			values.put(Images.Media.DATE_MODIFIED, System.currentTimeMillis() / 1000);
			values.put(Images.Media.SIZE, path.length());
			values.put(Images.Media.TITLE, path.getName());
			values.put(Images.Media.DATA, path.getPath());
			contentResolver.insert(Media.EXTERNAL_CONTENT_URI, values);
		} catch (Exception e) {
			throw e;
		}
	}

	public static Pair<Integer, Integer> getScreenWidthAndHeight(Activity activity) {
		// 画面サイズを取得する
		DisplayMetrics metrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int screenWidth = (Integer) metrics.widthPixels;
		int screenHeight = (Integer) metrics.heightPixels;
		Pair<Integer, Integer> size = new Pair<Integer, Integer>(screenWidth, screenHeight);
		return size;
	}
}
