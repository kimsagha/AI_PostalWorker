package com.aim.project.pwp.interfaces;

import com.aim.project.pwp.instance.Location;

public interface Visualisable {

	/**
	 * 
	 * @return The PWP route in visited location order.
	 */
	public Location[] getRouteOrderedByLocations();
	
	/**
	 * 
	 * @return The problem instance that is currently loaded.
	 */
	public PWPInstanceInterface getLoadedInstance();
}
