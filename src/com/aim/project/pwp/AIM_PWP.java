package com.aim.project.pwp;


import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.aim.project.pwp.heuristics.AdjacentSwap;
import com.aim.project.pwp.heuristics.CX;
import com.aim.project.pwp.heuristics.DavissHillClimbing;
import com.aim.project.pwp.heuristics.InversionMutation;
import com.aim.project.pwp.heuristics.NextDescent;
import com.aim.project.pwp.heuristics.OX;
import com.aim.project.pwp.heuristics.Reinsertion;
import com.aim.project.pwp.instance.InitialisationMode;
import com.aim.project.pwp.instance.Location;
import com.aim.project.pwp.instance.reader.PWPInstanceReader;
import com.aim.project.pwp.interfaces.HeuristicInterface;
import com.aim.project.pwp.interfaces.ObjectiveFunctionInterface;
import com.aim.project.pwp.interfaces.PWPInstanceInterface;
import com.aim.project.pwp.interfaces.PWPSolutionInterface;
import com.aim.project.pwp.interfaces.Visualisable;
import com.aim.project.pwp.interfaces.XOHeuristicInterface;
import AbstractClasses.ProblemDomain;

public class AIM_PWP extends ProblemDomain implements Visualisable {

	private String[] instanceFiles = {
		"square", "libraries-15", "carparks-40", "tramstops-85", "trafficsignals-446", "streetlights-35714"
	};
	
	private PWPSolutionInterface[] aoMemoryOfSolutions;
	
	public PWPSolutionInterface oBestSolution;
	
	public PWPInstanceInterface oInstance;
	
	private HeuristicInterface[] aoHeuristics;
	
	private ObjectiveFunctionInterface oObjectiveFunction;
	
	private final long seed = 0;
	
	private Random rng;
	
	public AIM_PWP(long seed) {

		super(seed);
		
		setMemorySize(2);
		
		this.rng = new Random(seed);
		
		aoHeuristics = new HeuristicInterface[getNumberOfHeuristics()];
		
		aoHeuristics[0] = new InversionMutation(rng);
		aoHeuristics[1] = new AdjacentSwap(rng);
		aoHeuristics[2] = new Reinsertion(rng);
		aoHeuristics[3] = new NextDescent(rng);
		aoHeuristics[4] = new DavissHillClimbing(rng);
		aoHeuristics[5] = new OX(rng);
		aoHeuristics[6] = new CX(rng);
		
	}
	
	public PWPSolutionInterface getSolution(int index) {
		
		// TODO 
		if(aoMemoryOfSolutions[index] == null) {
			initialiseSolution(index);
		}
		return aoMemoryOfSolutions[index];
	}
	
	public PWPSolutionInterface getBestSolution() {
		
		// TODO
		return oBestSolution;
	}
	
	@Override
	public double applyHeuristic(int hIndex, int currentIndex, int candidateIndex) {
		
		// TODO - apply heuristic and return the objective value of the candidate solution
		//			remembering to keep track/update the best solution
		
		// don't modify solution if crossover
		if(!aoHeuristics[hIndex].isCrossover()) {
			copySolution(currentIndex, candidateIndex);
			
			double oCandidateObjectiveValue = aoHeuristics[hIndex].apply(getSolution(candidateIndex), getIntensityOfMutation(), getDepthOfSearch());
			
			if (oCandidateObjectiveValue < getBestSolutionValue()) {
				updateBestSolution(candidateIndex);
			}
			return oCandidateObjectiveValue;
		} else {
			return getFunctionValue(currentIndex);
		}
	}

	@Override
	public double applyHeuristic(int hIndex, int parent1Index, int parent2Index, int candidateIndex) {
		
		// TODO - apply heuristic and return the objective value of the candidate solution
		//			remembering to keep track/update the best solution	
		if(!aoHeuristics[hIndex].isCrossover()) {
			return getFunctionValue(parent1Index);
		} else {
			//copySolution(parent1Index, candidateIndex);
			double oChildObjectiveValue = ((XOHeuristicInterface)aoHeuristics[hIndex]).apply(getSolution(parent1Index), 
					getSolution(parent2Index), getSolution(candidateIndex), getDepthOfSearch(), getIntensityOfMutation());
			if (oChildObjectiveValue < getBestSolutionValue()) {
				updateBestSolution(candidateIndex);
			}
			return oChildObjectiveValue;
		}
	}

	@Override
	public String bestSolutionToString() {
		
		// TODO return the location IDs of the best solution including DEPOT and HOME locations
		//		e.g. "DEPOT -> 0 -> 2 -> 1 -> HOME"
		PWPSolutionInterface oSolution = getBestSolution();
		int noLocations = oSolution.getNumberOfLocations();
		int[] solutionPermutation = new int[noLocations];
		solutionPermutation = oSolution.getSolutionRepresentation().getSolutionRepresentation();
		
		String oString = new String();
		oString+="DEPOT (" + oInstance.getPostalDepot().getX() + "," + oInstance.getPostalDepot().getY() + ")";
		for(int i = 0; i < noLocations; i++) {
			oString+=" -> (";
			oString+=oInstance.getLocationForDelivery(solutionPermutation[i]).getX();
			oString+=",";
			oString+=oInstance.getLocationForDelivery(solutionPermutation[i]).getY();
			oString+=")";
		}
		oString+=" -> HOME (" + oInstance.getHomeAddress().getX() + "," + oInstance.getHomeAddress().getY() + ")";
		
		return oString;
	}
	
	@Override
	public boolean compareSolutions(int iIndexA, int iIndexB) {

		// TODO return true if the objective values of the two solutions are the same, else false
		double functionValA = getFunctionValue(iIndexA);
		double functionValB = getFunctionValue(iIndexB);
		boolean comparison = false;
		if (functionValA == functionValB) {
			comparison = true;
		}
		return comparison;
	}

	@Override
	public void copySolution(int iIndexA, int iIndexB) {

		// TODO - BEWARE this should copy the solution, not the reference to it!
		//			That is, that if we apply a heuristic to the solution in index 'b',
		//			then it does not modify the solution in index 'a' or vice-versa.
		
		PWPSolutionInterface oCandidateSolution = getSolution(iIndexA).clone();
		aoMemoryOfSolutions[iIndexB] = oCandidateSolution;		
	}
	
	@Override
	public double getBestSolutionValue() {

		// TODO
		if(oBestSolution == null) {
			oBestSolution = getSolution(0);
		}
		return oBestSolution.getObjectiveFunctionValue();
	}
	
	@Override
	public double getFunctionValue(int index) {
		
		// TODO
		return getSolution(index).getObjectiveFunctionValue();
	}
	
	@Override
	public int[] getHeuristicsOfType(HeuristicType type) {
		
		// TODO return an array of heuristic IDs based on the heuristic's type.
		List<Integer> oMutation = new ArrayList<Integer>();
		List<Integer> oLocalSearch = new ArrayList<Integer>();
		List<Integer> ocrossover = new ArrayList<Integer>();

		for(int i = 0; i < 3; i++) {
			oMutation.add(i);
		}
		for(int i = 3; i < 5; i++) {
			oLocalSearch.add(i);
		}
		for(int i = 5; i < getNumberOfHeuristics(); i++) {
			ocrossover.add(i);
		}
		int[] oHeuristicsOfType;
		if(type == HeuristicType.MUTATION) {
			oHeuristicsOfType = new int[oMutation.size()];
			oHeuristicsOfType = oMutation.stream().mapToInt(i->i).toArray();
			return oHeuristicsOfType;
		}
		if(type == HeuristicType.LOCAL_SEARCH) {
			oHeuristicsOfType = new int[oLocalSearch.size()];
			oHeuristicsOfType = oLocalSearch.stream().mapToInt(i->i).toArray();
			return oHeuristicsOfType;
		} else {
			oHeuristicsOfType = new int[ocrossover.size()];
			oHeuristicsOfType = ocrossover.stream().mapToInt(i->i).toArray();
			return oHeuristicsOfType;
		}
	}

	@Override
	public int[] getHeuristicsThatUseDepthOfSearch() {
		
		// TODO return the array of heuristic IDs that use depth of search.
		List<Integer> temp = new ArrayList<Integer>();

		for(int i = 0; i < getNumberOfHeuristics(); i++) {
			if(aoHeuristics[i].usesDepthOfSearch()) {
				temp.add(i);
			}
		}
		
		int[] oDOSHeuristics = new int[temp.size()];
		oDOSHeuristics = temp.stream().mapToInt(i->i).toArray();
		
		return oDOSHeuristics;
	}

	@Override
	public int[] getHeuristicsThatUseIntensityOfMutation() {
		
		// TODO return the array of heuristic IDs that use intensity of mutation.
		List<Integer> temp = new ArrayList<Integer>();

		for(int i = 0; i < getNumberOfHeuristics(); i++) {
			if(aoHeuristics[i].usesIntensityOfMutation()) {
				temp.add(i);
			}
		}
		
		int[] oIOMHeuristics = new int[temp.size()];
		oIOMHeuristics = temp.stream().mapToInt(i->i).toArray();
		
		return oIOMHeuristics;
	}

	@Override
	public int getNumberOfHeuristics() {

		// TODO - has to be hard-coded due to the design of the HyFlex framework...
		return 7;
	}

	@Override
	public int getNumberOfInstances() {

		// TODO return the number of available instances
		return aoMemoryOfSolutions.length;
	}
	
	@Override
	public void initialiseSolution(int index) {
		
		// TODO - initialise a solution in index 'index' 
		// 		making sure that you also update the best solution!
		this.aoMemoryOfSolutions[index] = getLoadedInstance().createSolution(InitialisationMode.RANDOM);;
		if(getFunctionValue(index) < getBestSolutionValue()) {
			updateBestSolution(index);
		}
	}

	// TODO implement the instance reader that this method uses
	//		to correctly read in the PWP instance, and set up the objective function.
	@Override
	public void loadInstance(int instanceId) {

		String SEP = FileSystems.getDefault().getSeparator();
		String instanceName = "instances" + SEP + "pwp" + SEP + instanceFiles[instanceId] + ".pwp";

		Path path = Paths.get(instanceName);
		Random random = new Random(seed);
		PWPInstanceReader oPwpReader = new PWPInstanceReader();
		oInstance = oPwpReader.readPWPInstance(path, random);
		oObjectiveFunction = oInstance.getPWPObjectiveFunction();
		
		for(HeuristicInterface h : aoHeuristics) {
			h.setObjectiveFunction(oObjectiveFunction);
		}
	}

	@Override
	public void setMemorySize(int size) {

		// TODO sets a new memory size
		// IF the memory size is INCREASED, then
		//		the existing solutions should be copied to the new memory at the same indices.
		// IF the memory size is DECREASED, then
		//		the first 'size' solutions are copied to the new memory.

		if (size <= 1) {
			return;
		}
		
		else if (aoMemoryOfSolutions == null) {
			aoMemoryOfSolutions = new PWPSolutionInterface[size];
		}
		
		else if (size > aoMemoryOfSolutions.length) {
			PWPSolutionInterface[] newArray = new PWPSolutionInterface[size];
			System.arraycopy(aoMemoryOfSolutions, 0, newArray, 0, aoMemoryOfSolutions.length);
			aoMemoryOfSolutions = newArray.clone();
		}
		
		else if (size < aoMemoryOfSolutions.length) {
			PWPSolutionInterface[] newArray = new PWPSolutionInterface[size];
			System.arraycopy(aoMemoryOfSolutions, 0, newArray, 0, size);
			aoMemoryOfSolutions = newArray.clone();
		}
	}

	@Override
	public String solutionToString(int index) {

		// TODO
		PWPSolutionInterface oSolution = getSolution(index);
		int noLocations = oSolution.getNumberOfLocations();
		int[] solutionPermutation = new int[noLocations];
		solutionPermutation = oSolution.getSolutionRepresentation().getSolutionRepresentation();
		
		String oString = new String();
		oString+="DEPOT";
		for(int i = 0; i < noLocations; i++) {
			oString+=" -> ";
			oString+=solutionPermutation[i];
		}
		oString+=" -> HOME";
		
		return oString;
	}

	@Override
	public String toString() {

		// TODO change 'AAA' to be your username
		return "psyks10's G52AIM PWP";
	}
	
	private void updateBestSolution(int index) {
		
		// TODO
		oBestSolution = getSolution(index);
	}
	
	@Override
	public PWPInstanceInterface getLoadedInstance() {
		return this.oInstance;
	}

	@Override
	public Location[] getRouteOrderedByLocations() {
		int[] city_ids = getBestSolution().getSolutionRepresentation().getSolutionRepresentation();
		Location[] route = Arrays.stream(city_ids).boxed().map(getLoadedInstance()::getLocationForDelivery).toArray(Location[]::new);
		return route;
	}
}
