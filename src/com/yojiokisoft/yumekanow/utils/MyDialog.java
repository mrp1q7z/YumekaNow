package com.yojiokisoft.yumekanow.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.yojiokisoft.yumekanow.R;

public class MyDialog {
	public static void showDialog(Context context, String title, String message) {
		AlertDialog.Builder alert = new AlertDialog.Builder(context);
		alert.setTitle(title);
		alert.setMessage(message);
		alert.setPositiveButton(R.string.ok, null);
		alert.show();
	}

	public MyDialog(Builder builder) {
		AlertDialog.Builder alert = new AlertDialog.Builder(builder.mContext);
		if (builder.mTitle != null) {
			alert.setTitle(builder.mTitle);
		}
		if (builder.mMessage != null) {
			alert.setMessage(builder.mMessage);
		}
		if (builder.mPositiveLabel != null) {
			alert.setPositiveButton(builder.mPositiveLabel, builder.mPositiveClickListener);
		}
		if (builder.mNegativeLabel != null) {
			alert.setNegativeButton(R.string.cancel, null);
		}
		alert.show();
	}

	public static class Builder {
		private Context mContext;
		private String mTitle = null;
		private String mMessage = null;
		private String mPositiveLabel = null;
		private String mNegativeLabel = null;
		private DialogInterface.OnClickListener mPositiveClickListener = null;

		private Builder() {
			;
		}

		public static Builder newInstance(Context context) {
			Builder builder = new Builder();
			builder.mContext = context;
			return builder;
		}

		public Builder setTitle(String title) {
			this.mTitle = title;
			return this;
		}

		public Builder setMessage(String message) {
			this.mMessage = message;
			return this;
		}

		public Builder setPositiveLabel(String label) {
			this.mPositiveLabel = label;
			return this;
		}

		public Builder setNegativeLabel(String label) {
			this.mNegativeLabel = label;
			return this;
		}

		public Builder setPositiveClickListener(DialogInterface.OnClickListener listener) {
			this.mPositiveClickListener = listener;
			return this;
		}

		public MyDialog show() {
			return new MyDialog(this);
		}
	}
}
