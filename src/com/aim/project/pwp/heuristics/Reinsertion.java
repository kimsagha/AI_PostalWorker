package com.aim.project.pwp.heuristics;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.aim.project.pwp.interfaces.HeuristicInterface;
import com.aim.project.pwp.interfaces.PWPSolutionInterface;


public class Reinsertion extends HeuristicOperators implements HeuristicInterface {

	private final Random oRandom;
	
	public Reinsertion(Random oRandom) {

		super();
		
		this.oRandom = oRandom;
	}

	@Override
	public double apply(PWPSolutionInterface solution, double depthOfSearch, double intensityOfMutation) {
		int limit = getTerminationCriteria(intensityOfMutation);		
		int noLocations = solution.getNumberOfLocations();
		double objVal = 0, preCostOld, postCostOld, preCostNew, postCostNew;
		
		for(int i = 0; i < limit; i++) {
			objVal = getObjectiveFunctionValue(solution);
			
			int indexOld = oRandom.nextInt(noLocations);
			int indexNew = oRandom.nextInt(noLocations);
			while (indexNew == indexOld) {
				indexNew = oRandom.nextInt(noLocations);
			}
			if (indexOld == 0) {
				preCostOld = getDeltaCostDepot(indexOld, solution);
				postCostOld = getDeltaCost(indexOld, solution);
			}
			else if (indexOld == (noLocations-1)) {
				preCostOld = getDeltaCost(indexOld-1, solution);
				postCostOld = getDeltaCostHome(indexOld, solution);
			}
			else {
				preCostOld = getDeltaCost(indexOld-1, solution);
				postCostOld = getDeltaCost(indexOld, solution);
			}
			
			if (indexNew == 0) {
				preCostNew = getDeltaCostDepot(indexNew, solution);
				postCostNew = getDeltaCost(indexNew, solution);
			}
			else if (indexNew == (noLocations-1)) {
				preCostNew = getDeltaCost(indexNew-1, solution);
				postCostNew = getDeltaCostHome(indexNew, solution);
			}
			else {
				preCostNew = getDeltaCost(indexNew-1, solution);
				postCostNew = getDeltaCost(indexNew, solution);
			}
			
			objVal = objVal - preCostOld - postCostOld - preCostNew - postCostNew;
			solution = reinsert(solution, indexOld, indexNew);
			
			if (indexOld == 0) {
				preCostOld = getDeltaCostDepot(indexOld, solution);
				postCostOld = getDeltaCost(indexOld, solution);
			}
			else if (indexOld == (noLocations-1)) {
				preCostOld = getDeltaCost(indexOld-1, solution);
				postCostOld = getDeltaCostHome(indexOld, solution);
			}
			else {
				preCostOld = getDeltaCost(indexOld-1, solution);
				postCostOld = getDeltaCost(indexOld, solution);
			}
			
			if (indexNew == 0) {
				preCostNew = getDeltaCostDepot(indexNew, solution);
				postCostNew = getDeltaCost(indexNew, solution);
			}
			else if (indexNew == (noLocations-1)) {
				preCostNew = getDeltaCost(indexNew-1, solution);
				postCostNew = getDeltaCostHome(indexNew, solution);
			}
			else {
				preCostNew = getDeltaCost(indexNew-1, solution);
				postCostNew = getDeltaCost(indexNew, solution);
			}
			
			objVal = objVal + preCostOld + postCostOld + preCostNew + postCostNew;
			solution.setObjectiveFunctionValue(objVal);
		}
		
		return objVal;
	}
	
	public PWPSolutionInterface reinsert(PWPSolutionInterface oSolution, int indexOld, int indexNew) {		
		int[] solutionPermutation = getSolutionPermutation(oSolution);
		List<Integer> tempList = Arrays.stream(solutionPermutation).boxed().collect(Collectors.toList());
		int iLocation = tempList.remove(indexOld);
		tempList.add(indexNew, iLocation);
		solutionPermutation = tempList.stream().mapToInt(i->i).toArray();
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
