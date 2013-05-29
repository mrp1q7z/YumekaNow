package com.yojiokisoft.yumekanow.activity;

import android.app.Activity;
import android.content.Context;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.yojiokisoft.yumekanow.R;
import com.yojiokisoft.yumekanow.db.DatabaseHelper;
import com.yojiokisoft.yumekanow.model.SettingDao;

@EActivity(R.layout.activity_init)
public class InitActivity extends Activity {
	@AfterViews
	public void initActiviy() {
		Context context = getApplicationContext();
		DatabaseHelper.getInstance(context);
		SettingDao.getInstance(context);
	}
}
