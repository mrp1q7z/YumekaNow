package com.yojiokisoft.yumekanow.activity;

import java.sql.SQLException;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.yojiokisoft.yumekanow.R;
import com.yojiokisoft.yumekanow.db.DatabaseHelper;
import com.yojiokisoft.yumekanow.dialog.ColorPickerDialog;
import com.yojiokisoft.yumekanow.entity.CardEntity;

public class CardPreviewActivity extends Activity {
	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_MAX_OFF_PATH = 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	private final int LONG_PRESS_TIMEOUT = ViewConfiguration.getLongPressTimeout();

	private GestureDetector mGestureDetector = null;
	private ScaleGestureDetector mScaleGestureDetector = null;
	private TextView mTextView;
	private ImageView mImageView;
	private int mPosition;
	private List<CardEntity> mList;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card_preview);

		mTextSize = new float[50];
		for (int i = 10; i <= mTextSize.length; i++) {
			mTextSize[i - 1] = i;
		}
		mInflater = this.getLayoutInflater();
		mContext = this;

		Bundle extras = getIntent().getExtras();
		if (extras == null) {
			return;
		}
		CardEntity card = (CardEntity) extras.getSerializable("Card");
		mPosition = (Integer) extras.getSerializable("Position");

		mTextView = (TextView) findViewById(R.id.affirmationText);
		mTextView.setText(card.affirmationText);
		mTextView.setTextColor(card.textColor);
		mTextView.setShadowLayer(1.5f, 1.5f, 1.5f, card.shadowColor);
		mTextView.setTextSize(card.textSize);
//		mTextView.setPadding(card.marginLeft, card.marginTop, 0, 0);
		LinearLayout textContainer = (LinearLayout) findViewById(R.id.textContainer);
		textContainer.setPadding(card.marginLeft, card.marginTop, 0, 0);

		mImageView = (ImageView) findViewById(R.id.backImage);
		mImageView.setImageResource(card.backImageResourceId);

		Button okButton = (Button) findViewById(R.id.okButton);
		Button cancelButton = (Button) findViewById(R.id.cancelButton);

		// 編集画面からの遷移？
		if (mPosition == -1) {
			mScaleGestureDetector = new ScaleGestureDetector(this, mOnScaleGestureListener);
			okButton.setText("OK");
			cancelButton.setText("Cancel");
			mTextView.setOnTouchListener(mAffirmationTextOnTouch);
			mDragView = mTextView;
		} else { // 一覧画面からの遷移
			mGestureDetector = new GestureDetector(this, mOnGestureListener);
			DatabaseHelper helper = new DatabaseHelper(this);
			Dao<CardEntity, Integer> cardDao;
			try {
				cardDao = helper.getDao(CardEntity.class);
				mList = cardDao.queryForAll();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			cancelButton.setOnClickListener(mEditButtonOnClick);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (mScaleGestureDetector != null) {
			final boolean isInProgres = mScaleGestureDetector.isInProgress();
			mScaleGestureDetector.onTouchEvent(event);
			if (isInProgres || mScaleGestureDetector.isInProgress()) {
				return true;
			}
		}
		if (mGestureDetector != null) {
			mGestureDetector.onTouchEvent(event);
		}
		return super.onTouchEvent(event);
	}

	private final OnTouchListener mAffirmationTextOnTouch = new OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (mDragView == null) {
				return false;
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
			case MotionEvent.ACTION_UP:
				Log.v("taoka", "ACTION_UP");
				mIsLongPress = false;
				mDragging = false;
				mDragView.setBackgroundColor(0x00000000);
				mHandler.removeCallbacks(mLongPressRunnable);
				if (System.currentTimeMillis() - mDownTime < LONG_PRESS_TIMEOUT) {
					setTextColor();
				}
				break;
			}
			// 今回のタッチ位置を保持
			mOldX = x;
			mOldY = y;
			// イベント処理完了
			return true;
		}
	};

	private final SimpleOnGestureListener mOnGestureListener = new SimpleOnGestureListener() {

		@Override
		public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
			if (Math.abs(event1.getX() - event2.getX()) > SWIPE_MAX_OFF_PATH) {
				return false;
			}
			if (event1.getX() - event2.getX() > SWIPE_MIN_DISTANCE
					&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
				if (mPosition < (mList.size() - 1)) {
					mPosition++;
					CardEntity card = mList.get(mPosition);
					mTextView.setText(card.affirmationText);
					mImageView.setImageResource(card.backImageResourceId);
				} else {
					Toast.makeText(CardPreviewActivity.this, "次ページはありません", Toast.LENGTH_SHORT).show();
					return false;
				}
			} else if (event2.getX() - event1.getX() > SWIPE_MIN_DISTANCE
					&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
				if (mPosition > 0) {
					mPosition--;
					CardEntity card = mList.get(mPosition);
					mTextView.setText(card.affirmationText);
					mImageView.setImageResource(card.backImageResourceId);
				} else {
					Toast.makeText(CardPreviewActivity.this, "前ページはありません", Toast.LENGTH_SHORT).show();
					return false;
				}
			}
			return false;
		}
	};

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
			return true;
		}
	};

	// 編集ボタンクリック
	private final OnClickListener mEditButtonOnClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			CardEntity card = mList.get(mPosition);
			Intent myIntent = new Intent(getApplicationContext(), MakeCardActivity.class);
			myIntent.putExtra("Card", card);
			myIntent.putExtra("Position", mPosition);
			myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(myIntent);
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
				Log.v("taoka", "textColor:" + color);
				textColor.setBackgroundColor(color);
				textColor.setTag(color);
			}
		};
		final ColorPickerDialog.DialogCallback shadowColorOnDialgOk = new ColorPickerDialog.DialogCallback() {
			@Override
			public void onDialogOk(int color) {
				Log.v("taoka", "shadowColor:" + color);
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
				if (textColor.getTag() != null) {
					mTextView.setTextColor((Integer) textColor.getTag());
				}
				if (shadowColor.getTag() != null) {
					mTextView.setShadowLayer(1.5f, 1.5f, 1.5f, (Integer) shadowColor.getTag());
				}
			}
		});
		builder.setNegativeButton("Cancel", null);
		builder.show();
	}
}
