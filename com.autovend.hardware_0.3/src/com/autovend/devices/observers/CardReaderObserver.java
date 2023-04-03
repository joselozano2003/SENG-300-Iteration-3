package com.autovend.devices.observers;

import com.autovend.Card.CardData;
import com.autovend.devices.CardReader;

/**
 * Observes events emanating from a coin dispenser.
 */
public interface CardReaderObserver extends AbstractDeviceObserver {
	/**
	 * Announces that a card has been inserted in the indicated card reader.
	 * 
	 * @param reader
	 *            The reader where the event occurred.
	 */
	void reactToCardInsertedEvent(CardReader reader);

	/**
	 * Announces that a card has been removed from the indicated card reader.
	 * 
	 * @param reader
	 *            The reader where the event occurred.
	 */
	void reactToCardRemovedEvent(CardReader reader);

	/**
	 * Announces that a (tap-enabled) card has been tapped on the indicated card
	 * reader.
	 * 
	 * @param reader
	 *            The reader where the event occurred.
	 */
	void reactToCardTappedEvent(CardReader reader);

	/**
	 * Announces that a card has swiped on the indicated card reader.
	 * 
	 * @param reader
	 *            The reader where the event occurred.
	 */
	void reactToCardSwipedEvent(CardReader reader);

	/**
	 * Announces that the data has been read from a card.
	 * 
	 * @param reader
	 *            The reader where the event occurred.
	 * @param data
	 *            The data that was read. Note that this data may be corrupted.
	 */
	void reactToCardDataReadEvent(CardReader reader, CardData data);
}
