package com.aim.project.pwp.heuristics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import com.aim.project.pwp.interfaces.HeuristicInterface;
import com.aim.project.pwp.interfaces.PWPSolutionInterface;


public class InversionMutation extends HeuristicOperators implements HeuristicInterface {
	
	private final Random oRandom;
	
	public InversionMutation(Random oRandom) {
	
		super();
		
		this.oRandom = oRandom;
	}

	@Override
	public double apply(PWPSolutionInterface solution, double depthOfSearch, double intensityOfMutation) {		
		int limit = getTerminationCriteria(intensityOfMutation);
		int noLocations = solution.getNumberOfLocations();
		double objVal = 0, preCost, postCost;
		
		for(int i = 0; i < limit; i++) {
			objVal = getObjectiveFunctionValue(solution);
			
			int indexA = oRandom.nextInt(noLocations);
			int indexB = oRandom.nextInt(noLocations);
			while (indexB <= indexA) {
				indexA = oRandom.nextInt(noLocations);
				indexB = oRandom.nextInt(noLocations);
			}
			if (indexA == 0) {
				preCost = getDeltaCostDepot(indexA, solution);
			} else {
				preCost = getDeltaCost(indexA-1, solution);
			}
			if (indexB == (noLocations-1)) {
				postCost = getDeltaCostHome(indexB, solution);
			} else {
				postCost = getDeltaCost(indexB, solution);
			}
			
			objVal = objVal - preCost - postCost;
			solution = reverseSubarray(solution, indexA, indexB);
			
			if (indexA == 0) {
				preCost = getDeltaCostDepot(indexA, solution);
			} else {
				preCost = getDeltaCost(indexA-1, solution);
			}
			if (indexB == (noLocations-1)) {
				postCost = getDeltaCostHome(indexB, solution);
			} else {
				postCost = getDeltaCost(indexB, solution);
			}
			
			objVal = objVal + preCost + postCost;
			solution.setObjectiveFunctionValue(objVal);
		}
		return objVal;
	}
	
	public PWPSolutionInterface reverseSubarray(PWPSolutionInterface oSolution, int indexA, int indexB) {
		int[] solutionPermutation = getSolutionPermutation(oSolution);
		
		List<Integer> temp = new ArrayList<Integer>();
		for(int i = indexA; i <= indexB; i++) {
			temp.add(solutionPermutation[i]);
		}
		Collections.reverse(temp);
		
		int j = 0;
		for(int i = indexA; i <= indexB; i++) {
			solutionPermutation[i] = temp.get(j);
			j++;
		}
		oSolution.getSolutionRepresentation().setSolutionRepresentation(solutionPermutation);
		return oSolution;
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
			times = 3;
		}
		if(intensityOfMutation >= 0.6 && intensityOfMutation < 0.8) {
			times = 4;
		}
		if(intensityOfMutation >= 0.8 && intensityOfMutation < 1) {
			times = 5;
		}
		if(intensityOfMutation == 1) {
			times = 6;
		}
		
		return times;
	}
	
}
