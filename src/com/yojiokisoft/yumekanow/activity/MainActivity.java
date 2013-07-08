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

package com.yojiokisoft.yumekanow.activity;

import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
import com.googlecode.androidannotations.annotations.OptionsItem;
import com.googlecode.androidannotations.annotations.OptionsMenu;
import com.googlecode.androidannotations.annotations.ViewById;
import com.yojiokisoft.yumekanow.R;
import com.yojiokisoft.yumekanow.adapter.MainPagerAdapter;
import com.yojiokisoft.yumekanow.db.SettingDao;
import com.yojiokisoft.yumekanow.exception.MyUncaughtExceptionHandler;
import com.yojiokisoft.yumekanow.utils.MyAlarmManager;
import com.yojiokisoft.yumekanow.utils.MyConst;

/**
 * メインアクティビティ
 */
@EActivity(R.layout.activity_main)
@OptionsMenu(R.menu.activity_main)
public class MainActivity extends FragmentActivity {
	@ViewById(android.R.id.tabhost)
	/*package*/TabHost mTabHost;

	@ViewById(R.id.pager)
	/*package*/ViewPager mPager;

	@Extra(MyConst.EN_FIRE_EVENT)
	/*package*/String mFireEvent;

	private Vibrator mVibrator = null;
	private Ringtone mRingtone = null;

	/**
	 * アクティビティの初期化 (onCreateと同等のタイミングで呼ばれる）
	 */
	@AfterViews
	/*package*/void initActivity() {
		// キャッチされない例外をキャッチするデフォルトのハンドラを設定する
		Thread.setDefaultUncaughtExceptionHandler(new MyUncaughtExceptionHandler());

		SettingDao settingDao = SettingDao.getInstance();

		if (MyAlarmManager.getStartTime() == 0 && MyAlarmManager.getWakeUpTime() == 0) {
			MyAlarmManager.setStartTimer(this);
		}

		FragmentManager manager = getSupportFragmentManager();
		mTabHost.setup();
		final MainPagerAdapter adapter = new MainPagerAdapter(manager, this, mTabHost, mPager);

		// tab size from screen size
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int tabWidth = metrics.widthPixels / 3;
		int tabHeight = metrics.widthPixels / 7;

		String tabTitle[] = new String[3];
		tabTitle[0] = getString(R.string.tab_title1);
		tabTitle[1] = getString(R.string.tab_title2);
		tabTitle[2] = getString(R.string.tab_title3);
		for (int i = 0; i < tabTitle.length; i++) {
			TabSpec spec = mTabHost.newTabSpec("tab" + i);
			spec.setIndicator(getTabView(tabWidth, tabHeight, tabTitle[i]));
			adapter.addTab(spec, "tab" + i);
		}

		if ("Timer".equals(mFireEvent)) {
			if (settingDao.getVibrator()) {
				mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
				long[] pattern = { 0, 500, 250, 500, 250, 500 }; // OFF/ON/OFF/ON...
				mVibrator.vibrate(pattern, -1);
			}

			String url = settingDao.getAlarmUrl();
			if (url != null) {
				mRingtone = RingtoneManager.getRingtone(this, Uri.parse(url));
				mRingtone.play();
			}
		}
	}

	/**
	 * 開始処理
	 */
	@Override
	protected void onStart() {
		super.onStart();
		MyUncaughtExceptionHandler.sendBugReport(this);
	}

	/**
	 * 終了処理
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		stopVibrator();
	}

	/**
	 * 指定の幅・高さ・タイトルをもったテキストビューを生成し返却する.
	 * 
	 * @param width
	 * @param height
	 * @param title
	 * @return テキストビュー
	 */
	private TextView getTabView(int width, int height, String title) {
		TextView view = new TextView(this);
		view.setMinimumWidth(width);
		view.setMinimumHeight(height);
		view.setGravity(Gravity.CENTER);
		view.setBackgroundResource(R.drawable.tab_indicator_holo);
		view.setText(title);
		return view;
	}

	/**
	 * カードを作るメニューが選択された.
	 */
	@OptionsItem(R.id.make_card)
	/*package*/void onMenuMakeCard() {
		Intent myIntent = new Intent(getApplicationContext(), MakeCardActivity_.class);
		myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(myIntent);
	}

	/**
	 * カードを選ぶメニューが選択された.
	 */
	@OptionsItem(R.id.select_card)
	/*package*/void onMenuSelectCard() {
		Intent myIntent = new Intent(getApplicationContext(), CardListActivity_.class);
		myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(myIntent);
	}

	/**
	 * 設定メニューが選択された.
	 */
	@OptionsItem(R.id.settings)
	/*package*/void onMenuSettings() {
		Intent myIntent = new Intent(getApplicationContext(), MyPreference_.class);
		myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(myIntent);
	}

	/**
	 * 使い方メニューが選択された.
	 */
	@OptionsItem(R.id.usage)
	/*package*/void onMenuUsage() {
		Intent myIntent = new Intent(getApplicationContext(), UsageActivity_.class);
		myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(myIntent);
	}

	/**
	 * バイブレーターと着信音の停止
	 */
	public void stopVibrator() {
		if (mVibrator != null) {
			mVibrator.cancel();
			mVibrator = null;
		}
		if (mRingtone != null) {
			mRingtone.stop();
			mRingtone = null;
		}
	}

	/**
	 * アクティビティを閉じる
	 */
	public void closeActivity() {
		finish();
	}
}