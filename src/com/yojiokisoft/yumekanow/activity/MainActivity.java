package com.yojiokisoft.yumekanow.activity;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.yojiokisoft.yumekanow.fragment.CardFragment;
import com.yojiokisoft.yumekanow.fragment.StateFragment;
import com.yojiokisoft.yumekanow.fragment.UsageFragment;
import com.yojiokisoft.yumekanow.model.DummyGenerator;

public class MainActivity extends FragmentActivity {

	// メニューアイテム
	private static final int MENU_CREATE_CARD = 0; // カードを作る
	private static final int MENU_SELECT_CARD = 1; // カードを選ぶ
	private static final int MENU_SETTING = 2; // 設定
	private static final int MENU_USAGE = 3; // 使い方

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);

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

		String tabTitle[] = { "カード", "情報", "使い方" };
		for (int i = 0; i < 3; i++) {
			TabSpec spec = tabHost.newTabSpec("tab" + i);
			spec.setIndicator(getTabView(tabWidth, tabHeight, tabTitle[i]));
			adapter.addTab(spec, "tab" + i);
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
				return new UsageFragment();
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
			break;
		}
		return true;
	}
}