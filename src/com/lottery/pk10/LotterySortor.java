package com.lottery.pk10;

import java.util.Comparator;

public class LotterySortor implements Comparator{

	private boolean isAsc=true;

	/**
	 * constructor
	 * @param isAsc true:升序 false:降序
	 */
	public LotterySortor(boolean isAsc) {
		this.isAsc = isAsc;
	}

	@Override
	public int compare(Object o1, Object o2) {
		Lottery lot1=(Lottery)o1;
		Lottery lot2=(Lottery)o2;
		return isAsc?lot1.getNumber().compareTo(lot2.getNumber()):
				lot2.getNumber().compareTo(lot1.getNumber());
	}
}

