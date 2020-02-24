package com.zhaoxi.Open_source_Android.common.view.addressbook;

import com.zhaoxi.Open_source_Android.common.bean.BandingAddressBean;

import java.util.Comparator;

public class PinyinComparator implements Comparator<BandingAddressBean> {

	public int compare(BandingAddressBean o1, BandingAddressBean o2) {
		if (o1.getSortLetters().equals("@") || o2.getSortLetters().equals("#")) {
			return -1;
		} else if (o1.getSortLetters().equals("#")
				|| o2.getSortLetters().equals("@")) {
			return 1;
		} else {
			return o1.getSortLetters().compareTo(o2.getSortLetters());
		}
	}

}
