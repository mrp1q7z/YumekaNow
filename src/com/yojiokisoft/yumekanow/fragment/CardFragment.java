package com.yojiokisoft.yumekanow.fragment;

import java.sql.SQLException;
import java.util.List;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;
import com.yojiokisoft.yumekanow.R;
import com.yojiokisoft.yumekanow.db.DatabaseHelper;
import com.yojiokisoft.yumekanow.entity.CardEntity;

public class CardFragment extends Fragment {
	private View view;
	private Activity mActivity;
	private TextView mAffirmationText;
	private LinearLayout mBackImage;
	private OnCardClickListener mListener;

	// Container Activity must implement this interface  
	public interface OnCardClickListener {
		public void onCardClick();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = null;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_card, container, false);

		mAffirmationText = (TextView) view.findViewById(R.id.affirmationText);
		mAffirmationText.setOnClickListener(mAffirmationTextClick);
		mBackImage = (LinearLayout) view.findViewById(R.id.affirmationBack);

		// カード情報を取得
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mActivity);
		int cardId = sp.getInt("UseCard", -1);
		if (cardId == -1) {
			setDefalutCard();
			return view;
		}
		DatabaseHelper helper = new DatabaseHelper(mActivity);
		Dao<CardEntity, Integer> cardDao;
		List<CardEntity> cardList = null;
		try {
			cardDao = helper.getDao(CardEntity.class);
			cardList = cardDao.queryForEq("id", cardId);
			if (cardList.size() < 1) {
				throw new ClassCastException("Card not found id=" + cardId);
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
		LayoutParams params = (LayoutParams) mAffirmationText.getLayoutParams();
		params.setMargins(card.marginLeft, card.marginTop, 0, 0);
		mAffirmationText.setLayoutParams(params);
		mAffirmationText.setShadowLayer(1.5f, 1.0f, 1.0f, card.shadowColor);

		mBackImage.setBackgroundResource(card.backImageResourceId);

		return view;
	}

	private void setDefalutCard() {
		mAffirmationText = (TextView) view.findViewById(R.id.affirmationText);
		mAffirmationText.setTextColor(0xff333333);
		mAffirmationText.setTextSize(20.0f);
		LayoutParams params = (LayoutParams) mAffirmationText.getLayoutParams();
		params.setMargins(30, 120, 0, 0);
		mAffirmationText.setLayoutParams(params);
		mAffirmationText.setShadowLayer(1.5f, 1.0f, 1.0f, Color.WHITE);
		mAffirmationText.setText("カードを選択してください");

		mBackImage.setBackgroundResource(R.drawable.back_img01);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnCardClickListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement OnCardClickListener");
		}
		mActivity = activity;
	}

	private final View.OnClickListener mAffirmationTextClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			mListener.onCardClick();
		}
	};
}
