package com.yojiokisoft.yumekanow.model;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.yojiokisoft.yumekanow.db.DatabaseHelper;
import com.yojiokisoft.yumekanow.entity.CardEntity;

public class CardDao {
	private Dao<CardEntity, Integer> mCardDao = null;

	private CardDao() {
	}

	public CardDao(Context context) {
		DatabaseHelper helper = DatabaseHelper.getInstance(context);
		try {
			mCardDao = helper.getDao(CardEntity.class);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean isUsed(String backImagePath) {
		List<CardEntity> list = null;
		try {
			list = mCardDao.queryForEq("backImagePath", backImagePath);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (list == null || list.size() <= 0) {
			return false;
		}
		return true;
	}
}
