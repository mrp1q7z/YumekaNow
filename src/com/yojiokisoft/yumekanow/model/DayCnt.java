package com.yojiokisoft.yumekanow.model;

import java.io.Serializable;
import java.util.Date;

public class DayCnt implements Serializable {

	private static final long serialVersionUID = 1L;

	private int day;
	private Date date;
	private int okCnt;
	private int ngCnt;

	// clone
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getOkCnt() {
		return okCnt;
	}

	public void setOkCnt(int okCnt) {
		this.okCnt = okCnt;
	}

	public int getNgCnt() {
		return ngCnt;
	}

	public void setNgCnt(int ngCnt) {
		this.ngCnt = ngCnt;
	}
}
