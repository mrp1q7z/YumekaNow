//
// DO NOT EDIT THIS FILE, IT HAS BEEN GENERATED USING AndroidAnnotations.
//


package com.yojiokisoft.yumekanow.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import com.yojiokisoft.yumekanow.R.layout;

public final class SleepFragment_
    extends SleepFragment
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
        mWakeUpTime = ((TimePicker) findViewById(com.yojiokisoft.yumekanow.R.id.wakeUpTime));
        mTimeKind = ((RadioGroup) findViewById(com.yojiokisoft.yumekanow.R.id.timeKind));
        mCurrentTimer = ((TextView) findViewById(com.yojiokisoft.yumekanow.R.id.currentTimer));
        mCancelTimerButton = ((Button) findViewById(com.yojiokisoft.yumekanow.R.id.cancelTimerButton));
        {
            View view = findViewById(com.yojiokisoft.yumekanow.R.id.setTimerButton);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        SleepFragment_.this.setTimerButtonClicked();
                    }

                }
                );
            }
        }
        {
            View view = findViewById(com.yojiokisoft.yumekanow.R.id.cancelTimerButton);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        SleepFragment_.this.cancelTimerButtonClicked();
                    }

                }
                );
            }
        }
        initFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView_ = super.onCreateView(inflater, container, savedInstanceState);
        if (contentView_ == null) {
            contentView_ = inflater.inflate(layout.fragment_sleep, container, false);
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

    public static SleepFragment_.FragmentBuilder_ builder() {
        return new SleepFragment_.FragmentBuilder_();
    }

    public static class FragmentBuilder_ {

        private Bundle args_;

        private FragmentBuilder_() {
            args_ = new Bundle();
        }

        public SleepFragment build() {
            SleepFragment_ fragment_ = new SleepFragment_();
            fragment_.setArguments(args_);
            return fragment_;
        }

    }

}
