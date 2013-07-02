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

import java.sql.SQLException;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.ViewPager;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
import com.googlecode.androidannotations.annotations.ViewById;
import com.yojiokisoft.yumekanow.R;
import com.yojiokisoft.yumekanow.adapter.CardDetailAdapter;
import com.yojiokisoft.yumekanow.db.CardDao;
import com.yojiokisoft.yumekanow.db.SettingDao;
import com.yojiokisoft.yumekanow.entity.CardEntity;
import com.yojiokisoft.yumekanow.exception.MyUncaughtExceptionHandler;
import com.yojiokisoft.yumekanow.utils.MyConst;

/**
 * カード詳細アクティビティ
 */
@EActivity(R.layout.activity_card_detail)
public class CardDetailActivity extends Activity {
	@ViewById(R.id.pager)
	/*package*/ViewPager mViewPager;

	@Extra(MyConst.EN_CARD)
	/*package*/CardEntity mCard;

	/**
	 * アクティビティの初期化 (onCreateと同等のタイミングで呼ばれる）
	 */
	@AfterViews
	/*package*/void initActivity() {
		List<CardEntity> list = null;
		try {
			CardDao cardDao = new CardDao();
			list = cardDao.queryForAll();
		} catch (SQLException e) {
			MyUncaughtExceptionHandler.sendBugReport(this, e);
		}
		if (list == null) {
			return;
		}

		CardDetailAdapter adapter = new CardDetailAdapter(this);
		CardEntity cardEntity;
		int page = 0;
		for (int i = 0; i < list.size(); i++) {
			cardEntity = list.get(i);
			adapter.add(cardEntity.id);
			if (cardEntity.id == mCard.id) {
				page = i;
			}
		}

		mViewPager.setAdapter(adapter);
		mViewPager.setCurrentItem(page);
	}

	/**
	 * このカードを使うボタンのクリック.
	 */
	@Click(R.id.useButton)
	/*package*/void useButtonOnClick() {
		CardDetailAdapter adapter = (CardDetailAdapter) mViewPager.getAdapter();
		CardEntity card = adapter.getCard(mViewPager.getCurrentItem());
		SettingDao settingDao = SettingDao.getInstance();
		settingDao.setUseCard(card.id);
		finish();

		Intent intent = new Intent(getApplication(), MainActivity_.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	/**
	 * 編集ボタンのクリック.
	 */
	@Click(R.id.editButton)
	/*package*/void editButtonOnClick() {
		CardDetailAdapter adapter = (CardDetailAdapter) mViewPager.getAdapter();
		CardEntity card = adapter.getCard(mViewPager.getCurrentItem());
		Intent myIntent = new Intent(getApplicationContext(), MakeCardActivity_.class);
		myIntent.putExtra(MyConst.EN_CARD, card);
		myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(myIntent);
	}
}
