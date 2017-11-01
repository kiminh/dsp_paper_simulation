/*
 * Copyright 2015 Alibaba.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with Alibaba.com.
 */

package com.alibaba.wxb.dsp.utility.spa;

import com.alibaba.wxb.dsp.entity.Relation;
import com.alibaba.wxb.dsp.function.SpaFunction;
import com.alibaba.wxb.dsp.utility.FunctionFactory;

/**
 * @author huahui.lhh
 */
public abstract class SpaFunctionFactory implements FunctionFactory<SpaFunction> {

    public final SpaFunction create(Relation relation) {
        return new SpaFunction(relation.getImpression().getDistribution(),
                getPhi1(relation),
                getPhi2(relation));
    }

    protected abstract Double getPhi1(Relation relation);

    protected abstract Double getPhi2(Relation relation);
}
