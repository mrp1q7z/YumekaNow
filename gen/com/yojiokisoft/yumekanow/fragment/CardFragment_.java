//
// DO NOT EDIT THIS FILE, IT HAS BEEN GENERATED USING AndroidAnnotations.
//


package com.yojiokisoft.yumekanow.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.yojiokisoft.yumekanow.R.layout;

public final class CardFragment_
    extends CardFragment
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
        mBackImage = ((FrameLayout) findViewById(com.yojiokisoft.yumekanow.R.id.cardContainer));
        mAffirmationText = ((TextView) findViewById(com.yojiokisoft.yumekanow.R.id.affirmationText));
        {
            View view = findViewById(com.yojiokisoft.yumekanow.R.id.okButton);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        CardFragment_.this.okButtonClicked();
                    }

                }
                );
            }
        }
        {
            View view = findViewById(com.yojiokisoft.yumekanow.R.id.cancelButton);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        CardFragment_.this.cancelButtonClicked();
                    }

                }
                );
            }
        }
        {
            View view = findViewById(com.yojiokisoft.yumekanow.R.id.cardContainer);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        CardFragment_.this.cardContainerClicked();
                    }

                }
                );
            }
        }
        printCard();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView_ = super.onCreateView(inflater, container, savedInstanceState);
        if (contentView_ == null) {
            contentView_ = inflater.inflate(layout.fragment_card, container, false);
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

    public static CardFragment_.FragmentBuilder_ builder() {
        return new CardFragment_.FragmentBuilder_();
    }

    public static class FragmentBuilder_ {

        private Bundle args_;

        private FragmentBuilder_() {
            args_ = new Bundle();
        }

        public CardFragment build() {
            CardFragment_ fragment_ = new CardFragment_();
            fragment_.setArguments(args_);
            return fragment_;
        }

    }

}
