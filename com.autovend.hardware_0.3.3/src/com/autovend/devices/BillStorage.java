package com.autovend.devices;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.autovend.Bill;
import com.autovend.devices.observers.BillStorageObserver;

/**
 * Represents devices that store bills. They only receive bills, not dispense
 * them. To access the bills inside, a human operator needs to physically remove
 * the bills, simulated with the {@link #unload()} method. A
 * {@link #load(Bill...)} method is provided for symmetry.
 */
public class BillStorage extends AbstractDevice<BillStorageObserver> implements Acceptor<Bill> {
	private static final long serialVersionUID = 1883560903694570789L;
	private Bill[] storage;
	private int nextIndex = 0;

	/**
	 * Creates a bill storage unit that can hold the indicated number of bills.
	 * 
	 * @param capacity
	 *            The maximum number of bills that the unit can hold.
	 * @throws SimulationException
	 *             If the capacity is not positive.
	 */
	public BillStorage(int capacity) {
		if(capacity <= 0)
			throw new SimulationException(new IllegalArgumentException("The capacity must be positive."));

		storage = new Bill[capacity];
	}

	/**
	 * Gets the maximum number of bills that this storage unit can hold.
	 * 
	 * @return The capacity.
	 */
	public int getCapacity() {
		return storage.length;
	}

	/**
	 * Gets the current count of bills contained in this storage unit.
	 * 
	 * @return The current count.
	 */
	public int getBillCount() {
		return nextIndex;
	}

	/**
	 * Allows a set of bills to be loaded into the storage unit directly. Existing
	 * bills in the dispenser are not removed. Causes a "billsLoaded" event to be
	 * announced. Disabling has no effect on loading/unloading.
	 * 
	 * @param bills
	 *            A sequence of bills to be added. Each cannot be null.
	 * @throws SimulationException
	 *             if the number of bills to be loaded exceeds the capacity of the
	 *             unit.
	 * @throws SimulationException
	 *             If the bills argument is null.
	 * @throws SimulationException
	 *             If any bill is null.
	 * @throws OverloadException
	 *             If too many bills are stuffed in the unit.
	 */
	public void load(Bill... bills) throws SimulationException, OverloadException {
		if(bills == null)
			throw new SimulationException(new NullPointerException("bills is null"));

		if(bills.length + nextIndex > storage.length)
			throw new OverloadException("You tried to stuff too many bills in the storage unit.");

		for(Bill bill : bills)
			if(bill == null)
				throw new SimulationException(new NullPointerException("No bill may be null"));

		System.arraycopy(bills, 0, storage, nextIndex, bills.length);
		nextIndex += bills.length;

		for(BillStorageObserver observer : observers)
			observer.reactToBillsLoadedEvent(this);
	}

	/**
	 * Unloads bills from the storage unit directly. Causes a "billsUnloaded" event
	 * to be announced.
	 * 
	 * @return A list of the bills unloaded. May be empty. Will never be null.
	 */
	public List<Bill> unload() {
		List<Bill> bills = new ArrayList<>(storage.length);

		for(Bill bill : storage)
			if(bill != null)
				bills.add(bill);

		nextIndex = 0;

		for(BillStorageObserver observer : observers)
			observer.reactToBillsUnloadedEvent(this);

		return bills;
	}

	/**
	 * Causes the indicated bill to be added to the storage unit. If successful, a
	 * "billAdded" event is announced to its listeners. If a successful bill
	 * addition causes the unit to become full, a "billsFull" event is instead
	 * announced to its listeners.
	 * 
	 * @param bill
	 *            The bill to add.
	 * @throws DisabledException
	 *             If the unit is currently disabled.
	 * @throws SimulationException
	 *             If bill is null.
	 * @throws OverloadException
	 *             If the unit is already full.
	 */
	public boolean accept(Bill bill) throws DisabledException, OverloadException {
		if(isDisabled())
			throw new DisabledException();

		if(bill == null)
			throw new SimulationException(new NullPointerException("bill is null"));

		if(nextIndex < storage.length) {
			storage[nextIndex++] = bill;

			if(nextIndex == storage.length)
				for(BillStorageObserver observer : observers)
					observer.reactToBillsFullEvent(this);
			else
				for(BillStorageObserver observer : observers)
					observer.reactToBillAddedEvent(this);
		}
		else
			throw new OverloadException();

		return true;
	}

	@Override
	public boolean hasSpace() {
		return nextIndex < storage.length;
	}
}
