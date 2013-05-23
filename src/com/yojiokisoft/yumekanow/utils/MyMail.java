package com.yojiokisoft.yumekanow.utils;

import android.content.Context;
import android.content.Intent;

public class MyMail {
	private class MailParam {
		String[] to = null;
		String[] cc = null;
		String[] bcc = null;
		String subject = null;
		String body = null;
	}

	private MailParam mailParam;
	private Context mContext;

	private MyMail() {
		;
	}

	public MyMail(Context context) {
		mContext = context;
		mailParam = new MailParam();
	}

	public MyMail setTo(String to) {
		mailParam.to = new String[] { to };
		return this;
	}

	public MyMail setCc(String cc) {
		mailParam.cc = new String[] { cc };
		return this;
	}

	public MyMail setBcc(String bcc) {
		mailParam.bcc = new String[] { bcc };
		return this;
	}

	public MyMail setSubject(String subject) {
		mailParam.subject = subject;
		return this;
	}

	public MyMail setBody(String body) {
		mailParam.body = body;
		return this;
	}

	public void send() {
		Intent intent = new Intent();

		intent.setAction(Intent.ACTION_SEND);
		intent.setType("message/rfc822");

		// 宛先を指定  
		if (mailParam.to != null) {
			intent.putExtra(Intent.EXTRA_EMAIL, mailParam.to);
		}
		// CCを指定  
		if (mailParam.cc != null) {
			intent.putExtra(Intent.EXTRA_CC, mailParam.cc);
		}
		// BCCを指定  
		if (mailParam.bcc != null) {
			intent.putExtra(Intent.EXTRA_BCC, mailParam.bcc);
		}
		// 件名を指定  
		if (mailParam.subject != null) {
			intent.putExtra(Intent.EXTRA_SUBJECT, mailParam.subject);
		}
		// 本文を指定  
		if (mailParam.body != null) {
			intent.putExtra(Intent.EXTRA_TEXT, mailParam.body);
		}

		// Intentを発行  
		Intent it = Intent.createChooser(intent, "Choose Email Client");
		it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startActivity(it);
	}
}
