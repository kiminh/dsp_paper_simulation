/*
 * Copyright 2015 Alibaba.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with Alibaba.com.
 */

package com.alibaba.wxb.dsp.simulator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.alibaba.wxb.dsp.distribution.LogNormalDistribution;
import com.alibaba.wxb.dsp.entity.Ad;
import com.alibaba.wxb.dsp.entity.Impression;
import com.alibaba.wxb.dsp.entity.Relation;
import com.alibaba.wxb.dsp.function.Function;
import com.alibaba.wxb.dsp.utility.FunctionFactory;
import com.alibaba.wxb.dsp.utils.MapUtility;
import com.alibaba.wxb.dsp.utils.MapUtility.Mapper;
import com.alibaba.wxb.dsp.utils.Vec;

/**
 * @author huahui.lhh
 */
public class Auction<T extends Function> {

    public static final Random RANDOM = new Random(0L);

    public final Impression impression;
    public final Map<Long, Relation> relations;
    public final Map<Long, T> gainFunctions;
    public final Map<Long, Map<Long, T>> consumptionFunctionsMap;

    public Auction(Long id, List<Ad> ads, Map<Long, Double> maxPpiMap,
            FunctionFactory<T> gainFactory,
            Map<Long, Set<Long>> bindedAdIdsMap,
            Map<Long, FunctionFactory<T>> consumptionFactoryMap) {
        impression = createImpression(id);

        relations = new HashMap<Long, Relation>();
        gainFunctions = new HashMap<Long, T>();
        consumptionFunctionsMap = new HashMap<Long, Map<Long, T>>();

        for (Ad ad : ads) {
            Relation relation = createRelation(impression, ad, maxPpiMap);
            relations.put(ad.getId(), relation);

            T gainFunction = createFunction(gainFactory, relation);
            gainFunctions.put(ad.getId(), gainFunction);

            Map<Long, T> consumptionFunctions = new HashMap<Long, T>();
            for (Long resourceId : consumptionFactoryMap.keySet()) {
                if (!bindedAdIdsMap.get(resourceId).contains(ad.getId())) {
                    continue;
                }

                FunctionFactory<T> consumptionFactory = consumptionFactoryMap.get(resourceId);
                T consumptionFunction = createFunction(consumptionFactory, relation);
                consumptionFunctions.put(resourceId, consumptionFunction);
            }
            consumptionFunctionsMap.put(ad.getId(), consumptionFunctions);
        }
    }

    public Double evaluateGain(Action action) {
        return getGainFunction(action.ad).evaluate(action.bidPrice);
    }

    public T getGainFunction(Ad ad) {
        return gainFunctions.get(ad.getId());
    }

    public Vec<Long> evaluateConsumptions(final Action action) {
        Map<Long, T> consumptionFunctions = getConsumptionFunctions(action.ad);
        return new Vec<Long>(MapUtility.map(consumptionFunctions, new Mapper<T, Double>() {

            public Double map(T consumptionFunction) {
                return consumptionFunction.evaluate(action.bidPrice);
            }
        }));
    }

    public Map<Long, T> getConsumptionFunctions(Ad ad) {
        return consumptionFunctionsMap.get(ad.getId());
    }

    private Impression createImpression(Long id) {
        final double mu = RANDOM.nextDouble();
        return new Impression(id, new LogNormalDistribution(mu, 0.5));
    }

    private Relation createRelation(Impression impression, Ad ad, Map<Long, Double> maxPpiMap) {
        final double ppi = maxPpiMap.get(ad.getId()) * RANDOM.nextDouble();
        return new Relation(impression, ad, ppi);
    }

    private T createFunction(FunctionFactory<T> factory, Relation relation) {
        return factory.create(relation);
    }
}
