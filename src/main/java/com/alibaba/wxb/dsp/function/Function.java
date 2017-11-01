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
public interface Function {

    public Double evaluate(Double x);

    public Double derivate(Double x);

    public Double max();

    public Double argmax();
}
