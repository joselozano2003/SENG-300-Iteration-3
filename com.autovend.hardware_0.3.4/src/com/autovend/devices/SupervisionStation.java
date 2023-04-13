package com.autovend.devices;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Simulates the station used by the attendant.
 * <p>
 * A supervisor station possesses:
 * <ul>
 * <li>one touch screen; and,</li>
 * <li>one QWERTY keyboard.</li>
 * </ul>
 * </p>
 * <p>
 * All other functionality of the supervisor station must be performed in
 * software. A given self-checkout station can be supervised by at most one
 * supervision station.
 * </p>
 */
public class SupervisionStation {
	private final ArrayList<SelfCheckoutStation> supervisedStations;

	/**
	 * Represents a touch screen display on which there is a graphical user
	 * interface.
	 */
	public final TouchScreen screen;

	/**
	 * Represents a physical keyboard.
	 */
	public final Keyboard keyboard;

	/**
	 * Creates a supervisor station.
	 */
	public SupervisionStation() {
		screen = new TouchScreen();
		supervisedStations = new ArrayList<SelfCheckoutStation>();
		keyboard = new Keyboard();
	}

	/**
	 * Accesses the list of supervised self-checkout stations.
	 * 
	 * @return An immutable list of the self-checkout stations supervised by this
	 *             supervisor station.
	 */
	public List<SelfCheckoutStation> supervisedStations() {
		return Collections.unmodifiableList(supervisedStations);
	}

	/**
	 * Obtains the number of self-checkout stations supervised by this supervision
	 * station.
	 * 
	 * @return The count, which will always be non-negative.
	 */
	public int supervisedStationCount() {
		return supervisedStations.size();
	}

	/**
	 * Adds a self-checkout station to the ones supervised by this supervision
	 * station.
	 * 
	 * @param station
	 *            The self-checkout station to be added to the supervision of this
	 *            supervision station.
	 * @throws IllegalArgumentException
	 *             If station is null.
	 * @throws IllegalStateException
	 *             If station is already supervised.
	 */
	public void add(SelfCheckoutStation station) {
		if(station == null)
			throw new IllegalArgumentException("station cannot be null");
		if(station.isSupervised())
			throw new IllegalStateException("station is already supervised but cannot be");

		station.setSupervisor(this);
		supervisedStations.add(station);
	}

	/**
	 * Removes the indicated station from the ones supervised by this supervision
	 * station.
	 * 
	 * @param station
	 *            The station to be removed from supervision.
	 * @return true, if the indicated station was successfully removed from
	 *             supervision; otherwise, false.
	 */
	public boolean remove(SelfCheckoutStation station) {
		boolean result = supervisedStations.remove(station);

		if(result) {
			station.setSupervisor(null);
		}

		return result;
	}
}
