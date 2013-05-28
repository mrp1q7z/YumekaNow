package com.yojiokisoft.yumekanow.mycomponent;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yojiokisoft.yumekanow.R;

public class MyProgress extends LinearLayout {

	// パーセントテキスト
	private TextView mPercentText = null;
	// プログレスバー部分のコンテナ
	private LinearLayout mProgressContainer = null;
	// スタート画像
	private ImageView mStartImageView = null;
	// プログレスバー
	private ProgressBar mProgressBar = null;
	// ゴール画像
	private ImageView mGoalImageView = null;
	// 注釈テキスト
	private TextView mDescriptionText = null;

	public MyProgress(Context context, AttributeSet attrs) {
		super(context, attrs);

		// MyProgress の属性名をすべて取得する
		TypedArray attrsArray = context.obtainStyledAttributes(attrs, R.styleable.MyProgress);
		// パーセント表示フラグ
		Boolean showPersent = attrsArray.getBoolean(R.styleable.MyProgress_show_percent, false);
		// スタート画像
		Drawable startImage = attrsArray.getDrawable(R.styleable.MyProgress_start_image);
		// ゴール画像
		Drawable goalImage = attrsArray.getDrawable(R.styleable.MyProgress_goal_image);
		// 注釈テキスト
		String description = attrsArray.getString(R.styleable.MyProgress_description);

		// レイアウトを生成
		setOrientation(HORIZONTAL);
		// スタート画像
		mStartImageView = new ImageView(context, attrs);
		mStartImageView.setImageDrawable(startImage);
		addView(mStartImageView);
		LayoutParams params = (LayoutParams) mStartImageView.getLayoutParams();
		mStartImageView.setLayoutParams(params);
		params.setMargins(5, 5, 5, 5);
		// プログレスバーのコンテナ
		mProgressContainer = new LinearLayout(context, attrs);
		mProgressContainer.setOrientation(VERTICAL);
		mProgressContainer
				.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1.0f));
		// パーセント
		mPercentText = new TextView(context, attrs);
		mPercentText.setGravity(Gravity.CENTER);
		mPercentText.setTextColor(getResources().getColor(R.color.theme_headerTextColor));
		setShowPercent(showPersent);
		mProgressContainer.addView(mPercentText);
		// プログレスバー
		mProgressBar = new ProgressBar(context, attrs, android.R.attr.progressBarStyleHorizontal);
		mProgressContainer.addView(mProgressBar);
		params = (LayoutParams) mProgressBar.getLayoutParams();
		params.setMargins(5, 0, 5, 0);
		mProgressBar.setLayoutParams(params);
		// 注釈
		mDescriptionText = new TextView(context, attrs);
		mDescriptionText.setGravity(Gravity.CENTER);
		mDescriptionText.setTextColor(getResources().getColor(R.color.theme_headerTextColor));
		setDescription(description);
		mProgressContainer.addView(mDescriptionText);
		addView(mProgressContainer);
		// ゴール画像
		mGoalImageView = new ImageView(context, attrs);
		mGoalImageView.setImageDrawable(goalImage);
		addView(mGoalImageView);
		params = (LayoutParams) mGoalImageView.getLayoutParams();
		mStartImageView.setLayoutParams(params);
		params.setMargins(5, 5, 5, 5);
	}

	public void setShowPercent(boolean showFlag) {
		if (showFlag) {
			mPercentText.setVisibility(VISIBLE);
		} else {
			mPercentText.setVisibility(GONE);
		}
	}

	public void setDescription(String description) {
		if (description != null && description.length() > 0) {
			mDescriptionText.setText(description);
			mDescriptionText.setVisibility(VISIBLE);
		} else {
			mDescriptionText.setVisibility(GONE);
		}
	}

	public void drawableDestroy() {
		Drawable drawable = mStartImageView.getDrawable();
		drawable = null;
		mStartImageView.setImageDrawable(null);

		drawable = mGoalImageView.getDrawable();
		drawable = null;
		mGoalImageView.setImageDrawable(null);
	}

	public void setMax(int max) {
		mProgressBar.setMax(max);
		setPercent();
	}

	public void setProgress(int progress) {
		mProgressBar.setProgress(progress);
		setPercent();
	}

	private void setPercent() {
		if (mProgressBar.getMax() != 0) {
			int p = mProgressBar.getProgress() * 100 / mProgressBar.getMax();
			mPercentText.setText(p + "%");
		}
	}
}
