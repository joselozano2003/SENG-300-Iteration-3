package com.autovend.devices.observers;

import java.util.Currency;

import com.autovend.Bill;
import com.autovend.devices.BillValidator;

/**
 * Observes events emanating from a bill validator.
 */
public interface BillValidatorObserver extends AbstractDeviceObserver {
	/**
	 * An event announcing that the indicated bill has been detected and
	 * determined to be valid.
	 * 
	 * @param validator
	 *            The device on which the event occurred.
	 * @param currency
	 *            The kind of currency of the inserted bill.
	 * @param value
	 *            The value of the inserted bill.
	 */
	void reactToValidBillDetectedEvent(BillValidator validator, Currency currency, int value);

	/**
	 * An event announcing that the indicated bill has been detected and
	 * determined to be invalid.
	 * 
	 * @param validator
	 *            The device on which the event occurred.
	 */
	void reactToInvalidBillDetectedEvent(BillValidator validator);
}
