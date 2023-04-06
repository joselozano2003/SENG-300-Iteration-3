package com.autovend.devices;

import com.autovend.Bill;
import com.autovend.devices.observers.BillSlotObserver;

/**
 * Represents a simple bill slot device that can either accept a bill or eject
 * the most recently inserted bill, leaving it dangling until the customer
 * removes it, via {@link #removeDanglingBill()}.
 */
public class BillSlot extends AbstractDevice<BillSlotObserver> implements Acceptor<Bill>, FlowThroughEmitter<Bill> {
	private static final long serialVersionUID = -7533562575934809971L;
	private BidirectionalChannel<Bill> sink;
	private boolean invert;
	private Bill danglingEjectedBill = null;

	/**
	 * Creates a bill slot.
	 * 
	 * @param invert
	 *            If the slot is to be inverted.
	 */
	public BillSlot(boolean invert) {
		this.invert = invert;
	}

	/**
	 * Connects an output channel to the bill slot. Causes no events. This is an
	 * infrastructural method.
	 * 
	 * @param sink
	 *            Where bills are passed into the machine.
	 */
	void connect(BidirectionalChannel<Bill> sink) {
		if(sink == null)
			throw new SimulationException(new NullPointerException());

		this.sink = sink;
	}

	/**
	 * Tells the bill slot that the indicated bill is being inserted. If the slot's
	 * sink can accept the bill, the bill is passed to the sink and a "billInserted"
	 * event is announced to the slot's listeners; otherwise, a "billEjected" event
	 * is announced to the slot's listeners, meaning that the bill is returned to
	 * the user.
	 * 
	 * @param bill
	 *            The bill to be added. Cannot be null.
	 * @throws DisabledException
	 *             if the bill slot is currently disabled.
	 * @throws NullPointerException
	 *             If the bill is null.
	 * @throws OverloadException
	 *             If a bill is dangling from the slot.
	 */
	public boolean accept(Bill bill) throws DisabledException, OverloadException {
		if(bill == null)
			throw new SimulationException(new NullPointerException("bill"));

		if(isDisabled())
			throw new DisabledException();

		if(danglingEjectedBill != null)
			throw new OverloadException("A bill is dangling from the slot. Remove that before adding another.");

		for(BillSlotObserver observer : observers)
			observer.reactToBillInsertedEvent(this);

		if(!invert && sink.hasSpace()) {
			try {
				sink.deliver(bill);
				return true;
			}
			catch(OverloadException e) {
				// Should never happen
				throw new SimulationException(e);
			}
		}
		else {
			danglingEjectedBill = bill;

			for(BillSlotObserver observer : observers)
				observer.reactToBillEjectedEvent(this);
		}

		return false;
	}

	/**
	 * Ejects the indicated bill, leaving it dangling until the customer grabs it.
	 * Announces a "billEjected" event when successful.
	 * 
	 * @param bill
	 *            The bill to be ejected.
	 * @throws DisabledException
	 *             If the device is disabled.
	 * @throws SimulationException
	 *             If the argument is null.
	 * @throws OverloadException
	 *             If a bill is already dangling from the slot.
	 */
	public boolean emit(Bill bill) throws DisabledException, SimulationException, OverloadException {
		if(isDisabled())
			throw new DisabledException();

		if(bill == null)
			throw new SimulationException(new NullPointerException("bill is null"));

		if(danglingEjectedBill != null)
			throw new OverloadException(
				"A bill is already dangling from the slot. Remove that before ejecting another.");

		danglingEjectedBill = bill;

		for(BillSlotObserver observer : observers)
			observer.reactToBillEjectedEvent(this);

		return true;
	}

	/**
	 * Simulates the user removing a bill that is dangling from the slot. Announces
	 * a "billRemoved" event if successful.
	 * 
	 * @return The formerly dangling bill if successful; otherwise null.
	 */
	public Bill removeDanglingBill() {
		if(danglingEjectedBill == null)
			return null;

		Bill b = danglingEjectedBill;
		danglingEjectedBill = null;

		for(BillSlotObserver observer : observers)
			observer.reactToBillRemovedEvent(this);

		return b;
	}

	/**
	 * Tests whether a bill can be accepted by or ejected from this slot.
	 * 
	 * @return True if the slot is not occupied by a dangling bill; otherwise,
	 *             false.
	 */
	public boolean hasSpace() {
		return danglingEjectedBill == null;
	}
}
