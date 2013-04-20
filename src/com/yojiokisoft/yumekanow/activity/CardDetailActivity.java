package com.yojiokisoft.yumekanow.activity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;
import com.yojiokisoft.yumekanow.R;
import com.yojiokisoft.yumekanow.db.DatabaseHelper;
import com.yojiokisoft.yumekanow.entity.CardEntity;

public class CardDetailActivity extends Activity {
	private Context mContext;
	private ViewPager mViewPager;

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

		Button useButton = (Button) findViewById(R.id.useButton);
		Button editButton = (Button) findViewById(R.id.editButton);

		DatabaseHelper helper = DatabaseHelper.getInstance(this);
		Dao<CardEntity, Integer> cardDao;
		List<CardEntity> list = null;
		try {
			cardDao = helper.getDao(CardEntity.class);
			list = cardDao.queryForAll();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		useButton.setOnClickListener(mUseButtonOnClick);
		editButton.setOnClickListener(mEditButtonOnClick);

		CustomPagerAdapter adapter = new CustomPagerAdapter(this);
		CardEntity cardEntity;
		int page = 0;
		for (int i = 0; i < list.size(); i++) {
			cardEntity = list.get(i);
			adapter.add(cardEntity.id);
			if (cardEntity.id == card.id) {
				page = i;
			}
		}

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(adapter);
		mViewPager.setCurrentItem(page);
	}

	// このカードを使うボタンクリック
	private final OnClickListener mUseButtonOnClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			CustomPagerAdapter adapter = (CustomPagerAdapter) mViewPager.getAdapter();
			CardEntity card = adapter.getCard(mViewPager.getCurrentItem());
			SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
			sp.edit().putInt("UseCard", card.id).commit();
			finish();
		}
	};

	// 編集ボタンクリック
	private final OnClickListener mEditButtonOnClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			CustomPagerAdapter adapter = (CustomPagerAdapter) mViewPager.getAdapter();
			CardEntity card = adapter.getCard(mViewPager.getCurrentItem());
			Intent myIntent = new Intent(getApplicationContext(), MakeCardActivity.class);
			myIntent.putExtra("Card", card);
			myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(myIntent);
		}
	};

	public class CustomPagerAdapter extends PagerAdapter {

		private TextView mTextView;
		private ImageView mImageView;
		private LinearLayout mTextContainer;
		private Dao<CardEntity, Integer> mCardDao;
		/** コンテキスト. */
		private Context mContext;

		/** リスト. */
		private ArrayList<Integer> mList;

		private LayoutInflater mInflter;

		/**
		 * コンストラクタ.
		 */
		public CustomPagerAdapter(Context context) {
			mContext = context;
			mInflter = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			mList = new ArrayList<Integer>();

			DatabaseHelper helper = DatabaseHelper.getInstance(mContext);
			try {
				mCardDao = helper.getDao(CardEntity.class);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		/**
		 * リストにアイテムを追加する.
		 * @param item アイテム
		 */
		public void add(Integer item) {
			mList.add(item);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			FrameLayout layout = (FrameLayout) this.mInflter.inflate(R.layout.card_detail, null);

			mTextView = (TextView) layout.findViewById(R.id.affirmationText);
			mTextContainer = (LinearLayout) layout.findViewById(R.id.textContainer);
			mImageView = (ImageView) layout.findViewById(R.id.backImage);

			// リストから取得
			Integer cardId = mList.get(position);
			Log.d("taoka", "pos=" + position + ",cardId=" + cardId);
			try {
				List<CardEntity> list = mCardDao.queryForEq("id", cardId);
				printCard(list.get(0));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// コンテナに追加
			container.addView(layout);

			return layout;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// コンテナから View を削除
			container.removeView((View) object);
		}

		@Override
		public int getCount() {
			// リストのアイテム数を返す
			return mList.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			// Object 内に View が存在するか判定する
			return view == (FrameLayout) object;
		}

		private void printCard(CardEntity card) {
			mTextView.setText(card.affirmationText);
			mTextView.setTextColor(card.textColor);
			mTextView.setShadowLayer(1.5f, 1.5f, 1.5f, card.shadowColor);
			mTextView.setTextSize(card.textSize);
			mTextContainer.setPadding(card.marginLeft, card.marginTop, 0, 0);

			mImageView.setImageResource(card.backImageResourceId);
		}

		public CardEntity getCard(int position) {
			int cardId = mList.get(position);
			try {
				List<CardEntity> list = mCardDao.queryForEq("id", cardId);
				return list.get(0);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
	}
}
