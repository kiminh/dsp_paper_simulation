/*
 * Copyright 2015 Alibaba.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with Alibaba.com.
 */

package com.alibaba.wxb.dsp.entity;

import com.alibaba.wxb.dsp.utils.Idable;

/**
 * @author huahui.lhh
 */
public class Ad implements Idable {

    private final Long id;
    private final Double parameter;

    public Ad(Long id, Double parameter) {
        this.id = id;
        this.parameter = parameter;
    }

    public Long getId() {
        return id;
    }

    public Double getCpp() {
        return parameter;
    }

    public Double getCr() {
        return parameter;
    }
}
