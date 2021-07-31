package com.aim.project.pwp.solution;

import com.aim.project.pwp.interfaces.PWPSolutionInterface;
import com.aim.project.pwp.interfaces.SolutionRepresentationInterface;

public class PWPSolution implements PWPSolutionInterface {

	private SolutionRepresentationInterface oRepresentation;
	
	private double dObjectiveFunctionValue;
	
	public PWPSolution(SolutionRepresentationInterface representation, double objectiveFunctionValue) {
		
		this.oRepresentation = representation;
		this.dObjectiveFunctionValue = objectiveFunctionValue;
	}

	@Override
	public double getObjectiveFunctionValue() {

		return dObjectiveFunctionValue;
	}

	@Override
	public void setObjectiveFunctionValue(double objectiveFunctionValue) {
		
		this.dObjectiveFunctionValue = objectiveFunctionValue;
	}

	@Override
	public SolutionRepresentationInterface getSolutionRepresentation() {		
		return oRepresentation;
	}

	@Override
	public PWPSolutionInterface clone() {
		
		SolutionRepresentationInterface representation = oRepresentation.clone();
		double objVal = dObjectiveFunctionValue;
		PWPSolution solution = new PWPSolution(representation, objVal);
		
		return solution;
	}

	@Override
	public int getNumberOfLocations() {
		
		return oRepresentation.getNumberOfLocations();
	}
}
