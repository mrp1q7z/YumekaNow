package com.yojiokisoft.yumekanow.entity;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "counter")
public class CounterEntity implements Serializable {
	@DatabaseField(uniqueCombo = true)
	public int cardId; // カードID
	@DatabaseField(uniqueCombo = true)
	public long procTime; // 処理時間（積算ミリ秒）
	@DatabaseField
	public int okCnt; // OKカウンタ
	@DatabaseField
	public int ngCnt; // NGカウンタ
	@DatabaseField
	public String procDay; // 処理日(yyyymmdd)
}
