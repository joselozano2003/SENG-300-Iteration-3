package com.autovend.devices;

import java.util.Arrays;
import java.util.List;

import com.autovend.Coin;
import com.autovend.devices.observers.CoinTrayObserver;

/**
 * Simulates the tray where dispensed coins go for the user to collect them.
 */
public class CoinTray extends AbstractDevice<CoinTrayObserver> implements Acceptor<Coin> {
	private static final long serialVersionUID = -981843992658030426L;
	private Coin[] coins;
	private int nextIndex = 0;

	/**
	 * Creates a coin tray.
	 * 
	 * @param capacity
	 *            The maximum number of coins that this tray can hold without
	 *            overflowing.
	 * @throws SimulationException
	 *             If the capacity is &le;0.
	 */
	public CoinTray(int capacity) {
		if(capacity <= 0)
			throw new SimulationException(new IllegalArgumentException("capacity must be positive."));

		coins = new Coin[capacity];
	}

	/**
	 * Causes the indicated coin to be added to the tray. A "coinAdded" event is
	 * announced to listeners.
	 * 
	 * @param coin
	 *            The coin to add.
	 * @throws SimulationException
	 *             If coin is null.
	 * @throws OverloadException
	 *             If the tray overflows.
	 */
	public boolean accept(Coin coin) throws OverloadException, DisabledException {
		if(coin == null)
			throw new SimulationException(new NullPointerException("coin is null"));

		if(nextIndex < coins.length) {
			coins[nextIndex++] = coin;

			for(CoinTrayObserver observer : observers)
				observer.reactToCoinAddedEvent(this);

			return true;
		}
		else
			throw new OverloadException("The tray has overflowed.");
	}

	/**
	 * Simulates the customer's action of collecting the coins from the tray.
	 * 
	 * @return A list of coins. Cannot be null. May be empty.
	 */
	public List<Coin> collectCoins() {
		List<Coin> result = Arrays.asList(coins);

		coins = new Coin[coins.length];
		nextIndex = 0;

		return result;
	}

	/**
	 * Returns whether this coin receptacle has enough space to accept at least one
	 * more coin: always true. Causes no events.
	 */
	@Override
	public boolean hasSpace() {
		return nextIndex < coins.length;
	}
}
