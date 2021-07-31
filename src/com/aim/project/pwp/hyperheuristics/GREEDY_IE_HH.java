package com.aim.project.pwp.hyperheuristics;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.aim.project.pwp.AIM_PWP;
import com.aim.project.pwp.SolutionPrinter;
import com.aim.project.pwp.interfaces.PWPSolutionInterface;

import AbstractClasses.HyperHeuristic;
import AbstractClasses.ProblemDomain;

public class GREEDY_IE_HH extends HyperHeuristic {
	
	public GREEDY_IE_HH(long lSeed) {
		
		super(lSeed);
	}
	
	@Override
	protected void solve(ProblemDomain oProblem) {

		oProblem.setMemorySize(3);
		
		oProblem.initialiseSolution(0);
		double current = oProblem.getFunctionValue(0);
		
		oProblem.setIntensityOfMutation(0.2);
		oProblem.setDepthOfSearch(0.2);
		
		long iteration = 0;
		boolean accept;
		// initialise array of objective values for each of the heuristics
		double[] aoObjVals = new double[oProblem.getNumberOfHeuristics()];
		double candidate;
		
		while(!hasTimeExpired() ) {
			// run all heuristics to see which has the best value and use that one's candidate
			for(int h = 0; h < aoObjVals.length; h++) {
				if(h < 5) {
					candidate = oProblem.applyHeuristic(h, 0, 1);
					aoObjVals[h] = candidate;
				} else {
					oProblem.initialiseSolution(2);
					candidate = oProblem.applyHeuristic(h, 0, 2, 1);
					aoObjVals[h] = candidate;
				}
			}
			
			List<double[]> temp = new ArrayList<double[]>(Arrays.asList(aoObjVals));
			int h = temp.indexOf(Collections.min(temp,null));
			if(h < 5) {
				candidate = oProblem.applyHeuristic(h, 0, 1);
				aoObjVals[h] = candidate;
			} else {
				oProblem.initialiseSolution(2);
				candidate = oProblem.applyHeuristic(h, 0, 2, 1);
				aoObjVals[h] = candidate;
			}

			accept = candidate <= current;
			if(accept) {
				oProblem.copySolution(1, 0);
				current = candidate;
			}
			iteration++;
		}
		PWPSolutionInterface oSolution = ((AIM_PWP) oProblem).getBestSolution();
		SolutionPrinter oSP = new SolutionPrinter("out.csv");
		oSP.printSolution( ((AIM_PWP) oProblem).oInstance.getSolutionAsListOfLocations(oSolution));
		//System.out.println(String.format("Greedy Total iterations = %d", iteration));
	}

	@Override
	public String toString() {

		return "GREEDY_IE_HH";
	}
}
