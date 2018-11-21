package com.ecar.domain;

import java.util.Comparator;

import com.ecar.ws.CoordinatesView;
import com.ecar.ws.ParkView;

/**
 * This class compares the coordinates of "Two different parks" by implementing
 * the euclidean distance
 *
 */
public class ParkComparator implements Comparator<ParkView> {
	private CoordinatesView coordinates;

	public ParkComparator(CoordinatesView coordinates) {
		this.coordinates = coordinates;
	}

	// Comparison based on Euclidean Distance
	public int compare(ParkView s1, ParkView s2) {
		CoordinatesView coordsS1 = s1.getCoords();
		CoordinatesView coordsS2 = s2.getCoords();
		double distS1 = Math.sqrt(Math.pow(coordsS1.getY() - this.coordinates.getY(), 2)
				+ Math.pow(coordsS1.getX() - this.coordinates.getX(), 2));
		double distS2 = Math.sqrt(Math.pow(coordsS2.getY() - this.coordinates.getY(), 2)
				+ Math.pow(coordsS2.getX() - this.coordinates.getX(), 2));
		return Double.compare(distS1, distS2);
	}

}
