package com.yojiokisoft.yumekanow.activity;

import android.app.Activity;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.yojiokisoft.yumekanow.R;
import com.yojiokisoft.yumekanow.db.DatabaseHelper;
import com.yojiokisoft.yumekanow.model.SettingDao;

@EActivity(R.layout.activity_init)
public class InitActivity extends Activity {
	@AfterViews
	public void initActiviy() {
		DatabaseHelper.getInstance(this);
		SettingDao.getInstance(this);
	}
}
