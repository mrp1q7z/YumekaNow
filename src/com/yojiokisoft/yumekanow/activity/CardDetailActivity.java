package com.yojiokisoft.yumekanow.activity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
import com.googlecode.androidannotations.annotations.ViewById;
import com.yojiokisoft.yumekanow.R;
import com.yojiokisoft.yumekanow.db.CardDao;
import com.yojiokisoft.yumekanow.db.SettingDao;
import com.yojiokisoft.yumekanow.entity.CardEntity;
import com.yojiokisoft.yumekanow.exception.MyUncaughtExceptionHandler;
import com.yojiokisoft.yumekanow.utils.MyConst;

@EActivity(R.layout.activity_card_detail)
public class CardDetailActivity extends Activity {
	@ViewById(R.id.pager)
	ViewPager mViewPager;

	@Extra(MyConst.CARD)
	CardEntity mCard;

	@AfterViews
	public void initActivity() {
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

		CustomPagerAdapter adapter = new CustomPagerAdapter(this);
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

	// このカードを使うボタンクリック
	@Click(R.id.useButton)
	void useButtonOnClick() {
		CustomPagerAdapter adapter = (CustomPagerAdapter) mViewPager.getAdapter();
		CardEntity card = adapter.getCard(mViewPager.getCurrentItem());
		SettingDao settingDao = SettingDao.getInstance();
		settingDao.setUseCard(card.id);
		finish();

		Intent intent = new Intent(getApplication(), MainActivity_.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	// 編集ボタンクリック
	@Click(R.id.editButton)
	void editButtonOnClick() {
		CustomPagerAdapter adapter = (CustomPagerAdapter) mViewPager.getAdapter();
		CardEntity card = adapter.getCard(mViewPager.getCurrentItem());
		Intent myIntent = new Intent(getApplicationContext(), MakeCardActivity_.class);
		myIntent.putExtra(MyConst.CARD, card);
		myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(myIntent);
	}

	public class CustomPagerAdapter extends PagerAdapter {

		private TextView mTextView;
		private ImageView mImageView;
		private CardDao mCardDao = null;
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

			try {
				mCardDao = new CardDao();
			} catch (SQLException e) {
				MyUncaughtExceptionHandler.sendBugReport((Activity) mContext, e);
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
			mImageView = (ImageView) layout.findViewById(R.id.backImage);

			// リストから取得
			if (mCardDao != null) {
				Integer cardId = mList.get(position);
				try {
					List<CardEntity> list = mCardDao.queryForEq("id", cardId);
					printCard(list.get(0));
				} catch (SQLException e) {
					MyUncaughtExceptionHandler.sendBugReport((Activity) mContext, e);
				}
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
			MarginLayoutParams params = (MarginLayoutParams) mTextView.getLayoutParams();
			params.leftMargin = card.marginLeft;
			params.topMargin = card.marginTop;
			mTextView.setLayoutParams(params);

			if (card.backImageResourceId == 0) {
				mImageView.setImageURI(Uri.parse("file:///" + card.backImagePath));
			} else {
				mImageView.setImageResource(card.backImageResourceId);
			}
		}

		public CardEntity getCard(int position) {
			if (mCardDao != null) {
				int cardId = mList.get(position);
				try {
					List<CardEntity> list = mCardDao.queryForEq("id", cardId);
					return list.get(0);
				} catch (SQLException e) {
					MyUncaughtExceptionHandler.sendBugReport((Activity) mContext, e);
				}
			}
			return null;
		}
	}
}
