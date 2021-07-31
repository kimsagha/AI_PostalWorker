package com.aim.project.pwp.heuristics;

import java.util.Random;
import com.aim.project.pwp.interfaces.HeuristicInterface;
import com.aim.project.pwp.interfaces.PWPSolutionInterface;


public class AdjacentSwap extends HeuristicOperators implements HeuristicInterface {

	private final Random oRandom;
	
	public AdjacentSwap(Random oRandom) {

		super();
		
		this.oRandom = oRandom;
	}

	@Override
	public double apply(PWPSolutionInterface solution, double depthOfSearch, double intensityOfMutation) {		
		int limit = getTerminationCriteria(intensityOfMutation);
		int noLocations = solution.getNumberOfLocations();
		double objVal = 0, preCostA, postCostA, preCostB = 0, postCostB = 0;
		
		for(int i = 0; i < limit; i++) {
			objVal = getObjectiveFunctionValue(solution);
			
			int index = oRandom.nextInt(noLocations);
			if (index == 0) {
				preCostA = getDeltaCostDepot(index, solution);
				postCostA = getDeltaCost(index+1, solution);
			}
			else if (index == (noLocations-2)) {
				preCostA = getDeltaCost(index-1, solution);
				postCostA = getDeltaCostHome(index+1, solution);
			}
			else if (index == (noLocations-1)) {
				preCostA = getDeltaCost(index-1, solution);
				postCostA = getDeltaCostHome(index, solution);
				preCostB = getDeltaCostDepot(0, solution);
				postCostB = getDeltaCost(0, solution);
			}
			else {
				preCostA = getDeltaCost(index-1, solution);
				postCostA = getDeltaCost(index+1, solution);
			}
			
			objVal = objVal - preCostA - postCostA - preCostB - postCostB;
			solution = swap(index, solution);
			
			if (index == 0) {
				preCostA = getDeltaCostDepot(index, solution);
				postCostA = getDeltaCost(index+1, solution);
			}
			else if (index == (noLocations-2)) {
				preCostA = getDeltaCost(index-1, solution);
				postCostA = getDeltaCostHome(index+1, solution);
			}
			else if (index == (noLocations-1)) {
				preCostA = getDeltaCost(index-1, solution);
				postCostA = getDeltaCostHome(index, solution);
				preCostB = getDeltaCostDepot(0, solution);
				postCostB = getDeltaCost(0, solution);
			}
			else {
				preCostA = getDeltaCost(index-1, solution);
				postCostA = getDeltaCost(index+1, solution);
			}
			
			objVal = objVal + preCostA + postCostA + preCostB + postCostB;
			solution.setObjectiveFunctionValue(objVal);
		}
		
		return objVal;
	}

	@Override
	public boolean isCrossover() {
		return false;
	}

	@Override
	public boolean usesIntensityOfMutation() {
		return true;
	}

	@Override
	public boolean usesDepthOfSearch() {
		return false;
	}
	
	public int getTerminationCriteria(double intensityOfMutation) {
		int times = 0;
		if(intensityOfMutation >= 0 && intensityOfMutation < 0.2) {
			times = 1;
		}
		if(intensityOfMutation >= 0.2 && intensityOfMutation < 0.4) {
			times = 2;
		}
		if(intensityOfMutation >= 0.4 && intensityOfMutation < 0.6) {
			times = 4;
		}
		if(intensityOfMutation >= 0.6 && intensityOfMutation < 0.8) {
			times = 8;
		}
		if(intensityOfMutation >= 0.8 && intensityOfMutation < 1) {
			times = 16;
		}
		if(intensityOfMutation == 1) {
			times = 32;
		}
		
		return times;
	}

}

