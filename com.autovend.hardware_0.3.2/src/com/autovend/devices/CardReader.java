package com.autovend.devices;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

import com.autovend.Card;
import com.autovend.ChipFailureException;
import com.autovend.MagneticStripeFailureException;
import com.autovend.Card.CardData;
import com.autovend.devices.observers.CardReaderObserver;

/**
 * Represents the card reader, capable of tap, chip insert, and swipe. Either
 * the reader or the card may fail, or the data read in can be corrupted, with
 * varying probabilities.
 */
public class CardReader extends AbstractDevice<CardReaderObserver> {
	private static final long serialVersionUID = 6963605438990237161L;
	private boolean cardIsInserted = false;

	/**
	 * Create a card reader.
	 */
	public CardReader() {}

	private final static ThreadLocalRandom random = ThreadLocalRandom.current();
	private final static double PROBABILITY_OF_TAP_FAILURE = 0.01;
	private final static double PROBABILITY_OF_INSERT_FAILURE = 0.01;
	private final static double PROBABILITY_OF_SWIPE_FAILURE = 0.1;

	/**
	 * Tap the card. Announces a "cardTapped" event when successful. Announces a
	 * "cardDataRead" event when the data is successfully read.
	 * 
	 * @param card
	 *            The card to tap.
	 * @return The card's (possibly corrupted) data, or null if the card data cannot
	 *             be read.
	 * @throws IOException
	 *             If the tap failed (lack of failure does not mean that the data is
	 *             not corrupted).
	 */
	public CardData tap(Card card) throws IOException {
		if(card.isTapEnabled) {
			for(CardReaderObserver observer : observers)
				observer.reactToCardTappedEvent(this);

			if(random.nextDouble(0.0, 1.0) > PROBABILITY_OF_TAP_FAILURE) {
				CardData data = card.tap();

				for(CardReaderObserver observer : observers)
					// The data is immutable so no need to copy it.
					observer.reactToCardDataReadEvent(this, data);

				return data;
			}
			else
				throw new ChipFailureException();
		}

		// else ignore
		return null;
	}

	/**
	 * Swipe the card. Announces a "cardSwiped" event when successful. Announces a
	 * "cardDataRead" event when the data is successfully read.
	 * 
	 * @param card
	 *            The card to swipe.
	 * @param signature
	 *            An image of the customer's signature. (Yes, this workflow is a bit
	 *            off, but hey, it's a simulation.)
	 * @return The card data.
	 * @throws IOException
	 *             If the swipe failed.
	 */
	public CardData swipe(Card card, BufferedImage signature) throws IOException {
		for(CardReaderObserver observer : observers)
			observer.reactToCardSwipedEvent(this);

		if(random.nextDouble(0.0, 1.0) > PROBABILITY_OF_SWIPE_FAILURE) {
			CardData data = card.swipe();

			for(CardReaderObserver observer : observers)
				// The data is immutable so no need to copy it.
				observer.reactToCardDataReadEvent(this, data);

			return data;
		}

		throw new MagneticStripeFailureException();
	}

	/**
	 * Insert the card. Announces a "cardInserted" event when successful. Announces
	 * a "cardDataRead" event when the data is successfully read.
	 * 
	 * @param card
	 *            The card to insert.
	 * @param pin
	 *            The customer's PIN.
	 * @return The card data.
	 * @throws SimulationException
	 *             If there is already a card in the slot.
	 * @throws IOException
	 *             The insertion failed.
	 */
	public CardData insert(Card card, String pin) throws IOException {
		if(cardIsInserted)
			throw new IllegalStateException("There is already a card in the slot");

		cardIsInserted = true;

		for(CardReaderObserver observer : observers)
			observer.reactToCardInsertedEvent(this);

		if(card.hasChip && random.nextDouble(0.0, 1.0) > PROBABILITY_OF_INSERT_FAILURE) {
			CardData data = card.insert(pin);

			for(CardReaderObserver observer : observers)
				// The data is immutable so no need to copy it.
				observer.reactToCardDataReadEvent(this, data);

			return data;
		}

		throw new ChipFailureException();
	}

	/**
	 * Remove the card from the slot. Announces a "cardRemoved" event.
	 * 
	 * @return true if the removal is successful; otherwise false.
	 * @throws SimulationException
	 *             if there is no card in the reader.
	 */
	public boolean remove() {
		if(!cardIsInserted)
			throw new SimulationException("no card in reader");

		cardIsInserted = false;

		for(CardReaderObserver observer : observers)
			observer.reactToCardRemovedEvent(this);

		return true;
	}
}
