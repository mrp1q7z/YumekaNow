package com.yojiokisoft.yumekanow.utils;

import android.content.Context;
import android.content.Intent;

public final class MyMail {
	public MyMail(Builder builder) {
		Intent intent = new Intent();

		intent.setAction(Intent.ACTION_SEND);
		intent.setType("message/rfc822");

		// 宛先を指定  
		if (builder.to != null) {
			intent.putExtra(Intent.EXTRA_EMAIL, builder.to);
		}
		// CCを指定  
		if (builder.cc != null) {
			intent.putExtra(Intent.EXTRA_CC, builder.cc);
		}
		// BCCを指定  
		if (builder.bcc != null) {
			intent.putExtra(Intent.EXTRA_BCC, builder.bcc);
		}
		// 件名を指定  
		if (builder.subject != null) {
			intent.putExtra(Intent.EXTRA_SUBJECT, builder.subject);
		}
		// 本文を指定  
		if (builder.body != null) {
			intent.putExtra(Intent.EXTRA_TEXT, builder.body);
		}

		// Intentを発行  
		Intent it = Intent.createChooser(intent, "Choose Email Client");
		it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		builder.mContext.startActivity(it);
	}

	public static class Builder {
		private Context mContext;
		private String[] to = null;
		private String[] cc = null;
		private String[] bcc = null;
		private String subject = null;
		private String body = null;

		private Builder() {
			;
		}

		public static Builder newInstance(Context context) {
			Builder builder = new Builder();
			builder.mContext = context;
			return builder;
		}

		public Builder setTo(String to) {
			this.to = new String[] { to };
			return this;
		}

		public Builder setCc(String cc) {
			this.cc = new String[] { cc };
			return this;
		}

		public Builder setBcc(String bcc) {
			this.bcc = new String[] { bcc };
			return this;
		}

		public Builder setSubject(String subject) {
			this.subject = subject;
			return this;
		}

		public Builder setBody(String body) {
			this.body = body;
			return this;
		}

		public MyMail send() {
			return new MyMail(this);
		}
	}
}
