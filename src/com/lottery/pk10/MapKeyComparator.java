package com.lottery.pk10;

import java.util.Comparator;

/**
 * Created by lxzh on 2018/7/3.
 * Description: Map键排序
 */

class MapKeyComparator implements Comparator<Integer> {

    @Override
    public int compare(Integer i1, Integer i2) {

        return i1.compareTo(i2);
    }
}