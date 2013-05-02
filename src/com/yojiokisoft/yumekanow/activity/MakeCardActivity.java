package com.yojiokisoft.yumekanow.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.Pair;
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
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
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
import com.yojiokisoft.yumekanow.utils.MyImage;

public class MakeCardActivity extends Activity implements ViewFactory {
	private final int TEXT_SIZE_MIN = 10;
	private final int INTENT_REQUEST_PICTURE = 3;

	private BaseAdapter mAdapter;
	// GUI items
	private ImageSwitcher mImageSwitcher;
	private Gallery mGallery;
	private EditText mAffirmationText;
	private TextView mTextColor;
	private TextView mShadowColor;
	private TextView mTextSize;
	private TextView mMarginTop;
	private TextView mMarginLeft;

	private final DatabaseHelper mHelper = DatabaseHelper.getInstance(this);
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
				BackImageEntity backImage = (BackImageEntity) mGallery.getItemAtPosition(position);
				if (backImage.resouceId == 0) {
					mImageSwitcher.setImageURI(Uri.parse("file:///" + backImage.bitmapPath));
				} else {
					mImageSwitcher.setImageResource(backImage.resouceId);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		BackImageDao backImageDao = new BackImageDao(this.getResources());
		List<BackImageEntity> list = backImageDao.queryForAll();
		mAdapter = new MyListArrayAdapter(this, list);
		mGallery.setAdapter(mAdapter);

		Button addBackImgButton = (Button) findViewById(R.id.addBackImgButton);
		addBackImgButton.setOnClickListener(mAddBackImgButtonClick);

		Button previewButton = (Button) findViewById(R.id.previewButton);
		previewButton.setOnClickListener(mPreviewClickListener);

		Button okButton = (Button) findViewById(R.id.okButton);
		okButton.setOnClickListener(mOkButtonClick);

		Button cancelButton = (Button) findViewById(R.id.cancelButton);
		cancelButton.setOnClickListener(mCancelButtonClick);

		mTextColor = (TextView) findViewById(R.id.textColor);
		mTextColor.setTag(0x33ee88);
		mTextColor.setOnClickListener(mTextColorClick);

		mShadowColor = (TextView) findViewById(R.id.shadowColor);
		mShadowColor.setTag(0x000001);
		mShadowColor.setOnClickListener(mShadowColorClick);

		SeekBar textSizeBar = (SeekBar) findViewById(R.id.textSizeBar);
		textSizeBar.setOnSeekBarChangeListener(mTextSizeOnSeekBarChange);

		SeekBar marginTopBar = (SeekBar) findViewById(R.id.marginTopBar);
		marginTopBar.setOnSeekBarChangeListener(mMarginTopOnSeekBarChange);

		SeekBar marginLeftBar = (SeekBar) findViewById(R.id.marginLeftBar);
		marginLeftBar.setOnSeekBarChangeListener(mMarginLeftOnSeekBarChange);

		mTextSize = (TextView) findViewById(R.id.textSize);
		mTextSize.setText(String.valueOf(textSizeBar.getProgress() + TEXT_SIZE_MIN));
		mMarginTop = (TextView) findViewById(R.id.marginTop);
		mMarginTop.setText(String.valueOf(marginTopBar.getProgress()));
		mMarginLeft = (TextView) findViewById(R.id.marginLeft);
		mMarginLeft.setText(String.valueOf(marginLeftBar.getProgress()));

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			CardEntity card = (CardEntity) extras.getSerializable("Card");
			mAffirmationText.setText(card.affirmationText);
			mTextColor.setBackgroundColor(card.textColor);
			mTextColor.setTag(card.textColor);
			mShadowColor.setBackgroundColor(card.shadowColor);
			mShadowColor.setTag(card.shadowColor);
			mTextSize.setText(String.valueOf(card.textSize));
			textSizeBar.setProgress(card.textSize - TEXT_SIZE_MIN);
			mMarginTop.setText(String.valueOf(card.marginTop));
			marginTopBar.setProgress(card.marginTop);
			mMarginLeft.setText(String.valueOf(card.marginLeft));
			marginLeftBar.setProgress(card.marginLeft);
			int position = -1;
			for (int i = 0; i < list.size(); i++) {
				if (card.backImageResourceId == 0) {
					if (card.backImagePath.equals(list.get(i).bitmapPath)) {
						position = i;
						break;
					}
				} else {
					if (list.get(i).resouceId == card.backImageResourceId) {
						position = i;
						break;
					}
				}
			}
			if (position == -1) {
				mGallery.setSelection(0);
			} else {
				mGallery.setSelection(position, false);
			}
			mCardId = card.id;
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == INTENT_REQUEST_PICTURE && resultCode == RESULT_OK) {
			Bitmap bitmap = null;
			try {
				// 戻り値からInputStreamを取得
				InputStream in = getContentResolver().openInputStream(data.getData());
				// 読み込む際のオプション
				BitmapFactory.Options options = new BitmapFactory.Options();
				// 画像を読み込まずサイズを調整するだけにする
				options.inJustDecodeBounds = true;
				// optionsに画像情報を入れる
				BitmapFactory.decodeStream(in, null, options);
				// InputStreamは1回クローズする(もう中身が無い為、再利用は出来無い)
				in.close();
				// Displayに収まるサイズに調整するための割合を取得
				Pair<Integer, Integer> size = MyImage.getScreenWidthAndHeight(this);
				int width = options.outWidth / size.first + 1;
				int height = options.outHeight / size.second + 1;
				// 画像を 1 / Math.max(width, height) のサイズで取得するように調整
				options.inSampleSize = Math.max(width, height);
				// 実際に画像を読み込ませる
				options.inJustDecodeBounds = false;
				// もう1回InputStreamを取得
				in = getContentResolver().openInputStream(data.getData());
				// Bitmapの取得
				bitmap = BitmapFactory.decodeStream(in, null, options);
				// InputStreamのクローズ
				in.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			//ファイル名用フォーマット  
			Date today = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
			String filename = "img_" + dateFormat.format(today) + ".jpg";
			String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
					+ "YumekaNow" + File.separator + filename;
			Log.d("taoka", "path=" + path);
			File file = new File(path);
			try {
				MyImage.saveImage(file, bitmap);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
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
	 * プレビューボタンのクリックリスナー
	 */
	private final OnClickListener mPreviewClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent myIntent = new Intent(getApplicationContext(), CardPreviewActivity.class);
			CardEntity card = new CardEntity();
			card.id = mCardId;
			card.affirmationText = mAffirmationText.getText().toString();
			BackImageEntity backImage = (BackImageEntity) mGallery.getSelectedItem();
			card.backImageResourceId = backImage.resouceId;
			card.backImagePath = backImage.bitmapPath;
			card.textColor = (Integer) mTextColor.getTag();
			card.shadowColor = (Integer) mShadowColor.getTag();
			card.textSize = Integer.parseInt(mTextSize.getText().toString());
			card.marginTop = Integer.parseInt(mMarginTop.getText().toString());
			card.marginLeft = Integer.parseInt(mMarginLeft.getText().toString());
			myIntent.putExtra("Card", card);
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
				BackImageEntity backImage = (BackImageEntity) mGallery.getSelectedItem();
				cardEntity.backImageResourceId = backImage.resouceId;
				cardEntity.backImagePath = backImage.bitmapPath;
				cardEntity.affirmationText = mAffirmationText.getText().toString();
				cardEntity.textColor = (Integer) mTextColor.getTag();
				cardEntity.shadowColor = (Integer) mShadowColor.getTag();
				cardEntity.textSize = Integer.parseInt(mTextSize.getText().toString());
				cardEntity.marginTop = Integer.parseInt(mMarginTop.getText().toString());
				cardEntity.marginLeft = Integer.parseInt(mMarginLeft.getText().toString());
				CreateOrUpdateStatus ret = cardDao.createOrUpdate(cardEntity);
				Log.d("taoka", "MakeCardActivity#mOkButtonClick#onClick:createOrUpdate ret=" + ret.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				//				mHelper.close();
			}
			finish();

			Intent intent = new Intent(getApplication(), MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}
	};

	/**
	 * Cancelボタンのクリックリスナー
	 */
	private final OnClickListener mCancelButtonClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			finish();

			Intent intent = new Intent(getApplication(), MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}
	};

	/**
	 * 背景画像の追加ボタンのクリックリスナー
	 */
	private final OnClickListener mAddBackImgButtonClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(Intent.ACTION_PICK);
			intent.setType("image/*");
			startActivityForResult(intent, INTENT_REQUEST_PICTURE);
		}
	};

	private final OnSeekBarChangeListener mTextSizeOnSeekBarChange = new OnSeekBarChangeListener() {
		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			mTextSize.setText(String.valueOf(progress + TEXT_SIZE_MIN));
		}
	};

	private final OnSeekBarChangeListener mMarginTopOnSeekBarChange = new OnSeekBarChangeListener() {
		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			mMarginTop.setText(String.valueOf(progress));
		}
	};

	private final OnSeekBarChangeListener mMarginLeftOnSeekBarChange = new OnSeekBarChangeListener() {
		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			mMarginLeft.setText(String.valueOf(progress));
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
			if (item.resouceId == 0) {
				imageView.setImageBitmap(BitmapFactory.decodeFile(item.bitmapPath));
			} else {
				imageView.setImageResource(item.resouceId);
			}

			return convertView;
		}
	}
}
