package com.yojiokisoft.yumekanow.activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.yojiokisoft.yumekanow.R;
import com.yojiokisoft.yumekanow.service.MyWidgetService;

public class WakeUpActivity extends Activity {
	private Activity mActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wake_up);
		mActivity = this;
		Button okButton = (Button) findViewById(R.id.okButton);
		okButton.setOnClickListener(mOkButtonClick);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		return true;
	}

	private final View.OnClickListener mOkButtonClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(mActivity, MyWidgetService.class);
			PendingIntent pendingIntent = PendingIntent.getService(mActivity, 0, intent,
					PendingIntent.FLAG_UPDATE_CURRENT);
			AlarmManager alarmManager = (AlarmManager) mActivity.getSystemService(Context.ALARM_SERVICE);
			long now = System.currentTimeMillis();
			SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mActivity);
			String dispInterval = sp.getString("DISP_INTERVAL", "60");
			Log.d("taoka", "TimerActivity.mOkButtonClick : Interval=" + dispInterval);
			long interval = Integer.parseInt(dispInterval) * 60 * 1000;
			alarmManager.setRepeating(AlarmManager.RTC, now, interval, pendingIntent);

			finish();
		}
	};
}
