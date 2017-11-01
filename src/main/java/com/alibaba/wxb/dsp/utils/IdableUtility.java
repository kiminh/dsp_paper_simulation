/*
 * Copyright 2015 Alibaba.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with Alibaba.com.
 */

package com.alibaba.wxb.dsp.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author huahui.lhh
 */
public final class IdableUtility {

    public static <T extends Idable> Map<Long, T> listToMap(List<T> list) {
        Map<Long, T> map = new HashMap<Long, T>();
        for (T idable : list) {
            map.put(idable.getId(), idable);
        }
        return map;
    }

    public static <T extends Idable> List<T> mapToList(Map<Long, T> map) {
        List<T> list = new ArrayList<T>();
        for (T idable : map.values()) {
            list.add(idable);
        }
        return list;
    }
}
