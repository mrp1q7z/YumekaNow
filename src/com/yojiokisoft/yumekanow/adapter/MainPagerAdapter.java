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

package com.yojiokisoft.yumekanow.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabWidget;

import com.yojiokisoft.yumekanow.fragment.CardFragment_;
import com.yojiokisoft.yumekanow.fragment.SleepFragment_;
import com.yojiokisoft.yumekanow.fragment.StateFragment_;

/**
 * メインアクティビティのページャーアダプター
 */
public class MainPagerAdapter extends FragmentPagerAdapter
		implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {

	private final Context mContext;
	private final TabHost mTabHost;
	private final ViewPager mPager;
	private final ArrayList<String> mTabs = new ArrayList<String>();

	/**
	 * 動的にタブの中身を作るためのファクトリークラス
	 */
	private class DummyTabFactory implements TabHost.TabContentFactory {
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

	/**
	 * コンストラクタ.
	 * 
	 * @param fragmentManager
	 * @param context
	 * @param tabHost
	 * @param pager
	 */
	public MainPagerAdapter(FragmentManager fragmentManager, Context context, TabHost tabHost, ViewPager pager) {
		super(fragmentManager);
		this.mContext = context;
		this.mTabHost = tabHost;
		this.mPager = pager;
		this.mTabHost.setOnTabChangedListener(this);
		this.mPager.setAdapter(this);
		this.mPager.setOnPageChangeListener(this);
	}

	/**
	 * タブの追加.
	 * 
	 * @param tabSpec
	 * @param content
	 */
	public void addTab(TabHost.TabSpec tabSpec, String content) {
		tabSpec.setContent(new DummyTabFactory(this.mContext));
		mTabs.add(content);
		mTabHost.addTab(tabSpec);
		notifyDataSetChanged();
	}

	/**
	 * @see FragmentPagerAdapter#getItem(int)
	 */
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

	/**
	 * @see FragmentPagerAdapter#getCount()
	 */
	@Override
	public int getCount() {
		return 3;
	}

	/**
	 * @see ViewPager.OnPageChangeListener#onPageScrollStateChanged(int)
	 */
	@Override
	public void onPageScrollStateChanged(int state) {
		;
	}

	/**
	 * @see ViewPager.OnPageChangeListener#onPageScrolled(int, float, int)
	 */
	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOggsetPixesl) {
		;
	}

	/**
	 * @see ViewPager.OnPageChangeListener#onPageSelected(int)
	 */
	@Override
	public void onPageSelected(int position) {
		TabWidget widget = mTabHost.getTabWidget();

		int oldFocus = widget.getDescendantFocusability();
		widget.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);

		mTabHost.setCurrentTab(position);

		widget.setDescendantFocusability(oldFocus);
	}

	/**
	 * @see TabHost.OnTabChangeListener#onTabChanged(String)
	 */
	@Override
	public void onTabChanged(String tabId) {
		int position = mTabHost.getCurrentTab();

		mPager.setCurrentItem(position);
	}
}