package com.yojiokisoft.yumekanow.activity;

import java.sql.SQLException;
import java.util.List;

import android.app.Activity;
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
import com.googlecode.androidannotations.annotations.ViewById;
import com.j256.ormlite.dao.Dao;
import com.yojiokisoft.yumekanow.R;
import com.yojiokisoft.yumekanow.db.DatabaseHelper;
import com.yojiokisoft.yumekanow.entity.CardEntity;

@EActivity(R.layout.activity_card_list)
public class CardListActivity extends Activity {
	@ViewById(R.id.cardList)
	ListView mListView;

	@AfterViews
	public void initActivity() {
		DatabaseHelper helper = DatabaseHelper.getInstance(this);
		List<CardEntity> cardList = null;
		try {
			Dao<CardEntity, Integer> cardDao = helper.getDao(CardEntity.class);
			cardList = cardDao.queryForAll();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BaseAdapter adapter = new MyListArrayAdapter(this, cardList);
		mListView.setAdapter(adapter);
	}

	@ItemClick
	public void cardListItemClicked(CardEntity card) {
		Intent myIntent = new Intent(getApplicationContext(), CardDetailActivity_.class);
		myIntent.putExtra("Card", card);
		myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(myIntent);
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
