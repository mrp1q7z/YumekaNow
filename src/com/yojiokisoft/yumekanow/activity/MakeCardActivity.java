package com.yojiokisoft.yumekanow.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ViewSwitcher.ViewFactory;

import com.yojiokisoft.yumekanow.R;
import com.yojiokisoft.yumekanow.model.DummyGenerator;
import com.yojiokisoft.yumekanow.model.Item;

public class MakeCardActivity extends Activity implements ViewFactory {
	private BaseAdapter mAdapter;
	private ImageSwitcher mImageSwitcher;
	private Gallery mGallery;
	private int mCurImgPos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_makecard);

		mImageSwitcher = (ImageSwitcher) findViewById(R.id.backImgSwitcher);
		mImageSwitcher.setFactory(this);
		mImageSwitcher.setInAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
		mImageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_out));
		mImageSwitcher.setImageResource(R.drawable.image_1);

		mGallery = (Gallery) findViewById(R.id.backImgGallery);
		mGallery.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				mImageSwitcher.setImageResource((int) mGallery.getItemIdAtPosition(position));
				mCurImgPos = position;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		ArrayList<Item> list = (ArrayList<Item>) DummyGenerator.getItemAlphabetList();
		mAdapter = new MyListArrayAdapter(this, list);
		mGallery.setAdapter(mAdapter);

		Button previewButton = (Button) findViewById(R.id.previewButton);
		previewButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				EditText editText = (EditText) findViewById(R.id.affirmationText);
				Intent myIntent = new Intent(getApplicationContext(), CardPreviewActivity.class);
				Item item = new Item();
				item.setLabel(editText.getText().toString());
				item.setDrawable((int) mGallery.getItemIdAtPosition(mCurImgPos));
				myIntent.putExtra("Item", item);
				myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(myIntent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		return true;
	}

	@Override
	public View makeView() {
		ImageView imageView = new ImageView(this);
		imageView.setBackgroundColor(0xFF000000);
		imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
		imageView.setLayoutParams(new ImageSwitcher.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		return imageView;
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
			return mItems.get(pos).getDrawable();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				LinearLayout layout = new LinearLayout(mActivity);
				layout.setPadding(5, 5, 5, 5);
				layout.setOrientation(LinearLayout.VERTICAL);
				layout.setGravity(Gravity.CENTER);
				convertView = layout;

				ImageView view = new ImageView(mActivity);
				view.setTag("image");
				view.setLayoutParams(new LinearLayout.LayoutParams(240, 120));
				layout.addView(view);
			}

			Item item = mItems.get(position);
			ImageView imageView = (ImageView) convertView.findViewWithTag("image");
			imageView.setImageResource(item.getDrawable());

			return convertView;
		}
	}
}
