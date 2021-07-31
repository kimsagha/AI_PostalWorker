package com.aim.project.pwp.instance;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.aim.project.pwp.PWPObjectiveFunction;
import com.aim.project.pwp.interfaces.ObjectiveFunctionInterface;
import com.aim.project.pwp.interfaces.PWPInstanceInterface;
import com.aim.project.pwp.interfaces.PWPSolutionInterface;
import com.aim.project.pwp.solution.SolutionRepresentation;
import com.aim.project.pwp.solution.PWPSolution;


public class PWPInstance implements PWPInstanceInterface {
	
	private final Location[] aoLocations;
	
	private final Location oPostalDepotLocation;
	
	private final Location oHomeAddressLocation;
	
	private final int iNumberOfLocations;
	
	private final Random oRandom;
	
	private ObjectiveFunctionInterface oObjectiveFunction = null;
		
	/**
	 * 
	 * @param numberOfLocations The TOTAL number of locations (including DEPOT and HOME).
	 * @param aoLocations The delivery locations.
	 * @param oPostalDepotLocation The DEPOT location.
	 * @param oHomeAddressLocation The HOME location.
	 * @param random The random number generator to use.
	 */
	public PWPInstance(int numberOfLocations, Location[] aoLocations, Location oPostalDepotLocation, Location oHomeAddressLocation, Random random) {
		
		this.iNumberOfLocations = numberOfLocations;
		this.oRandom = random;
		this.aoLocations = aoLocations;
		this.oPostalDepotLocation = oPostalDepotLocation;
		this.oHomeAddressLocation = oHomeAddressLocation;
	}

	@Override
	public PWPSolution createSolution(InitialisationMode mode) {
		
		// TODO construct a new 'PWPSolution' using RANDOM initialisation
		int noLocations = aoLocations.length;
		
		List<Integer> listPermutation = new ArrayList<Integer>();
		for (int i = 0; i < noLocations; i++) {
			listPermutation.add(i);
		}
		
		Collections.shuffle(listPermutation, oRandom);
		
		int[] permutationArray = new int[noLocations];
		permutationArray = listPermutation.stream().mapToInt(i->i).toArray();
		
		SolutionRepresentation oRepresentation = new SolutionRepresentation(permutationArray);
		double oObjectiveFunctionValue = oObjectiveFunction.getObjectiveFunctionValue(oRepresentation);
		PWPSolution oSolution = new PWPSolution(oRepresentation, oObjectiveFunctionValue);
		
		return oSolution;
	}
	
	@Override
	public ObjectiveFunctionInterface getPWPObjectiveFunction() {
		
		if(oObjectiveFunction == null) {
			this.oObjectiveFunction = new PWPObjectiveFunction(this);
		}

		return oObjectiveFunction;
	}

	@Override
	public int getNumberOfLocations() {

		return iNumberOfLocations;
	}

	@Override
	public Location getLocationForDelivery(int deliveryId) {

		return aoLocations[deliveryId];
	}

	@Override
	public Location getPostalDepot() {
		
		return this.oPostalDepotLocation;
	}

	@Override
	public Location getHomeAddress() {
		
		return this.oHomeAddressLocation;
	}
	
	@Override
	public ArrayList<Location> getSolutionAsListOfLocations(PWPSolutionInterface oSolution) {
		
		// TODO return an 'ArrayList' of ALL LOCATIONS in the solution.
		
		int[] solutionLocations = oSolution.getSolutionRepresentation().getSolutionRepresentation();
		Location[] aoLocations = Arrays.stream(solutionLocations).boxed().map(this::getLocationForDelivery).toArray(Location[]::new);
		ArrayList<Location> loLocations = new ArrayList<Location>(Arrays.asList(aoLocations));
		loLocations.add(0, oPostalDepotLocation);
		loLocations.add(oHomeAddressLocation);
		
		return loLocations;
	}

}
