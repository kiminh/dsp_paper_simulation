/*
 * Copyright 2015 Alibaba.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with Alibaba.com.
 */

package com.alibaba.wxb.dsp.distribution;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.integration.SimpsonIntegrator;
import org.apache.commons.math3.distribution.NormalDistribution;

/**
 * @author huahui.lhh
 */
public class LogNormalDistribution implements Distribution {

    private final org.apache.commons.math3.distribution.LogNormalDistribution distribution;
    private final SimpsonIntegrator integrator;
    private final UnivariateFunction function;

    private final Double coefficient;
    private final NormalDistribution denominatorDist;
    private final NormalDistribution nominatorDist;

    public LogNormalDistribution(Double mu, Double sigma) {
        this.distribution =
                new org.apache.commons.math3.distribution.LogNormalDistribution(mu, sigma);
        integrator = new SimpsonIntegrator();
        function = new UnivariateFunction() {

            public double value(double v) {
                return v * LogNormalDistribution.this.pdf(v);
            }
        };

        coefficient = Math.exp(mu + sigma * sigma / 2);
        denominatorDist = new NormalDistribution(mu + sigma * sigma, sigma);
        nominatorDist = new NormalDistribution(mu, sigma);
    }

    public static void main(String args[]) {
        double mu = 1D;
        double sigma = 1D;

        Distribution d = new LogNormalDistribution(mu, sigma);
        System.out.println(d.cdf(1000D));
        System.out.println(d.exp(1000D));
    }

    public Double pdf(Double x) {
        return distribution.density(x);
    }

    public Double cdf(Double x) {
        return distribution.cumulativeProbability(x);
    }

    public Double exp(Double x) {
        Double logX = Math.log(x);
        return coefficient * denominatorDist.cumulativeProbability(logX) /
                nominatorDist.cumulativeProbability(logX);
    }
}
