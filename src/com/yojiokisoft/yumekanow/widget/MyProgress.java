package com.yojiokisoft.yumekanow.widget;

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
	private TextView persentText = null;
	// プログレスバー部分のコンテナ
	private LinearLayout progressContainer = null;
	// スタート画像
	private ImageView startImageView = null;
	// プログレスバー
	private ProgressBar progressBar = null;
	// ゴール画像
	private ImageView goalImageView = null;
	// 注釈テキスト
	private TextView descriptionText = null;

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
		setOrientation(VERTICAL);
		// パーセント
		persentText = new TextView(context, attrs);
		persentText.setGravity(Gravity.CENTER);
		setShowPsersent(showPersent);
		persentText.setText("37%");
		addView(persentText);
		// プログレスバーのコンテナ
		progressContainer = new LinearLayout(context, attrs);
		progressContainer.setOrientation(HORIZONTAL);
		// スタート画像
		startImageView = new ImageView(context, attrs);
		startImageView.setImageDrawable(startImage);
		progressContainer.addView(startImageView);
		// プログレスバー
		progressBar = new ProgressBar(context, attrs, android.R.attr.progressBarStyleHorizontal);
		progressBar.setLayoutParams(new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1.0f));
		progressContainer.addView(progressBar);
		// ゴール画像
		goalImageView = new ImageView(context, attrs);
		goalImageView.setImageDrawable(goalImage);
		progressContainer.addView(goalImageView);
		addView(progressContainer);
		// 注釈
		descriptionText = new TextView(context, attrs);
		descriptionText.setGravity(Gravity.CENTER);
		setDescription(description);
		addView(descriptionText);
	}

	public void setShowPsersent(boolean showFlag) {
		if (showFlag) {
			persentText.setVisibility(VISIBLE);
		} else {
			persentText.setVisibility(GONE);
		}
	}

	public void setDescription(String description) {
		if (description != null && description.length() > 0) {
			descriptionText.setText(description);
			descriptionText.setVisibility(VISIBLE);
		} else {
			descriptionText.setVisibility(GONE);
		}
	}
	
	public void drawableDestroy() {
		Drawable drawable = startImageView.getDrawable();
		drawable = null;
		startImageView.setImageDrawable(null);
		
		drawable = goalImageView.getDrawable();
		drawable = null;
		goalImageView.setImageDrawable(null);
	}
}
