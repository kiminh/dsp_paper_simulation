/*
 * Copyright 2015 Alibaba.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with Alibaba.com.
 */

package com.alibaba.wxb.dsp.utils;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.alibaba.wxb.dsp.utils.MapUtility.Mapper;

/**
 * @author huahui.lhh
 */
public final class Vec<K> {

    public static final Double DEFAULT_VALUE = 0D;

    private final Map<K, Double> map;

    public Vec() {
        this(new HashMap<K, Double>());
    }

    public Vec(Map<K, Double> map) {
        this.map = map;
    }

    public Vec(Collection<K> ids, Double initValue) {
        map = new HashMap<K, Double>();
        for (K id : ids) {
            map.put(id, initValue);
        }
    }

    public Vec<K> scale(Double scaler) {
        return emptyVec().scaleAndAdd(this, scaler);
    }

    public Vec<K> add(Vec<K> vector) {
        return scaleAndAdd(vector, 1D);
    }

    public Vec<K> minus(Vec<K> vector) {
        return scaleAndAdd(vector, -1D);
    }

    public Vec<K> copy() {
        return reg(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    public Vec<K> copy(Double value) {
        return reg(value, value);
    }

    public Vec<K> lreg(Double lb) {
        return reg(lb, Double.POSITIVE_INFINITY);
    }

    public Vec<K> ureg(Double ub) {
        return reg(Double.NEGATIVE_INFINITY, ub);
    }

    public Vec<K> reg(final Double lb, final Double ub) {
        return new Vec<K>(MapUtility.map(map, new Mapper<Double, Double>() {

            public Double map(Double input) {
                return Math.max(lb, Math.min(ub, input));
            }
        }));
    }

    public Vec<K> scaleAndAdd(Vec<K> rhs, Double scaler) {
        Vec<K> result = new Vec<K>();
        for (K id : mergeKeys(rhs)) {
            result.map.put(id, get(id) + rhs.get(id) * scaler);
        }
        return result;
    }

    public boolean allNonNegative() {
        for (K key : map.keySet()) {
            if (map.get(key) < 0D) {
                return false;
            }
        }
        return true;
    }

    public Double inner(Vec<K> rhs) {
        Double result = 0D;
        for (K id : mergeKeys(rhs)) {
            result += get(id) * rhs.get(id);
        }
        return result;
    }

    public Map<K, Double> map() {
        return map;
    }

    public Double get(K key) {
        return map.containsKey(key) ? map.get(key) : DEFAULT_VALUE;
    }

    public void assign(Vec<K> vector) {
        map.clear();
        map.putAll(vector.map);
    }

    @Override
    public String toString() {
        return map.toString();
    }

    private Set<K> mergeKeys(Vec<K> rhs) {
        Set<K> keys = new HashSet<K>();
        keys.addAll(map.keySet());
        keys.addAll(rhs.map.keySet());
        return keys;
    }

    private Vec<K> emptyVec() {
        return new Vec<K>(Collections.<K, Double>emptyMap());
    }
}
