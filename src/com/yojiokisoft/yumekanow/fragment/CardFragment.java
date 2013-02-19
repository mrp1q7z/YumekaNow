package com.yojiokisoft.yumekanow.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yojiokisoft.yumekanow.R;

public class CardFragment extends Fragment {
	private View view;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = null;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.card_fragment, container, false);

		LinearLayout back = (LinearLayout) view.findViewById(R.id.affirmationBack);
		back.setBackgroundResource(R.drawable.image_8);
//		back.setGravity(Gravity.CENTER_VERTICAL);

		TextView text = (TextView) view.findViewById(R.id.affirmationText);
		text.setText("すごく調子がいい\n頭も冴えて\nエネルギーで満ち溢れています");

		return view;
	}

}
