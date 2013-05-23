package com.yojiokisoft.yumekanow.service;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import com.yojiokisoft.yumekanow.R;
import com.yojiokisoft.yumekanow.activity.MainActivity_;
import com.yojiokisoft.yumekanow.model.CounterDao;
import com.yojiokisoft.yumekanow.model.SettingDao;
import com.yojiokisoft.yumekanow.utils.MyLog;
import com.yojiokisoft.yumekanow.widget.MyWidget;

public class MyWidgetService extends Service {
	private static final String ACTION_MY_CLICK = "com.example.android.appwidget.ClickSample.ACTION_MY_CLICK";

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		Log.d("TAG", "Serviceを生成しました");
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		Log.d("TAG", "Serviceを破棄しました");
		super.onDestroy();
	}

	/* (non-Javadoc)
	 * @see android.app.Service#onStartCommand(android.content.Intent, int, int)
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		MyLog.d("onStartCommand begin");

		if (ACTION_MY_CLICK.equals(intent.getAction())) {
			startMainActivity();
		}

		setClickListner();
		setAnimation();
		startMainActivity();

		MyLog.d("onStartCommand end");
		return super.onStartCommand(intent, flags, startId);
	}

	private void setClickListner() {
		RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.my_widget);
		Intent clickIntent = new Intent();
		clickIntent.setAction(ACTION_MY_CLICK);
		PendingIntent pendingIntent = PendingIntent.getService(this, 0, clickIntent, 0);
		remoteViews.setOnClickPendingIntent(R.id.widgetImage, pendingIntent);
		remoteViews.setOnClickPendingIntent(R.id.widgetName, pendingIntent);

		AppWidgetManager manager = AppWidgetManager.getInstance(this);
		ComponentName myWidget = new ComponentName(this, MyWidget.class);
		manager.updateAppWidget(myWidget, remoteViews);
	}

	private void setAnimation() {
		RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.my_widget);
		remoteViews.setImageViewResource(R.id.widgetImage, getWidgetImageResource());
		ComponentName myWidget = new ComponentName(this, MyWidget.class);
		AppWidgetManager manager = AppWidgetManager.getInstance(this);
		manager.updateAppWidget(myWidget, remoteViews);
	}

	private void startMainActivity() {
		Intent myIntent = new Intent(getApplicationContext(), MainActivity_.class);
		myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(myIntent);
	}

	private int getWidgetImageResource() {
		CounterDao counterDao = new CounterDao(this);
		int okCnt = counterDao.getGrowLevel();
		int imageNo;
		if (okCnt < 10) {
			imageNo = 1;
		} else if (10 <= okCnt && okCnt <= 30) {
			imageNo = 2;
		} else if (31 <= okCnt && okCnt <= 55) {
			imageNo = 3;
		} else if (56 <= okCnt && okCnt <= 65) {
			imageNo = 4;
		} else if (66 <= okCnt && okCnt <= 70) {
			imageNo = 5;
		} else if (71 <= okCnt && okCnt <= 78) {
			imageNo = 6;
		} else if (79 <= okCnt && okCnt <= 85) {
			imageNo = 7;
		} else if (86 <= okCnt && okCnt <= 90) {
			imageNo = 8;
		} else if (91 <= okCnt && okCnt <= 96) {
			imageNo = 9;
		} else if (97 <= okCnt && okCnt <= 97) {
			imageNo = 10;
		} else if (98 <= okCnt && okCnt <= 99) {
			imageNo = 11;
		} else {
			imageNo = 12;
		}

		SettingDao setting = SettingDao.getInstance(getApplicationContext());
		String animation = setting.getAnimationId();
		int resId = getResources().getIdentifier(
				"ic_anim_" + animation + "_" + String.format("%1$02d", imageNo), "drawable",
				"com.yojiokisoft.yumekanow");
		return resId;
	}
}
