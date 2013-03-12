package com.yojiokisoft.yumekanow.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.yojiokisoft.yumekanow.R;
import com.yojiokisoft.yumekanow.model.Item;

public class CardPreviewActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card_preview);

		Item item = null;
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			item = (Item) extras.getSerializable("Item");
		}
		Log.v("taoka", String.format("CardPreviewActivity: %s", item.getLabel()));

		TextView textView = (TextView) findViewById(R.id.affirmationText);
		textView.setText(item.getLabel());

		ImageView imageView = (ImageView) findViewById(R.id.backImage);
		imageView.setImageResource(item.getDrawable());
	}

}
