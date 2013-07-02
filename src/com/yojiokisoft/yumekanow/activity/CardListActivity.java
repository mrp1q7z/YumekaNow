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
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ItemClick;
import com.googlecode.androidannotations.annotations.ItemLongClick;
import com.googlecode.androidannotations.annotations.ViewById;
import com.yojiokisoft.yumekanow.R;
import com.yojiokisoft.yumekanow.adapter.CardListAdapter;
import com.yojiokisoft.yumekanow.db.CardDao;
import com.yojiokisoft.yumekanow.db.SettingDao;
import com.yojiokisoft.yumekanow.entity.CardEntity;
import com.yojiokisoft.yumekanow.exception.MyUncaughtExceptionHandler;
import com.yojiokisoft.yumekanow.utils.MyConst;
import com.yojiokisoft.yumekanow.utils.MyDialog;

/**
 * カード一覧アクティビティ
 */
@EActivity(R.layout.activity_card_list)
public class CardListActivity extends Activity {
	@ViewById(R.id.cardList)
	/*package*/ListView mListView;

	private Activity mActivity;

	/**
	 * アクティビティの初期化 (onCreateと同等のタイミングで呼ばれる）
	 */
	@AfterViews
	/*package*/void initActivity() {
		mActivity = this;
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
		BaseAdapter adapter = new CardListAdapter(this, list);
		mListView.setAdapter(adapter);
	}

	/**
	 * カード一覧のアイテムがクリックされた.
	 * 
	 * @param card クリックされたカード
	 */
	@ItemClick
	/*package*/void cardListItemClicked(CardEntity card) {
		Intent intent = new Intent(getApplicationContext(), CardDetailActivity_.class);
		intent.putExtra(MyConst.EN_CARD, card);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

	/**
	 * カード一覧のアイテムが長押しされた.
	 * 
	 * @param card 長押しされたカード
	 */
	@ItemLongClick
	/*package*/void cardListItemLongClicked(CardEntity card) {
		SettingDao settingDao = SettingDao.getInstance();
		int useCardId = settingDao.getUseCard();
		if (card.id == useCardId) {
			MyDialog.Builder.newInstance(this).setTitle(getString(R.string.oops))
					.setMessage(getString(R.string.not_del_msg))
					.show();
			return;
		}

		// カード削除リスナー
		final CardEntity delCard = card;
		OnClickListener deleteCard = new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				try {
					CardDao cardDao = new CardDao();
					cardDao.delete(delCard);
					List<CardEntity> list = cardDao.queryForAll();
					((CardListAdapter) mListView.getAdapter()).setItems(list);
					mListView.invalidateViews();
				} catch (SQLException e) {
					MyUncaughtExceptionHandler.sendBugReport(mActivity, e);
				}
			}
		};

		// 削除確認ダイアログの表示
		MyDialog.Builder.newInstance(this).setTitle(getString(R.string.del))
				.setMessage(getString(R.string.del_confirm_msg))
				.setPositiveLabel(getString(R.string.yes))
				.setPositiveClickListener(deleteCard)
				.setNegativeLabel(getString(R.string.no))
				.show();
	}
}
