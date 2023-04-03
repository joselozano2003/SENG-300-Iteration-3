package com.autovend;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

import com.autovend.devices.SimulationException;

/**
 * Represents membership cards.
 */
public final class MembershipCard extends Card {
	private static final long serialVersionUID = -7052033200582973083L;

	/**
	 * Create a membership card instance.
	 * 
	 * @param type
	 *            The type of the card.
	 * @param number
	 *            The number of the card. This has to be a string of digits.
	 * @param cardholder
	 *            The name of the cardholder.
	 * @param isTapEnabled
	 *            If the card supports being tapped.
	 * @throws SimulationException
	 *             If type, number, or cardholder is null.
	 */
	public MembershipCard(String type, String number, String cardholder, boolean isTapEnabled) {
		super(type, number, cardholder, null, null, isTapEnabled, false);
	}
}
