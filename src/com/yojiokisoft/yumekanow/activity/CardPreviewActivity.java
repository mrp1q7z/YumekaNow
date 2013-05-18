package com.yojiokisoft.yumekanow.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.yojiokisoft.yumekanow.dialog.ColorPickerDialog;
import com.yojiokisoft.yumekanow.entity.CardEntity;

@EActivity(R.layout.activity_card_preview)
public class CardPreviewActivity extends Activity {
	private final int LONG_PRESS_TIMEOUT = ViewConfiguration.getLongPressTimeout();

	private ScaleGestureDetector mScaleGestureDetector;

	@ViewById(R.id.affirmationText)
	TextView mTextView;

	@ViewById(R.id.backImage)
	ImageView mImageView;

	@Extra("Card")
	CardEntity mCard;

	private LayoutInflater mInflater;
	private Context mContext;

	// ドラッグ対象のView
	private TextView mDragView = null;
	// ドラッグ中に移動量を取得するための変数
	private int mOldX;
	private int mOldY;

	private boolean mDragging = false;
	private Handler mHandler = new Handler();
	private boolean mIsLongPress = false;
	private float[] mTextSize;
	private int mTextSizeIndex = 0;
	private long mDownTime;

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

	@AfterViews
	public void initActivity() {
		mTextSize = new float[50];
		for (int i = 10; i <= mTextSize.length; i++) {
			mTextSize[i - 1] = i;
		}
		mInflater = this.getLayoutInflater();
		mContext = this;

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

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		final boolean isInProgres = mScaleGestureDetector.isInProgress();
		mScaleGestureDetector.onTouchEvent(event);
		if (isInProgres || mScaleGestureDetector.isInProgress()) {
			return true;
		}
		return super.onTouchEvent(event);
	}

	@Touch(R.id.affirmationText)
	public void affirmationTextOnTouch(MotionEvent event) {
		if (mDragView == null) {
			return;
		}
		// タッチしている位置取得
		int x = (int) event.getRawX();
		int y = (int) event.getRawY();

		final int action = event.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			Log.v("taoka", "ACTION_DOWN: x=" + x + ",y=" + y);
			mIsLongPress = true;
			mHandler.postDelayed(mLongPressRunnable, LONG_PRESS_TIMEOUT);
			mDownTime = System.currentTimeMillis();
			break;
		case MotionEvent.ACTION_MOVE:
			Log.v("taoka", "ACTION_MOVE: x=" + x + ",y=" + y);
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
			Log.v("taoka", "ACTION_CANCEL");
			mIsLongPress = false;
			mDragging = false;
			mDragView.setBackgroundColor(0x00000000);
			mHandler.removeCallbacks(mLongPressRunnable);
			break;
		case MotionEvent.ACTION_UP:
			Log.v("taoka", "ACTION_UP");
			mIsLongPress = false;
			mDragging = false;
			mDragView.setBackgroundColor(0x00000000);
			mHandler.removeCallbacks(mLongPressRunnable);
			if (System.currentTimeMillis() - mDownTime < LONG_PRESS_TIMEOUT) {
				setTextColor();
			} else {
				mCard.marginLeft = mDragView.getLeft();
				mCard.marginTop = mDragView.getTop();
			}
			break;
		}
		// 今回のタッチ位置を保持
		mOldX = x;
		mOldY = y;
	}

	// スケールジェスチャー
	private final SimpleOnScaleGestureListener mOnScaleGestureListener = new SimpleOnScaleGestureListener() {
		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			if (detector.getScaleFactor() > 1.0f) {
				mTextSizeIndex++;
				if (mTextSizeIndex >= mTextSize.length) {
					mTextSizeIndex = mTextSize.length - 1;
				}
			} else {
				mTextSizeIndex--;
				if (mTextSizeIndex < 0) {
					mTextSizeIndex = 0;
				}
			}
			mDragView.setTextSize(mTextSize[mTextSizeIndex]);
			mCard.textSize = (int) mTextSize[mTextSizeIndex];
			return true;
		}
	};

	// 文字色の設定
	private void setTextColor() {
		View view = mInflater.inflate(R.layout.dialog_set_color, null);

		final TextView textColor = (TextView) view.findViewById(R.id.textColor);
		final TextView shadowColor = (TextView) view.findViewById(R.id.shadowColor);
		final ColorPickerDialog.DialogCallback textColorOnDialgOk = new ColorPickerDialog.DialogCallback() {
			@Override
			public void onDialogOk(int color) {
				textColor.setBackgroundColor(color);
				textColor.setTag(color);
			}
		};
		final ColorPickerDialog.DialogCallback shadowColorOnDialgOk = new ColorPickerDialog.DialogCallback() {
			@Override
			public void onDialogOk(int color) {
				shadowColor.setBackgroundColor(color);
				shadowColor.setTag(color);
			}
		};

		// 色の設定ダイアログで文字色の設定
		textColor.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ColorPickerDialog dialog = new ColorPickerDialog(v.getContext());
				dialog.setDialogOkClickListener(textColorOnDialgOk);
				dialog.show();
			}
		});

		// 色の設定ダイアログで文字の影の設定
		shadowColor.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ColorPickerDialog dialog = new ColorPickerDialog(v.getContext());
				dialog.setDialogOkClickListener(shadowColorOnDialgOk);
				dialog.show();
			}
		});

		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle("色の設定");
		builder.setView(view);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
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
		builder.setNegativeButton("Cancel", null);
		textColor.setBackgroundColor(mCard.textColor);
		textColor.setTag(mCard.textColor);
		shadowColor.setBackgroundColor(mCard.shadowColor);
		shadowColor.setTag(mCard.shadowColor);
		builder.show();
	}

	@Click
	public void okButtonClicked() {
		Intent myIntent = new Intent(getApplicationContext(), MakeCardActivity_.class);
		myIntent.putExtra("Card", mCard);
		myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(myIntent);
		finish();
	}

	@Click
	public void cancelButtonClicked() {
		finish();
	}
}
