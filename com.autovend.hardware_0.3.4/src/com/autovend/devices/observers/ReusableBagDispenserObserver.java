package com.autovend.devices.observers;

import com.autovend.devices.ReusableBagDispenser;

/**
 * Observes events emanating from a reusable-bag dispenser.
 */
public interface ReusableBagDispenserObserver extends AbstractDeviceObserver {
	/**
	 * Announces that a reusable bag has been dispensed.
	 * 
	 * @param dispenser
	 *            The device on which the event occurred.
	 */
	public void bagDispensed(ReusableBagDispenser dispenser);

	/**
	 * Announces that a dispenser has run out of bags to dispense.
	 * 
	 * @param dispenser
	 *            The device on which the event occurred.
	 */
	public void outOfBags(ReusableBagDispenser dispenser);

	/**
	 * Announces that bags have been loaded into a dispenser.
	 * 
	 * @param dispenser
	 *            The device on which the event occurred.
	 * @param count
	 *            The number of bags loaded into the dispenser.
	 */
	public void bagsLoaded(ReusableBagDispenser dispenser, int count);

}
