package com.autovend.devices.observers;

import com.autovend.Coin;
import com.autovend.devices.CoinSlot;

/**
 * Observes events emanating from a coin slot.
 */
public interface CoinSlotObserver extends AbstractDeviceObserver {
	/**
	 * An event announcing that a coin has been inserted.
	 * 
	 * @param slot
	 *             The device on which the event occurred.
	 */
	void reactToCoinInsertedEvent(CoinSlot slot);
}
