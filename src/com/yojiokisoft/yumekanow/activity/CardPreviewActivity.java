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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
import com.googlecode.androidannotations.annotations.Touch;
import com.googlecode.androidannotations.annotations.ViewById;
import com.yojiokisoft.yumekanow.R;
import com.yojiokisoft.yumekanow.dialog.AmbilWarnaDialog;
import com.yojiokisoft.yumekanow.dialog.AmbilWarnaDialog.OnAmbilWarnaListener;
import com.yojiokisoft.yumekanow.entity.CardEntity;
import com.yojiokisoft.yumekanow.utils.MyConst;

/**
 * カードプレビューアクティビティ
 */
@EActivity(R.layout.activity_card_preview)
public class CardPreviewActivity extends Activity {
	private final int LONG_PRESS_TIMEOUT = ViewConfiguration.getLongPressTimeout();
	private final int FONT_SIZE_MIN = 10; // 最小のフォントサイズ
	private final int FONT_SIZE_MAX = 40; // 最大のフォントサイズ
	private final int FONT_SIZE_DELAY = 4; // フォントサイズが指定しにくいので遅延させる倍数（大きいほどゆっくりになる）

	private ScaleGestureDetector mScaleGestureDetector;

	@ViewById(R.id.affirmationText)
	/*package*/TextView mTextView;

	@ViewById(R.id.backImage)
	/*package*/ImageView mImageView;

	@Extra(MyConst.EN_CARD)
	/*package*/CardEntity mCard;

	private TextView mDragView = null; // ドラッグ対象のビュー
	private int mOldX; // ドラッグ中の移動量(x)
	private int mOldY; // ドラッグ中の移動量(y)

	private boolean mDragging = false;
	private Handler mHandler = new Handler();
	private boolean mIsLongPress = false;
	private float[] mTextSize;
	private int mTextSizeIndex = 0;
	private long mDownTime;

	/**
	 * 長押し状態を判定し、ドラッグモードを開始する.
	 * LONG_PRESS_TIMEOUTの時間を過ぎても押下されていた場合のみドラッグモードに入る
	 */
	private final Runnable mLongPressRunnable = new Runnable() {
		@Override
		public void run() {
			if (mIsLongPress) {
				mDragView.setBackgroundColor(0x77ff0000);
				mDragging = true;
				mIsLongPress = false;
			}
		}
	};

	/**
	 * アクティビティの初期化 (onCreateと同等のタイミングで呼ばれる）
	 */
	@AfterViews
	/*package*/void initActivity() {
		mTextSize = new float[FONT_SIZE_MAX - FONT_SIZE_MIN + 1];
		for (int i = FONT_SIZE_MIN; i <= FONT_SIZE_MAX; i++) {
			mTextSize[i - FONT_SIZE_MIN] = i;
		}

		mTextView.setText(mCard.affirmationText);
		mTextView.setTextColor(mCard.textColor);
		mTextView.setShadowLayer(1.5f, 1.5f, 1.5f, mCard.shadowColor);
		mTextView.setTextSize(mCard.textSize);

		MarginLayoutParams params = (MarginLayoutParams) mTextView.getLayoutParams();
		params.leftMargin = mCard.marginLeft;
		params.topMargin = mCard.marginTop;
		mTextView.setLayoutParams(params);

		if (mCard.backImageResourceId == 0) {
			mImageView.setImageURI(Uri.parse("file:///" + mCard.backImagePath));
		} else {
			mImageView.setImageResource(mCard.backImageResourceId);
		}

		mScaleGestureDetector = new ScaleGestureDetector(this, mOnScaleGestureListener);
		mDragView = mTextView;
	}

	/**
	 * タッチイベント
	 * @see Activity#onTouchEvent(MotionEvent)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		final boolean isInProgres = mScaleGestureDetector.isInProgress();
		mScaleGestureDetector.onTouchEvent(event);
		if (isInProgres || mScaleGestureDetector.isInProgress()) {
			return true;
		}
		return super.onTouchEvent(event);
	}

	/**
	 * アファーメーションの言葉をタッチ.
	 * 
	 * @param event
	 */
	@Touch(R.id.affirmationText)
	/*package*/void affirmationTextOnTouch(MotionEvent event) {
		if (mDragView == null) {
			return;
		}
		// タッチしている位置取得
		int x = (int) event.getRawX();
		int y = (int) event.getRawY();

		final int action = event.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			mIsLongPress = true;
			mHandler.postDelayed(mLongPressRunnable, LONG_PRESS_TIMEOUT);
			mDownTime = System.currentTimeMillis();
			break;
		case MotionEvent.ACTION_MOVE:
			if (x < mOldX - 5 || mOldX + 5 < x ||
					y < mOldY - 5 || mOldY + 5 < y) {
				mIsLongPress = false;
			}
			if (mDragging) {
				// 今回イベントでのView移動先の位置
				int left = mDragView.getLeft() + (x - mOldX);
				int top = mDragView.getTop() + (y - mOldY);
				// Viewを移動する
				mDragView.layout(left, top, left + mDragView.getWidth(), top + mDragView.getHeight());
			}
			break;
		case MotionEvent.ACTION_CANCEL:
			mIsLongPress = false;
			mDragging = false;
			mDragView.setBackgroundColor(0x00000000);
			mHandler.removeCallbacks(mLongPressRunnable);
			break;
		case MotionEvent.ACTION_UP:
			mIsLongPress = false;
			mDragging = false;
			mDragView.setBackgroundColor(0x00000000);
			mHandler.removeCallbacks(mLongPressRunnable);
			if (System.currentTimeMillis() - mDownTime < LONG_PRESS_TIMEOUT) {
				setTextColor();
			} else {
				mCard.marginLeft = mDragView.getLeft();
				mCard.marginTop = mDragView.getTop();
				MarginLayoutParams params = (MarginLayoutParams) mDragView.getLayoutParams();
				params.leftMargin = mCard.marginLeft;
				params.topMargin = mCard.marginTop;
				mDragView.setLayoutParams(params);
			}
			break;
		}
		// 今回のタッチ位置を保持
		mOldX = x;
		mOldY = y;
	}

	/**
	 * 文字の拡大／縮小を行う.
	 */
	private final SimpleOnScaleGestureListener mOnScaleGestureListener = new SimpleOnScaleGestureListener() {
		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			if (detector.getScaleFactor() > 1.0f) {
				mTextSizeIndex++;
				if (mTextSizeIndex >= (mTextSize.length * FONT_SIZE_DELAY)) {
					mTextSizeIndex = mTextSize.length * FONT_SIZE_DELAY - 1;
				}
			} else {
				mTextSizeIndex--;
				if (mTextSizeIndex < 0) {
					mTextSizeIndex = 0;
				}
			}
			int index = (int) (mTextSizeIndex / FONT_SIZE_DELAY);
			mDragView.setTextSize(mTextSize[index]);
			mCard.textSize = (int) mTextSize[index];
			return true;
		}
	};

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
	 * 文字の色を設定.
	 */
	private void setTextColor() {
		View view = getLayoutInflater().inflate(R.layout.dialog_set_color, null);

		final TextView textColor = (TextView) view.findViewById(R.id.textColor);
		final TextView shadowColor = (TextView) view.findViewById(R.id.shadowColor);

		// 色の設定ダイアログで文字色の設定
		textColor.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int initColor = (Integer) textColor.getTag();
				AmbilWarnaDialog dialog = new AmbilWarnaDialog(v.getContext(), initColor, new OnAmbilWarnaListener() {
					@Override
					public void onOk(AmbilWarnaDialog dialog, int color) {
						setBackAndForeColorLabel(textColor, color);
					}

					@Override
					public void onCancel(AmbilWarnaDialog dialog) {
						;
					}
				});
				dialog.show();
			}
		});

		// 色の設定ダイアログで文字の影の設定
		shadowColor.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int initColor = (Integer) shadowColor.getTag();
				AmbilWarnaDialog dialog = new AmbilWarnaDialog(v.getContext(), initColor, new OnAmbilWarnaListener() {
					@Override
					public void onOk(AmbilWarnaDialog dialog, int color) {
						setBackAndForeColorLabel(shadowColor, color);
					}

					@Override
					public void onCancel(AmbilWarnaDialog dialog) {
						;
					}
				});
				dialog.show();
			}
		});

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.color_dialog_title));
		builder.setView(view);
		builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				int color;
				if (textColor.getTag() != null) {
					color = (Integer) textColor.getTag();
					mTextView.setTextColor(color);
					mCard.textColor = color;
				}
				if (shadowColor.getTag() != null) {
					color = (Integer) shadowColor.getTag();
					mTextView.setShadowLayer(1.5f, 1.5f, 1.5f, color);
					mCard.shadowColor = color;
				}
			}
		});
		builder.setNegativeButton(getString(R.string.cancel), null);
		setBackAndForeColorLabel(textColor, mCard.textColor);
		setBackAndForeColorLabel(shadowColor, mCard.shadowColor);
		builder.show();
	}

	/**
	 * OKボタンのクリック
	 */
	@Click
	/*package*/void okButtonClicked() {
		Intent intent = new Intent(getApplicationContext(), MakeCardActivity_.class);
		intent.putExtra(MyConst.EN_CARD, mCard);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		finish();
	}
}
