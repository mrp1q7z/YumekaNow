package com.yojiokisoft.yumekanow.entity;

import java.io.Serializable;
import java.util.Calendar;

public class DayCntEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	public int day;
	public Calendar date;
	public int okCnt;
	public int ngCnt;
	public int totalOkCnt;
	public int totalNgCnt;
	public String encouragmentMsg;
}
