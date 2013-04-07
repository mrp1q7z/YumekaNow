package com.yojiokisoft.yumekanow.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.yojiokisoft.yumekanow.R;

public class CardFragment extends Fragment {
	private View view;
	private OnCardClickListener mListener;

	// Container Activity must implement this interface  
	public interface OnCardClickListener {
		public void onCardClick();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = null;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_card, container, false);

		LinearLayout back = (LinearLayout) view.findViewById(R.id.affirmationBack);
		back.setBackgroundResource(R.drawable.back_img01);
		//		back.setGravity(Gravity.CENTER_VERTICAL);

		TextView text = (TextView) view.findViewById(R.id.affirmationText);
		text.setTextColor(0xff333333);
		text.setTextSize(20.0f);
		LayoutParams params = (LayoutParams) text.getLayoutParams();
		params.setMargins(30, 120, 0, 0);
		text.setLayoutParams(params);
		text.setShadowLayer(1.5f, 1.0f, 1.0f, Color.WHITE);
		text.setText("すごく調子がいい\n頭も冴えて\nエネルギーで満ち溢れています");

		text.setOnClickListener(mAffirmationTextClick);

		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnCardClickListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement OnCardClickListener");
		}
	}

	private final View.OnClickListener mAffirmationTextClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			mListener.onCardClick();
		}
	};
}
