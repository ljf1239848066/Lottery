package com.lottery.pk10;

import java.util.Comparator;
import java.util.Map;

/**
 * Created by lxzh on 2018/7/3.
 * Description:Map值排序
 */
class MapValueComparator implements Comparator<Map.Entry<Integer, Integer>> {

    @Override
    public int compare(Map.Entry<Integer, Integer> me1, Map.Entry<Integer, Integer> me2) {
        return me1.getValue().compareTo(me2.getValue());
    }
}
