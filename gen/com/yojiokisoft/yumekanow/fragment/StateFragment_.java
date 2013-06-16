//
// DO NOT EDIT THIS FILE, IT HAS BEEN GENERATED USING AndroidAnnotations.
//


package com.yojiokisoft.yumekanow.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.yojiokisoft.yumekanow.R.layout;
import com.yojiokisoft.yumekanow.mycomponent.MyProgress;

public final class StateFragment_
    extends StateFragment
{

    private View contentView_;

    private void init_(Bundle savedInstanceState) {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        init_(savedInstanceState);
        super.onCreate(savedInstanceState);
    }

    private void afterSetContentView_() {
        mListView = ((ListView) findViewById(com.yojiokisoft.yumekanow.R.id.dayToDayList));
        mProgress = ((MyProgress) findViewById(com.yojiokisoft.yumekanow.R.id.totalProgress));
        setAdapter();
        setProgress();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView_ = super.onCreateView(inflater, container, savedInstanceState);
        if (contentView_ == null) {
            contentView_ = inflater.inflate(layout.fragment_state, container, false);
        }
        return contentView_;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        afterSetContentView_();
    }

    public View findViewById(int id) {
        if (contentView_ == null) {
            return null;
        }
        return contentView_.findViewById(id);
    }

    public static StateFragment_.FragmentBuilder_ builder() {
        return new StateFragment_.FragmentBuilder_();
    }

    public static class FragmentBuilder_ {

        private Bundle args_;

        private FragmentBuilder_() {
            args_ = new Bundle();
        }

        public StateFragment build() {
            StateFragment_ fragment_ = new StateFragment_();
            fragment_.setArguments(args_);
            return fragment_;
        }

    }

}
