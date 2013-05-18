package com.yojiokisoft.yumekanow.activity;

import android.app.Activity;
import android.webkit.WebView;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.yojiokisoft.yumekanow.R;

@EActivity(R.layout.activity_usage)
public class UsageActivity extends Activity {
	@AfterViews
	void initActivity() {
		WebView webView = (WebView) findViewById(R.id.webView);
		webView.loadUrl("file:///android_asset/usage.html");
	}
}
