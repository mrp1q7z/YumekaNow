package com.yojiokisoft.yumekanow.dialog;

import com.yojiokisoft.yumekanow.R;
import com.yojiokisoft.yumekanow.R.id;
import com.yojiokisoft.yumekanow.R.layout;
import com.yojiokisoft.yumekanow.adapter.ColorPickerAdapter;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class ColorPickerDialog extends Dialog {

	public ColorPickerDialog(Context context) {
		super(context);
		this.setTitle("ColorPickerDialog");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.color_picker);
		
		GridView gridViewColors = (GridView) findViewById(R.id.gridViewColors);
		gridViewColors.setAdapter(new ColorPickerAdapter(getContext()));
		
		// close the dialog on item click
		gridViewColors.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ColorPickerDialog.this.dismiss();
			}
		});
	}
}
