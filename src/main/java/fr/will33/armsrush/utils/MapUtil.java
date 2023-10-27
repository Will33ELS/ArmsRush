package fr.will33.armsrush.utils;

import java.util.*;

public class MapUtil {

    /**
     * Sorts map in descending order
     *
     * @param map
     * @return
     */
    public static <Key, Value extends Comparable<? super Value>> LinkedHashMap<Key, Value> sortByValue(Map<Key, Value> map) {
        List<Map.Entry<Key, Value>> list = new LinkedList<Map.Entry<Key, Value>>(map.entrySet());
        Collections.sort(list, (o1, o2) -> (o2.getValue()).compareTo(o1.getValue()));

        LinkedHashMap<Key, Value> result = new LinkedHashMap<Key, Value>();
        for (Map.Entry<Key, Value> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
}