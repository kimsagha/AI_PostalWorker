package com.aim.project.pwp.heuristics;

import java.util.Random;

import com.aim.project.pwp.interfaces.ObjectiveFunctionInterface;
import com.aim.project.pwp.interfaces.PWPSolutionInterface;
import com.aim.project.pwp.interfaces.XOHeuristicInterface;


public class CX extends HeuristicOperators implements XOHeuristicInterface {
	
	private final Random oRandom;
	
	private ObjectiveFunctionInterface oObjectiveFunction;

	public CX(Random oRandom) {
		
		this.oRandom = oRandom;
	}

	@Override
	public double apply(PWPSolutionInterface solution, double depthOfSearch, double intensityOfMutation) {
		return solution.getObjectiveFunctionValue();
	}

	@Override
	public double apply(PWPSolutionInterface p1, PWPSolutionInterface p2, PWPSolutionInterface c, double depthOfSearch, double intensityOfMutation) {
		int limit = getTerminationCriteria(intensityOfMutation);
		int noLocations = p1.getNumberOfLocations();
		
		int[] parentX = getSolutionPermutation(p1);
		int[] parentY = getSolutionPermutation(p2);
		int[] childX = new int[noLocations];
		int[] childY = new int[noLocations];
		
		for(int i = 0; i < limit; i++ ) {
			int startingIndex = oRandom.nextInt(noLocations);
			childX = cycleCrossover(parentX, parentY, childX, startingIndex);
			childY = cycleCrossover(parentY, parentX, childY, startingIndex);
			parentX = childX.clone();
			parentY = childY.clone();
		}
		
		if(oRandom.nextBoolean()) {
			c.getSolutionRepresentation().setSolutionRepresentation(childX);
		} else {
			c.getSolutionRepresentation().setSolutionRepresentation(childY);
			
		}
		double objVal = oObjectiveFunction.getObjectiveFunctionValue(c.getSolutionRepresentation());
		c.setObjectiveFunctionValue(objVal);
		return objVal;
	}
	
	public int[] cycleCrossover(int[] parent0, int[] parent1, int[] child, int startingIndex) {
		int noLocations = parent1.length;
		child = parent1.clone();
		int index = 0;
		int startingValue = parent0[startingIndex];
		index = startingIndex;
		for(int i = 0; i < noLocations; i++) {
			child[index] = parent0[index];
			if (parent1[index] == startingValue) { // cycle has been completed
				break;
			}
			int j = 0;
			while (j < noLocations) {
				if(parent0[j] == parent1[index]) {
					break;
				}
				j++;
			}
			index = j;
		}
		return child;
	}
	
	@Override
	public void setObjectiveFunction(ObjectiveFunctionInterface oObjectiveFunction) {
		
		this.oObjectiveFunction = oObjectiveFunction;
	}

	@Override
	public boolean isCrossover() {
		return true;
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
