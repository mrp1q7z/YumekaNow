package com.yojiokisoft.yumekanow.utils;

import android.content.Context;
import android.os.Environment;

import com.yojiokisoft.yumekanow.R;
import com.yojiokisoft.yumekanow.entity.EncouragementMsgEntity;

public class MyConst {
	// 表示間隔
	public static final String DISP_INTERVAL = "DispInterval";

	// 目標回数
	public static final String GOAL_CNT = "GoalCnt";

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

	// 次回起動時間
	public static final String NEXT_START_TIME = "NextStartTime";

	// 目覚まし起動時間
	public static final String WAKE_UP_TIME = "WakeUpTime";

	// アプリケーションデータの保存パス
	public static final String APP_DATA_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/YumekaNow/";

	// バグファイル名(キャッチした)
	public static final String BUG_CAUGHT_FILE = "bug_caught.txt";

	// バグファイル名(キャッチされなかった)
	public static final String BUG_UNCAUGHT_FILE = "bug_uncaught.txt";

	// キャッチしたバグファイルのフルパス
	public static String getCaughtBugFilePath() {
		return APP_DATA_PATH + BUG_CAUGHT_FILE;
	}

	// キャッチされなかったバグファイルのフルパス
	public static String getUncaughtBugFilePath() {
		return APP_DATA_PATH + BUG_UNCAUGHT_FILE;
	}

	// SQLiteのDB名
	public static final String DATABASE_FILE = "yumekanow.db";

	// SQLiteのDB名のフルパス
	public static String getDatabasePath() {
		return APP_DATA_PATH + DATABASE_FILE;
	}

	// 励ましメッセージ [パーセント, n日目, メッセージID]
	private static final int[][] encouragementMsg = {
			{ 0, 1, R.string.ec_start },
			{ 0, 3, R.string.ec_konochousi },
			{ 5, 0, R.string.ec_yaruzo },
			{ 10, 0, R.string.ec_iizo },
			{ 20, 0, R.string.ec_ganbatte },
			{ 30, 0, R.string.ec_syosikantetu },
			{ 40, 0, R.string.ec_dekiruyo },
			{ 50, 0, R.string.ec_daijoubu },
			{ 60, 0, R.string.ec_akirameruna },
			{ 70, 0, R.string.ec_kanauyo },
			{ 80, 0, R.string.ec_tudukeyou },
			{ 90, 0, R.string.ec_atosukosi },
			{ 100, 0, R.string.ec_yatta } };

	public static EncouragementMsgEntity[] getEncouragementMsg(Context context) {
		int len = encouragementMsg.length;
		EncouragementMsgEntity[] msg = new EncouragementMsgEntity[len];
		for (int i = 0; i < len; i++) {
			msg[i] = new EncouragementMsgEntity();
			msg[i].percent = encouragementMsg[i][0];
			msg[i].day = encouragementMsg[i][1];
			msg[i].message = context.getString(encouragementMsg[i][2]);
		}
		return msg;
	}
}
