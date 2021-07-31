package com.aim.project.pwp.instance.reader;


import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Random;

import com.aim.project.pwp.instance.Location;
import com.aim.project.pwp.instance.PWPInstance;
import com.aim.project.pwp.interfaces.PWPInstanceInterface;
import com.aim.project.pwp.interfaces.PWPInstanceReaderInterface;


public class PWPInstanceReader implements PWPInstanceReaderInterface {

	@Override
	public PWPInstanceInterface readPWPInstance(Path path, Random random) {
		
		BufferedReader bfr;
		PWPInstance PWPInstanced;
		String line = null;
		int linecount = 0, noLocations = 0;
		ArrayList<Location> locationList = new ArrayList<Location>();
		Location locationArray[];
		Location pLocation = null, hLocation = null;
		String[] numbers;
		Location iLocation;
		
		try {
			bfr = Files.newBufferedReader(path);
			// line.split("\\s+")
			// TODO read in the PWP instance and create and return a new 'PWPInstance'

			while ((line = bfr.readLine()) != null) {
				linecount++;
				if (linecount == 4) {
					numbers = line.split("\\s+");
					pLocation = new Location(Double.valueOf(numbers[0]), Double.valueOf(numbers[1]));
				}
				if (linecount == 6) {
					numbers = line.split("\\s+");
					hLocation = new Location(Double.valueOf(numbers[0]), Double.valueOf(numbers[1]));
				}
				if (linecount > 7) {
					if(line.contains("EOF")) {
						break;
					}
					numbers = line.split("\\s+");
					iLocation = new Location(Double.valueOf(numbers[0]), Double.valueOf(numbers[1]));
					locationList.add(iLocation);
				}
			}
			
			noLocations = locationList.size();
			locationArray = new Location[noLocations];
			for(int i = 0; i < noLocations; i++) {
				locationArray[i] = new Location(locationList.get(i).getX(), locationList.get(i).getY());
			}
			noLocations+=2;
			PWPInstanced = new PWPInstance(noLocations, locationArray, pLocation, hLocation, random);

		} catch (IOException e) {

			e.printStackTrace();
			return null;
		}
		
		return PWPInstanced;
	}
}
