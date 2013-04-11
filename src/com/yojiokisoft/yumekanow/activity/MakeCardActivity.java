package com.yojiokisoft.yumekanow.activity;

import java.sql.SQLException;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import android.widget.TextView;
import android.widget.ViewSwitcher.ViewFactory;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.Dao.CreateOrUpdateStatus;
import com.yojiokisoft.yumekanow.R;
import com.yojiokisoft.yumekanow.db.DatabaseHelper;
import com.yojiokisoft.yumekanow.dialog.ColorPickerDialog;
import com.yojiokisoft.yumekanow.entity.BackImageEntity;
import com.yojiokisoft.yumekanow.entity.CardEntity;
import com.yojiokisoft.yumekanow.model.BackImageDao;
import com.yojiokisoft.yumekanow.model.CardDetailDto;
import com.yojiokisoft.yumekanow.model.Item;

public class MakeCardActivity extends Activity implements ViewFactory {
	private BaseAdapter mAdapter;
	// GUI items
	private ImageSwitcher mImageSwitcher;
	private Gallery mGallery;
	private EditText mAffirmationText;
	private TextView mTextColor;
	private TextView mShadowColor;
	private Button mTextSizeDownButton;
	private Button mTextSizeUpButton;
	private TextView mTextSize;
	private Button mMarginTopDownButton;
	private Button mMarginTopUpButton;
	private TextView mMarginTop;
	private Button mMarginLeftDownButton;
	private Button mMarginLeftUpButton;
	private TextView mMarginLeft;

	private final DatabaseHelper mHelper = new DatabaseHelper(this);
	private int mCardId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_make_card);

		mImageSwitcher = (ImageSwitcher) findViewById(R.id.backImgSwitcher);
		mImageSwitcher.setFactory(this);
		mImageSwitcher.setInAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
		mImageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_out));
		mImageSwitcher.setImageResource(R.drawable.image_1);

		mGallery = (Gallery) findViewById(R.id.backImgGallery);
		mAffirmationText = (EditText) findViewById(R.id.affirmationText);

		mGallery.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				mImageSwitcher.setImageResource((int) mGallery.getItemIdAtPosition(position));
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		BackImageDao backImageDao = new BackImageDao(this.getResources());
		List<BackImageEntity> list = backImageDao.queryForAll();
		mAdapter = new MyListArrayAdapter(this, list);
		mGallery.setAdapter(mAdapter);

		Button previewButton = (Button) findViewById(R.id.previewButton);
		previewButton.setOnClickListener(mPreviewClickListener);

		Button okButton = (Button) findViewById(R.id.okButton);
		okButton.setOnClickListener(mOkButtonClick);

		mTextColor = (TextView) findViewById(R.id.textColor);
		mTextColor.setTag(0x33ee88);
		mTextColor.setOnClickListener(mTextColorClick);

		mShadowColor = (TextView) findViewById(R.id.shadowColor);
		mShadowColor.setTag(0x000001);
		mShadowColor.setOnClickListener(mShadowColorClick);

		mTextSizeDownButton = (Button) findViewById(R.id.textSizeDownButton);
		mTextSizeDownButton.setOnClickListener(mTextSizeDownOnClick);
		mTextSizeUpButton = (Button) findViewById(R.id.textSizeUpButton);
		mTextSizeUpButton.setOnClickListener(mTextSizeUpOnClick);
		mTextSize = (TextView) findViewById(R.id.textSize);

		mMarginTopDownButton = (Button) findViewById(R.id.marginTopDownButton);
		mMarginTopDownButton.setOnClickListener(mMarginTopDownOnClick);
		mMarginTopUpButton = (Button) findViewById(R.id.marginTopUpButton);
		mMarginTopUpButton.setOnClickListener(mMarginTopUpOnClick);
		mMarginTop = (TextView) findViewById(R.id.marginTop);

		mMarginLeftDownButton = (Button) findViewById(R.id.marginLeftDownButton);
		mMarginLeftDownButton.setOnClickListener(mMarginLeftDownOnClick);
		mMarginLeftUpButton = (Button) findViewById(R.id.marginLeftUpButton);
		mMarginLeftUpButton.setOnClickListener(mMarginLeftUpOnClick);
		mMarginLeft = (TextView) findViewById(R.id.marginLeft);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			CardDetailDto dto = (CardDetailDto) extras.getSerializable("dto");
			mAffirmationText.setText(dto.affirmationText);
			int position = -1;
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).resouceId == dto.backImageResId) {
					position = i;
					break;
				}
			}
			if (position == -1) {
				mGallery.setSelection(0);
			} else {
				mGallery.setSelection(position, false);
			}
			mCardId = (Integer) extras.getSerializable("Position");
		}
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
	 * 文字色の設定
	 */
	private final OnClickListener mTextColorClick = new OnClickListener() {
		final ColorPickerDialog.DialogCallback textColorOnDialgOk = new ColorPickerDialog.DialogCallback() {
			@Override
			public void onDialogOk(int color) {
				mTextColor.setBackgroundColor(color);
				mTextColor.setTag(color);
			}
		};

		@Override
		public void onClick(View v) {
			ColorPickerDialog dialog = new ColorPickerDialog(MakeCardActivity.this);
			dialog.setDialogOkClickListener(textColorOnDialgOk);
			dialog.show();
		}
	};

	/**
	 * 影の色の設定
	 */
	private final OnClickListener mShadowColorClick = new OnClickListener() {
		final ColorPickerDialog.DialogCallback textColorOnDialgOk = new ColorPickerDialog.DialogCallback() {
			@Override
			public void onDialogOk(int color) {
				mShadowColor.setBackgroundColor(color);
				mShadowColor.setTag(color);
			}
		};

		@Override
		public void onClick(View v) {
			ColorPickerDialog dialog = new ColorPickerDialog(MakeCardActivity.this);
			dialog.setDialogOkClickListener(textColorOnDialgOk);
			dialog.show();
		}
	};

	/**
	 * 文字サイズ縮小ボタンのクリックリスナー
	 */
	private final OnClickListener mTextSizeDownOnClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			int size = Integer.parseInt(mTextSize.getText().toString());
			size--;
			if (size < 10) {
				size = 10;
			}
			mTextSize.setText(String.valueOf(size));
		}
	};

	/**
	 * 文字サイズ拡大ボタンのクリックリスナー
	 */
	private final OnClickListener mTextSizeUpOnClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			int size = Integer.parseInt(mTextSize.getText().toString());
			size++;
			if (size > 50) {
				size = 50;
			}
			mTextSize.setText(String.valueOf(size));
		}
	};

	/**
	 * 上マージン縮小ボタンのクリックリスナー
	 */
	private final OnClickListener mMarginTopDownOnClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			int size = Integer.parseInt(mMarginTop.getText().toString());
			size--;
			if (size < 10) {
				size = 10;
			}
			mMarginTop.setText(String.valueOf(size));
		}
	};

	/**
	 * 上マージン拡大ボタンのクリックリスナー
	 */
	private final OnClickListener mMarginTopUpOnClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			int size = Integer.parseInt(mMarginTop.getText().toString());
			size++;
			if (size > 60) {
				size = 60;
			}
			mMarginTop.setText(String.valueOf(size));
		}
	};

	/**
	 * 左マージン縮小ボタンのクリックリスナー
	 */
	private final OnClickListener mMarginLeftDownOnClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			int size = Integer.parseInt(mMarginLeft.getText().toString());
			size--;
			if (size < 10) {
				size = 10;
			}
			mMarginLeft.setText(String.valueOf(size));
		}
	};

	/**
	 * 左マージン拡大ボタンのクリックリスナー
	 */
	private final OnClickListener mMarginLeftUpOnClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			int size = Integer.parseInt(mMarginLeft.getText().toString());
			size++;
			if (size > 60) {
				size = 60;
			}
			mMarginLeft.setText(String.valueOf(size));
		}
	};

	/**
	 * プレビューボタンのクリックリスナー
	 */
	private final OnClickListener mPreviewClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent myIntent = new Intent(getApplicationContext(), CardPreviewActivity.class);
			Item item = new Item();
			item.setLabel(mAffirmationText.getText().toString());
			item.setDrawable((int) mGallery.getItemIdAtPosition(mCardId));
			myIntent.putExtra("Item", item);
			myIntent.putExtra("Position", -1);
			myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(myIntent);
		}
	};

	/**
	 * OKボタンのクリックリスナー
	 */
	private final OnClickListener mOkButtonClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Log.d("taoka", "MakeCardActivity#mOkButtonClick#onClick:bigin");
			try {
				Dao<CardEntity, Integer> cardDao = mHelper.getDao(CardEntity.class);
				CardEntity cardEntity = new CardEntity();
				cardEntity.id = mCardId;
				cardEntity.backImageResourceId = ((BackImageEntity) mGallery.getSelectedItem()).resouceId;
				cardEntity.affirmationText = mAffirmationText.getText().toString();
				cardEntity.textColor = (Integer) mTextColor.getTag();
				cardEntity.shadowColor = (Integer) mShadowColor.getTag();
				cardEntity.textSize = Integer.parseInt(mTextSize.getText().toString());
				cardEntity.marginTop = Integer.parseInt(mMarginTop.getText().toString());
				cardEntity.marginLeft = Integer.parseInt(mMarginLeft.getText().toString());
				CreateOrUpdateStatus ret = cardDao.createOrUpdate(cardEntity);
				finish();
				Log.d("taoka", "MakeCardActivity#mOkButtonClick#onClick:createOrUpdate ret=" + ret.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				mHelper.close();
			}
		}
	};

	/**
	 * アダプタークラス
	 */
	private class MyListArrayAdapter extends BaseAdapter {
		private Activity mActivity;
		private List<BackImageEntity> mItems;

		MyListArrayAdapter(Activity activity, List<BackImageEntity> items) {
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
			return mItems.get(pos).resouceId;
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
				view.setLayoutParams(new LinearLayout.LayoutParams(80, 120));
				layout.addView(view);
			}

			BackImageEntity item = mItems.get(position);
			ImageView imageView = (ImageView) convertView.findViewWithTag("image");
			imageView.setImageResource(item.resouceId);

			return convertView;
		}
	}
}
