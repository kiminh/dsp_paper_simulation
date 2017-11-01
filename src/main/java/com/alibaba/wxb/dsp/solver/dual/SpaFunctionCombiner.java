/*
 * Copyright 2015 Alibaba.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with Alibaba.com.
 */

package com.alibaba.wxb.dsp.solver.dual;

import java.util.Map;

import com.alibaba.wxb.dsp.function.SpaFunction;
import com.alibaba.wxb.dsp.utils.MapUtility;
import com.alibaba.wxb.dsp.utils.MapUtility.Mapper;
import com.alibaba.wxb.dsp.utils.Vec;

/**
 * @author huahui.lhh
 */
public class SpaFunctionCombiner implements FunctionCombiner<SpaFunction> {

    private final Mapper<SpaFunction, Double> phi1Mapper =
            new Mapper<SpaFunction, Double>() {

                public Double map(SpaFunction input) {
                    //System.out.println("cphi1=" + input.phi1);
                    return input.phi1;
                }
            };

    private final Mapper<SpaFunction, Double> phi2Mapper =
            new Mapper<SpaFunction, Double>() {

                public Double map(SpaFunction input) {
                    //System.out.println("cphi2=" + input.phi2);
                    return input.phi2;
                }
            };

    public SpaFunction createScoreFunction(SpaFunction v,
            Map<Long, SpaFunction> wMap, Vec<Long> alphas) {
        //System.out.println(wMap.keySet());
        //System.out.println(alphas);
        Double phi1 = FunctionCombinerUtility.combine(v, wMap, alphas, phi1Mapper);
        Double phi2 = FunctionCombinerUtility.combine(v, wMap, alphas, phi2Mapper);
        return new SpaFunction(v.distribution, phi1, phi2);
    }

    public Vec<Long> calculateGradients(SpaFunction v,
            Map<Long, SpaFunction> wMap, Vec<Long> alphas) {
        final Double bidPrice = createScoreFunction(v, wMap, alphas).argmax();
        return new Vec<Long>(MapUtility.map(wMap,
                new Mapper<SpaFunction, Double>() {

                    public Double map(SpaFunction input) {
                        return input.evaluate(bidPrice);
                    }
                }));
    }
}
