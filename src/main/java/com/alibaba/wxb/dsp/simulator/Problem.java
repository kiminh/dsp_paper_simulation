/*
 * Copyright 2015 Alibaba.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with Alibaba.com.
 */

package com.alibaba.wxb.dsp.simulator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.wxb.dsp.entity.Ad;
import com.alibaba.wxb.dsp.function.Function;
import com.alibaba.wxb.dsp.utility.FunctionFactory;
import com.alibaba.wxb.dsp.utils.Vec;

/**
 * @author huahui.lhh
 */
public class Problem<T extends Function> {

    public final List<Ad> ads;
    public final List<Auction<T>> auctions;
    public final Vec<Long> limitMap;

    public Problem(List<Ad> ads, List<Auction<T>> auctions, Vec<Long> limitMap) {
        this.ads = ads;
        this.auctions = auctions;
        this.limitMap = limitMap;
    }

    public static class Builder<T extends Function> {

        private final List<Ad> ads;
        private final Map<Long, Double> maxPpiMap;

        private final Map<Long, Set<Long>> bindedAdIdsMap;
        private final Map<Long, FunctionFactory<T>> consumptionFactoryMap;
        private final Map<Long, Double> limitMap;

        public Builder() {
            ads = new ArrayList<Ad>();
            maxPpiMap = new HashMap<Long, Double>();

            bindedAdIdsMap = new HashMap<Long, Set<Long>>();
            consumptionFactoryMap = new HashMap<Long, FunctionFactory<T>>();
            limitMap = new HashMap<Long, Double>();
        }

        public Builder<T> appendAd(Long id, Double parameter, Double maxPpi) {
            ads.add(new Ad(id, parameter));
            maxPpiMap.put(id, maxPpi);
            return this;
        }

        public Builder<T> appendConstraint(Long id, List<Long> adIds,
                FunctionFactory<T> consumptionFactory, Double limit) {
            bindedAdIdsMap.put(id, new HashSet<Long>(adIds));
            consumptionFactoryMap.put(id, consumptionFactory);
            limitMap.put(id, limit);
            return this;
        }

        public Problem<T> build(int numImpressions, FunctionFactory<T> gainFactory) {
            List<Auction<T>> auctions = new ArrayList<Auction<T>>();
            for (Long impressionId = 0L; impressionId < numImpressions; impressionId += 1L) {
                auctions.add(new Auction<T>(impressionId, ads, maxPpiMap, gainFactory,
                        bindedAdIdsMap, consumptionFactoryMap));
            }
            return new Problem<T>(ads, auctions, new Vec<Long>(limitMap));
        }
    }
}
