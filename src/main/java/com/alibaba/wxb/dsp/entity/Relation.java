/*
 * Copyright 2015 Alibaba.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with Alibaba.com.
 */

package com.alibaba.wxb.dsp.entity;

/**
 * @author huahui.lhh
 */
public class Relation {

    private final Impression impression;
    private final Ad ad;
    private final Double ppi;

    public Relation(Impression impression, Ad ad, Double ppi) {
        this.impression = impression;
        this.ad = ad;
        this.ppi = ppi;
    }

    public Impression getImpression() {
        return impression;
    }

    public Ad getAd() {
        return ad;
    }

    public Double getPpi() {
        return ppi;
    }
}
