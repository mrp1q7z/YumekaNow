package com.yojiokisoft.yumekanow.db;

import java.sql.SQLException;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.yojiokisoft.yumekanow.entity.CardEntity;
import com.yojiokisoft.yumekanow.entity.CounterEntity;
import com.yojiokisoft.yumekanow.exception.MyUncaughtExceptionHandler;
import com.yojiokisoft.yumekanow.utils.MyConst;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
	private static final int DATABASE_VERSION = 1;
	private static DatabaseHelper mInstance = null;

	private DatabaseHelper(Context context) {
		super(context, MyConst.getDatabasePath(), null, DATABASE_VERSION);
	}

	public static DatabaseHelper getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new DatabaseHelper(context);
			createTable(context, mInstance.getConnectionSource());
		}
		return mInstance;
	}

	public static void createTable(Context context, ConnectionSource conn) {
		try {
			TableUtils.createTableIfNotExists(conn, CardEntity.class);
			TableUtils.createTableIfNotExists(conn, CounterEntity.class);
		} catch (SQLException e) {
			MyUncaughtExceptionHandler.sendBugReport((Activity) context, e);
		}
	}

	@Override
	public void onCreate(SQLiteDatabase arg0, ConnectionSource arg1) {
		;
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource conn, int oldVersion, int newVersion) {
		;
	}
}
