package com.yojiokisoft.yumekanow.activity;

import java.sql.SQLException;
import java.util.List;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ItemClick;
import com.googlecode.androidannotations.annotations.ItemLongClick;
import com.googlecode.androidannotations.annotations.ViewById;
import com.yojiokisoft.yumekanow.R;
import com.yojiokisoft.yumekanow.db.CardDao;
import com.yojiokisoft.yumekanow.db.SettingDao;
import com.yojiokisoft.yumekanow.entity.CardEntity;
import com.yojiokisoft.yumekanow.exception.MyUncaughtExceptionHandler;
import com.yojiokisoft.yumekanow.utils.MyConst;
import com.yojiokisoft.yumekanow.utils.MyDialog;

@EActivity(R.layout.activity_card_list)
public class CardListActivity extends Activity {
	@ViewById(R.id.cardList)
	ListView mListView;

	private Activity mActivity;

	@AfterViews
	public void initActivity() {
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
		BaseAdapter adapter = new MyListArrayAdapter(this, list);
		mListView.setAdapter(adapter);
	}

	@ItemClick
	public void cardListItemClicked(CardEntity card) {
		Intent intent = new Intent(getApplicationContext(), CardDetailActivity_.class);
		intent.putExtra(MyConst.EN_CARD, card);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

	@ItemLongClick
	public void cardListItemLongClicked(CardEntity card) {
		SettingDao settingDao = SettingDao.getInstance();
		int useCardId = settingDao.getUseCard();
		if (card.id == useCardId) {
			MyDialog.Builder.newInstance(this).setTitle(getString(R.string.oops))
					.setMessage(getString(R.string.not_del_msg))
					.show();
			return;
		}

		final CardEntity delCard = card;
		// カード削除リスナー
		OnClickListener deleteCard = new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				try {
					CardDao cardDao = new CardDao();
					cardDao.delete(delCard);
					List<CardEntity> list = cardDao.queryForAll();
					((MyListArrayAdapter) mListView.getAdapter()).setItems(list);
					mListView.invalidateViews();
				} catch (SQLException e) {
					MyUncaughtExceptionHandler.sendBugReport(mActivity, e);
				}
			}
		};

		MyDialog.Builder.newInstance(this).setTitle(getString(R.string.del))
				.setMessage(getString(R.string.del_confirm_msg))
				.setPositiveLabel(getString(R.string.yes))
				.setPositiveClickListener(deleteCard)
				.setNegativeLabel(getString(R.string.no))
				.show();
	}

	/**
	 * アダプタークラス
	 */
	private class MyListArrayAdapter extends BaseAdapter {
		private Activity mActivity;
		private List<CardEntity> mItems;

		MyListArrayAdapter(Activity activity, List<CardEntity> items) {
			super();
			mActivity = activity;
			mItems = items;
		}

		public void setItems(List<CardEntity> items) {
			mItems = items;
		}

		@Override
		public int getCount() {
			return mItems.size();
		}

		@Override
		public Object getItem(int pos) {
			return mItems.get(pos);
		}

		@Override
		public long getItemId(int pos) {
			return pos;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			ViewWrapper wrapper = null;

			if (view == null) {
				view = mActivity.getLayoutInflater().inflate(R.layout.item_row, null);
				wrapper = new ViewWrapper(view);
				view.setTag(wrapper);
			} else {
				wrapper = (ViewWrapper) view.getTag();
			}

			CardEntity item = mItems.get(position);
			if (item.backImageResourceId == 0) {
				wrapper.image.setImageURI(Uri.parse("file:///" + item.backImagePath));
			} else {
				wrapper.image.setImageResource(item.backImageResourceId);
			}
			wrapper.label.setText(item.affirmationText);

			return view;
		}

		class ViewWrapper {
			public final ImageView image;
			public final TextView label;

			ViewWrapper(View view) {
				this.image = (ImageView) view.findViewById(R.id.item_row_img);
				this.label = (TextView) view.findViewById(R.id.item_row_txt);
			}
		}
	}
}
