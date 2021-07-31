package com.aim.project.pwp.runners;


import com.aim.project.pwp.AIM_PWP;
import AbstractClasses.HyperHeuristic;

/**
 *
 * Runs a hh using a default configuration then prints the best solution used for comparison.
 */
public abstract class TestFramework_Runner {

	public TestFramework_Runner() {
		
	}
	
	public double run(HyperHeuristic hh, long seed, int instance) {
		
		AIM_PWP problem = new AIM_PWP(seed);
		problem.loadInstance(instance);
		hh.loadProblemDomain(problem);
		hh.run();
		//System.out.println("f(s_best) = " + hh.getBestSolutionValue());
		return hh.getBestSolutionValue();
	}
	
	/**
	 * Allows a general visualiser runner by making the HyperHeuristic abstract.
	 * You can sub-class this class to run any hyper-heuristic that you want.
	 */
	//protected abstract HyperHeuristic getHyperHeuristic(long seed);
}
