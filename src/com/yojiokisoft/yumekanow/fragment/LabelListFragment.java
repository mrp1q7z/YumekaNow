package com.yojiokisoft.yumekanow.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.ListFragment;

import com.yojiokisoft.yumekanow.adapter.LabelListAdapter;
import com.yojiokisoft.yumekanow.model.Label;

public class LabelListFragment extends ListFragment {

	private ArrayList<Label> list;
	private LabelListAdapter adapter;

	public LabelListFragment(ArrayList<Label> list) {
		this.list = list;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		setListAdapter(null);

		if (adapter == null) {
			adapter = new LabelListAdapter(getActivity(), list);
		}

		setListAdapter(adapter);
	}

}
