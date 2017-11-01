/*
 * Copyright 2015 Alibaba.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with Alibaba.com.
 */

package com.alibaba.wxb.dsp.solver;

import com.alibaba.wxb.dsp.function.Function;
import com.alibaba.wxb.dsp.simulator.Bidder;
import com.alibaba.wxb.dsp.simulator.Problem;

/**
 * @author huahui.lhh
 */
public interface Solver<T extends Function> {

    public Bidder<T> solve(Problem<T> problem);
}
