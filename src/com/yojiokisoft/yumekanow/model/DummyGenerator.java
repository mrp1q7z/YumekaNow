package com.yojiokisoft.yumekanow.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class DummyGenerator {
	public static ArrayList<DayCnt> getDayCntList() {
		ArrayList<DayCnt> list = new ArrayList<DayCnt>();
		Random rand = new Random();
		Calendar cal = Calendar.getInstance();
		cal.set(2012, 11, 23);
		for (int i = 1; i <= 31; i++) {
			cal.add(Calendar.DATE, 1);
			DayCnt dayCnt = new DayCnt();
			dayCnt.setDay(i);
			dayCnt.setDate(cal.getTime());
			dayCnt.setOkCnt(rand.nextInt(12));
			dayCnt.setNgCnt(rand.nextInt(5));
			list.add(dayCnt);
		}
		return list;
	}
}
