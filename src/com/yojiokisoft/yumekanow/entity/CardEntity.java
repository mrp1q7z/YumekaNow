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
 * カード情報エンティティ
 */
@DatabaseTable(tableName = "card")
public class CardEntity implements Serializable {
	/** カードID */
	@DatabaseField(generatedId = true)
	public int id;

	/** 背景画像のリソースID */
	@DatabaseField
	public int backImageResourceId;

	/** 背景画像のパス */
	@DatabaseField
	public String backImagePath;

	/** アファーメーションの言葉 */
	@DatabaseField
	public String affirmationText;

	/** 文字の色 */
	@DatabaseField
	public int textColor;

	/** 影の色 */
	@DatabaseField
	public int shadowColor;

	/** 文字の大きさ */
	@DatabaseField
	public int textSize;

	/** 上マージン */
	@DatabaseField
	public int marginTop;

	/** 左マージン */
	@DatabaseField
	public int marginLeft;
}
