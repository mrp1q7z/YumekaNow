package com.yojiokisoft.yumekanow.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.yojiokisoft.yumekanow.R;
import com.yojiokisoft.yumekanow.model.Info;

public class InfoListAdapter extends ArrayAdapter<Info> {
	private Activity activity;
	
	public InfoListAdapter(Activity activity, List<Info> Infos) {
		super(activity, R.layout.info_row, Infos);
		this.activity = activity;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		ViewWrapper wrapper = null;

		if (view == null) {
			view = activity.getLayoutInflater().inflate(R.layout.info_row, null);
			wrapper = new ViewWrapper(view);
			view.setTag(wrapper);
		} else {
			wrapper = (ViewWrapper)view.getTag();
		}
		
		Info Info = getItem(position);
		wrapper.label.setText(Info.getLabel());
		wrapper.text.setText(Info.getText());
		
		return view;
	}
	
	class ViewWrapper {
		public final TextView label;
		public final TextView text;
				
		ViewWrapper(View view){
			this.label = (TextView)view.findViewById(R.id.info_row_label);
			this.text = (TextView)view.findViewById(R.id.info_row_txt);
		}
	}
}
