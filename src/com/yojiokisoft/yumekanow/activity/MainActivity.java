package com.yojiokisoft.yumekanow.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.yojiokisoft.yumekanow.R;
import com.yojiokisoft.yumekanow.entity.CounterEntity;
import com.yojiokisoft.yumekanow.fragment.CardFragment;
import com.yojiokisoft.yumekanow.fragment.SleepFragment;
import com.yojiokisoft.yumekanow.fragment.StateFragment;
import com.yojiokisoft.yumekanow.model.CounterDao;
import com.yojiokisoft.yumekanow.model.DummyGenerator;

public class MainActivity extends FragmentActivity implements CardFragment.OnCardClickListener {

	// メニューアイテム
	private static final int MENU_CREATE_CARD = 0; // カードを作る
	private static final int MENU_SELECT_CARD = 1; // カードを選ぶ
	private static final int MENU_SETTING = 2; // 設定
	private static final int MENU_USAGE = 3; // 使い方

	Ringtone mRingtone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		FragmentManager manager = getSupportFragmentManager();

		// tabhost
		TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
		tabHost.setup();

		// viewpager
		final ViewPager pager = (ViewPager) findViewById(R.id.pager);

		final MyAdapter1 adapter = new MyAdapter1(manager, this, tabHost, pager);
		//pager.setAdapter(adapter);

		// tab size from screen size
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int tabWidth = metrics.widthPixels / 3;
		int tabHeight = metrics.widthPixels / 7;

		String tabTitle[] = { "カード", "情報", "スリープ" };
		for (int i = 0; i < 3; i++) {
			TabSpec spec = tabHost.newTabSpec("tab" + i);
			spec.setIndicator(getTabView(tabWidth, tabHeight, tabTitle[i]));
			adapter.addTab(spec, "tab" + i);
		}

		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		boolean isVibrator = sp.getBoolean("Vibrator", false);
		if (isVibrator) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			long[] pattern = { 0, 500, 250, 500, 250, 500 }; // OFF/ON/OFF/ON...
			vibrator.vibrate(pattern, -1);
		}

		String url = sp.getString("Alarm", "");
		if (url.length() > 0) {
			mRingtone = RingtoneManager.getRingtone(this, Uri.parse(url));
			mRingtone.play();
		}
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

	private class MyAdapter1 extends FragmentPagerAdapter
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

		public MyAdapter1(FragmentManager fm, Context context, TabHost tabHost, ViewPager pager) {
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
				//return new ButtonSwitchFragment();
				return new CardFragment();
			} else if (position == 1) {
				//return new LabelListFragment(DummyGenerator.getLabelList());
				StateFragment stateFragment = new StateFragment();
				Bundle args = new Bundle();
				args.putSerializable("DTO", DummyGenerator.getDayCntList());
				stateFragment.setArguments(args);
				return stateFragment;
			} else {
				return new SleepFragment();
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//		getMenuInflater().inflate(R.menu.activity_tab, menu);
		super.onCreateOptionsMenu(menu);

		MenuItem item0 = menu.add(0, MENU_CREATE_CARD, 0, "カードを作る");
		item0.setIcon(android.R.drawable.ic_menu_add);

		MenuItem item1 = menu.add(0, MENU_SELECT_CARD, 0, "カードを選ぶ");
		item1.setIcon(android.R.drawable.ic_menu_gallery);

		MenuItem item2 = menu.add(0, MENU_SETTING, 0, "設定");
		item2.setIcon(android.R.drawable.ic_menu_preferences);

		MenuItem item3 = menu.add(0, MENU_USAGE, 0, "使い方");
		item3.setIcon(android.R.drawable.ic_menu_help);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Toast toast;
		Intent myIntent;
		switch (item.getItemId()) {
		case MENU_CREATE_CARD:
			toast = Toast.makeText(this, "カードを作るが選択されました", Toast.LENGTH_LONG);
			toast.show();
			myIntent = new Intent(getApplicationContext(), MakeCardActivity.class);
			myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(myIntent);
			break;
		case MENU_SELECT_CARD:
			toast = Toast.makeText(this, "カードを選ぶが選択されました", Toast.LENGTH_LONG);
			toast.show();
			myIntent = new Intent(getApplicationContext(), CardListActivity.class);
			myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(myIntent);
			break;
		case MENU_SETTING:
			toast = Toast.makeText(this, "設定が選択されました", Toast.LENGTH_LONG);
			toast.show();
			myIntent = new Intent(getApplicationContext(), MyPreference.class);
			myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(myIntent);
			break;
		case MENU_USAGE:
			toast = Toast.makeText(this, "使い方が選択されました", Toast.LENGTH_LONG);
			toast.show();
			myIntent = new Intent(getApplicationContext(), UsageActivity.class);
			myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(myIntent);
			break;
		}
		return true;
	}

	@Override
	public void onCardClick() {
		mRingtone.stop();
	}

	public void cancelButtonOnClick(View view) {
		CounterDao counterDao = new CounterDao(this);
		if (counterDao.getCurrentCardId() != -1) {
			CounterEntity cnt = new CounterEntity();
			cnt.cardId = counterDao.getCurrentCardId();
			cnt.procTime = System.currentTimeMillis();
			cnt.okCnt = 0;
			cnt.ngCnt = 1;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			cnt.procDay = sdf.format(new Date(cnt.procTime));
			counterDao.setCounter(cnt);
		}
		
		finish();
	}

	public void okButtonOnClick(View view) {
		CounterDao counterDao = new CounterDao(this);
		if (counterDao.getCurrentCardId() != -1) {
			CounterEntity cnt = new CounterEntity();
			cnt.cardId = counterDao.getCurrentCardId();
			cnt.procTime = System.currentTimeMillis();
			cnt.okCnt = 1;
			cnt.ngCnt = 0;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			cnt.procDay = sdf.format(new Date(cnt.procTime));
			counterDao.setCounter(cnt);
		}

		finish();
	}
}