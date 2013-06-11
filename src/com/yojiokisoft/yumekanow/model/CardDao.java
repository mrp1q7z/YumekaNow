package com.yojiokisoft.yumekanow.model;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.yojiokisoft.yumekanow.db.DatabaseHelper;
import com.yojiokisoft.yumekanow.entity.CardEntity;

public class CardDao {
	private Dao<CardEntity, Integer> mCardDao = null;

	public CardDao(Context context) throws SQLException {
		DatabaseHelper helper = DatabaseHelper.getInstance(context);
		mCardDao = helper.getDao(CardEntity.class);
	}

	public boolean isUsed(String backImagePath) throws SQLException {
		List<CardEntity> list = null;
		list = mCardDao.queryForEq("backImagePath", backImagePath);
		if (list == null || list.size() <= 0) {
			return false;
		}
		return true;
	}

	public List<CardEntity> queryForAll() throws SQLException {
		return mCardDao.queryForAll();
	}

	public List<CardEntity> queryForEq(String fieldName, Object value) throws SQLException {
		return mCardDao.queryForEq(fieldName, value);
	}

	public Dao.CreateOrUpdateStatus createOrUpdate(CardEntity data) throws SQLException {
		return mCardDao.createOrUpdate(data);
	}
	
	public int delete(CardEntity card) throws SQLException {
		return mCardDao.delete(card);
	}
}
