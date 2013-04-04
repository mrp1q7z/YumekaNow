package com.yojiokisoft.yumekanow.dialog;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class VersionDialogPreference extends DialogPreference {
	public VersionDialogPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public VersionDialogPreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected View onCreateDialogView() {
		TextView textView = new TextView(this.getContext());
		textView.setText("Version " + getPersistedString("0.0.1"));
		textView.setPadding(10, 10, 10, 10);
		return textView;
	}
}
