package com.autovend.devices.observers;

import com.autovend.Coin;
import com.autovend.devices.CoinStorage;

/**
 * Observes events emanating from a coin storage unit.
 */
public interface CoinStorageObserver extends AbstractDeviceObserver {
	/**
	 * Announces that the indicated coin storage unit is full of coins.
	 * 
	 * @param unit
	 *            The storage unit where the event occurred.
	 */
	void reactToCoinsFullEvent(CoinStorage unit);

	/**
	 * Announces that a coin has been added to the indicated storage unit.
	 * 
	 * @param unit
	 *            The storage unit where the event occurred.
	 */
	void reactToCoinAddedEvent(CoinStorage unit);

	/**
	 * Announces that the indicated storage unit has been loaded with coins.
	 * Used to simulate direct, physical loading of the unit.
	 * 
	 * @param unit
	 *            The storage unit where the event occurred.
	 */
	void reactToCoinsLoadedEvent(CoinStorage unit);

	/**
	 * Announces that the storage unit has been emptied of coins. Used to
	 * simulate direct, physical unloading of the unit.
	 * 
	 * @param unit
	 *            The storage unit where the event occurred.
	 */
	void reactToCoinsUnloadedEvent(CoinStorage unit);
}
