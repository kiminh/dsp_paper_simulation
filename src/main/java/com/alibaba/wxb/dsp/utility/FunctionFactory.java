/*
 * Copyright 2015 Alibaba.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with Alibaba.com.
 */

package com.alibaba.wxb.dsp.utility;

import com.alibaba.wxb.dsp.entity.Relation;
import com.alibaba.wxb.dsp.function.Function;

/**
 * @author huahui.lhh
 */
public interface FunctionFactory<T extends Function> {

    public T create(Relation relation);
}
