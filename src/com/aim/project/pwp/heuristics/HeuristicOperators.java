package com.aim.project.pwp.heuristics;

import com.aim.project.pwp.interfaces.ObjectiveFunctionInterface;
import com.aim.project.pwp.interfaces.PWPSolutionInterface;
import com.aim.project.pwp.interfaces.SolutionRepresentationInterface;

public class HeuristicOperators {

	private ObjectiveFunctionInterface oObjectiveFunction;

	public HeuristicOperators() {
	}

	public void setObjectiveFunction(ObjectiveFunctionInterface f) {
		this.oObjectiveFunction = f;
	}
	
	public double getObjectiveFunctionValue(PWPSolutionInterface oSolution) {
		SolutionRepresentationInterface oRepresentation = oSolution.getSolutionRepresentation();
		double value = oObjectiveFunction.getObjectiveFunctionValue(oRepresentation);
		oSolution.setObjectiveFunctionValue(value);
		return oSolution.getObjectiveFunctionValue();
	}

	public PWPSolutionInterface swap(int indexA, PWPSolutionInterface oSolution) {		
		int noLocations = oSolution.getNumberOfLocations();
		int[] solutionPermutation = getSolutionPermutation(oSolution);
		int indexB = 0; // set indexB to be the first delivery location
		if (indexA == (noLocations-1)) { // if indexA is the last delivery location, keep indexB as the first delivery location
		} else {
			indexB = indexA + 1;
		}
		
		int a = solutionPermutation[indexA];
		int b = solutionPermutation[indexB];
		solutionPermutation[indexA] = b;
		solutionPermutation[indexB] = a;		
		
		oSolution.getSolutionRepresentation().setSolutionRepresentation(solutionPermutation);

		return oSolution;
	}
	
	public int[] getSolutionPermutation(PWPSolutionInterface solution) {
		int[] permutationArray = new int[solution.getNumberOfLocations()];
		permutationArray = solution.getSolutionRepresentation().getSolutionRepresentation().clone();
		
		return permutationArray;
	}
	
	public double getDeltaCost(int indexA, PWPSolutionInterface solution) {
		int[] representation = getSolutionPermutation(solution);
		return oObjectiveFunction.getCost(representation[indexA], representation[indexA+1]);
	}
	
	public double getDeltaCostDepot(int indexA, PWPSolutionInterface solution) {
		int[] representation = getSolutionPermutation(solution);
		return oObjectiveFunction.getCostBetweenDepotAnd(representation[indexA]);
	}
	

	public double getDeltaCostHome(int indexA, PWPSolutionInterface solution) {
		int[] representation = getSolutionPermutation(solution);
		return oObjectiveFunction.getCostBetweenHomeAnd(representation[indexA]);
	}
}
