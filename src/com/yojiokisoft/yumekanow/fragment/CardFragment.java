package com.yojiokisoft.yumekanow.fragment;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ViewById;
import com.j256.ormlite.dao.Dao;
import com.yojiokisoft.yumekanow.R;
import com.yojiokisoft.yumekanow.activity.MainActivity;
import com.yojiokisoft.yumekanow.db.DatabaseHelper;
import com.yojiokisoft.yumekanow.entity.CardEntity;
import com.yojiokisoft.yumekanow.entity.CounterEntity;
import com.yojiokisoft.yumekanow.model.CounterDao;
import com.yojiokisoft.yumekanow.model.SettingDao;

@EFragment(R.layout.fragment_card)
public class CardFragment extends Fragment {
	private Activity mActivity;

	@ViewById(R.id.affirmationText)
	TextView mAffirmationText;

	@ViewById(R.id.affirmationBack)
	ImageView mBackImage;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return null;
	}

	@AfterViews
	public void printCard() {
		// カード情報を取得
		int cardId = SettingDao.getInstance(mActivity).getUseCard();
		if (cardId == -1) {
			setDefalutCard();
			return;
		}
		DatabaseHelper helper = DatabaseHelper.getInstance(mActivity);
		Dao<CardEntity, Integer> cardDao;
		List<CardEntity> cardList = null;
		try {
			cardDao = helper.getDao(CardEntity.class);
			cardList = cardDao.queryForEq("id", cardId);
			if (cardList.size() < 1) {
				CardEntity emptyCard = new CardEntity();
				emptyCard.affirmationText = "カードを作成してください";
				emptyCard.textColor = Color.BLACK;
				emptyCard.shadowColor = Color.WHITE;
				emptyCard.textSize = 20;
				emptyCard.marginLeft = 10;
				emptyCard.marginTop = 50;
				emptyCard.backImageResourceId = R.drawable.back_img01;
				cardList.add(emptyCard);
			}
			if (cardList.size() > 1) {
				throw new ClassCastException("Card is multi id=" + cardId);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			mBackImage.setImageDrawable(drawable);
		} else {
			mBackImage.setImageResource(card.backImageResourceId);
		}
	}

	private void setDefalutCard() {
		mAffirmationText.setTextColor(0xff333333);
		mAffirmationText.setTextSize(20.0f);
		LayoutParams params = (LayoutParams) mAffirmationText.getLayoutParams();
		params.setMargins(30, 120, 0, 0);
		mAffirmationText.setLayoutParams(params);
		mAffirmationText.setShadowLayer(1.5f, 1.0f, 1.0f, Color.WHITE);
		mAffirmationText.setText("カードを選択してください");

		mBackImage.setImageResource(R.drawable.back_img01);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = activity;
	}

	private void setCounter(int okFlag) {
		CounterDao counterDao = new CounterDao(mActivity);
		if (counterDao.getCurrentCardId() != -1) {
			CounterEntity cnt = new CounterEntity();
			cnt.cardId = counterDao.getCurrentCardId();
			cnt.procTime = System.currentTimeMillis();
			if (okFlag != 0) {
				cnt.okCnt = 1;
				cnt.ngCnt = 0;
			} else {
				cnt.okCnt = 0;
				cnt.ngCnt = 1;
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			cnt.procDay = sdf.format(new Date(cnt.procTime));
			counterDao.setCounter(cnt);
		}
	}

	@Click(R.id.okButton)
	void okButtonClicked() {
		setCounter(1);
		try {
			((MainActivity) mActivity).closeActivity();
		} catch (ClassCastException e) {
			throw new ClassCastException("activity が closeActivity を実装していません.");
		}
	}

	@Click(R.id.cancelButton)
	void cancelButtonClicked() {
		setCounter(0);
		try {
			((MainActivity) mActivity).closeActivity();
		} catch (ClassCastException e) {
			throw new ClassCastException("activity が closeActivity を実装していません.");
		}
	}

	@Click(R.id.cardContainer)
	void cardContainerClicked() {
		try {
			((MainActivity) mActivity).stopVibrator();
		} catch (ClassCastException e) {
			throw new ClassCastException("activity が stopVibrator を実装していません.");
		}
	}
}
