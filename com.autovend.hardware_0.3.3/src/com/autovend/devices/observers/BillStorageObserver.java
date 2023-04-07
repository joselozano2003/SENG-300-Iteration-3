package com.autovend.devices.observers;

import com.autovend.Bill;
import com.autovend.devices.BillStorage;

/**
 * Observes events emanating from a bill storage unit.
 */
public interface BillStorageObserver extends AbstractDeviceObserver {
	/**
	 * Announces that the indicated bill storage unit is full of bills.
	 * 
	 * @param unit
	 *            The storage unit where the event occurred.
	 */
	void reactToBillsFullEvent(BillStorage unit);

	/**
	 * Announces that a bill has been added to the indicated storage unit.
	 * 
	 * @param unit
	 *            The storage unit where the event occurred.
	 */
	void reactToBillAddedEvent(BillStorage unit);

	/**
	 * Announces that the indicated storage unit has been loaded with bills.
	 * Used to simulate direct, physical loading of the unit.
	 * 
	 * @param unit
	 *            The storage unit where the event occurred.
	 */
	void reactToBillsLoadedEvent(BillStorage unit);

	/**
	 * Announces that the storage unit has been emptied of bills. Used to
	 * simulate direct, physical unloading of the unit.
	 * 
	 * @param unit
	 *            The storage unit where the event occurred.
	 */
	void reactToBillsUnloadedEvent(BillStorage unit);
}
