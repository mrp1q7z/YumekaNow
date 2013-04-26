package com.yojiokisoft.yumekanow.model;

import java.io.Serializable;
import java.util.Calendar;

public class DayCnt implements Serializable {
	private static final long serialVersionUID = 1L;

	public int day;
	public Calendar date;
	public int okCnt;
	public int ngCnt;
	public int totalOkCnt;
	public int totalNgCnt;
}
