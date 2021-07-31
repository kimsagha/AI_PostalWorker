package com.aim.project.pwp.heuristics;


import java.util.Random;
import com.aim.project.pwp.interfaces.HeuristicInterface;
import com.aim.project.pwp.interfaces.PWPSolutionInterface;

public class NextDescent extends HeuristicOperators implements HeuristicInterface {
	
	private final Random oRandom;
	
	public NextDescent(Random oRandom) {
	
		super();
		
		this.oRandom = oRandom;
	}

	@Override
	public double apply(PWPSolutionInterface solution, double depthOfSearch, double intensityOfMutation) {		
		int limit = getTerminationCriteria(depthOfSearch);
		int improvementCounter = 0, noImprovementcounter = 0;
		int noLocations = solution.getNumberOfLocations();
		int index = oRandom.nextInt(noLocations);
		PWPSolutionInterface candidateSolution = solution.clone();
		double objVal = 0, preCost, postCost;
		
		while(noImprovementcounter < noLocations) {
			objVal = getObjectiveFunctionValue(solution);
			
			if (index == 0) {
				preCost = getDeltaCostDepot(index, solution);
				postCost = getDeltaCost(index+1, solution);
			}
			else if (index == (noLocations-2)) {
				preCost = getDeltaCost(index-1, solution);
				postCost = getDeltaCostHome(index+1, solution);
			}
			else if (index == (noLocations-1)) {
				preCost = getDeltaCost(index-1, solution);
				postCost = getDeltaCostHome(index, solution);
			}
			else {
				preCost = getDeltaCost(index-1, solution);
				postCost = getDeltaCost(index+1, solution);
			}
			
			candidateSolution = swap(index, candidateSolution);
			
			if(getObjectiveFunctionValue(candidateSolution) < solution.getObjectiveFunctionValue()) {
				objVal = objVal - preCost - postCost;
				if (index == 0) {
					preCost = getDeltaCostDepot(index, solution);
					postCost = getDeltaCost(index+1, solution);
				}
				else if (index == (noLocations-2)) {
					preCost = getDeltaCost(index-1, solution);
					postCost = getDeltaCostHome(index+1, solution);
				}
				else if (index == (noLocations-1)) {
					preCost = getDeltaCost(index-1, solution);
					postCost = getDeltaCostHome(index, solution);
				}
				else {
					preCost = getDeltaCost(index-1, solution);
					postCost = getDeltaCost(index+1, solution);
				}
				
				objVal = objVal + preCost + postCost;
				
				candidateSolution.setObjectiveFunctionValue(objVal);
				solution.getSolutionRepresentation().setSolutionRepresentation(getSolutionPermutation(candidateSolution));
				solution.setObjectiveFunctionValue(objVal);
				noImprovementcounter = 0;
				improvementCounter++;
				if (improvementCounter == limit) {
					break;
				}
			} else {
				noImprovementcounter++;
			}
			
			index++;
			if(index == noLocations) {
				index = 0;
			}
		}
		
		return objVal;
	}

	@Override
	public boolean isCrossover() {
		return false;
	}

	@Override
	public boolean usesIntensityOfMutation() {
		return false;
	}

	@Override
	public boolean usesDepthOfSearch() {
		return true;
	}
	
	public int getTerminationCriteria(double depthOfSearch) {
		int times = 0;
		if(depthOfSearch >= 0 && depthOfSearch < 0.2) {
			times = 1;
		}
		if(depthOfSearch >= 0.2 && depthOfSearch < 0.4) {
			times = 2;
		}
		if(depthOfSearch >= 0.4 && depthOfSearch < 0.6) {
			times = 3;
		}
		if(depthOfSearch >= 0.6 && depthOfSearch < 0.8) {
			times = 4;
		}
		if(depthOfSearch >= 0.8 && depthOfSearch < 1) {
			times = 5;
		}
		if(depthOfSearch == 1) {
			times = 6;
		}
		
		return times;
	}
}
