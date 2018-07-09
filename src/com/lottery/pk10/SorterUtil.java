package com.lottery.pk10;

import java.util.*;

/**
 * Created by lxzh on 2018/7/3.
 * Description: 排序类
 */
public class SorterUtil {
    /**
     * 使用 Map按key进行排序
     * @param map
     * @return
     */
    public static Map<Integer, Integer> sortMapByKey(Map<Integer, Integer> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }

        Map<Integer, Integer> sortMap = new TreeMap<Integer, Integer>(
                new MapKeyComparator());

        sortMap.putAll(map);

        return sortMap;
    }
    /**
     * 使用 Map按value进行排序
     * @param oriMap
     * @return
     */
    public static Map<Integer, Integer> sortMapByValue(Map<Integer, Integer> oriMap) {
        if (oriMap == null || oriMap.isEmpty()) {
            return null;
        }
        Map<Integer, Integer> sortedMap = new LinkedHashMap<Integer, Integer>();
        List<Map.Entry<Integer, Integer>> entryList = new ArrayList<Map.Entry<Integer, Integer>>(
                oriMap.entrySet());
        Collections.sort(entryList, new MapValueComparator());

        Iterator<Map.Entry<Integer, Integer>> iter = entryList.iterator();
        Map.Entry<Integer, Integer> tmpEntry = null;
        while (iter.hasNext()) {
            tmpEntry = iter.next();
            sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());
        }
        return sortedMap;
    }
}
