/*
 * Copyright (C) 2013 YojiokiSoft
 * 
 * This program is free software; you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 3 of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this
 * program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.yojiokisoft.yumekanow;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.yojiokisoft.yumekanow.activity.MainActivity_;
import com.yojiokisoft.yumekanow.utils.MyConst;

/**
 * サービスクラス
 */
public class YumekaNowService extends Service {
	/**
	 * サービスへの通信チャネルを返す.
	 * @see Service#onBind(Intent)
	 */
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	/**
	 * サービスが最初に作成されたシステムによって呼び出される.
	 * @see Service#onCreate()
	 */
	@Override
	public void onCreate() {
		super.onCreate();
	}

	/**
	 * 使用されなくなり、削除されるときにシステムによって呼び出される.
	 * @see Service#onDestroy()
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	/**
	 * サービス開始
	 * MainActivityを起動するだけ.
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
		mainIntent.putExtra(MyConst.EN_FIRE_EVENT, "Timer");
		startActivity(mainIntent);
		
		stopSelf();

		return ret;
	}
}
