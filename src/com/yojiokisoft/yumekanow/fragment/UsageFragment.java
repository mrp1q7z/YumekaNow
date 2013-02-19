package com.yojiokisoft.yumekanow.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.yojiokisoft.yumekanow.R;

public class UsageFragment extends Fragment {
	private View view;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = null;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.usage_fragment, container, false);

		WebView webView = (WebView) view.findViewById(R.id.webView);
		webView.loadUrl("file:///android_asset/usage.html");

		return view;
	}

}
