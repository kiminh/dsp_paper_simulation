/*
 * Copyright 2015 Alibaba.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with Alibaba.com.
 */

package com.alibaba.wxb.dsp.simulator;

import com.alibaba.wxb.dsp.function.Function;

/**
 * @author huahui.lhh
 */
public interface Bidder<T extends Function> {

    public Action bid(Auction<T> auction);
}
