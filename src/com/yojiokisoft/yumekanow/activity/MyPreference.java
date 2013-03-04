package com.yojiokisoft.yumekanow.activity;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.yojiokisoft.yumekanow.R;

public class MyPreference extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
	}

}
