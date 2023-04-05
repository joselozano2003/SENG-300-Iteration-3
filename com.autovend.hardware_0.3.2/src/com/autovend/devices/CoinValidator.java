package com.autovend.devices;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Currency;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.autovend.Coin;
import com.autovend.devices.observers.CoinValidatorObserver;

/**
 * Represents a device for optically and/or physically validating coins. Coins
 * deemed valid are moved to storage; coins deemed invalid are ejected.
 */
public final class CoinValidator extends AbstractDevice<CoinValidatorObserver> implements Acceptor<Coin> {
	private static final long serialVersionUID = -7740071359530849998L;
	private List<BigDecimal> denominations;
	private UnidirectionalChannel<Coin> rejectionSink, overflowSink;
	private Map<BigDecimal, UnidirectionalChannel<Coin>> standardSinks = null;

	/**
	 * The currency supported by this validator.
	 */
	public final Currency currency;

	/**
	 * Creates a coin validator that recognizes coins of the specified denominations
	 * (i.e., values) and currency.
	 * 
	 * @param currency
	 *            The kind of currency to accept.
	 * @param denominations
	 *            An array of the valid coin denominations (like $0.05, $0.10, etc.)
	 *            to accept. Each value must be &gt;0 and unique in this array.
	 * @throws SimulationException
	 *             If either argument is null.
	 * @throws SimulationException
	 *             If the denominations array does not contain at least one value.
	 * @throws SimulationException
	 *             If any value in the denominations array is non-positive.
	 * @throws SimulationException
	 *             If any value in the denominations array is non-unique.
	 */
	public CoinValidator(Currency currency, List<BigDecimal> denominations) {
		if(currency == null)
			throw new SimulationException(new NullPointerException("currency is null"));

		if(denominations == null)
			throw new SimulationException(new NullPointerException("denominations is null"));

		if(denominations.size() < 1)
			throw new SimulationException(new IllegalArgumentException("There must be at least one denomination."));

		this.currency = currency;
		Collections.sort(denominations);

		standardSinks = new HashMap<>();

		for(BigDecimal denomination : denominations) {
			if(denomination == null)
				throw new SimulationException(new NullPointerException("A denomination is null"));

			if(denomination.compareTo(BigDecimal.ZERO) <= 0)
				throw new SimulationException(
					new IllegalArgumentException("Non-positive denomination detected: " + denomination + "."));

			if(standardSinks.containsKey(denomination))
				throw new SimulationException(new IllegalArgumentException(
					"Each denomination must be unique, but " + denomination + " is repeated."));

			standardSinks.put(denomination, null);
		}

		this.denominations = denominations;
	}

	/**
	 * Connects input and output channels to the coin slot. Causes no events.
	 * 
	 * @param rejectionSink
	 *            The channel to which rejected coins are routed.
	 * @param overflowSink
	 *            The channel to which valid coins are routed when the normal sink
	 *            is full.
	 * @param standardSinks
	 *            The channels to which valid coins are normally routed. There must
	 *            be one sink to correspond to each valid currency denomination, and
	 *            they must be in the same order as the valid denominations.
	 * @throws SimulationException
	 *             If any argument is null.
	 * @throws SimulationException
	 *             If any standard sink is null.
	 * @throws SimulationException
	 *             If the number of standard sinks differs from the number of
	 *             denominations.
	 * @throws SimulationException
	 *             If any sink is used in more than one position.
	 */
	void connect(UnidirectionalChannel<Coin> rejectionSink, Map<BigDecimal, UnidirectionalChannel<Coin>> standardSinks,
		UnidirectionalChannel<Coin> overflowSink) {
		if(rejectionSink == null)
			throw new SimulationException(new NullPointerException("rejectionSink is null"));

		if(overflowSink == null)
			throw new SimulationException(new NullPointerException("overflowSink is null"));

		if(standardSinks == null)
			throw new SimulationException(new NullPointerException("standardSinks is null"));

		if(standardSinks.keySet().size() != denominations.size())
			throw new SimulationException(
				new IllegalArgumentException("The number of standard sinks must equal the number of denominations."));

		this.rejectionSink = rejectionSink;
		this.overflowSink = overflowSink;

		HashSet<UnidirectionalChannel<Coin>> set = new HashSet<>();

		for(BigDecimal denomination : standardSinks.keySet()) {
			UnidirectionalChannel<Coin> sink = standardSinks.get(denomination);
			if(sink == null)
				throw new SimulationException(
					new NullPointerException("The sink for denomination " + denomination + " is null."));
			else {
				if(set.contains(sink))
					throw new SimulationException(new IllegalArgumentException("Each channel must be unique."));

				set.add(sink);
			}
		}

		this.standardSinks = standardSinks;

		if(set.contains(rejectionSink))
			throw new SimulationException(new IllegalArgumentException("Each channel must be unique."));
		else
			set.add(rejectionSink);

		if(set.contains(overflowSink))
			throw new SimulationException(new IllegalArgumentException("Each channel must be unique."));

		this.overflowSink = overflowSink;
	}

	private final Random pseudoRandomNumberGenerator = new Random();
	private static final int PROBABILITY_OF_FALSE_REJECTION = 1; /* out of 100 */

	private boolean isValid(Coin coin) {
		if(currency.equals(coin.getCurrency()))
			for(BigDecimal denomination : denominations)
				if(denomination.equals(coin.getValue()))
					return pseudoRandomNumberGenerator.nextInt(100) >= PROBABILITY_OF_FALSE_REJECTION;

		return false;
	}

	/**
	 * Tells the coin validator that the indicated coin is being inserted. If the
	 * coin is valid, a "validCoinDetected" event is announced to its listeners;
	 * otherwise, an "invalidCoinDetected" event is announced to its listeners.
	 * <p>
	 * If there is space in the machine to store a valid coin, it is passed to the
	 * sink channel corresponding to the denomination of the coin.
	 * </p>
	 * <p>
	 * If the coin is invalid, it should be passed to the rejection sink channel. If
	 * there is no space in the machine to store it, the coin is ejected to the
	 * overflow sink channel. If the channel representing the correct routing fails
	 * to accept the coin, an exception will result.
	 * </p>
	 * 
	 * @param coin
	 *            The coin to be added. Cannot be null.
	 * @throws DisabledException
	 *             If the coin validator is currently disabled.
	 * @throws SimulationException
	 *             If the coin is null.
	 * @throws SimulationException
	 *             If the coin cannot be delivered to the correct sink channel.
	 */
	public boolean accept(Coin coin) throws DisabledException {
		if(isDisabled())
			throw new DisabledException();

		if(coin == null)
			throw new SimulationException(new NullPointerException("coin is null"));

		if(isValid(coin)) {
			for(CoinValidatorObserver observer : observers)
				observer.reactToValidCoinDetectedEvent(this, coin.getValue());

			UnidirectionalChannel<Coin> sink = standardSinks.get(coin.getValue());

			if(sink.hasSpace()) {
				try {
					sink.deliver(coin);
				}
				catch(OverloadException e) {
					// Should never happen
					throw new SimulationException(e);
				}

				return true;
			}
			else {
				if(overflowSink.hasSpace()) {
					try {
						overflowSink.deliver(coin);
					}
					catch(OverloadException e) {
						// Should never happen
						throw new SimulationException(e);
					}
				}
				else {
					throw new SimulationException("Overflow sink is full; unable to determine route forward. "
						+ "In the real world, this would mean that coins are spewing out of the machine, "
						+ "or that the machine is otherwise jammed or even broken.");
				}

				return false;
			}
		}
		else {
			for(CoinValidatorObserver observer : observers)
				observer.reactToInvalidCoinDetectedEvent(this);

			if(rejectionSink.hasSpace()) {
				try {
					rejectionSink.deliver(coin);
				}
				catch(OverloadException e) {
					// Should never happen
					throw new SimulationException(e);
				}
			}
			else {
				if(overflowSink.hasSpace()) {
					try {
						overflowSink.deliver(coin);
					}
					catch(OverloadException e) {
						// Should never happen
						throw new SimulationException(e);
					}
				}
				else {
					throw new SimulationException("Overflow sink is full; unable to determine route forward. "
						+ "In the real world, this would mean that coins are spewing out of the machine, "
						+ "or that the machine is otherwise jammed or even broken.");
				}

				return false;
			}

			return false;
		}
	}

	@Override
	public boolean hasSpace() {
		return true; // Since we cannot know yet where a coin will route, assume that it is OK.
	}
}
