/*
 * Copyright 2015 Alibaba.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with Alibaba.com.
 */

package com.alibaba.wxb.dsp.entity;

import com.alibaba.wxb.dsp.distribution.Distribution;
import com.alibaba.wxb.dsp.utils.Idable;

/**
 * @author huahui.lhh
 */
public class Impression implements Idable {

    private final Long id;
    private final Distribution distribution;

    public Impression(Long id, Distribution distribution) {
        this.id = id;
        this.distribution = distribution;
    }

    public Long getId() {
        return id;
    }

    public Distribution getDistribution() {
        return distribution;
    }
}
