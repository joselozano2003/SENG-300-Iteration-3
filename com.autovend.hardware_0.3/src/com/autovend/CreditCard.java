package com.autovend;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

import com.autovend.devices.SimulationException;

/**
 * Represents credit cards.
 */
public final class CreditCard extends Card {
	private static final long serialVersionUID = -4003601866119078650L;

	/**
	 * Create a credit card instance.
	 * 
	 * @param type
	 *            The type of the card.
	 * @param number
	 *            The number of the card. This has to be a string of digits.
	 * @param cardholder
	 *            The name of the cardholder.
	 * @param cvv
	 *            The card verification value (CVV), a 3- or 4-digit value often on
	 *            the back of the card. This can be null.
	 * @param pin
	 *            The personal identification number (PIN) for access to the card.
	 *            This can be null if the card has no chip.
	 * @param isTapEnabled
	 *            Whether this card is capable of being tapped.
	 * @param hasChip
	 *            Whether this card has a chip.
	 * @throws SimulationException
	 *             If type, number, or cardholder is null.
	 * @throws SimulationException
	 *             If hasChip is true but pin is null.
	 */
	public CreditCard(String type, String number, String cardholder, String cvv, String pin, boolean isTapEnabled,
		boolean hasChip) {
		super(type, number, cardholder, cvv, pin, isTapEnabled, hasChip);
	}
}
