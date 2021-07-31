package com.aim.project.pwp.runners;


import com.aim.project.pwp.hyperheuristics.GREEDY_IE_HH;

import AbstractClasses.HyperHeuristic;

/**
 * Runs a greedy IE hyper-heuristic then displays the best solution found
 */
public class GREEDY_IE_VisualRunner extends HH_Runner_Visual {

	@Override
	protected HyperHeuristic getHyperHeuristic(long seed) {
		//System.out.print("Running Kim's Greedy HH!\n");
		return new GREEDY_IE_HH(seed);
	}
	
	public static void main(String [] args) {
		
		HH_Runner_Visual runner = new GREEDY_IE_VisualRunner();
		runner.run();
		System.out.print("GREEDY_IE_VisualRunner successful\n");
	}

}
