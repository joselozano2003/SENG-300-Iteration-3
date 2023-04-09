package com.autovend;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.concurrent.ThreadLocalRandom;

import com.autovend.Card.CardData;
import com.autovend.Card.CardInsertData;
import com.autovend.devices.SimulationException;

/**
 * Represents gift cards. These gift cards only operate via insertion; thus,
 * they possess a chip.
 */
public final class GiftCard extends Card {
	private static final long serialVersionUID = -8385691174430941679L;

	private Currency currency;
	private BigDecimal remainingBalance;

	/**
	 * Create a gift card instance.
	 * 
	 * @param type
	 *            The type of the card.
	 * @param number
	 *            The number of the card. This has to be a string of digits.
	 * @param pin
	 *            The personal identification number (PIN) for access to the card.
	 *            This can be null if the card has no chip.
	 * @param currency
	 *            The currency of the money to be stored on the card.
	 * @param amount
	 *            The amount of money to be stored on the card.
	 * @throws SimulationException
	 *             If type, number, pin, currency, or amount is null.
	 * @throws SimulationException
	 *             If amount is zero or less.
	 */
	public GiftCard(String type, String number, String pin, Currency currency, BigDecimal amount) {
		super(type, number, "BEARER", "0", pin, false, true, false);

		if(currency == null)
			throw new SimulationException("currency cannot be null");

		if(amount == null)
			throw new SimulationException("amount cannot be null");

		if(BigDecimal.ZERO.compareTo(amount) != -1)
			throw new SimulationException("amount must be greater than zero");

		this.currency = currency;
		this.remainingBalance = amount;
	}

	/**
	 * The data from inserting a gift card.
	 */
	public class GiftCardInsertData extends CardInsertData {
		GiftCardInsertData(String pin) throws InvalidPINException {
			super(pin);
		}

		/**
		 * Obtains the remaining balance on the associated gift card. There is a
		 * possibility that the attempt to read this information will randomly fail.
		 * 
		 * @return The remaining balance or null if the read fails.
		 */
		public final BigDecimal getRemainingBalance() {
			if(random.nextDouble(0.0, 1.0) <= PROBABILITY_OF_CHIP_CORRUPTION)
				return null;

			return remainingBalance;
		}

		/**
		 * Obtains the operating currency of the associated gift card. There is a
		 * possibility that the attempt to read this information will randomly fail.
		 * 
		 * @return The operating currency or null if the read fails.
		 */
		public final Currency getCurrency() {
			if(random.nextDouble(0.0, 1.0) <= PROBABILITY_OF_CHIP_CORRUPTION)
				return null;

			return currency;
		}

		/**
		 * Deducts the indicated cost from the remaining balance on this card. The
		 * deduction will fail if the read/write of the balance fails, if the cost is
		 * negative, or if the cost is greater than the balance. If the the deduction
		 * fails, the balance will remain unchanged.
		 * 
		 * @param cost
		 *            The cost to deduct. Must be greater than zero and not greater than
		 *            the remaining balance.
		 * @return true if the operation succeeds; false, otherwise.
		 * @throws ChipFailureException
		 *             If the chip read fails.
		 */
		public final boolean deduct(BigDecimal cost) throws ChipFailureException {
			if(random.nextDouble(0.0, 1.0) <= PROBABILITY_OF_CHIP_CORRUPTION)
				throw new ChipFailureException();

			if(BigDecimal.ZERO.compareTo(cost) != -1)
				return false;

			if(remainingBalance.compareTo(cost) == -1)
				return false;

			remainingBalance = remainingBalance.subtract(cost);

			return true;
		}
	}

	public GiftCardInsertData createCardInsertData(String pin) throws InvalidPINException {
		return new GiftCardInsertData(pin);
	}
}
