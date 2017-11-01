/*
 * Copyright 2015 Alibaba.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with Alibaba.com.
 */

package com.alibaba.wxb.dsp.solver.dual;

import java.util.Map;

import com.alibaba.wxb.dsp.function.Function;
import com.alibaba.wxb.dsp.utils.Vec;

/**
 * @author huahui.lhh
 */
public interface FunctionCombiner<T extends Function> {

    public T createScoreFunction(T v, Map<Long, T> wMap, Vec<Long> alphas);

    public Vec<Long> calculateGradients(T v, Map<Long, T> wMap, Vec<Long> alphas);
}
