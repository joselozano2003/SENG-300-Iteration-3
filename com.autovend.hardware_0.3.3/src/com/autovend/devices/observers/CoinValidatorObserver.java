package com.autovend.devices.observers;

import java.math.BigDecimal;

import com.autovend.Bill;
import com.autovend.Coin;
import com.autovend.devices.CoinValidator;

/**
 * Observes events emanating from a coin validator.
 */
public interface CoinValidatorObserver extends AbstractDeviceObserver {
	/**
	 * An event announcing that the indicated coin has been detected and determined
	 * to be valid.
	 * 
	 * @param validator
	 *            The device on which the event occurred.
	 * @param value
	 *            The value of the coin.
	 */
	void reactToValidCoinDetectedEvent(CoinValidator validator, BigDecimal value);

	/**
	 * An event announcing that a coin has been detected and determined to be
	 * invalid.
	 * 
	 * @param validator
	 *            The device on which the event occurred.
	 */
	void reactToInvalidCoinDetectedEvent(CoinValidator validator);
}
