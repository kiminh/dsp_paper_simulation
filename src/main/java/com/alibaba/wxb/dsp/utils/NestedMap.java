/*
 * Copyright 2015 Alibaba.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with Alibaba.com.
 */

package com.alibaba.wxb.dsp.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author huahui.lhh
 */
public class NestedMap<K, V> {

    private final int depth;
    private final Map<K, Object> map;

    public NestedMap(int depth) {
        this.depth = depth;
        map = new HashMap<K, Object>();
    }

    public static void main(String args[]) {
        NestedMap<Integer, Double> map = new NestedMap<Integer, Double>(2);
        map.set(1D, 1, 1);
        map.set(4D, 2, 2);
        System.out.println(map.get(1, 1));
        System.out.println(map.get(2, 2));
    }

    public Map<K, Object> sub(K... keys) {
        checkDimension("map", depth <= keys.length);
        Map<K, Object> innerMap = findInnerMap(keys.length, true, keys);
        return innerMap;
    }

    public void set(V value, K... keys) {
        checkDimension("value", depth != keys.length);
        Map<K, V> leafMap = (Map<K, V>)findInnerMap(depth - 1, false, keys);
        leafMap.put(keys[keys.length - 1], value);
    }

    public V get(K... keys) {
        checkDimension("value", depth != keys.length);
        Map<K, V> leafMap = (Map<K, V>)findInnerMap(depth - 1, true, keys);
        return leafMap.get(keys[keys.length - 1]);
    }

    private Map<K, Object> findInnerMap(int n, boolean isStrictMode, K... keys) {
        Map<K, Object> innerMap = map;
        for (int i = 0; i < n; i++) {
            if (!innerMap.containsKey(keys[i])) {
                if (isStrictMode) {
                    throw new IllegalArgumentException("invalid index");
                } else {
                    innerMap.put(keys[i], new HashMap<K, Object>());
                }
            }
            innerMap = (Map<K, Object>)innerMap.get(keys[i]);
        }
        return innerMap;
    }

    private void checkDimension(String info, boolean isInvalid) {
        if (isInvalid) {
            throw new IllegalArgumentException("invalid " + info + " dimension");
        }
    }
}
