/*
 * Copyright 2015 Alibaba.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with Alibaba.com.
 */

package com.alibaba.wxb.dsp.function;

/**
 * @author huahui.lhh
 */
public class ConstFunction implements Function {

    public final Double value;

    public ConstFunction(Double value) {
        this.value = value;
    }

    public Double evaluate(Double x) {
        return value;
    }

    public Double derivate(Double x) {
        return 0D;
    }

    public Double max() {
        return value;
    }

    public Double argmax() {
        return 0D;
    }
}
