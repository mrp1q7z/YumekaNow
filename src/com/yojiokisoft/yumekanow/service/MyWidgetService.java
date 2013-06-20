package com.yojiokisoft.yumekanow.service;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.widget.RemoteViews;

import com.yojiokisoft.yumekanow.R;
import com.yojiokisoft.yumekanow.activity.MainActivity_;
import com.yojiokisoft.yumekanow.utils.MyConst;
import com.yojiokisoft.yumekanow.widget.MyWidget;

public class MyWidgetService extends Service {
	private static final String ACTION_MY_CLICK = "com.yojiokisoft.yumekanow.ACTION_MY_CLICK";

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	/* (non-Javadoc)
	 * @see android.app.Service#onStartCommand(android.content.Intent, int, int)
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		int ret = super.onStartCommand(intent, flags, startId);

		if (intent == null) {
			return ret;
		}

		// クリックリスナーの登録
		RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.my_widget);
		Intent clickIntent = new Intent();
		clickIntent.setAction(ACTION_MY_CLICK);
		PendingIntent pendingIntent = PendingIntent.getService(this, 0, clickIntent, 0);
		remoteViews.setOnClickPendingIntent(R.id.widgetImage, pendingIntent);
		remoteViews.setOnClickPendingIntent(R.id.widgetName, pendingIntent);

		// ウィジェットに反映
		ComponentName myWidget = new ComponentName(this, MyWidget.class);
		AppWidgetManager manager = AppWidgetManager.getInstance(this);
		manager.updateAppWidget(myWidget, remoteViews);

		if (ACTION_MY_CLICK.equals(intent.getAction())) {
			startMainActivity(null);
		} else {
			startMainActivity("Timer");
		}

		return ret;
	}

	private void startMainActivity(String fireEvent) {
		Intent intent = new Intent(getApplicationContext(), MainActivity_.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if (fireEvent != null) {
			intent.putExtra(MyConst.FIRE_EVENT, fireEvent);
		}
		startActivity(intent);
	}
}
