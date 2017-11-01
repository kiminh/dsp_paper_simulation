/*
 * Copyright 2015 Alibaba.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with Alibaba.com.
 */

package com.alibaba.wxb.dsp.solver.dual;

import java.util.Map;

import com.alibaba.wxb.dsp.function.ConstFunction;
import com.alibaba.wxb.dsp.utils.MapUtility;
import com.alibaba.wxb.dsp.utils.MapUtility.Mapper;
import com.alibaba.wxb.dsp.utils.Vec;

/**
 * @author huahui.lhh
 */
public class ConstFunctionCombiner implements FunctionCombiner<ConstFunction> {

    private final Mapper<ConstFunction, Double> valueMapper =
            new Mapper<ConstFunction, Double>() {

                public Double map(ConstFunction input) {
                    return input.value;
                }
            };

    public ConstFunction createScoreFunction(ConstFunction v,
            Map<Long, ConstFunction> wMap, Vec<Long> alphas) {
        Double value = FunctionCombinerUtility.combine(v, wMap, alphas, valueMapper);
        return new ConstFunction(value);
    }

    public Vec<Long> calculateGradients(ConstFunction v,
            Map<Long, ConstFunction> wMap, Vec<Long> alphas) {
        final Double bidPrice = createScoreFunction(v, wMap, alphas).argmax();
        return new Vec<Long>(MapUtility.map(wMap,
                new Mapper<ConstFunction, Double>() {

                    public Double map(ConstFunction input) {
                        return input.evaluate(bidPrice);
                    }
                }));
    }
}
