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
import java.util.Calendar;

/**
 * 日々カウンタエンティティ
 */
public class DayCntEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/** ｎ日目 */
	public int day;

	/** 日付 */
	public Calendar date;

	/** OKカウンタ */
	public int okCnt;

	/** NGカウンタ */
	public int ngCnt;

	/** 積算OKカウンタ */
	public int totalOkCnt;

	/** 積算NGカウンタ */
	public int totalNgCnt;

	/** 励ましメッセージ */
	public String encouragmentMsg;
}
