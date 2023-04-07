package com.autovend.devices;

import java.util.ArrayList;
import java.util.Arrays;

import com.autovend.ReusableBag;
import com.autovend.devices.observers.ReusableBagDispenserObserver;

/**
 * Represents the reusable-bag dispenser.
 */
public class ReusableBagDispenser extends AbstractDevice<ReusableBagDispenserObserver> {
	private static final long serialVersionUID = 5116306380972350655L;
	private ArrayList<ReusableBag> bags = new ArrayList<ReusableBag>();
	private int capacity;

	/**
	 * Basic constructor permitting the capacity to be set.
	 * 
	 * @param capacity
	 *            The maximum number of bags that the dispenser can contain.
	 * @throws IllegalArgumentException
	 *             if capacity &le;0.
	 */
	public ReusableBagDispenser(int capacity) {
		if(capacity <= 0)
			throw new IllegalArgumentException("The capacity must be a positive integer.");

		this.capacity = capacity;
	}

	/**
	 * Obtains the maximum capacity of the dispenser.
	 * 
	 * @return The maximum capacity.
	 */
	public int getCapacity() {
		return capacity;
	}

	/**
	 * Adds the indicated bags to the dispenser. Causes a "bags loaded" event to be
	 * announced. Requires power.
	 * 
	 * @param bags
	 *            The bags to be added.
	 * @throws OverloadException
	 *             if the new total number of bags would exceed the capacity of the
	 *             dispenser.
	 */
	public void load(ReusableBag... bags) throws OverloadException {
		if(bags == null)
			throw new SimulationException("bags cannot be null");

		if(this.bags.size() + bags.length > capacity)
			throw new OverloadException("You have tried to stuff the dispenser with too many bags");

		this.bags.addAll(Arrays.asList(bags));

		notifyBagsLoaded(bags.length);
	}

	/**
	 * Removes all the bags from the dispenser. Causes an "out of bags" event to be
	 * announced. Requires power.
	 * 
	 * @return The bags formerly in the dispenser.
	 */
	public ReusableBag[] unload() {
		ReusableBag[] array = bags.toArray(new ReusableBag[bags.size()]);
		bags.clear();
		notifyOutOfBags();

		return array;
	}

	/**
	 * Dispenses one bag to the customer. Causes a "bag dispensed" event to be
	 * announced. May cause an "out of bags" event to also be announced, if the
	 * dispensed bag is the last one in the dispenser. Requires power.
	 * 
	 * @return The dispensed bag.
	 * @throws EmptyException
	 *             if the dispenser contained no bags when this method was called.
	 */
	public ReusableBag dispense() throws EmptyException {
		if(bags.isEmpty())
			throw new EmptyException("Out of bags");

		ReusableBag bag = bags.remove(bags.size() - 1);
		notifyBagDispensed();

		if(bags.isEmpty())
			notifyOutOfBags();

		return bag;
	}

	private void notifyBagsLoaded(int count) {
		for(ReusableBagDispenserObserver observer : observers)
			observer.bagsLoaded(this, count);
	}

	private void notifyBagDispensed() {
		for(ReusableBagDispenserObserver observer : observers)
			observer.bagDispensed(this);
	}

	private void notifyOutOfBags() {
		for(ReusableBagDispenserObserver observer : observers)
			observer.outOfBags(this);
	}
}
