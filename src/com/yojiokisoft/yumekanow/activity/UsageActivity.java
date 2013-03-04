package com.yojiokisoft.yumekanow.activity;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import com.yojiokisoft.yumekanow.R;

public class UsageActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_usage);
		
		WebView webView = (WebView)findViewById(R.id.webView);
		webView.loadUrl("file:///android_asset/usage.html");
	}

}
