package com.aim.project.pwp.solution;

import com.aim.project.pwp.interfaces.SolutionRepresentationInterface;

public class SolutionRepresentation implements SolutionRepresentationInterface {

	private int[] aiSolutionRepresentation;

	public SolutionRepresentation(int[] aiRepresentation) {
		this.aiSolutionRepresentation = aiRepresentation;
	}

	@Override
	public int[] getSolutionRepresentation() {

		return aiSolutionRepresentation;
	}

	@Override
	public void setSolutionRepresentation(int[] aiSolutionRepresentation) {
		
		this.aiSolutionRepresentation = aiSolutionRepresentation;
	}

	@Override
	public int getNumberOfLocations() {

		// TODO return the total number of locations in this instance (includes DEPOT and HOME).
		return aiSolutionRepresentation.length;
	}

	@Override
	public SolutionRepresentationInterface clone() {

		// TODO perform a DEEP clone of the solution representation!
		// aiSolutionRepresentation is an array of primitives (ints), i.e., does not need to be recreated, copying is sufficient
		int[] oSolution = new int[getNumberOfLocations()];
		oSolution = aiSolutionRepresentation.clone();
		SolutionRepresentation oRepresentation = new SolutionRepresentation(oSolution);
		
		return oRepresentation;
	}

}
