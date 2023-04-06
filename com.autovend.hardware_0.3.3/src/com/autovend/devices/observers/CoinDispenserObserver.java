package com.autovend.devices.observers;

import com.autovend.Coin;
import com.autovend.devices.CoinDispenser;

/**
 * Observes events emanating from a coin dispenser.
 */
public interface CoinDispenserObserver extends AbstractDeviceObserver {
	/**
	 * Announces that the indicated coin dispenser is full of coins.
	 * 
	 * @param dispenser
	 *             The dispenser where the event occurred.
	 */
	void reactToCoinsFullEvent(CoinDispenser dispenser);

	/**
	 * Announces that the indicated coin dispenser is empty of coins.
	 * 
	 * @param dispenser
	 *             The dispenser where the event occurred.
	 */
	void reactToCoinsEmptyEvent(CoinDispenser dispenser);

	/**
	 * Announces that the indicated coin has been added to the indicated coin dispenser.
	 * 
	 * @param dispenser
	 *             The dispenser where the event occurred.
	 * @param coin
	 *             The coin that was added.
	 */
	void reactToCoinAddedEvent(CoinDispenser dispenser, Coin coin);

	/**
	 * Announces that the indicated coin has been added to the indicated coin dispenser.
	 * 
	 * @param dispenser
	 *             The dispenser where the event occurred.
	 * @param coin
	 *             The coin that was removed.
	 */
	void reactToCoinRemovedEvent(CoinDispenser dispenser, Coin coin);

	/**
	 * Announces that the indicated sequence of coins has been added to the
	 * indicated coin dispenser. Used to simulate direct, physical loading of the dispenser.
	 * 
	 * @param dispenser
	 *              The dispenser where the event occurred.
	 * @param coins
	 *              The coins that were loaded.
	 */
	void reactToCoinsLoadedEvent(CoinDispenser dispenser, Coin... coins);

	/**
	 * Announces that the indicated sequence of coins has been removed to the
	 * indicated coin dispenser. Used to simulate direct, physical unloading of the dispenser.
	 * 
	 * @param dispenser
	 *              The dispenser where the event occurred.
	 * @param coins
	 *              The coins that were unloaded.
	 */
	void reactToCoinsUnloadedEvent(CoinDispenser dispenser, Coin... coins);
}
