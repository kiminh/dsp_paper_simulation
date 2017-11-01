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
public final class MapUtility {

    public static <K, I, O> Map<K, O> map(Map<K, I> inputMap, Mapper<I, O> mapper) {
        Map<K, O> outputMap = new HashMap<K, O>();
        for (K key : inputMap.keySet()) {
            O output = mapper.map(inputMap.get(key));
            if (output == null) {
                continue;
            }
            outputMap.put(key, output);
        }
        return outputMap;
    }

    public static interface Mapper<I, O> {

        public O map(I input);
    }
}
