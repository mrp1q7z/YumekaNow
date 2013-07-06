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

package com.yojiokisoft.yumekanow.db;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.yojiokisoft.yumekanow.App;
import com.yojiokisoft.yumekanow.entity.CardEntity;
import com.yojiokisoft.yumekanow.entity.CounterEntity;
import com.yojiokisoft.yumekanow.exception.MyUncaughtExceptionHandler;
import com.yojiokisoft.yumekanow.utils.MyConst;
import com.yojiokisoft.yumekanow.utils.MyFile;

/**
 * DBヘルパー
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
	private static final int DATABASE_VERSION = 1;
	private static DatabaseHelper mInstance = null;

	/**
	 * コンストラクタは公開しない
	 * インスタンスを取得するときは、getInstanceを使用する.
	 * 
	 * @param context
	 */
	private DatabaseHelper(Context context) {
		super(context, MyConst.getDatabasePath(), null, DATABASE_VERSION);
	}

	/**
	 * インスタンスの取得.
	 * 
	 * @return DatabaseHelper
	 */
	public static DatabaseHelper getInstance() {
		if (mInstance == null) {
			createPreInstallDatabaseIfNotExists();
			mInstance = new DatabaseHelper(App.getInstance().getAppContext());
		}
		return mInstance;
	}

	/**
	 * テーブルが存在しなければ、テーブルを作成する.
	 * 
	 * @param context
	 * @param conn
	 */
	public static void createTable(Context context, ConnectionSource conn) {
		try {
			TableUtils.createTableIfNotExists(conn, CardEntity.class);
			TableUtils.createTableIfNotExists(conn, CounterEntity.class);
		} catch (SQLException e) {
			MyUncaughtExceptionHandler.sendBugReport((Activity) context, e);
		}
	}

	/** 
	 * DB がなければプリインストールの DB を asset よりコピーして作成する
	 **/
	public static void createPreInstallDatabaseIfNotExists() {
		File file = new File(MyConst.getDatabasePath());
		if (file.exists()) {
			return;
		}

		try {
			if (!file.getParentFile().exists()) {
				if (!file.getParentFile().mkdirs()) {
					throw new IOException("mkdirs error : " + file.getPath());
				}
			}
			if (!file.createNewFile()) {
				throw new IOException("createNewFile error : " + file.getPath());
			}
			copyDatabaseFromAsset();
		} catch (IOException e) {
			MyUncaughtExceptionHandler.sendBugReport((Activity) App.getInstance().getAppContext(), e);
		}
	}

	/** 
	 * asset に格納した DB をデフォルトの DB パスにコピーする 
	 * */
	private static void copyDatabaseFromAsset() {
		InputStream in = null;
		OutputStream out = null;
		try {
			in = App.getInstance().getAppContext().getAssets().open(MyConst.DATABASE_FILE);
			out = new FileOutputStream(MyConst.getDatabasePath());

			byte[] buffer = new byte[1024];
			int size;
			while ((size = in.read(buffer)) > 0) {
				out.write(buffer, 0, size);
			}

			out.flush();
			out.close();
			in.close();
		} catch (IOException e) {
			MyUncaughtExceptionHandler.sendBugReport((Activity) App.getInstance().getAppContext(), e);
		} finally {
			MyFile.closeQuietly(out);
			MyFile.closeQuietly(in);
		}
	}

	/**
	 * @see DatabaseHelper#onCreate(SQLiteDatabase, ConnectionSource)
	 */
	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource sonn) {
		;
	}

	/**
	 * @see DatabaseHelper#onUpgrade(SQLiteDatabase, ConnectionSource, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource conn, int oldVersion, int newVersion) {
		;
	}
}
