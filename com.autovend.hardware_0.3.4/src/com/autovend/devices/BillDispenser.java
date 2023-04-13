package com.autovend.devices;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.autovend.Bill;
import com.autovend.devices.observers.BillDispenserObserver;

/**
 * Represents a device that holds bills of a particular denomination to dispense
 * them as change.
 * <p>
 * Bill dispensers cannot receive bills from other sources. To simplify the
 * simulation, no check is performed on the value of each bill, meaning it is an
 * external responsibility to ensure the correct routing of bills.
 * </p>
 */
public final class BillDispenser extends AbstractDevice<BillDispenserObserver> implements FromStorageEmitter<Bill> {
	private static final long serialVersionUID = 1404868089331821421L;
	private int maxCapacity;
	private Queue<Bill> queue = new LinkedList<Bill>();
	private UnidirectionalChannel<Bill> sink;

	/**
	 * Creates a bill dispenser with the indicated maximum capacity.
	 * 
	 * @param capacity
	 *            The maximum number of bills that can be stored in the dispenser.
	 *            Must be positive.
	 * @throws SimulationException
	 *             If capacity is not positive.
	 */
	public BillDispenser(int capacity) {
		if(capacity <= 0)
			throw new SimulationException(new IllegalArgumentException("Capacity must be positive: " + capacity));

		this.maxCapacity = capacity;
	}

	/**
	 * Accesses the current number of bills in the dispenser.
	 * 
	 * @return The number of bills currently in the dispenser.
	 */
	public int size() {
		return queue.size();
	}

	/**
	 * Allows a set of bills to be loaded into the dispenser directly. Existing
	 * bills in the dispenser are not removed. Causes a "billsLoaded" event to be
	 * announced.
	 * 
	 * @param bills
	 *            A sequence of bills to be added. Each cannot be null.
	 * @throws OverloadException
	 *             if the number of bills to be loaded exceeds the capacity of the
	 *             dispenser.
	 * @throws SimulationException
	 *             If any bill is null.
	 */
	public void load(Bill... bills) throws SimulationException, OverloadException {
		if(bills == null)
			throw new SimulationException(new NullPointerException("bills"));
		if(maxCapacity < queue.size() + bills.length)
			throw new OverloadException("Capacity of dispenser is exceeded by load");

		for(Bill bill : bills)
			if(bill == null)
				throw new SimulationException(new NullPointerException("A bill is null."));
			else
				queue.add(bill);

		for(BillDispenserObserver observer : observers)
			observer.reactToBillsLoadedEvent(this, bills);
	}

	/**
	 * Unloads bills from the dispenser directly. Causes a "billsUnloaded" event to
	 * be announced.
	 * 
	 * @return A list of the bills unloaded. May be empty. Will never be null.
	 */
	public List<Bill> unload() {
		// Each bill is immutable, so a shallow copy suffices.
		List<Bill> result = new ArrayList<>(queue);
		queue.clear();

		for(BillDispenserObserver observer : observers) {
			Bill[] bills = result.toArray(new Bill[result.size()]); // make new copy each time, to avoid security hole
			observer.reactToBillsUnloadedEvent(this, bills);
		}

		return result;
	}

	/**
	 * Connects an output channel to this bill dispenser. Any existing output
	 * channels are disconnected. Causes no events to be announced. This is an
	 * infrastructural method.
	 * 
	 * @param sink
	 *            The new output device to act as output. Can be null, which leaves
	 *            the channel without an output.
	 */
	void connect(UnidirectionalChannel<Bill> sink) {
		this.sink = sink;
	}

	/**
	 * Returns the maximum capacity of this bill dispenser.
	 * 
	 * @return The capacity. Will be positive.
	 */
	public int getCapacity() {
		return maxCapacity;
	}

	/**
	 * Emits a single bill from this bill dispenser. If successful, a "billRemoved"
	 * event is announced to its listeners. If a successful bill removal causes the
	 * dispenser to become empty, a "billsEmpty" event is instead announced to its
	 * listeners.
	 * 
	 * @throws OverloadException
	 *             if the output channel is unable to accept another bill.
	 * @throws EmptyException
	 *             if no bills are present in the dispenser to release.
	 * @throws DisabledException
	 *             if the dispenser is currently disabled.
	 */
	public boolean emit() throws EmptyException, DisabledException, OverloadException {
		if(isDisabled())
			throw new DisabledException();

		if(queue.size() == 0)
			throw new EmptyException();

		Bill bill = queue.remove();

		if(sink.hasSpace())
			try {
				sink.deliver(bill);
			}
			catch(OverloadException e) {
				// Should never happen
				throw new SimulationException(e);
			}
		else
			throw new OverloadException("The sink is full.");

		if(queue.isEmpty())
			for(BillDispenserObserver observer : observers)
				observer.reactToBillsEmptyEvent(this);
		else
			for(BillDispenserObserver observer : observers)
				observer.reactToBillRemovedEvent(this, bill);
		
		return true;
	}
}
