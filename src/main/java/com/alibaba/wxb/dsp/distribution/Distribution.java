/*
 * Copyright 2015 Alibaba.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with Alibaba.com.
 */

package com.alibaba.wxb.dsp.distribution;

/**
 * @author huahui.lhh
 */
public interface Distribution {

    public Double pdf(Double x);

    public Double cdf(Double x);

    public Double exp(Double x);
}
