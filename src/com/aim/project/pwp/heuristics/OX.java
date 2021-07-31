package com.aim.project.pwp.heuristics;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import com.aim.project.pwp.interfaces.ObjectiveFunctionInterface;
import com.aim.project.pwp.interfaces.PWPSolutionInterface;
import com.aim.project.pwp.interfaces.XOHeuristicInterface;


public class OX extends HeuristicOperators implements XOHeuristicInterface {
	
	private final Random oRandom;
	
	private ObjectiveFunctionInterface oObjectiveFunction;
	
	public OX(Random oRandom) {
		
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
		int[] parent1 = getSolutionPermutation(p1);
		int[] parent2 = getSolutionPermutation(p2);
		for(int j = 0; j < limit; j++) {
			int[] cutPoints = getCutPoints(noLocations);
			List<Integer> added1 = new ArrayList<>(), added2 = new ArrayList<>(), add1 = new ArrayList<>(), add2 = new ArrayList<>();
			for(int i = cutPoints[0]; i <= cutPoints[1]; i++) {
				added1.add(parent1[i]);
				added2.add(parent2[i]);
			}
			for(int i = (cutPoints[1]+1); i < noLocations; i++) {
				if (!added1.contains(parent2[i])) {
					add1.add(parent2[i]);
				}
				if (!added2.contains(parent1[i])) {
					add2.add(parent1[i]);
				}
			}
			for(int i = 0; i <= cutPoints[1]; i++) {
				if (!added1.contains(parent2[i])) {
					add1.add(parent2[i]);
				}
				if (!added2.contains(parent1[i])) {
					add2.add(parent1[i]);
				}
			}
			for(int i = (cutPoints[1]+1); i < noLocations; i++) {
				parent1[i] = add1.remove(0);
				parent2[i] = add2.remove(0);
			}
			for(int i = 0; i < cutPoints[0]; i++) {
				parent1[i] = add1.remove(0);
				parent2[i] = add2.remove(0);
			}
		}
		if(oRandom.nextBoolean()) {
			c.getSolutionRepresentation().setSolutionRepresentation(parent1);
		} else {
			c.getSolutionRepresentation().setSolutionRepresentation(parent2);
		}
		double value = oObjectiveFunction.getObjectiveFunctionValue(c.getSolutionRepresentation());
		c.setObjectiveFunctionValue(value);
		return value;
	}
	
	public int[] getCutPoints(int noLocations) {
		int[] cutPoints = new int[2];
		cutPoints[0] = oRandom.nextInt(noLocations);
		cutPoints[1] = oRandom.nextInt(noLocations);
		while((cutPoints[1] <= cutPoints[0]) || (cutPoints[0] == 0 && cutPoints[1] == (noLocations-1))) {
			cutPoints[0] = oRandom.nextInt(noLocations);
			cutPoints[1] = oRandom.nextInt(noLocations);
		}
		return cutPoints;
	}
	
	@Override
	public void setObjectiveFunction(ObjectiveFunctionInterface f) {
		
		this.oObjectiveFunction = f;
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
