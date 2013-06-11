//
// DO NOT EDIT THIS FILE, IT HAS BEEN GENERATED USING AndroidAnnotations.
//


package com.yojiokisoft.yumekanow.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import com.yojiokisoft.yumekanow.R.array;

public final class MyPreference_
    extends MyPreference
{


    @Override
    public void onCreate(Bundle savedInstanceState) {
        init_(savedInstanceState);
        super.onCreate(savedInstanceState);
    }

    private void init_(Bundle savedInstanceState) {
        Resources resources_ = this.getResources();
        mInquiryKey = resources_.getStringArray(array.inquiry_key);
        mDispIntervalVal = resources_.getStringArray(array.disp_interval_val);
        mDispIntervalKey = resources_.getStringArray(array.disp_interval_key);
        mInquiryVal = resources_.getStringArray(array.inquiry_val);
        mAnimationVal = resources_.getStringArray(array.animation_val);
        mAnimationKey = resources_.getStringArray(array.animation_key);
    }

    private void afterSetContentView_() {
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        afterSetContentView_();
    }

    @Override
    public void setContentView(View view, LayoutParams params) {
        super.setContentView(view, params);
        afterSetContentView_();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        afterSetContentView_();
    }

    public static MyPreference_.IntentBuilder_ intent(Context context) {
        return new MyPreference_.IntentBuilder_(context);
    }

    public static class IntentBuilder_ {

        private Context context_;
        private final Intent intent_;

        public IntentBuilder_(Context context) {
            context_ = context;
            intent_ = new Intent(context, MyPreference_.class);
        }

        public Intent get() {
            return intent_;
        }

        public MyPreference_.IntentBuilder_ flags(int flags) {
            intent_.setFlags(flags);
            return this;
        }

        public void start() {
            context_.startActivity(intent_);
        }

        public void startForResult(int requestCode) {
            if (context_ instanceof Activity) {
                ((Activity) context_).startActivityForResult(intent_, requestCode);
            } else {
                context_.startActivity(intent_);
            }
        }

    }

}
