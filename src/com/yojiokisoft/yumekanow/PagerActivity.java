package com.yojiokisoft.yumekanow;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PagerActivity extends Activity {
	private Context mContext;
	private MyPagerAdapter mPagerAdapter;
	private ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pager);

		mContext = this;
		mPagerAdapter = new MyPagerAdapter(this);
		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		mViewPager.setAdapter(mPagerAdapter);
		mViewPager.setCurrentItem(1);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_pager, menu);
		return true;
	}

	private class MyPagerAdapter extends PagerAdapter {
		private Context mContext = null;
		private LayoutInflater mInflater = null;

		public MyPagerAdapter(Context context) {
			mContext = context;
			mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			return 3;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			TextView view = new TextView(mContext);
			view.setText("Page " + position);
			if (position == 0) {
				view.setBackgroundColor(Color.RED);
			} else if (position == 1) {
				view.setBackgroundColor(Color.GREEN);
			} else {
				view.setBackgroundColor(Color.BLUE);
			}
			((ViewPager) container).addView(view, 0);
			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView((TextView) object);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == (TextView) arg1;
		}

	}

}
