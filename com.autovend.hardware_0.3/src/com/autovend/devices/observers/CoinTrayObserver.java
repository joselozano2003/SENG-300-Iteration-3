package com.autovend.devices.observers;

import com.autovend.Coin;
import com.autovend.devices.CoinTray;

/**
 * Observes events emanating from a coin tray. Coin trays are dumb devices so
 * very few kinds of events can be announced by them.
 */
public interface CoinTrayObserver extends AbstractDeviceObserver {
	/**
	 * Announces that a coin has been added to the indicated tray.
	 * 
	 * @param tray
	 *            The tray where the event occurred.
	 */
	void reactToCoinAddedEvent(CoinTray tray);
}
