package com.yojiokisoft.yumekanow;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.yojiokisoft.yumekanow.activity.MainActivity_;
import com.yojiokisoft.yumekanow.utils.MyConst;

public class YumekaNowService extends Service {
	@Override
	public IBinder onBind(Intent intent) {
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

		Intent mainIntent = new Intent(getApplicationContext(), MainActivity_.class);
		mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mainIntent.putExtra(MyConst.FIRE_EVENT, "Timer");
		startActivity(mainIntent);
		
		stopSelf();

		return ret;
	}
}
