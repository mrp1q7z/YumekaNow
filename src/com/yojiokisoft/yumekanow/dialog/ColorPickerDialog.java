package com.yojiokisoft.yumekanow.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.yojiokisoft.yumekanow.R;
import com.yojiokisoft.yumekanow.adapter.ColorPickerAdapter;

public class ColorPickerDialog extends Dialog {
	private DialogCallback mCallback = null;

	//ダイアログの終了時に呼び出されるコールバック
	public interface DialogCallback {
		public void onDialogOk(int color);
	}

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
				int color = ((ColorPickerAdapter)parent.getAdapter()).getItem(position);
				if (mCallback != null) {
					mCallback.onDialogOk(color);
				}
				ColorPickerDialog.this.dismiss();
			}
		});
	}

	public void setDialogOkClickListener(DialogCallback callback) {
		mCallback = callback;
	}
}
