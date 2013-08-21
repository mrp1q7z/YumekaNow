/*
 * Copyright (C) 2013 YojiokiSoft
 * 
 * This program is free software; you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 3 of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this
 * program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.yojiokisoft.yumekanow.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
import com.googlecode.androidannotations.annotations.SeekBarProgressChange;
import com.googlecode.androidannotations.annotations.ViewById;
import com.yojiokisoft.yumekanow.App;
import com.yojiokisoft.yumekanow.R;
import com.yojiokisoft.yumekanow.adapter.BackImagePagerAdapter;
import com.yojiokisoft.yumekanow.db.BackImageDao;
import com.yojiokisoft.yumekanow.db.CardDao;
import com.yojiokisoft.yumekanow.dialog.AmbilWarnaDialog;
import com.yojiokisoft.yumekanow.dialog.AmbilWarnaDialog.OnAmbilWarnaListener;
import com.yojiokisoft.yumekanow.entity.BackImageEntity;
import com.yojiokisoft.yumekanow.entity.CardEntity;
import com.yojiokisoft.yumekanow.exception.MyUncaughtExceptionHandler;
import com.yojiokisoft.yumekanow.mycomponent.CustomHorizontalScrollView;
import com.yojiokisoft.yumekanow.mycomponent.CustomHorizontalScrollView.IScrollStateListener;
import com.yojiokisoft.yumekanow.utils.MyConst;
import com.yojiokisoft.yumekanow.utils.MyDialog;
import com.yojiokisoft.yumekanow.utils.MyFile;
import com.yojiokisoft.yumekanow.utils.MyImage;
import com.yojiokisoft.yumekanow.utils.MyImage_;

/**
 * カードを作るアクティビティ
 */
@EActivity(R.layout.activity_make_card)
public class MakeCardActivity extends Activity {
	private final int TEXT_SIZE_MIN = 10;
	private final int INTENT_REQUEST_PICTURE = 3;

	@ViewById(R.id.backImgHScrollView)
	/*package*/CustomHorizontalScrollView mScrollView;

	@ViewById(R.id.backImgContainer)
	/*package*/LinearLayout mBackImgContainer;

	@ViewById(R.id.leftArrow)
	/*package*/ImageView mLeftArrow;

	@ViewById(R.id.rightArrow)
	/*package*/ImageView mRightArrow;

	@ViewById(R.id.backImgPager)
	/*package*/ViewPager mPager;

	@ViewById(R.id.affirmationText)
	/*package*/EditText mAffirmationText;

	@ViewById(R.id.textColor)
	/*package*/TextView mTextColor;

	@ViewById(R.id.shadowColor)
	/*package*/TextView mShadowColor;

	@ViewById(R.id.textSize)
	/*package*/TextView mTextSize;

	@ViewById(R.id.marginTop)
	/*package*/TextView mMarginTop;

	@ViewById(R.id.marginLeft)
	/*package*/TextView mMarginLeft;

	@ViewById(R.id.delBackImgButton)
	/*package*/Button mDelBackImgButton;

	@ViewById(R.id.textSizeBar)
	/*package*/SeekBar mTextSizeBar;

	@ViewById(R.id.marginTopBar)
	/*package*/SeekBar mMarginTopBar;

	@ViewById(R.id.marginLeftBar)
	/*package*/SeekBar mMarginLeftBar;

	@Extra(MyConst.EN_CARD)
	/*package*/CardEntity mCard;

	private Activity mActivity;
	private BackImageDao mBackImageDao;
	private BackImagePagerAdapter mPagerAdapter;

	/**
	 * アクティビティの初期化 (onCreateと同等のタイミングで呼ばれる）
	 */
	@AfterViews
	/*package*/void initActivity() {
		mActivity = this;
		mBackImageDao = new BackImageDao();
		List<BackImageEntity> list = mBackImageDao.queryForAll();
		setBackImageList(list);

		Pair<Integer, Integer> wh = MyImage.getScreenWidthAndHeight(this);
		int w = wh.first - 10;
		int h = (int) (w * 1.37);
		mPager.setLayoutParams(new LinearLayout.LayoutParams(w, h));
		mPagerAdapter = new BackImagePagerAdapter(this, list);
		mPager.setAdapter(mPagerAdapter);
		mPager.setOnPageChangeListener(mPagerChanged);

		mScrollView.setScrollStateListener(mBackImageScrolled);

		int color = getResources().getColor(R.color.textColor);
		setBackAndForeColorLabel(mTextColor, color);

		color = getResources().getColor(R.color.shadowColor);
		setBackAndForeColorLabel(mShadowColor, color);

		mTextSize.setText(String.valueOf(mTextSizeBar.getProgress() + TEXT_SIZE_MIN));
		mMarginTop.setText(String.valueOf(mMarginTopBar.getProgress()));
		mMarginLeft.setText(String.valueOf(mMarginLeftBar.getProgress()));

		if (mCard != null) {
			mAffirmationText.setText(mCard.affirmationText);
			setBackAndForeColorLabel(mTextColor, mCard.textColor);
			setBackAndForeColorLabel(mShadowColor, mCard.shadowColor);
			mTextSize.setText(String.valueOf(mCard.textSize));
			mTextSizeBar.setProgress(mCard.textSize - TEXT_SIZE_MIN);
			mMarginTop.setText(String.valueOf(mCard.marginTop));
			mMarginTopBar.setProgress(mCard.marginTop);
			mMarginLeft.setText(String.valueOf(mCard.marginLeft));
			mMarginLeftBar.setProgress(mCard.marginLeft);
			int position = -1;
			for (int i = 0; i < list.size(); i++) {
				if (mCard.backImageType == BackImageEntity.IT_BITMAP) {
					if (mCard.backImagePath.equals(list.get(i).bitmapPath)) {
						position = i;
						break;
					}
				} else {
					if (list.get(i).resourceName.equals(mCard.backImageResourceName)) {
						position = i;
						break;
					}
				}
			}
			setPagerCurrentItem(position);
		} else {
			setPagerCurrentItem(0);
		}
	}

	/**
	 * 水平スクロールビューがスクロールされた
	 */
	private IScrollStateListener mBackImageScrolled = new IScrollStateListener() {
		public void onScrollMostRight() {
			mRightArrow.setVisibility(View.INVISIBLE);
		}

		public void onScrollMostLeft() {
			mLeftArrow.setVisibility(View.INVISIBLE);
		}

		public void onScrollFromMostLeft() {
			mLeftArrow.setVisibility(View.VISIBLE);
		}

		public void onScrollFromMostRight() {
			mRightArrow.setVisibility(View.VISIBLE);
		}
	};

	/**
	 * 背景画像一覧の生成
	 * 
	 * @param list
	 */
	private void setBackImageList(List<BackImageEntity> list) {
		int cnt = mBackImgContainer.getChildCount();
		if (cnt > 0) {
			mBackImgContainer.removeAllViews();
		}
		int size = list.size();
		for (int i = 0; i < size; i++) {
			ImageView image = new ImageView(this);
			MyImage.setImageThum(image, list.get(i));
			image.setLayoutParams(new LinearLayout.LayoutParams(80, 120));
			image.setPadding(5, 5, 5, 5);
			image.setTag(i);
			image.setOnClickListener(mBackImageClicked);
			mBackImgContainer.addView(image);
		}
	}

	/**
	 * ギャラリーから画像を受け取る
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode != INTENT_REQUEST_PICTURE || resultCode != RESULT_OK) {
			return;
		}

		// 画像のサイズを取得
		InputStream in = null;
		BitmapFactory.Options options = null;
		try {
			in = getContentResolver().openInputStream(data.getData());
			options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(in, null, options);
		} catch (FileNotFoundException e) {
			MyDialog.Builder.newInstance(this)
					.setTitle(getString(R.string.error))
					.setMessage(getString(R.string.file_not_found))
					.show();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					MyUncaughtExceptionHandler.sendBugReport(this, e);
				}
			}
		}
		if (options == null) {
			return;
		}

		// Displayに収まるサイズで画像を取得
		Pair<Integer, Integer> size = MyImage.getScreenWidthAndHeight(this);
		int sampleSize = MyImage.calculateInSampleSize(options, size.first, size.second);
		options.inSampleSize = sampleSize;
		options.inJustDecodeBounds = false;
		Bitmap bitmap = null;
		in = null;
		try {
			in = getContentResolver().openInputStream(data.getData());
			bitmap = BitmapFactory.decodeStream(in, null, options);
		} catch (FileNotFoundException e) {
			MyDialog.Builder.newInstance(this)
					.setTitle(getString(R.string.error))
					.setMessage(getString(R.string.file_not_found))
					.show();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					MyUncaughtExceptionHandler.sendBugReport(this, e);
				}
			}
		}
		if (bitmap == null) {
			return;
		}

		//ファイル名用フォーマット  
		Date today = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.JAPANESE);
		String filename = "img_" + dateFormat.format(today) + ".jpg";
		String path = MyFile.pathCombine(MyConst.getBackImagePath(), filename);
		File file = new File(path);
		try {
			MyImage.saveImage(file, bitmap);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// サムネイル画像も作る
		MyImage_ myImage = MyImage_.getInstance_(App.getInstance().getAppContext());
		myImage.ReductionImage(path);
		// ギャラリーの再読み込み
		List<BackImageEntity> list = mBackImageDao.queryForAll();
		mPagerAdapter.setItems(list);
		mPagerAdapter.notifyDataSetChanged();
		setBackImageList(list);
	}

	/**
	 * 文字色のクリック
	 */
	@Click(R.id.textColor)
	/*package*/void textColorClicked() {
		CardEntity card = getInputCard();
		AmbilWarnaDialog dialog = new AmbilWarnaDialog(this, card.textColor, new OnAmbilWarnaListener() {
			@Override
			public void onOk(AmbilWarnaDialog dialog, int color) {
				setBackAndForeColorLabel(mTextColor, color);
			}

			@Override
			public void onCancel(AmbilWarnaDialog dialog) {
				;
			}
		});
		dialog.show();
	}

	/**
	 * 文字／文字の影のラベルをセットする.
	 * 
	 * @param textView
	 * @param backColor
	 */
	private void setBackAndForeColorLabel(TextView textView, int backColor) {
		int foreColor = backColor ^ 0xffffff;
		textView.setTextColor(foreColor);
		textView.setBackgroundColor(backColor);
		textView.setTag(backColor);

		String hex = String.format("%06x", backColor & 0x00ffffff);
		textView.setText("#" + hex);
	}

	/**
	 * 影の色のクリック
	 */
	@Click(R.id.shadowColor)
	/*package*/void shadowColorClicked() {
		CardEntity card = getInputCard();
		AmbilWarnaDialog dialog = new AmbilWarnaDialog(this, card.shadowColor, new OnAmbilWarnaListener() {
			@Override
			public void onOk(AmbilWarnaDialog dialog, int color) {
				setBackAndForeColorLabel(mShadowColor, color);
			}

			@Override
			public void onCancel(AmbilWarnaDialog dialog) {
				;
			}
		});
		dialog.show();
	}

	/**
	 * 入力されたカード情報を取得する.
	 * 
	 * @return カード情報
	 */
	private CardEntity getInputCard() {
		CardEntity card = new CardEntity();
		card.id = (mCard == null) ? 0 : mCard.id;
		card.affirmationText = mAffirmationText.getText().toString();
		BackImageEntity backImage = mPagerAdapter.getBackImage(mPager.getCurrentItem());
		card.backImageType = backImage.type;
		card.backImageResourceName = backImage.resourceName;
		card.backImagePath = backImage.bitmapPath;
		card.textColor = (Integer) mTextColor.getTag();
		card.shadowColor = (Integer) mShadowColor.getTag();
		card.textSize = Integer.parseInt(mTextSize.getText().toString());
		card.marginTop = Integer.parseInt(mMarginTop.getText().toString());
		card.marginLeft = Integer.parseInt(mMarginLeft.getText().toString());

		return card;
	}

	/**
	 * プレビューボタンのクリック
	 */
	@Click(R.id.previewButton)
	/*package*/void previewButtonClicked() {
		Intent intent = new Intent(getApplicationContext(), CardPreviewActivity_.class);
		CardEntity card = getInputCard();
		intent.putExtra(MyConst.EN_CARD, card);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

	/**
	 * OKボタンのクリック
	 */
	@Click(R.id.okButton)
	/*package*/void okButtonClicked() {
		try {
			CardDao cardDao = new CardDao();
			CardEntity cardEntity = getInputCard();
			if (cardEntity.affirmationText.length() <= 0) {
				MyDialog.Builder.newInstance(this)
						.setTitle(getString(R.string.oops))
						.setMessage(getString(R.string.enter_affirmation_words))
						.show();
				return;
			}
			cardDao.createOrUpdate(cardEntity);
		} catch (SQLException e) {
			MyUncaughtExceptionHandler.sendBugReport(this, e);
		}
		finish();

		Intent intent = new Intent(getApplication(), MainActivity_.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	/**
	 * 背景画像の追加ボタンのクリック
	 */
	@Click(R.id.addBackImgButton)
	/*package*/void addBackImgButtonClicked() {
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");
		startActivityForResult(intent, INTENT_REQUEST_PICTURE);
	}

	/**
	 * 背景画像の削除ボタンのクリック
	 */
	@Click(R.id.delBackImgButton)
	/*package*/void delBackImgButtonClicked() {
		OnClickListener delBackImg = new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				BackImageEntity backImage = mPagerAdapter.getBackImage(mPager.getCurrentItem());
				File file = new File(backImage.bitmapPath);
				file.delete();
				// ギャラリーの再読み込み
				List<BackImageEntity> list = mBackImageDao.queryForAll();
				mPagerAdapter.setItems(list);
				mPagerAdapter.notifyDataSetChanged();
				setBackImageList(list);
			}
		};

		MyDialog.Builder.newInstance(this)
				.setTitle(getString(R.string.confirm))
				.setMessage(getString(R.string.backimg_del_confirm_msg))
				.setPositiveLabel(getString(R.string.yes))
				.setPositiveClickListener(delBackImg)
				.setNegativeLabel(getString(R.string.no))
				.show();
	}

	/**
	 * 文字サイズの変更.
	 * 
	 * @param seekBar
	 * @param progress
	 */
	@SeekBarProgressChange(R.id.textSizeBar)
	/*package*/void textSizeChanged(SeekBar seekBar, int progress) {
		mTextSize.setText(String.valueOf(progress + TEXT_SIZE_MIN));
	}

	/**
	 * 文字の位置（上マージン）の変更.
	 * 
	 * @param seekBar
	 * @param progress
	 */
	@SeekBarProgressChange(R.id.marginTopBar)
	/*package*/void marginTopChanged(SeekBar seekBar, int progress) {
		mMarginTop.setText(String.valueOf(progress));
	}

	/**
	 * 文字の位置（左マージン）の変更.
	 * 
	 * @param seekBar
	 * @param progress
	 */
	@SeekBarProgressChange(R.id.marginLeftBar)
	/*package*/void marginLeftChanged(SeekBar seekBar, int progress) {
		mMarginLeft.setText(String.valueOf(progress));
	}

	/**
	 * 背景画像一覧のクリック
	 */
	private View.OnClickListener mBackImageClicked = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			int position = (Integer) v.getTag();
			setPagerCurrentItem(position);
		}
	};

	/**
	 * 背景画像のカレントアイテムをセット
	 * 
	 * @param position
	 */
	private void setPagerCurrentItem(int position) {
		if (position < 0) {
			position = 0;
		}
		mPager.setCurrentItem(position);

		BackImageEntity backImage = mPagerAdapter.getBackImage(position);
		if (backImage.type == BackImageEntity.IT_BITMAP) {
			try {
				CardDao cardDao = new CardDao();
				if (cardDao.isUsed(backImage.bitmapPath)) {
					mDelBackImgButton.setVisibility(View.GONE);
				} else {
					mDelBackImgButton.setVisibility(View.VISIBLE);
				}
			} catch (SQLException e) {
				MyUncaughtExceptionHandler.sendBugReport(mActivity, e);
			}
		} else {
			mDelBackImgButton.setVisibility(View.GONE);
		}
	}

	/**
	 * 背景画像のページャーがスワイプされた
	 */
	private SimpleOnPageChangeListener mPagerChanged = new SimpleOnPageChangeListener() {
		@Override
		public void onPageSelected(int position) {
			setPagerCurrentItem(position);
		}
	};
}
