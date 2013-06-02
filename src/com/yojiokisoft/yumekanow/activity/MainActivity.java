package com.yojiokisoft.yumekanow.activity;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.OptionsItem;
import com.googlecode.androidannotations.annotations.OptionsMenu;
import com.googlecode.androidannotations.annotations.ViewById;
import com.yojiokisoft.yumekanow.R;
import com.yojiokisoft.yumekanow.exception.MyUncaughtExceptionHandler;
import com.yojiokisoft.yumekanow.fragment.CardFragment_;
import com.yojiokisoft.yumekanow.fragment.SleepFragment_;
import com.yojiokisoft.yumekanow.fragment.StateFragment_;
import com.yojiokisoft.yumekanow.model.SettingDao;

@EActivity(R.layout.activity_main)
@OptionsMenu(R.menu.activity_main)
public class MainActivity extends FragmentActivity {
	private Vibrator mVibrator = null;
	private Ringtone mRingtone = null;

	@ViewById(android.R.id.tabhost)
	TabHost mTabHost;

	@ViewById(R.id.pager)
	ViewPager mPager;

	@AfterViews
	public void initActivity() {
		FragmentManager manager = getSupportFragmentManager();
		mTabHost.setup();
		final MyPagerAdapter adapter = new MyPagerAdapter(manager, this, mTabHost, mPager);

		// tab size from screen size
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int tabWidth = metrics.widthPixels / 3;
		int tabHeight = metrics.widthPixels / 7;

		String tabTitle[] = { "カード", "情報", "スリープ" };
		for (int i = 0; i < 3; i++) {
			TabSpec spec = mTabHost.newTabSpec("tab" + i);
			spec.setIndicator(getTabView(tabWidth, tabHeight, tabTitle[i]));
			adapter.addTab(spec, "tab" + i);
		}

		SettingDao settingDao = SettingDao.getInstance(this);
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

	@Override
	protected void onStart() {
		super.onStart();
		MyUncaughtExceptionHandler.sendBugReport(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		stopVibrator();
	}

	private TextView getTabView(int width, int height, String title) {
		TextView view = new TextView(this);
		view.setMinimumWidth(width);
		view.setMinimumHeight(height);
		view.setGravity(Gravity.CENTER);
		view.setBackgroundResource(R.drawable.tab_indicator_holo);
		view.setText(title);
		return view;
	}

	private class MyPagerAdapter extends FragmentPagerAdapter
			implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {

		private final Context context;
		private final TabHost tabHost;
		private final ViewPager pager;
		private final ArrayList<String> tabs = new ArrayList<String>();

		// dummy contents class
		class DummyTabFactory implements TabHost.TabContentFactory {
			private final Context context;

			public DummyTabFactory(Context context) {
				this.context = context;
			}

			public View createTabContent(String tag) {
				View v = new View(context);
				v.setMinimumWidth(0);
				v.setMinimumHeight(0);
				return v;
			}
		}

		public MyPagerAdapter(FragmentManager fm, Context context, TabHost tabHost, ViewPager pager) {
			super(fm);
			this.context = context;
			this.tabHost = tabHost;
			this.pager = pager;
			this.tabHost.setOnTabChangedListener(this);
			this.pager.setAdapter(this);
			this.pager.setOnPageChangeListener(this);
		}

		public void addTab(TabHost.TabSpec tabSpec, String content) {
			tabSpec.setContent(new DummyTabFactory(this.context));
			tabs.add(content);
			tabHost.addTab(tabSpec);
			notifyDataSetChanged();
		}

		@Override
		public Fragment getItem(int position) {
			if (position == 0) {
				return new CardFragment_();
			} else if (position == 1) {
				return new StateFragment_();
			} else {
				return new SleepFragment_();
			}
		}

		@Override
		public int getCount() {
			return 3;
		}

		public void onPageScrollStateChanged(int state) {
			;
		}

		public void onPageScrolled(int position, float positionOffset, int positionOggsetPixesl) {
			;
		}

		public void onPageSelected(int position) {
			TabWidget widget = tabHost.getTabWidget();

			int oldFocus = widget.getDescendantFocusability();
			widget.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);

			tabHost.setCurrentTab(position);

			widget.setDescendantFocusability(oldFocus);
		}

		public void onTabChanged(String tabId) {
			int position = tabHost.getCurrentTab();

			pager.setCurrentItem(position);
		}
	}

	@OptionsItem(R.id.make_card)
	void onMenuMakeCard() {
		Toast toast = Toast.makeText(this, "カードを作るが選択されました", Toast.LENGTH_LONG);
		toast.show();
		Intent myIntent = new Intent(getApplicationContext(), MakeCardActivity_.class);
		myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(myIntent);
	}

	@OptionsItem(R.id.select_card)
	void onMenuSelectCard() {
		Toast toast = Toast.makeText(this, "カードを選ぶが選択されました", Toast.LENGTH_LONG);
		toast.show();
		Intent myIntent = new Intent(getApplicationContext(), CardListActivity_.class);
		myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(myIntent);
	}

	@OptionsItem(R.id.settings)
	void onMenuSettings() {
		Toast toast = Toast.makeText(this, "設定が選択されました", Toast.LENGTH_LONG);
		toast.show();
		Intent myIntent = new Intent(getApplicationContext(), MyPreference_.class);
		myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(myIntent);
	}

	@OptionsItem(R.id.usage)
	void onMenuUsage() {
		Toast toast = Toast.makeText(this, "使い方が選択されました", Toast.LENGTH_LONG);
		toast.show();
		Intent myIntent = new Intent(getApplicationContext(), UsageActivity_.class);
		myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(myIntent);
	}

	public void stopVibrator() {
		if (mVibrator != null) {
			mVibrator.cancel();
		}
		if (mRingtone != null) {
			mRingtone.stop();
		}
	}

	public void closeActivity() {
		finish();
	}
}