package com.yojiokisoft.yumekanow.model;

import java.util.ArrayList;
import java.util.List;

import android.content.res.Resources;

import com.yojiokisoft.yumekanow.entity.BackImageEntity;
import com.yojiokisoft.yumekanow.utils.MyConst;
import com.yojiokisoft.yumekanow.utils.MyFile;

public class BackImageDao {
	private Resources mRes;

	public BackImageDao(Resources res) {
		mRes = res;
	}

	public List<BackImageEntity> queryForAll() {
		List<BackImageEntity> list = new ArrayList<BackImageEntity>();
		for (int i = 1; i <= 99; i++) {
			int resId = mRes.getIdentifier("back_img" + String.format("%1$02d", i), "drawable",
					"com.yojiokisoft.yumekanow");
			if (resId == 0) {
				break;
			}
			BackImageEntity item = new BackImageEntity();
			item.resouceId = resId;
			list.add(item);
		}

		List<String> files = MyFile.getFileList(MyConst.APP_DATA_PATH, ".jpg");
		for (String file : files) {
			BackImageEntity item = new BackImageEntity();
			item.resouceId = 0;
			item.bitmapPath = MyConst.APP_DATA_PATH + file;
			list.add(item);
		}

		return list;
	}
}
