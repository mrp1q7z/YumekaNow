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

/**
 * カスタムプログレスバー
 */
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

	/**
	 * コンストラクタ.
	 * 
	 * @param context
	 * @param attrs
	 */
	public MyProgress(Context context) {
		super(context);
	}
	
	/**
	 * コンストラクタ.
	 * 
	 * @param context
	 * @param attrs
	 */
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

		attrsArray.recycle();
	}

	/**
	 * パーセント表示フラグのセット.
	 * 
	 * @param showFlag true=パーセントを表示する、false=しない
	 */
	public void setShowPercent(boolean showFlag) {
		if (showFlag) {
			mPercentText.setVisibility(VISIBLE);
		} else {
			mPercentText.setVisibility(GONE);
		}
	}

	/**
	 * 注釈に表示する文字をセット.
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		if (description != null && description.length() > 0) {
			mDescriptionText.setText(description);
			mDescriptionText.setVisibility(VISIBLE);
		} else {
			mDescriptionText.setVisibility(GONE);
		}
	}

	/**
	 * メモリの開放
	 */
	public void drawableDestroy() {
		Drawable drawable;

		drawable = mStartImageView.getDrawable();
		if (drawable != null) {
			drawable = null;
			mStartImageView.setImageDrawable(null);
		}

		drawable = mGoalImageView.getDrawable();
		if (drawable != null) {
			drawable = null;
			mGoalImageView.setImageDrawable(null);
		}
	}

	/**
	 * プログレスバーの上限値をセット.
	 * 
	 * @param max
	 */
	public void setMax(int max) {
		mProgressBar.setMax(max);
		setPercent();
	}

	/**
	 * プログレスバーの進行具合をセット.
	 * 
	 * @param progress 0〜getMaxの範囲
	 */
	public void setProgress(int progress) {
		mProgressBar.setProgress(progress);
		setPercent();
	}

	/**
	 * パーセントのセット
	 */
	private void setPercent() {
		if (mProgressBar.getMax() != 0) {
			int p = mProgressBar.getProgress() * 100 / mProgressBar.getMax();
			mPercentText.setText(p + "%");
		}
	}
}
