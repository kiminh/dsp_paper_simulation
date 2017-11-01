/*
 * Copyright 2015 Alibaba.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with Alibaba.com.
 */

package com.alibaba.wxb.dsp.solver;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.alibaba.wxb.dsp.entity.Ad;
import com.alibaba.wxb.dsp.function.Function;
import com.alibaba.wxb.dsp.simulator.Action;
import com.alibaba.wxb.dsp.simulator.Auction;
import com.alibaba.wxb.dsp.simulator.Bidder;
import com.alibaba.wxb.dsp.simulator.Problem;
import com.alibaba.wxb.dsp.simulator.Simulator;
import com.alibaba.wxb.dsp.simulator.Simulator.Summary;

/**
 * @author huahui.lhh
 */
public class McSolver<T extends Function> implements Solver<T> {

    public static final Random RANDOM = new Random(100L);

    private static class ConcreteBidder<T extends Function> implements Bidder<T> {

        private final Map<Long, Action> map;

        private ConcreteBidder(Map<Long, Action> map) {
            this.map = map;
        }

        public Action bid(Auction<T> auction) {
            return map.get(auction.impression.getId());
        }
    }

    public Bidder<T> solve(Problem<T> problem) {
        Simulator<T> simulator = new Simulator<T>(problem);
        Bidder<T> bestBidder = null;
        Double bestObjective = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < 10; i++) {
            Bidder<T> bidder = createBidder(problem);
            Summary summary = simulator.simulate(bidder, false);
            if (summary.suplus.allNonNegative() && summary.objective > bestObjective) {
                bestBidder = bidder;
                bestObjective = summary.objective;
            }
        }
        return bestBidder;
    }

    private Bidder<T> createBidder(Problem<T> problem) {
        Map<Long, Action> map = new HashMap<Long, Action>();
        final int numAds = problem.ads.size();
        for (Auction<T> auction : problem.auctions) {
            Ad ad = problem.ads.get(RANDOM.nextInt(numAds));
            Double bidPrice = 1 * RANDOM.nextDouble();
            map.put(auction.impression.getId(), new Action(ad, bidPrice));
        }
        return new ConcreteBidder<T>(map);
    }
}
