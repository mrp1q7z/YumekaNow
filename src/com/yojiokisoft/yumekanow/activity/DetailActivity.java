package com.yojiokisoft.yumekanow.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.yojiokisoft.yumekanow.R;
import com.yojiokisoft.yumekanow.fragment.DetailFragment;
import com.yojiokisoft.yumekanow.model.Item;

public class DetailActivity extends FragmentActivity {

	public final static String TAG = DetailActivity.class.getSimpleName();
	public final static String KEY_LIST = "key_list";
	public final static String KEY_POSITION = "key_position";
	
	ArrayList<Item> list;
	private List<DetailFragment> fragments;
	
	public DetailActivity() {
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_activity);
		
		Intent intent = getIntent();
		list = (ArrayList<Item>)intent.getSerializableExtra(KEY_LIST);
		
		fragments = new ArrayList<DetailFragment>();
		for (Item item : list) {
			fragments.add(new DetailFragment(item));
		}
		
		final ViewPager pager = (ViewPager)findViewById(R.id.pager);
		pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
			@Override
			public int getCount() {
				return list.size();
			}
			
			@Override
			public Fragment getItem(int position) {
//				return new DetailFragment(list.get(position));
				return fragments.get(position);
//				return new LabelListFragment(DummyGenerator.getLabelList());
			}
		});
		pager.setCurrentItem(intent.getIntExtra(KEY_POSITION, 0));

//		del by taoka
//		ActionBar actionBar = getActionBar();
//		actionBar.setDisplayHomeAsUpEnabled(true);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_tab, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return true;

//		del by taoka
//		switch (item.getItemId()) {
//		case android.R.id.home:
//			Intent intent = new Intent(this, MainActivity.class);
//			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			startActivity(intent);
//			return true;
//		default:
//			return super.onOptionsItemSelected(item);
//		}
	}
}
