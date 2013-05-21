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
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ViewSwitcher.ViewFactory;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
import com.googlecode.androidannotations.annotations.ItemSelect;
import com.googlecode.androidannotations.annotations.SeekBarProgressChange;
import com.googlecode.androidannotations.annotations.ViewById;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.Dao.CreateOrUpdateStatus;
import com.yojiokisoft.yumekanow.R;
import com.yojiokisoft.yumekanow.db.DatabaseHelper;
import com.yojiokisoft.yumekanow.dialog.ColorPickerDialog;
import com.yojiokisoft.yumekanow.entity.BackImageEntity;
import com.yojiokisoft.yumekanow.entity.CardEntity;
import com.yojiokisoft.yumekanow.model.BackImageDao;
import com.yojiokisoft.yumekanow.model.CardDao;
import com.yojiokisoft.yumekanow.utils.MyImage;

@EActivity(R.layout.activity_make_card)
public class MakeCardActivity extends Activity implements ViewFactory {
	private final int TEXT_SIZE_MIN = 10;
	private final int INTENT_REQUEST_PICTURE = 3;
	private final DatabaseHelper mHelper = DatabaseHelper.getInstance(this);
	private Activity mActivity;
	private BaseAdapter mAdapter;

	@ViewById(R.id.backImgSwitcher)
	ImageSwitcher mImageSwitcher;

	@ViewById(R.id.backImgGallery)
	Gallery mGallery;

	@ViewById(R.id.affirmationText)
	EditText mAffirmationText;

	@ViewById(R.id.textColor)
	TextView mTextColor;

	@ViewById(R.id.shadowColor)
	TextView mShadowColor;

	@ViewById(R.id.textSize)
	TextView mTextSize;

	@ViewById(R.id.marginTop)
	TextView mMarginTop;

	@ViewById(R.id.marginLeft)
	TextView mMarginLeft;

	@ViewById(R.id.delBackImgButton)
	Button mDelBackImgButton;

	@ViewById(R.id.textSizeBar)
	SeekBar mTextSizeBar;

	@ViewById(R.id.marginTopBar)
	SeekBar mMarginTopBar;

	@ViewById(R.id.marginLeftBar)
	SeekBar mMarginLeftBar;

	@Extra("Card")
	CardEntity mCard;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity = this;
	}

	@AfterViews
	void initActivity() {
		mImageSwitcher.setFactory(this);
		mImageSwitcher.setInAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
		mImageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_out));

		BackImageDao backImageDao = new BackImageDao(this.getResources());
		List<BackImageEntity> list = backImageDao.queryForAll();
		mAdapter = new MyListArrayAdapter(this, list);
		mGallery.setAdapter(mAdapter);

		int color = getResources().getColor(R.color.textColor);
		mTextColor.setBackgroundColor(color);
		mTextColor.setTag(color);

		color = getResources().getColor(R.color.shadowColor);
		mShadowColor.setBackgroundColor(color);
		mShadowColor.setTag(color);

		mTextSize.setText(String.valueOf(mTextSizeBar.getProgress() + TEXT_SIZE_MIN));
		mMarginTop.setText(String.valueOf(mMarginTopBar.getProgress()));
		mMarginLeft.setText(String.valueOf(mMarginLeftBar.getProgress()));

		if (mCard != null) {
			mAffirmationText.setText(mCard.affirmationText);
			mTextColor.setBackgroundColor(mCard.textColor);
			mTextColor.setTag(mCard.textColor);
			mShadowColor.setBackgroundColor(mCard.shadowColor);
			mShadowColor.setTag(mCard.shadowColor);
			mTextSize.setText(String.valueOf(mCard.textSize));
			mTextSizeBar.setProgress(mCard.textSize - TEXT_SIZE_MIN);
			mMarginTop.setText(String.valueOf(mCard.marginTop));
			mMarginTopBar.setProgress(mCard.marginTop);
			mMarginLeft.setText(String.valueOf(mCard.marginLeft));
			mMarginLeftBar.setProgress(mCard.marginLeft);
			int position = -1;
			for (int i = 0; i < list.size(); i++) {
				if (mCard.backImageResourceId == 0) {
					if (mCard.backImagePath.equals(list.get(i).bitmapPath)) {
						position = i;
						break;
					}
				} else {
					if (list.get(i).resouceId == mCard.backImageResourceId) {
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
		}
	}

	@Override
	public View makeView() {
		ImageView imageView = new ImageView(this);
		imageView.setBackgroundColor(0xFF000000);
		imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
		imageView.setLayoutParams(new ImageSwitcher.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
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
		// ギャラリーの再読み込み
		BackImageDao backImageDao = new BackImageDao(this.getResources());
		List<BackImageEntity> list = backImageDao.queryForAll();
		MyListArrayAdapter adapter = (MyListArrayAdapter) mGallery.getAdapter();
		adapter.setData(list);
		adapter.notifyDataSetChanged();
	}

	/**
	 * 文字色の設定
	 */
	final ColorPickerDialog.DialogCallback mTextColorOnDialgOk = new ColorPickerDialog.DialogCallback() {
		@Override
		public void onDialogOk(int color) {
			mTextColor.setBackgroundColor(color);
			mTextColor.setTag(color);
		}
	};

	/**
	 * 文字色のクリック
	 */
	@Click(R.id.textColor)
	void textColorClicked() {
		ColorPickerDialog dialog = new ColorPickerDialog(MakeCardActivity.this);
		dialog.setDialogOkClickListener(mTextColorOnDialgOk);
		dialog.show();
	}

	/**
	 * 影の色の設定
	 */
	final ColorPickerDialog.DialogCallback mShadowColorOnDialgOk = new ColorPickerDialog.DialogCallback() {
		@Override
		public void onDialogOk(int color) {
			mShadowColor.setBackgroundColor(color);
			mShadowColor.setTag(color);
		}
	};

	/**
	 * 影の色のクリック
	 */
	@Click(R.id.shadowColor)
	void shadowColorClicked() {
		ColorPickerDialog dialog = new ColorPickerDialog(MakeCardActivity.this);
		dialog.setDialogOkClickListener(mShadowColorOnDialgOk);
		dialog.show();
	}

	private CardEntity getInputCard() {
		CardEntity card = new CardEntity();
		card.id = (mCard == null) ? 0 : mCard.id;
		card.affirmationText = mAffirmationText.getText().toString();
		BackImageEntity backImage = (BackImageEntity) mGallery.getSelectedItem();
		card.backImageResourceId = backImage.resouceId;
		card.backImagePath = backImage.bitmapPath;
		card.textColor = (Integer) mTextColor.getTag();
		card.shadowColor = (Integer) mShadowColor.getTag();
		card.textSize = Integer.parseInt(mTextSize.getText().toString());
		card.marginTop = Integer.parseInt(mMarginTop.getText().toString());
		card.marginLeft = Integer.parseInt(mMarginLeft.getText().toString());

		return card;
	}

	/**
	 * プレビューボタンのクリックリスナー
	 */
	@Click(R.id.previewButton)
	void previewButtonClicked() {
		Intent myIntent = new Intent(getApplicationContext(), CardPreviewActivity_.class);
		CardEntity card = getInputCard();
		myIntent.putExtra("Card", card);
		myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(myIntent);
	}

	/**
	 * OKボタンのクリックリスナー
	 */
	@Click(R.id.okButton)
	void okButtonClicked() {
		Log.d("taoka", "MakeCardActivity#mOkButtonClick#onClick:bigin");
		try {
			Dao<CardEntity, Integer> cardDao = mHelper.getDao(CardEntity.class);
			CardEntity cardEntity = getInputCard();
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

	/**
	 * Cancelボタンのクリックリスナー
	 */
	@Click(R.id.cancelButton)
	void cancelButtonClicked() {
		finish();

		Intent intent = new Intent(getApplication(), MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	/**
	 * 背景画像の追加ボタンのクリックリスナー
	 */
	@Click(R.id.addBackImgButton)
	void addBackImgButtonClicked() {
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");
		startActivityForResult(intent, INTENT_REQUEST_PICTURE);
	}

	/**
	 * 背景画像の削除ボタンのクリックリスナー
	 */
	@Click(R.id.delBackImgButton)
	void delBackImgButtonClicked() {
		AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
		builder.setMessage("削除します。よろしいですか？")
				.setCancelable(false)
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						BackImageEntity backImage = (BackImageEntity) mGallery.getSelectedItem();
						File file = new File(backImage.bitmapPath);
						file.delete();
						// ギャラリーの再読み込み
						BackImageDao backImageDao = new BackImageDao(mActivity.getResources());
						List<BackImageEntity> list = backImageDao.queryForAll();
						mGallery.setSelection(0);
						MyListArrayAdapter adapter = (MyListArrayAdapter) mGallery.getAdapter();
						adapter.setData(list);
						adapter.notifyDataSetChanged();
					}
				})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

	@SeekBarProgressChange(R.id.textSizeBar)
	void textSizeChanged(SeekBar seekBar, int progress) {
		mTextSize.setText(String.valueOf(progress + TEXT_SIZE_MIN));
	}

	@SeekBarProgressChange(R.id.marginTopBar)
	void marginTopChanged(SeekBar seekBar, int progress) {
		mMarginTop.setText(String.valueOf(progress));
	}

	@SeekBarProgressChange(R.id.marginLeftBar)
	void marginLeftChanged(SeekBar seekBar, int progress) {
		mMarginLeft.setText(String.valueOf(progress));
	}

	@ItemSelect
	void backImgGalleryItemSelected(boolean selected, BackImageEntity backImage) {
		if (backImage.resouceId == 0) {
			mImageSwitcher.setImageURI(Uri.parse("file:///" + backImage.bitmapPath));
			CardDao cardDao = new CardDao(mActivity);
			if (cardDao.isUsed(backImage.bitmapPath)) {
				mDelBackImgButton.setVisibility(View.GONE);
			} else {
				mDelBackImgButton.setVisibility(View.VISIBLE);
			}
		} else {
			mImageSwitcher.setImageResource(backImage.resouceId);
			mDelBackImgButton.setVisibility(View.GONE);
		}
	}

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

		public void setData(List<BackImageEntity> items) {
			if (mItems != null) {
				mItems = null;
			}
			mItems = items;
		}
	}
}
