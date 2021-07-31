package com.aim.project.pwp.heuristics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import com.aim.project.pwp.interfaces.HeuristicInterface;
import com.aim.project.pwp.interfaces.PWPSolutionInterface;

public class DavissHillClimbing extends HeuristicOperators implements HeuristicInterface {
	
	private final Random oRandom;
	
	public DavissHillClimbing(Random oRandom) {
	
		super();
		
		this.oRandom = oRandom;
	}

	@Override
	public double apply(PWPSolutionInterface solution, double depthOfSearch, double intensityOfMutation) {		
		int limit = getTerminationCriteria(depthOfSearch);
		int noLocations = solution.getNumberOfLocations();
		int[] randomPermutation = getRandomPermutation(noLocations);
		PWPSolutionInterface candidateSolution = solution.clone();
		double objVal = 0, preCost, postCost;
		
		for (int i = 0; i < limit; i++) {
			objVal = getObjectiveFunctionValue(solution);
			
			if (randomPermutation[i] == 0) {
				preCost = getDeltaCostDepot(randomPermutation[i], solution);
				postCost = getDeltaCost(randomPermutation[i]+1, solution);
			}
			else if (randomPermutation[i] == (noLocations-2)) {
				preCost = getDeltaCost(randomPermutation[i]-1, solution);
				postCost = getDeltaCostHome(randomPermutation[i]+1, solution);
			}
			else if (randomPermutation[i] == (noLocations-1)) {
				preCost = getDeltaCost(randomPermutation[i]-1, solution);
				postCost = getDeltaCostHome(randomPermutation[i], solution);
			}
			else {
				preCost = getDeltaCost(randomPermutation[i]-1, solution);
				postCost = getDeltaCost(randomPermutation[i]+1, solution);
			}
			
			candidateSolution = swap(randomPermutation[i], candidateSolution);
						
			if(getObjectiveFunctionValue(candidateSolution) <= solution.getObjectiveFunctionValue()) {
				objVal = objVal - preCost - postCost;
				
				if (randomPermutation[i] == 0) {
					preCost = getDeltaCostDepot(randomPermutation[i], solution);
					postCost = getDeltaCost(randomPermutation[i]+1, solution);
				}
				else if (randomPermutation[i] == (noLocations-2)) {
					preCost = getDeltaCost(randomPermutation[i]-1, solution);
					postCost = getDeltaCostHome(randomPermutation[i]+1, solution);
				}
				else if (randomPermutation[i] == (noLocations-1)) {
					preCost = getDeltaCost(randomPermutation[i]-1, solution);
					postCost = getDeltaCostHome(randomPermutation[i], solution);
				}
				else {
					preCost = getDeltaCost(randomPermutation[i]-1, solution);
					postCost = getDeltaCost(randomPermutation[i]+1, solution);
				}
				
				objVal = objVal + preCost + postCost;
				
				candidateSolution.setObjectiveFunctionValue(objVal);
				solution.getSolutionRepresentation().setSolutionRepresentation(getSolutionPermutation(candidateSolution));
				solution.setObjectiveFunctionValue(objVal);
			}
		}
		
		return objVal;
	}
	
	public int[] getRandomPermutation(int noLocations) {
		List<Integer> indexList = new ArrayList<Integer>();
		for (int i = 0; i < noLocations; i++) {
			indexList.add(i);
		}
		Collections.shuffle(indexList, oRandom);
		
		int[] randomPermutation = new int[noLocations];
		randomPermutation = indexList.stream().mapToInt(i->i).toArray();
		
		return randomPermutation;
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
