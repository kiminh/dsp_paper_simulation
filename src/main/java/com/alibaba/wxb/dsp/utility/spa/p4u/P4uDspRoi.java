/*
 * Copyright 2015 Alibaba.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with Alibaba.com.
 */

package com.alibaba.wxb.dsp.utility.spa.p4u;

import com.alibaba.wxb.dsp.entity.Relation;
import com.alibaba.wxb.dsp.utility.spa.SpaFunctionFactory;

/**
 * @author huahui.lhh
 */
public class P4uDspRoi extends SpaFunctionFactory {

    private final Double roi;

    public P4uDspRoi(Double roi) {
        this.roi = roi;
    }

    protected Double getPhi1(Relation relation) {
        return 0D;
    }

    protected Double getPhi2(Relation relation) {
        return roi - (1D + relation.getAd().getCr());
    }
}
