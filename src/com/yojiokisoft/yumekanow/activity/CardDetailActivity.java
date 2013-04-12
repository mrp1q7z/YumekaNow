package com.yojiokisoft.yumekanow.activity;

import java.sql.SQLException;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.yojiokisoft.yumekanow.R;
import com.yojiokisoft.yumekanow.db.DatabaseHelper;
import com.yojiokisoft.yumekanow.entity.CardEntity;

public class CardDetailActivity extends Activity {
	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_MAX_OFF_PATH = 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;

	private GestureDetector mGestureDetector;
	private TextView mTextView;
	private ImageView mImageView;
	private LinearLayout mTextContainer;
	private int mPosition;
	private List<CardEntity> mList;
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card_detail);

		mContext = this;

		Bundle extras = getIntent().getExtras();
		if (extras == null) {
			return;
		}
		CardEntity card = (CardEntity) extras.getSerializable("Card");
		mPosition = (Integer) extras.getSerializable("Position");

		mTextView = (TextView) findViewById(R.id.affirmationText);
		mTextContainer = (LinearLayout) findViewById(R.id.textContainer);
		mImageView = (ImageView) findViewById(R.id.backImage);
		
		printCard(card);

		Button useButton = (Button) findViewById(R.id.useButton);
		Button editButton = (Button) findViewById(R.id.editButton);

		mGestureDetector = new GestureDetector(this, mOnGestureListener);
		DatabaseHelper helper = new DatabaseHelper(this);
		Dao<CardEntity, Integer> cardDao;
		try {
			cardDao = helper.getDao(CardEntity.class);
			mList = cardDao.queryForAll();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		useButton.setOnClickListener(mUseButtonOnClick);
		editButton.setOnClickListener(mEditButtonOnClick);
	}
	
	private void printCard(CardEntity card) {
		mTextView.setText(card.affirmationText);
		mTextView.setTextColor(card.textColor);
		mTextView.setShadowLayer(1.5f, 1.5f, 1.5f, card.shadowColor);
		mTextView.setTextSize(card.textSize);
		mTextContainer.setPadding(card.marginLeft, card.marginTop, 0, 0);

		mImageView.setImageResource(card.backImageResourceId);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mGestureDetector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}

	private final SimpleOnGestureListener mOnGestureListener = new SimpleOnGestureListener() {

		@Override
		public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
			if (Math.abs(event1.getX() - event2.getX()) > SWIPE_MAX_OFF_PATH) {
				return false;
			}
			if (event1.getX() - event2.getX() > SWIPE_MIN_DISTANCE
					&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
				if (mPosition < (mList.size() - 1)) {
					mPosition++;
					CardEntity card = mList.get(mPosition);
					printCard(card);
				} else {
					Toast.makeText(CardDetailActivity.this, "次ページはありません", Toast.LENGTH_SHORT).show();
					return false;
				}
			} else if (event2.getX() - event1.getX() > SWIPE_MIN_DISTANCE
					&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
				if (mPosition > 0) {
					mPosition--;
					CardEntity card = mList.get(mPosition);
					printCard(card);
				} else {
					Toast.makeText(CardDetailActivity.this, "前ページはありません", Toast.LENGTH_SHORT).show();
					return false;
				}
			}
			return false;
		}
	};

	// このカードを使うボタンクリック
	private final OnClickListener mUseButtonOnClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			CardEntity card = mList.get(mPosition);
			SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
			sp.edit().putInt("UseCard", card.id).commit();
			finish();
		}
	};

	// 編集ボタンクリック
	private final OnClickListener mEditButtonOnClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			CardEntity card = mList.get(mPosition);
			Intent myIntent = new Intent(getApplicationContext(), MakeCardActivity.class);
			myIntent.putExtra("Card", card);
			myIntent.putExtra("Position", mPosition);
			myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(myIntent);
		}
	};
}
