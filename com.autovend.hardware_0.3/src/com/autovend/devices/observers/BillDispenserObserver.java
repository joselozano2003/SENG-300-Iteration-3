package com.autovend.devices.observers;

import com.autovend.Bill;
import com.autovend.devices.BillDispenser;

/**
 * Observes events emanating from a bill dispenser.
 */
public interface BillDispenserObserver extends AbstractDeviceObserver {
	/**
	 * Announces that the indicated bill dispenser is full of bills.
	 * 
	 * @param dispenser
	 *             The dispenser where the event occurred.
	 */
	void reactToBillsFullEvent(BillDispenser dispenser);

	/**
	 * Announces that the indicated bill dispenser is empty of bills.
	 * 
	 * @param dispenser
	 *             The dispenser where the event occurred.
	 */
	void reactToBillsEmptyEvent(BillDispenser dispenser);

	/**
	 * Announces that the indicated bill has been added to the indicated bill dispenser.
	 * 
	 * @param dispenser
	 *             The dispenser where the event occurred.
	 * @param bill
	 *             The bill that was added.
	 */
	void reactToBillAddedEvent(BillDispenser dispenser, Bill bill);

	/**
	 * Announces that the indicated bill has been added to the indicated bill dispenser.
	 * 
	 * @param dispenser
	 *             The dispenser where the event occurred.
	 * @param bill
	 *             The bill that was removed.
	 */
	void reactToBillRemovedEvent(BillDispenser dispenser, Bill bill);

	/**
	 * Announces that the indicated sequence of bills has been added to the
	 * indicated bill dispenser. Used to simulate direct, physical loading of the dispenser.
	 * 
	 * @param dispenser
	 *              The dispenser where the event occurred.
	 * @param bills
	 *              The bills that were loaded.
	 */
	void reactToBillsLoadedEvent(BillDispenser dispenser, Bill... bills);

	/**
	 * Announces that the indicated sequence of bills has been removed to the
	 * indicated bill dispenser. Used to simulate direct, physical unloading of the dispenser.
	 * 
	 * @param dispenser
	 *              The dispenser where the event occurred.
	 * @param bills
	 *              The bills that were unloaded.
	 */
	void reactToBillsUnloadedEvent(BillDispenser dispenser, Bill... bills);
}
