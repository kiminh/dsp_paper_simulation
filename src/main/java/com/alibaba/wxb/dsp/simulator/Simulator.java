/*
 * Copyright 2015 Alibaba.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with Alibaba.com.
 */

package com.alibaba.wxb.dsp.simulator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.alibaba.wxb.dsp.function.Function;
import com.alibaba.wxb.dsp.function.SpaFunction;
import com.alibaba.wxb.dsp.solver.McSolver;
import com.alibaba.wxb.dsp.solver.Solver;
import com.alibaba.wxb.dsp.solver.dual.DualSolver;
import com.alibaba.wxb.dsp.solver.dual.SpaFunctionCombiner;
import com.alibaba.wxb.dsp.utility.spa.p4p.P4pAdvertiserRoi;
import com.alibaba.wxb.dsp.utility.spa.p4p.P4pBudget;
import com.alibaba.wxb.dsp.utility.spa.p4p.P4pDspRoi;
import com.alibaba.wxb.dsp.utility.spa.p4p.P4pPerformance;
import com.alibaba.wxb.dsp.utility.spa.p4p.P4pRevenue;
import com.alibaba.wxb.dsp.utility.spa.p4u.P4uAdvertiserRoi;
import com.alibaba.wxb.dsp.utility.spa.p4u.P4uBudget;
import com.alibaba.wxb.dsp.utility.spa.p4u.P4uDspRoi;
import com.alibaba.wxb.dsp.utility.spa.p4u.P4uPerformance;
import com.alibaba.wxb.dsp.utility.spa.p4u.P4uRevenue;
import com.alibaba.wxb.dsp.utils.Vec;

/**
 * @author huahui.lhh
 */
public class Simulator<T extends Function> {

    private final Problem<T> problem;

    private Double gain;
    private Vec<Long> consumptions;
    private List<Action> actions;

    public Simulator(Problem<T> problem) {
        this.problem = problem;
    }

    public static class Summary {

        public final Double objective;
        public final Vec<Long> suplus;

        public Summary(Double objective, Vec<Long> suplus) {
            this.objective = objective;
            this.suplus = suplus;
        }
    }

    public static void main(String args[]) {
        Problem<SpaFunction> problem = createP4pRevenue();
        //Problem<SpaFunction> problem = createP4pPerformance();

        //Problem<SpaFunction> problem = createP4uRevenue();
        //Problem<SpaFunction> problem = createP4uPerformance();

        Simulator<SpaFunction> simulator = new Simulator<SpaFunction>(problem);
        Solver<SpaFunction> solver = new DualSolver<SpaFunction>(
                new SpaFunctionCombiner(), 1e-2D, 10000, 100,
                1D, 1e-8, 0.3D, 1e-8);
        simulator.simulate(solver.solve(problem), true);
    }

    private static Problem<SpaFunction> createP4pRevenue() {
        Problem<SpaFunction> problem = new Problem.Builder<SpaFunction>()
                .appendAd(1L, 1D, 1D)
                .appendAd(2L, 2D, 0.5D)
                .appendConstraint(1L, Arrays.asList(1L), new P4pBudget(), 20D)
                .appendConstraint(2L, Arrays.asList(2L), new P4pBudget(), 10D)
                .appendConstraint(3L, Arrays.asList(1L, 2L), new P4pDspRoi(2D), 0D)
                .appendConstraint(4L, Arrays.asList(1L, 2L), new P4pAdvertiserRoi(0.5D), 0D)
                .appendConstraint(5L, Arrays.asList(1L), new P4pRevenue(), 10000D)
                .appendConstraint(6L, Arrays.asList(2L), new P4pRevenue(), 10000D)
                .build(200, new P4pRevenue());
        return problem;
    }

    private static Problem<SpaFunction> createP4pPerformance() {
        Problem<SpaFunction> problem = new Problem.Builder<SpaFunction>()
                .appendAd(1L, 1D, 1D)
                .appendAd(2L, 2D, 0.5D)
                .appendConstraint(1L, Arrays.asList(1L), new P4pBudget(), 20D)
                .appendConstraint(2L, Arrays.asList(2L), new P4pBudget(), 10D)
                .appendConstraint(3L, Arrays.asList(1L, 2L), new P4pDspRoi(2D), 0D)
                .appendConstraint(4L, Arrays.asList(1L, 2L), new P4pAdvertiserRoi(0.5D), 0D)
                .appendConstraint(5L, Arrays.asList(1L), new P4pPerformance(), 10000D)
                .appendConstraint(6L, Arrays.asList(2L), new P4pPerformance(), 10000D)
                .build(200, new P4pPerformance());
        return problem;
    }
    //
    //private static Problem<SpaFunction> createP4uRevenue() {
    //    Problem<SpaFunction> problem = new Problem.Builder<SpaFunction>()
    //            .appendAd(0L, 1D)
    //            .appendAd(1L, 2D)
    //            .appendConstraint(1L, Arrays.asList(0L, 1L), new P4uDspRoi(3D), 0D)
    //            .appendConstraint(2L, Arrays.asList(0L), new P4uBudget(), 20D)
    //            .appendConstraint(3L, Arrays.asList(1L), new P4uBudget(), 10D)
    //            .appendConstraint(4L, Arrays.asList(0L, 1L), new P4uAdvertiserRoi(0.35D), 0D)
    //            .build(200, new P4uRevenue());
    //    return problem;
    //}
    //
    //private static Problem<SpaFunction> createP4uPerformance() {
    //    Problem<SpaFunction> problem = new Problem.Builder<SpaFunction>()
    //            .appendAd(0L, 1D)
    //            .appendAd(1L, 2D)
    //            .appendConstraint(1L, Arrays.asList(0L, 1L), new P4uDspRoi(2D), 0D)
    //            .appendConstraint(2L, Arrays.asList(0L), new P4uBudget(), 20D)
    //            .appendConstraint(3L, Arrays.asList(1L), new P4uBudget(), 10D)
    //            .appendConstraint(4L, Arrays.asList(0L, 1L), new P4uAdvertiserRoi(0.5D), 0D)
    //            .build(200, new P4uPerformance());
    //    return problem;
    //}

    public Summary simulate(Bidder<T> bidder, boolean verbose) {
        reset();

        for (Auction<T> auction : problem.auctions) {
            Action action = bidder.bid(auction);
            updateStatus(auction, action);
        }

        if (verbose) {
            System.out.println("primal_obj=" + gain);
            System.out.println("consumptions=" + consumptions);
            System.out.println("limits=" + problem.limitMap);
            System.out.println("suplus=" + problem.limitMap.minus(consumptions));
        }

        return new Summary(gain, problem.limitMap.minus(consumptions));
    }

    private void updateStatus(Auction<T> auction, Action action) {
        actions.add(action);
        if (action == null) {
            System.out.println("give upppppppppppppppppppppppppppppppp");
            return;
        }
        //System.out.println("ad=" + action.ad.getId() + ", bp=" + action.bidPrice);
        gain += auction.evaluateGain(action);
        //System.out.println("sub_consumptions=" + auction.evaluateConsumptions(action));
        consumptions = consumptions.add(auction.evaluateConsumptions(action));
    }

    private void reset() {
        gain = 0D;
        consumptions = problem.limitMap.scale(0D);
        actions = new ArrayList<Action>();
    }
}
