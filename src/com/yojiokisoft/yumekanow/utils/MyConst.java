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

import com.yojiokisoft.yumekanow.App;
import com.yojiokisoft.yumekanow.R;
import com.yojiokisoft.yumekanow.entity.EncouragementMsgEntity;

/**
 * 定数クラス.
 */
public class MyConst {
	/** アプリケーション名（英語） */
	public static final String APP_NAME = "YumekaNow";

	/** 設定キー：表示間隔 */
	public static final String PK_DISP_INTERVAL = "DispInterval";

	/** 設定キー：目標回数 */
	public static final String PK_GOAL_CNT = "GoalCnt";

	/** 設定キー：バイブレータ */
	public static final String PK_VIBRATOR = "Vibrator";

	/** 設定キー：アラーム音 */
	public static final String PK_ALARM = "Alarm";

	/** 設定キー：目覚まし音 */
	public static final String PK_SLEEP_ALARM = "SleepAlarm";

	/** 設定キー：バージョン */
	public static final String PK_VERSION = "Version";

	/** 設定キー：お問い合わせ */
	public static final String PK_INQUIRY = "Inquiry";

	/** 設定キー：寄付 */
	public static final String PK_DONATION = "Donation";

	/** 設定キー：スリープ時間（時間指定） */
	public static final String PK_SLEEP_JIKAN = "SleepJikan";

	/** 設定キー：スリープ時間（タイマー指定） */
	public static final String PK_SLEEP_TIMER = "SleepTimer";

	/** 設定キー：次回起動時間 */
	public static final String PK_NEXT_START_TIME = "NextStartTime";

	/** 設定キー：目覚まし起動時間 */
	public static final String PK_WAKE_UP_TIME = "WakeUpTime";

	/** 設定キー：DP変換済みフラグ */
	public static final String PK_CONV_DP_FLAG = "ConvDpFlag";

	/** ExtraName:カード情報 */
	public static final String EN_CARD = "Card";

	/** ExtraName:起動イベント */
	public static final String EN_FIRE_EVENT = "FireEvent";

	/** バグファイル名(キャッチした) */
	public static final String BUG_CAUGHT_FILE = "bug_caught.txt";

	/** バグファイル名(キャッチされなかった) */
	public static final String BUG_UNCAUGHT_FILE = "bug_uncaught.txt";

	/** キャッチしたバグファイルのフルパス */
	public static String getCaughtBugFilePath() {
		return MyFile.pathCombine(App.getInstance().getAppDataPath(), BUG_CAUGHT_FILE);
	}

	/** キャッチされなかったバグファイルのフルパス */
	public static String getUncaughtBugFilePath() {
		return MyFile.pathCombine(App.getInstance().getAppDataPath(), BUG_UNCAUGHT_FILE);
	}

	/** SQLiteのDB名 */
	public static final String DATABASE_FILE = "yumekanow.db";

	/** SQLiteのDB名のフルパス */
	public static String getDatabasePath() {
		return App.getInstance().getDatabasePath(DATABASE_FILE).toString();
	}

	/** 背景画像のパス */
	public static String getBackImagePath() {
		return App.getInstance().getAppDataPath();
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

	/**
	 * @return 励ましメッセージ
	 */
	public static EncouragementMsgEntity[] getEncouragementMsg() {
		int len = encouragementMsg.length;
		EncouragementMsgEntity[] msg = new EncouragementMsgEntity[len];
		for (int i = 0; i < len; i++) {
			msg[i] = new EncouragementMsgEntity();
			msg[i].percent = encouragementMsg[i][0];
			msg[i].day = encouragementMsg[i][1];
			msg[i].message = App.getInstance().getString(encouragementMsg[i][2]);
		}
		return msg;
	}
}
