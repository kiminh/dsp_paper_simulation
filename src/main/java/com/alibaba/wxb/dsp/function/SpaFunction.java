/*
 * Copyright 2015 Alibaba.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with Alibaba.com.
 */

package com.alibaba.wxb.dsp.function;

import com.alibaba.wxb.dsp.distribution.Distribution;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.integration.SimpsonIntegrator;

/**
 * @author huahui.lhh
 */
public class SpaFunction implements Function {

    private static final Double EPSILON = 1e-8;

    public final Distribution distribution;
    public final Double phi1;
    public final Double phi2;

    private final SimpsonIntegrator integrator = new SimpsonIntegrator();

    public SpaFunction(Distribution distribution, Double phi1, Double phi2) {
        this.distribution = distribution;
        this.phi1 = phi1;
        this.phi2 = phi2;
    }

    public Double evaluate(final Double x) {
        //return phi1 * distribution.cdf(x) + phi2 * distribution.exp(x);

        if (x >= 1e2) {
            return phi1 + phi2 * distribution.cdf(Double.POSITIVE_INFINITY);
        } else if (x <= 0) {
            return 0D;
        }

        Double value = integrator.integrate(100000000, new UnivariateFunction() {

            public double value(double v) {
                return derivate(v);
            }
        }, 0D, x);
        return value;
    }

    public Double derivate(Double x) {
        return (phi1 + phi2 * x) * distribution.pdf(x);
    }

    public Double max() {
        return evaluate(argmax());
    }

    public Double argmax() {
        if (!neg(phi1) && !neg(phi2)) {
            return Double.POSITIVE_INFINITY;
        } else if (!pos(phi1) && !pos(phi2)) {
            return 0D;
        } else if (neg(phi1) && pos(phi2)) {
            return Double.POSITIVE_INFINITY;
        } else if (neg(phi2) && pos(phi1)) {
            return -phi1 / phi2;
        } else {
            throw new IllegalArgumentException("Invalid phi1=" + phi1 + " and phi2=" + phi2);
        }
    }

    private boolean pos(Double x) {
        return x > EPSILON;
    }

    private boolean neg(Double x) {
        return x < -EPSILON;
    }

    private boolean zero(Double x) {
        return x >= -EPSILON && x <= EPSILON;
    }
}
