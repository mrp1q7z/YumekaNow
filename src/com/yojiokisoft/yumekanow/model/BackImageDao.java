package com.yojiokisoft.yumekanow.model;

import java.util.ArrayList;
import java.util.List;

import android.content.res.Resources;

import com.yojiokisoft.yumekanow.entity.BackImageEntity;

public class BackImageDao {
	private Resources mRes;
	
	private BackImageDao() {
	}
	
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
		return list;
	}
}
