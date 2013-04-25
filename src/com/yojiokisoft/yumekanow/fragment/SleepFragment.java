package com.yojiokisoft.yumekanow.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

import com.yojiokisoft.yumekanow.R;

public class SleepFragment extends Fragment {
	private View view;
	private Activity mActivity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = null;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_sleep, container, false);

		TimePicker wakeUpTime = (TimePicker) view.findViewById(R.id.wakeUpTime);
		wakeUpTime.setIs24HourView(true);

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mActivity = getActivity();
	}
}
