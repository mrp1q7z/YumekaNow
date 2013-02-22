package com.yojiokisoft.yumekanow;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import com.yojiokisoft.yumekanow.activity.MainActivity;

public class MyWidgetService extends Service {
	private static final String ACTION_MY_CLICK = "com.example.android.appwidget.ClickSample.ACTION_MY_CLICK";
	int okCnt = 0;

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

	@Override
	public void onStart(Intent intent, int startId) {
		Log.d("TAG", "onStart:intent=" + intent.getAction());
		super.onStart(intent, startId);

		if (ACTION_MY_CLICK.equals(intent.getAction())) {
			Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
			myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(myIntent);
		}

		RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.activity_widget);
		Intent clickIntent = new Intent();
		clickIntent.setAction(ACTION_MY_CLICK);
		PendingIntent pendingIntent = PendingIntent.getService(this, 0, clickIntent, 0);
		remoteViews.setOnClickPendingIntent(R.id.icon_id, pendingIntent);
		remoteViews.setOnClickPendingIntent(R.id.time_text_id, pendingIntent);

		AppWidgetManager manager = AppWidgetManager.getInstance(this);
		ComponentName thisWidget = new ComponentName(this, MyWidget.class);
		manager.updateAppWidget(thisWidget, remoteViews);

		Log.d("TAG", "onStart:end");
	}

	/* (non-Javadoc)
	 * @see android.app.Service#onStartCommand(android.content.Intent, int, int)
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d("TAG", "onStartCommand begin");
		okCnt++;
		RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.activity_widget);
		remoteViews.setTextViewText(R.id.time_text_id, "OK:" + okCnt);
		ComponentName thisWidget = new ComponentName(this, MyWidget.class);
		AppWidgetManager manager = AppWidgetManager.getInstance(this);
		manager.updateAppWidget(thisWidget, remoteViews);

		Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
		myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(myIntent);

		Log.d("TAG", "Serviceの処理を実行しました");//コメント
		return super.onStartCommand(intent, flags, startId);
	}
}
