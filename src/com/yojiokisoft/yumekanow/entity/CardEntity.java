package com.yojiokisoft.yumekanow.entity;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="card")
public class CardEntity implements Serializable {
	@DatabaseField(generatedId = true)
	public int id; // カードID
	@DatabaseField
	public int backImageResourceId; // 背景画像のリソースID
	@DatabaseField
	public String backImagePath; // 背景画像のパス
	@DatabaseField
	public String affirmationText; // アファーメーションの言葉
	@DatabaseField
	public int textColor; // 文字の色
	@DatabaseField
	public int shadowColor; // 影の色
	@DatabaseField
	public int textSize; // 文字の大きさ
	@DatabaseField
	public int marginTop; // 上マージン
	@DatabaseField
	public int marginLeft; // 左マージン
}
