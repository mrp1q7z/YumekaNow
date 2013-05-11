package com.yojiokisoft.yumekanow.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ViewById;
import com.yojiokisoft.yumekanow.R;

@EFragment(R.layout.fragment_sleep)
public class SleepFragment extends Fragment {
	@ViewById(R.id.wakeUpTime)
	TimePicker mWakeUpTime;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return null;
	}

	@AfterViews
	void printWakuUpTime() {
		mWakeUpTime.setIs24HourView(true);
	}
}
