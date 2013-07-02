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

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
import com.googlecode.androidannotations.annotations.OptionsItem;
import com.googlecode.androidannotations.annotations.OptionsMenu;
import com.googlecode.androidannotations.annotations.ViewById;
import com.yojiokisoft.yumekanow.R;
import com.yojiokisoft.yumekanow.db.DatabaseHelper;
import com.yojiokisoft.yumekanow.db.SettingDao;
import com.yojiokisoft.yumekanow.exception.MyUncaughtExceptionHandler;
import com.yojiokisoft.yumekanow.fragment.CardFragment_;
import com.yojiokisoft.yumekanow.fragment.SleepFragment_;
import com.yojiokisoft.yumekanow.fragment.StateFragment_;
import com.yojiokisoft.yumekanow.utils.MyConst;
import com.yojiokisoft.yumekanow.utils.MyLog;
import com.yojiokisoft.yumekanow.utils.MyAlarmManager;

@EActivity(R.layout.activity_main)
@OptionsMenu(R.menu.activity_main)
public class MainActivity extends FragmentActivity {
	private Vibrator mVibrator = null;
	private Ringtone mRingtone = null;

	@ViewById(android.R.id.tabhost)
	TabHost mTabHost;

	@ViewById(R.id.pager)
	ViewPager mPager;

	@Extra(MyConst.EN_FIRE_EVENT)
	String mFireEvent;

	@AfterViews
	public void initActivity() {
		//キャッチされない例外により、スレッドが突然終了したときや、  
		//このスレッドに対してほかにハンドラが定義されていないときに  
		//呼び出されるデフォルトのハンドラを設定します。  
		Thread.setDefaultUncaughtExceptionHandler(new MyUncaughtExceptionHandler());

		DatabaseHelper.getInstance();
		SettingDao settingDao = SettingDao.getInstance();

		if (MyAlarmManager.getStartTime() == 0) {
			MyAlarmManager.setStartTimer(this);
		}

		FragmentManager manager = getSupportFragmentManager();
		mTabHost.setup();
		final MyPagerAdapter adapter = new MyPagerAdapter(manager, this, mTabHost, mPager);

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

	@Override
	protected void onStart() {
		super.onStart();
		MyUncaughtExceptionHandler.sendBugReport(this);
MyLog.d("test");
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

	private static class MyPagerAdapter extends FragmentPagerAdapter
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
		Intent myIntent = new Intent(getApplicationContext(), MakeCardActivity_.class);
		myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(myIntent);
	}

	@OptionsItem(R.id.select_card)
	void onMenuSelectCard() {
		Intent myIntent = new Intent(getApplicationContext(), CardListActivity_.class);
		myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(myIntent);
	}

	@OptionsItem(R.id.settings)
	void onMenuSettings() {
		Intent myIntent = new Intent(getApplicationContext(), MyPreference_.class);
		myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(myIntent);
	}

	@OptionsItem(R.id.usage)
	void onMenuUsage() {
		Intent myIntent = new Intent(getApplicationContext(), UsageActivity_.class);
		myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(myIntent);
	}

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

	public void closeActivity() {
		finish();
	}
}