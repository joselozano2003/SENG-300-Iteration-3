package com.autovend.devices;

import java.util.Arrays;
import java.util.Currency;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import com.autovend.Bill;
import com.autovend.devices.observers.BillValidatorObserver;

/**
 * Represents a device for optically and/or magnetically validating bills. Bills
 * deemed valid are moved to storage; bills deemed invalid are ejected.
 */
public final class BillValidator extends AbstractDevice<BillValidatorObserver>
	implements Acceptor<Bill>, Emitter<Bill> {
	private static final long serialVersionUID = -5694612337178284851L;
	private final Currency currency;
	private final int[] denominations;
	private BidirectionalChannel<Bill> source;
	private UnidirectionalChannel<Bill> sink;

	/**
	 * Creates a bill validator that recognizes bills of the specified denominations
	 * (i.e., values) and currency.
	 * 
	 * @param currency
	 *            The kind of currency to accept.
	 * @param denominations
	 *            An array of the valid bill denominations (like $5, $10, etc.) to
	 *            accept. Each value must be &gt;0 and unique in this array.
	 * @throws SimulationException
	 *             If either argument is null.
	 * @throws SimulationException
	 *             If the denominations array does not contain at least one value.
	 * @throws SimulationException
	 *             If any value in the denominations array is non-positive.
	 * @throws SimulationException
	 *             If any value in the denominations array is non-unique.
	 */
	public BillValidator(Currency currency, int[] denominations) {
		if(currency == null)
			throw new SimulationException(new NullPointerException("currency is null"));

		if(denominations == null)
			throw new SimulationException(new NullPointerException("denominations is null"));

		if(denominations.length < 1)
			throw new SimulationException(new IllegalArgumentException("There must be at least one denomination."));

		this.currency = currency;
		Arrays.sort(denominations);

		HashSet<Integer> set = new HashSet<>();

		for(int denomination : denominations) {
			if(denomination <= 0)
				throw new SimulationException(
					new IllegalArgumentException("Non-positive denomination detected: " + denomination + "."));

			if(set.contains(denomination))
				throw new SimulationException(new IllegalArgumentException(
					"Each denomination must be unique, but " + denomination + " is repeated."));

			set.add(denomination);
		}

		this.denominations = denominations;
	}

	/**
	 * Connects input and output channels to the bill slot. Causes no events.
	 * 
	 * @param source
	 *            The channel from which bills normally arrive for validation, and
	 *            to which invalid bills will be ejected.
	 * @param sink
	 *            The channel to which all valid bills are routed.
	 */
	public void connect(BidirectionalChannel<Bill> source, UnidirectionalChannel<Bill> sink) {
		if(source == null || sink == null)
			throw new SimulationException(new NullPointerException());

		this.source = source;
		this.sink = sink;
	}

	private final Random pseudoRandomNumberGenerator = new Random();
	private static final int PROBABILITY_OF_FALSE_REJECTION = 1; /* out of 100 */

	private boolean isValid(Bill bill) {
		if(currency.equals(bill.getCurrency()))
			for(int denomination : denominations)
				if(denomination == bill.getValue())
					return pseudoRandomNumberGenerator.nextInt(100) >= PROBABILITY_OF_FALSE_REJECTION;

		return false;
	}

	/**
	 * Tells the bill validator that the indicated bill is being inserted. If the
	 * bill is valid, a "validBillDetected" event is announced to its listeners;
	 * otherwise, an "invalidBillDetected" event is announced to its listeners.
	 * <p>
	 * If there is space in the machine to store a valid bill, it is passed to the
	 * sink channel.
	 * </p>
	 * <p>
	 * If there is no space in the machine to store it or the bill is invalid, the
	 * bill is ejected to the source.
	 * </p>
	 * 
	 * @param bill
	 *            The bill to be added. Cannot be null.
	 * @throws DisabledException
	 *             if the bill validator is currently disabled.
	 * @throws SimulationException
	 *             If the bill is null.
	 */
	@Override
	public boolean accept(Bill bill) throws DisabledException {
		if(isDisabled())
			throw new DisabledException();

		if(bill == null)
			throw new SimulationException(new NullPointerException("bill is null"));

		if(isValid(bill)) {
			for(BillValidatorObserver observer : observers)
				observer.reactToValidBillDetectedEvent(this, bill.getCurrency(), bill.getValue());

			if(sink.hasSpace()) {
				try {
					sink.deliver(bill);
				}
				catch(OverloadException e) {
					// Should never happen
					throw new SimulationException(e);
				}
			}
			else {
				try {
					source.eject(bill);
				}
				catch(OverloadException e) {
					// Should never happen
					throw new SimulationException(e);
				}
			}

			return true;
		}
		else {
			for(BillValidatorObserver observer : observers)
				observer.reactToInvalidBillDetectedEvent(this);

			try {
				source.eject(bill);
			}
			catch(OverloadException e) {
				// Should never happen
				throw new SimulationException("Unable to route bill: sink is full");
			}

			return false;
		}
	}

	@Override
	public boolean hasSpace() {
		return true;
	}
}
