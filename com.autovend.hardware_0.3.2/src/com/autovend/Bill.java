package com.autovend;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Currency;

import com.autovend.devices.SimulationException;

/**
 * Instances of this class represent individual bills (bills, paper money
 * units). The value of a bill is assumed to always be a positive integer
 * multiple of the base currency.
 */
public class Bill implements Serializable {
	private static final long serialVersionUID = 2419184157868887632L;
	private int value;
	private Currency currency;

	/**
	 * Constructs a bill.
	 * 
	 * @param value
	 *            The value of the bill, in multiples of the unit of currency.
	 * @param currency
	 *            The currency represented by this bill.
	 * @throws SimulationException
	 *             If the value is &le;0.
	 * @throws SimulationException
	 *             If currency is null.
	 */
	public Bill(int value, Currency currency) {
		if(currency == null)
			throw new SimulationException(new NullPointerException("currency is null"));

		if(value <= 0)
			throw new SimulationException(
				new IllegalArgumentException("The value must be greater than 0: the argument passed was " + value));

		this.value = value;
		this.currency = currency;
	}

	/**
	 * Accessor for the value.
	 * 
	 * @return The value of the bill. Should always be &gt;0.
	 */
	public int getValue() {
		return value;
	}

	/**
	 * Accessor for the currency.
	 * 
	 * @return The currency for this bill. Note that this is not the same as the
	 *             "denomination" (e.g., a Canadian $10 bill is worth 10 Canadian
	 *             dollars, so a Canadian $10 bill would have currency "Canadian
	 *             dollars").
	 */
	public Currency getCurrency() {
		return currency;
	}

	@Override
	public String toString() {
		return Integer.toString(value) + " " + currency;
	}
}
