package com.yojiokisoft.yumekanow.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.yojiokisoft.yumekanow.R;
import com.yojiokisoft.yumekanow.model.DummyGenerator;
import com.yojiokisoft.yumekanow.model.Item;

public class CardListActivity extends Activity {
	private BaseAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cardlist);
		ListView listView = (ListView) findViewById(R.id.cardList);
		ArrayList<Item> list = (ArrayList<Item>) DummyGenerator.getItemAlphabetList();
		adapter = new MyListArrayAdapter(this, list);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ListView listView = (ListView) parent;
				Intent myIntent = new Intent(getApplicationContext(), CardPreviewActivity.class);
				Item item = (Item) listView.getItemAtPosition(position);
				myIntent.putExtra("Item", item);
				myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(myIntent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_cardlist, menu);
		return true;
	}

	/**
	 * アダプタークラス
	 */
	private class MyListArrayAdapter extends BaseAdapter {
		private Activity mActivity;
		private ArrayList<Item> mItems;

		MyListArrayAdapter(Activity activity, ArrayList<Item> items) {
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

			Item item = mItems.get(position);
			wrapper.image.setImageResource(item.getDrawable());
			wrapper.label.setText(item.getLabel());

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

		//		@Override
		//		public View getView(int pos, View convertView, ViewGroup parent) {
		//			DayCnt item = mItems.get(pos);
		//			// レイアウトの生成
		//			if (convertView == null) {
		//				LinearLayout layout = new LinearLayout(mActivity);
		//				layout.setPadding(10, 10, 10, 10);
		//				convertView = layout;
		//
		//				// ｎ日目
		//				TextView nday = new TextView(mActivity);
		//				nday.setTag("nday");
		//				nday.setTextColor(Color.rgb(0, 255, 128));
		//				nday.setPadding(0, 0, 10, 0);
		//				layout.addView(nday);
		//
		//				// 日付
		//				TextView date = new TextView(mActivity);
		//				date.setTag("date");
		//				date.setTextColor(Color.rgb(0, 128, 255));
		//				date.setPadding(0, 0, 10, 0);
		//				layout.addView(date);
		//
		//				// OKカウント
		//				TextView cnt = new TextView(mActivity);
		//				cnt.setTag("cnt");
		//				cnt.setTextColor(Color.rgb(255, 128, 128));
		//				cnt.setPadding(0, 0, 10, 0);
		//				layout.addView(cnt);
		//			}
		//			// 値の設定
		//			String s;
		//			TextView nday = (TextView) convertView.findViewWithTag("nday");
		//			s = String.format("%02d", item.getDay()) + "日目";
		//			nday.setText(s);
		//			TextView date = (TextView) convertView.findViewWithTag("date");
		//			DateFormat format = new SimpleDateFormat("yyyy-MM-dd'('E')'", Locale.JAPAN);
		//			s = format.format(item.getDate());
		//			date.setText(s);
		//			TextView myCnt = (TextView) convertView.findViewWithTag("cnt");
		//			s = String.format("%2d", item.getDay()) + "回";
		//			myCnt.setText(s);
		//
		//			return convertView;
		//		}
	}

}
