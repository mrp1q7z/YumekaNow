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

import java.io.FileNotFoundException;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import com.yojiokisoft.yumekanow.App;

/**
 * リソース関連のユーティリティ
 */
public class MyResource {
	/**
	 * リソース名からリソースIDを得る.
	 * 
	 * @param name リソース名
	 * @return リソースID
	 */
	public static int getResourceIdByName(String name) {
		App app = App.getInstance();
		String packageName = app.getPackageName();
		return app.getResources().getIdentifier(name, "drawable", packageName);
	}

	/**
	 * @return パッケージ情報
	 */
	public static PackageInfo getPackageInfo() {
		App app = App.getInstance();
		PackageInfo packageInfo = null;
		try {
			packageInfo = app.getPackageManager()
					.getPackageInfo(app.getPackageName(), PackageManager.GET_META_DATA);
		} catch (NameNotFoundException e) {
			try {
				MyLog.writeStackTrace(MyConst.BUG_CAUGHT_FILE, e);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
		}
		return packageInfo;
	}

	/**
	 * dpiをpxに変換
	 * 
	 * @param dpi
	 * @return px
	 */
	public static int dpi2Px(int dpi) {
		float density = App.getInstance().getResources().getDisplayMetrics().density;
		int px = (int) (dpi * density + 0.5f);
		return px;
	}
}
