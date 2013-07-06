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

package com.yojiokisoft.yumekanow.entity;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 唱えた回数エンティティ
 */
@DatabaseTable(tableName = "counter")
public class CounterEntity implements Serializable {
	/** カードID */
	@DatabaseField(uniqueCombo = true)
	public int cardId;

	/** 処理時間（積算ミリ秒） */
	@DatabaseField(uniqueCombo = true)
	public long procTime;

	/** OKカウンタ（OKボタンを押して閉じた時＋１） */
	@DatabaseField
	public int okCnt;

	/** NGカウンタ（キャンセルボタンを押して閉じたとき＋１） */
	@DatabaseField
	public int ngCnt;

	/** 処理日(yyyymmdd) */
	@DatabaseField
	public String procDay;
}
