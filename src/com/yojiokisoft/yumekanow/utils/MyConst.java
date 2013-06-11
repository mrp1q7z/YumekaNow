package com.yojiokisoft.yumekanow.utils;

import android.os.Environment;

public class MyConst {
	// 表示間隔
	public static final String DISP_INTERVAL = "DispInterval";

	// 目標回数
	public static final String GOAL_CNT = "GoalCnt";

	// アニメーション
	public static final String ANIMATION = "Animation";

	// バイブレータ
	public static final String VIBRATOR = "Vibrator";

	// アラーム音
	public static final String ALARM = "Alarm";

	// 目覚まし音
	public static final String SLEEP_ALARM = "SleepAlarm";

	// バージョン
	public static final String VERSION = "Version";

	// お問い合わせ
	public static final String INQUIRY = "Inquiry";

	// スリープ時間（時間指定）
	public static final String SLEEP_JIKAN = "SleepJikan";
	
	// スリープ時間（タイマー指定）
	public static final String SLEEP_TIMER = "SleepTimer";
	
	// カード情報
	public static final String CARD = "Card";
	
	// 起動イベント
	public static final String FIRE_EVENT = "FireEvent";

	// SDカードのパス
	public static final String EX_STORAGE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();

	// アプリケーションデータの保存パス
	public static final String APP_DATA_PATH = "YumekaNow";
	
	// バグファイル名(キャッチした)
	public static final String BUG_CAUGHT_FILE = "bug_caught.txt";
	
	// バグファイル名(キャッチされなかった)
	public static final String BUG_UNCAUGHT_FILE = "bug_uncaught.txt";
	
	// キャッチしたバグファイルのフルパス
	public static String getCaughtBugFilePath() {
		return EX_STORAGE_PATH + "/" + APP_DATA_PATH + "/" + BUG_CAUGHT_FILE;
	}
	
	// キャッチされなかったバグファイルのフルパス
	public static String getUncaughtBugFilePath() {
		return EX_STORAGE_PATH + "/" + APP_DATA_PATH + "/" + BUG_UNCAUGHT_FILE;
	}
	
	// SQLiteのDB名
	public static final String DATABASE_NAME = "yumekanow.db";
	
	// SQLiteのDB名のフルパス
	public static String getDatabaseName() {
		return EX_STORAGE_PATH + "/" + APP_DATA_PATH + "/" + DATABASE_NAME;
	}
}
