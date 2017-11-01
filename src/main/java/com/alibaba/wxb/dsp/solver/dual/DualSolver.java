/*
 * Copyright 2015 Alibaba.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with Alibaba.com.
 */

package com.alibaba.wxb.dsp.solver.dual;

import com.alibaba.wxb.dsp.entity.Ad;
import com.alibaba.wxb.dsp.entity.Relation;
import com.alibaba.wxb.dsp.function.Function;
import com.alibaba.wxb.dsp.simulator.Action;
import com.alibaba.wxb.dsp.simulator.Auction;
import com.alibaba.wxb.dsp.simulator.Bidder;
import com.alibaba.wxb.dsp.simulator.Problem;
import com.alibaba.wxb.dsp.solver.Solver;
import com.alibaba.wxb.dsp.utils.Vec;

/**
 * @author huahui.lhh
 */
public class DualSolver<T extends Function> implements Solver<T> {

    private final FunctionCombiner<T> combiner;

    private final Double initAlpha;

    private final int numBatches;
    private final int roundsPerBatch;

    private final Double startEta;
    private final Double endEta;
    private final Double decay;
    private final Double threshold;

    public DualSolver(FunctionCombiner<T> combiner,
            Double initAlpha, int numBatches, int roundsPerBatch,
            Double startEta, Double endEta, Double decay, Double threshold) {
        this.combiner = combiner;

        this.initAlpha = initAlpha;

        this.numBatches = numBatches;
        this.roundsPerBatch = roundsPerBatch;

        this.startEta = startEta;
        this.endEta = endEta;
        this.decay = decay;
        this.threshold = threshold;
    }

    private static <T extends Function> Candidate<T> selectCandidate(
            FunctionCombiner<T> combiner, Auction<T> auction, Vec<Long> alphas) {
        Candidate<T> bestCandidate = new Candidate<T>(null,
                null, -10D);

        for (Relation relation : auction.relations.values()) {
            Ad ad = relation.getAd();
            T scoreFunction = DualSolver.createScoreFunction(
                    combiner, auction, alphas, ad);
            Double bidPrice = scoreFunction.argmax();
            Double score = scoreFunction.evaluate(bidPrice);
            Candidate<T> candidate = new Candidate<T>(
                    new Action(ad, bidPrice), scoreFunction, score);

            if (candidate.score >= bestCandidate.score) {
                bestCandidate = candidate;
            }
        }

        return bestCandidate;
    }

    private static <T extends Function> T createScoreFunction(
            FunctionCombiner<T> combiner, Auction<T> auction, Vec<Long> alphas, Ad ad) {
        return combiner.createScoreFunction(auction.getGainFunction(ad),
                auction.getConsumptionFunctions(ad), alphas);
    }

    private static <T extends Function> Vec<Long> calculateGradients(
            FunctionCombiner<T> combiner, Auction<T> auction, Vec<Long> alphas,
            Candidate<T> candidate) {
        if (candidate.action == null) {
            return new Vec<Long>();
        } else {
            return combiner.calculateGradients(auction.getGainFunction(candidate.action.ad),
                    auction.getConsumptionFunctions(candidate.action.ad), alphas);
        }
    }

    public Bidder<T> solve(Problem<T> problem) {
        Vec<Long> alphas = new Vec(problem.limitMap.map().keySet(), initAlpha);

        Vec<Long> globalGradients = problem.limitMap.scale(1D / problem.auctions.size());

        Double lastDualObjective = Double.POSITIVE_INFINITY;

        Double eta = startEta;

        for (int i = 0; i < numBatches; i++) {

            for (int j = 0; j < roundsPerBatch; j++) {
                iterate(problem, alphas, globalGradients, eta);
            }

            Double dualObjective = iterate(problem, alphas, globalGradients, 0D);

            System.out.println("iter=" + ((i + 1) * roundsPerBatch) +
                    ", dual_obj=" + dualObjective +
                    ", alphas=" + alphas +
                    ", eta=" + eta);

            if (dualObjective * (1 + threshold) >= lastDualObjective) {
                eta *= decay;
                if (eta <= endEta) {
                    break;
                }
            }
            lastDualObjective = dualObjective;
        }

        return new ConcreteBidder<T>(combiner, alphas);
    }

    private Double iterate(Problem<T> problem, Vec<Long> alphas, Vec<Long> globalGradients,
            Double eta) {

        Double dualObjective = 0D;

        for (Auction<T> auction : problem.auctions) {
            Candidate<T> candidate = selectCandidate(combiner, auction, alphas);

            Vec<Long> gradients = calculateGradients(combiner, auction, alphas, candidate);
            alphas.assign(alphas.scaleAndAdd(globalGradients.minus(gradients), -eta).lreg(0D));

            dualObjective += candidate.score;
        }

        dualObjective += alphas.inner(problem.limitMap);
        return dualObjective;
    }

    private static class Candidate<T extends Function> {

        public final Action action;
        public final T scoreFunction;
        public final Double score;

        private Candidate(Action action, T scoreFunction, Double score) {
            this.action = action;
            this.scoreFunction = scoreFunction;
            this.score = score;
        }
    }

    private static class ConcreteBidder<T extends Function> implements Bidder<T> {

        private final FunctionCombiner<T> combiner;
        private final Vec<Long> alphas;

        private ConcreteBidder(FunctionCombiner<T> combiner, Vec<Long> alphas) {
            this.combiner = combiner;
            this.alphas = alphas;
        }

        public Action bid(Auction<T> auction) {
            return selectCandidate(combiner, auction, alphas).action;
        }
    }
}
