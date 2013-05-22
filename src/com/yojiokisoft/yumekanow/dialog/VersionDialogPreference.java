package com.yojiokisoft.yumekanow.dialog;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.yojiokisoft.yumekanow.R;

public class VersionDialogPreference extends DialogPreference {
	private Context mContext;

	public VersionDialogPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
	}

	public VersionDialogPreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
	}

	@Override
	protected View onCreateDialogView() {
		String version = mContext.getResources().getString(R.string.version);

		TextView textView = new TextView(this.getContext());
		textView.setText(version);
		textView.setPadding(10, 10, 10, 10);
		return textView;
	}
}
