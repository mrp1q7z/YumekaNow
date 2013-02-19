package com.yojiokisoft.yumekanow;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class CardActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card);
		TextView messageText = (TextView) findViewById(R.id.messageText);
		messageText.setText("すごく調子がいい、頭も冴えて、エネルギーで満ち溢れている!!!");

	}

	public void onClickButton(View view) {
		int okCnt = 0;
		if (view.getId() == R.id.okButton) {
			okCnt++;
		}
		CardActivity.this.finish();
	}
}
