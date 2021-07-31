package com.aim.project.pwp;

import java.util.ArrayList;

import com.aim.project.pwp.instance.Location;
import com.aim.project.pwp.interfaces.ObjectiveFunctionInterface;
import com.aim.project.pwp.interfaces.PWPInstanceInterface;
import com.aim.project.pwp.interfaces.SolutionRepresentationInterface;

public class PWPObjectiveFunction implements ObjectiveFunctionInterface {
	
	private final PWPInstanceInterface oInstance;
	ArrayList<Location> aoLocations;
	SolutionRepresentationInterface solution;
	
	public PWPObjectiveFunction(PWPInstanceInterface oInstance) {
		
		this.oInstance = oInstance;
	}

	@Override
	public double getObjectiveFunctionValue(SolutionRepresentationInterface oRepresentation) {
		int[] permutationArray = new int[oRepresentation.getNumberOfLocations()];
		permutationArray = oRepresentation.getSolutionRepresentation();
		
		double objFuncVal = 0;

		for(int i = 0; i < (permutationArray.length-1); i++) {
			objFuncVal+=getCost(permutationArray[i], permutationArray[i+1]);
		}
		
		objFuncVal+=getCostBetweenDepotAnd(permutationArray[0]);
		objFuncVal+=getCostBetweenHomeAnd(permutationArray[permutationArray.length-1]);

		return objFuncVal;
	}

	@Override
	public double getCost(int iLocationA, int iLocationB) {
		Location A = oInstance.getLocationForDelivery(iLocationA);
		Location B = oInstance.getLocationForDelivery(iLocationB);
		double xA, yA, xB, yB;
		xA = A.getX();
		yA = A.getY();
		xB = B.getX();
		yB = B.getY();
		double cost = Math.sqrt((yB - yA) * (yB - yA) + (xB - xA) * (xB - xA));
		return cost;
	}

	@Override
	public double getCostBetweenDepotAnd(int iLocation) {
		Location depot = oInstance.getPostalDepot();
		Location i = oInstance.getLocationForDelivery(iLocation);
		double xD, yD, xI, yI;
		xD = depot.getX();
		yD = depot.getY();
		xI = i.getX();
		yI = i.getY();
		double cost = Math.sqrt((yI - yD) * (yI - yD) + (xI - xD) * (xI - xD));
		return cost;
	}

	@Override
	public double getCostBetweenHomeAnd(int iLocation) {
		Location home = oInstance.getHomeAddress();
		Location i = oInstance.getLocationForDelivery(iLocation);
		double xH, yH, xI, yI;
		xH = home.getX();
		yH = home.getY();
		xI = i.getX();
		yI = i.getY();
		double cost = Math.sqrt((yI - yH) * (yI - yH) + (xI - xH) * (xI - xH));
		return cost;
	}
}
