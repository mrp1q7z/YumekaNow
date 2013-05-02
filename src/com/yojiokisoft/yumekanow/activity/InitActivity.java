package com.yojiokisoft.yumekanow.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.yojiokisoft.yumekanow.R;
import com.yojiokisoft.yumekanow.R.layout;
import com.yojiokisoft.yumekanow.R.menu;
import com.yojiokisoft.yumekanow.db.DatabaseHelper;

public class InitActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_init);
		
		DatabaseHelper.getInstance(this);
		Log.d("taoak", "InitActivity.onCreate");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_init, menu);
		return true;
	}

}
