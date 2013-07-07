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

package com.yojiokisoft.yumekanow.fragment;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ViewById;
import com.yojiokisoft.yumekanow.R;
import com.yojiokisoft.yumekanow.activity.MainActivity;
import com.yojiokisoft.yumekanow.db.CardDao;
import com.yojiokisoft.yumekanow.db.CounterDao;
import com.yojiokisoft.yumekanow.db.SettingDao;
import com.yojiokisoft.yumekanow.entity.CardEntity;
import com.yojiokisoft.yumekanow.entity.CounterEntity;
import com.yojiokisoft.yumekanow.exception.MyUncaughtExceptionHandler;

/**
 * カードフラグメント
 */
@EFragment(R.layout.fragment_card)
public class CardFragment extends Fragment {
	@ViewById(R.id.affirmationText)
	/*package*/TextView mAffirmationText;

	@ViewById(R.id.cardContainer)
	/*package*/FrameLayout mBackImage;

	private Activity mActivity;

	/**
	 * フラグメントがアクティビティにアタッチされたときに呼ばれる.
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = activity;
	}

	/**
	 * フラグメント用のビューを作成
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// nullを返すことで @EFragment(xxx) で定義している xxx がインフレートされる
		return null;
	}

	/**
	 * フラグメントの初期化
	 */
	@AfterViews
	/*package*/void initFragment() {
		// カード情報を取得
		List<CardEntity> cardList = null;
		int cardId = SettingDao.getInstance().getUseCard();
		if (cardId == -1) {
			cardList = new ArrayList<CardEntity>();
			cardList.add(getDefalutCard());
		} else {
			try {
				CardDao cardDao = new CardDao();
				cardList = cardDao.queryForEq("id", cardId);
				if (cardList.size() < 1) {
					cardList.add(getEmptyCard());
				}
			} catch (SQLException e) {
				MyUncaughtExceptionHandler.sendBugReport(mActivity, e);
			}
		}
		if (cardList == null) {
			return;
		}
		CardEntity card = cardList.get(0);

		mAffirmationText.setText(card.affirmationText);
		mAffirmationText.setTextColor(card.textColor);
		mAffirmationText.setTextSize(card.textSize);
		mAffirmationText.setShadowLayer(1.5f, 1.0f, 1.0f, card.shadowColor);

		DisplayMetrics metrics = new DisplayMetrics();
		mActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int tabHeight = metrics.widthPixels / 7;
		MarginLayoutParams params = (MarginLayoutParams) mAffirmationText.getLayoutParams();
		params.leftMargin = card.marginLeft;
		params.topMargin = card.marginTop - (tabHeight / 2);
		mAffirmationText.setLayoutParams(params);

		if (card.backImageResourceId == 0) {
			Drawable drawable = Drawable.createFromPath(card.backImagePath);
			mBackImage.setBackgroundDrawable(drawable);
		} else {
			mBackImage.setBackgroundResource(card.backImageResourceId);
		}
	}

	/**
	 * 空のカード情報を返す.（カードの作成を促すメッセージ入り）
	 * 
	 * @return CardEntity
	 */
	private CardEntity getEmptyCard() {
		CardEntity card = new CardEntity();
		card.affirmationText = getString(R.string.make_card_msg);
		card.textColor = Color.BLACK;
		card.shadowColor = Color.WHITE;
		card.textSize = 20;
		card.marginLeft = 10;
		card.marginTop = 50;
		card.backImageResourceId = R.drawable.back_img01;
		return card;
	}

	/**
	 * カードが未選択時のカード情報を返す.（カードの選択を促すメッセージ入り）
	 * 
	 * @return CardEntity
	 */
	private CardEntity getDefalutCard() {
		CardEntity card = new CardEntity();
		card.affirmationText = getString(R.string.select_card_msg);
		card.textColor = Color.RED;
		card.shadowColor = Color.WHITE;
		card.textSize = 20;
		card.marginLeft = 30;
		card.marginTop = 120;
		card.backImageResourceId = R.drawable.back_img01;
		return card;
	}

	/**
	 * 唱えた回数を登録する.
	 * 
	 * @param buttonResId ボタンのリソースID
	 */
	private void setCounter(int buttonResId) {
		CounterDao counterDao = null;
		int currentCardId = -1;
		try {
			counterDao = new CounterDao();
			currentCardId = counterDao.getCurrentCardId();
		} catch (SQLException e) {
			MyUncaughtExceptionHandler.sendBugReport(mActivity, e);
		}
		if (currentCardId == -1) {
			return;
		}

		CounterEntity cnt = new CounterEntity();
		cnt.cardId = currentCardId;
		cnt.procTime = System.currentTimeMillis();
		if (buttonResId == R.id.okButton) {
			cnt.okCnt = 1;
			cnt.ngCnt = 0;
		} else {
			cnt.okCnt = 0;
			cnt.ngCnt = 1;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.JAPANESE);
		cnt.procDay = sdf.format(new Date(cnt.procTime));
		try {
			counterDao.setCounter(cnt);
		} catch (SQLException e) {
			MyUncaughtExceptionHandler.sendBugReport(mActivity, e);
		}
	}

	/**
	 * OKボタンのクリック
	 */
	@Click(R.id.okButton)
	/*package*/void okButtonClicked() {
		setCounter(R.id.okButton);
		try {
			((MainActivity) mActivity).closeActivity();
		} catch (RuntimeException e) {
			throw new RuntimeException("activity が closeActivity を実装していません.");
		}
	}

	/**
	 * キャンセルボタンのクリック
	 */
	@Click(R.id.cancelButton)
	/*package*/void cancelButtonClicked() {
		setCounter(R.id.cancelButton);
		try {
			((MainActivity) mActivity).closeActivity();
		} catch (RuntimeException e) {
			throw new RuntimeException("activity が closeActivity を実装していません.");
		}
	}

	/**
	 * カードのクリック
	 */
	@Click(R.id.cardContainer)
	/*package*/void cardContainerClicked() {
		try {
			((MainActivity) mActivity).stopVibrator();
		} catch (RuntimeException e) {
			throw new RuntimeException("activity が stopVibrator を実装していません.");
		}
	}
}
