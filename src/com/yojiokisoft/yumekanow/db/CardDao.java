/*
 * Copyright (C) 2013 YojiokiSoft
 * 
 * This program is free software; you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 3 of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this
 * program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.yojiokisoft.yumekanow.db;

import java.sql.SQLException;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.yojiokisoft.yumekanow.entity.CardEntity;

/**
 * カード情報のデータアクセス
 */
public class CardDao {
	private Dao<CardEntity, Integer> mCardDao = null;

	/**
	 * コンストラクタ.
	 * 
	 * @throws SQLException
	 */
	public CardDao() throws SQLException {
		DatabaseHelper helper = DatabaseHelper.getInstance();
		mCardDao = helper.getDao(CardEntity.class);
	}

	/**
	 * 背景画像がカードで使われているかどうかを判定する.
	 * 
	 * @param backImagePath
	 * @return 使用中の場合true
	 * @throws SQLException
	 */
	public boolean isUsed(String backImagePath) throws SQLException {
		List<CardEntity> list = null;
		list = mCardDao.queryForEq("backImagePath", backImagePath);
		if (list == null || list.size() <= 0) {
			return false;
		}
		return true;
	}

	/**
	 * 全データを検索.
	 * 
	 * @return カード一覧
	 * @throws SQLException
	 */
	public List<CardEntity> queryForAll() throws SQLException {
		return mCardDao.queryForAll();
	}

	/**
	 * フィールドの値が等しいものだけを検索.
	 * 
	 * @param fieldName
	 * @param value
	 * @return
	 * @throws SQLException
	 */
	public List<CardEntity> queryForEq(String fieldName, Object value) throws SQLException {
		return mCardDao.queryForEq(fieldName, value);
	}

	/**
	 * あれば更新、なければ追加.
	 * 
	 * @param data
	 * @return 変更、挿入または更新が実行されたかどうかの行数とステータスオブジェクト
	 * @throws SQLException
	 */
	public Dao.CreateOrUpdateStatus createOrUpdate(CardEntity data) throws SQLException {
		return mCardDao.createOrUpdate(data);
	}
	
	/**
	 * カードの削除.
	 * 
	 * @param card
	 * @return 更新された行数（正常の場合は１になるはず）
	 * @throws SQLException
	 */
	public int delete(CardEntity card) throws SQLException {
		return mCardDao.delete(card);
	}
}
