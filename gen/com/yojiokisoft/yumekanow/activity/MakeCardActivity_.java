//
// DO NOT EDIT THIS FILE, IT HAS BEEN GENERATED USING AndroidAnnotations.
//


package com.yojiokisoft.yumekanow.activity;

import java.io.Serializable;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import com.yojiokisoft.yumekanow.R.layout;
import com.yojiokisoft.yumekanow.entity.BackImageEntity;
import com.yojiokisoft.yumekanow.entity.CardEntity;

public final class MakeCardActivity_
    extends MakeCardActivity
{


    @Override
    public void onCreate(Bundle savedInstanceState) {
        init_(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_make_card);
    }

    private void init_(Bundle savedInstanceState) {
        injectExtras_();
    }

    private void afterSetContentView_() {
        mTextColor = ((TextView) findViewById(com.yojiokisoft.yumekanow.R.id.textColor));
        mMarginLeft = ((TextView) findViewById(com.yojiokisoft.yumekanow.R.id.marginLeft));
        mShadowColor = ((TextView) findViewById(com.yojiokisoft.yumekanow.R.id.shadowColor));
        mDelBackImgButton = ((Button) findViewById(com.yojiokisoft.yumekanow.R.id.delBackImgButton));
        mTextSize = ((TextView) findViewById(com.yojiokisoft.yumekanow.R.id.textSize));
        mMarginTop = ((TextView) findViewById(com.yojiokisoft.yumekanow.R.id.marginTop));
        mAffirmationText = ((EditText) findViewById(com.yojiokisoft.yumekanow.R.id.affirmationText));
        mMarginTopBar = ((SeekBar) findViewById(com.yojiokisoft.yumekanow.R.id.marginTopBar));
        mMarginLeftBar = ((SeekBar) findViewById(com.yojiokisoft.yumekanow.R.id.marginLeftBar));
        mGallery = ((Gallery) findViewById(com.yojiokisoft.yumekanow.R.id.backImgGallery));
        mTextSizeBar = ((SeekBar) findViewById(com.yojiokisoft.yumekanow.R.id.textSizeBar));
        mImageSwitcher = ((ImageSwitcher) findViewById(com.yojiokisoft.yumekanow.R.id.backImgSwitcher));
        {
            View view = findViewById(com.yojiokisoft.yumekanow.R.id.addBackImgButton);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        MakeCardActivity_.this.addBackImgButtonClicked();
                    }

                }
                );
            }
        }
        {
            View view = findViewById(com.yojiokisoft.yumekanow.R.id.shadowColor);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        MakeCardActivity_.this.shadowColorClicked();
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
                        MakeCardActivity_.this.cancelButtonClicked();
                    }

                }
                );
            }
        }
        {
            View view = findViewById(com.yojiokisoft.yumekanow.R.id.previewButton);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        MakeCardActivity_.this.previewButtonClicked();
                    }

                }
                );
            }
        }
        {
            View view = findViewById(com.yojiokisoft.yumekanow.R.id.delBackImgButton);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        MakeCardActivity_.this.delBackImgButtonClicked();
                    }

                }
                );
            }
        }
        {
            View view = findViewById(com.yojiokisoft.yumekanow.R.id.textColor);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        MakeCardActivity_.this.textColorClicked();
                    }

                }
                );
            }
        }
        {
            View view = findViewById(com.yojiokisoft.yumekanow.R.id.okButton);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        MakeCardActivity_.this.okButtonClicked();
                    }

                }
                );
            }
        }
        ((AdapterView<?> ) findViewById(com.yojiokisoft.yumekanow.R.id.backImgGallery)).setOnItemSelectedListener(new OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                backImgGalleryItemSelected(true, ((BackImageEntity) parent.getAdapter().getItem(position)));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                backImgGalleryItemSelected(false, null);
            }

        }
        );
        {
            final SeekBar view = ((SeekBar) findViewById(com.yojiokisoft.yumekanow.R.id.marginLeftBar));
            if (view!= null) {
                view.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {


                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        MakeCardActivity_.this.marginLeftChanged(seekBar, progress);
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }

                }
                );
            }
        }
        {
            final SeekBar view = ((SeekBar) findViewById(com.yojiokisoft.yumekanow.R.id.textSizeBar));
            if (view!= null) {
                view.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {


                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        MakeCardActivity_.this.textSizeChanged(seekBar, progress);
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }

                }
                );
            }
        }
        {
            final SeekBar view = ((SeekBar) findViewById(com.yojiokisoft.yumekanow.R.id.marginTopBar));
            if (view!= null) {
                view.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {


                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        MakeCardActivity_.this.marginTopChanged(seekBar, progress);
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }

                }
                );
            }
        }
        initActivity();
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

    public static MakeCardActivity_.IntentBuilder_ intent(Context context) {
        return new MakeCardActivity_.IntentBuilder_(context);
    }

    @SuppressWarnings("unchecked")
    private<T >T cast_(Object object) {
        return ((T) object);
    }

    private void injectExtras_() {
        Intent intent_ = getIntent();
        Bundle extras_ = intent_.getExtras();
        if (extras_!= null) {
            if (extras_.containsKey("Card")) {
                try {
                    mCard = cast_(extras_.get("Card"));
                } catch (ClassCastException e) {
                    Log.e("MakeCardActivity_", "Could not cast extra to expected type, the field is left to its default value", e);
                }
            }
        }
    }

    @Override
    public void setIntent(Intent newIntent) {
        super.setIntent(newIntent);
        injectExtras_();
    }

    public static class IntentBuilder_ {

        private Context context_;
        private final Intent intent_;

        public IntentBuilder_(Context context) {
            context_ = context;
            intent_ = new Intent(context, MakeCardActivity_.class);
        }

        public Intent get() {
            return intent_;
        }

        public MakeCardActivity_.IntentBuilder_ flags(int flags) {
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

        public MakeCardActivity_.IntentBuilder_ mCard(CardEntity mCard) {
            intent_.putExtra("Card", ((Serializable) mCard));
            return this;
        }

    }

}
