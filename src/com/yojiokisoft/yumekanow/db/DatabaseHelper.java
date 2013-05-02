package com.yojiokisoft.yumekanow.db;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.yojiokisoft.yumekanow.entity.CardEntity;
import com.yojiokisoft.yumekanow.entity.CounterEntity;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
	private static final String DATABASE_NAME = Environment.getExternalStorageDirectory() + "/YumekaNow/yumekanow.db";
	private static final int DATABASE_VERSION = 1;

	private static DatabaseHelper mInstance = null;

	private DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public static DatabaseHelper getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new DatabaseHelper(context);
			createTable(mInstance.getConnectionSource());
		}
		return mInstance;
	}

	public static void createTable(ConnectionSource conn) {
		try {
			TableUtils.createTableIfNotExists(conn, CardEntity.class);
			TableUtils.createTableIfNotExists(conn, CounterEntity.class);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource conn) {
		Log.d("taoka", "DatabaseHelper#onCreate:bigin");
		createTable(conn);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource conn, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
