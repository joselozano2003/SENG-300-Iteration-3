package com.autovend;

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.ThreadLocalRandom;

import com.autovend.devices.SimulationException;

/**
 * Represents plastic cards (e.g., credit cards, debit cards, membership cards).
 */
public abstract class Card implements Serializable, IBarcoded {
	private static final long serialVersionUID = -5007632679422484609L;
	private final String type;
	private final String number;
	private final String cardholder;
	private final String cvv;
	private final String pin;
	private int failedTrials = 0;
	private boolean isBlocked;
	private Barcode barcode = null; // immutable once set

	/**
	 * If this card supports taps.
	 */
	public final boolean isTapEnabled;

	/**
	 * If this card possesses a chip.
	 */
	public final boolean hasChip;

	/**
	 * If this card possesses a barcode.
	 */
	public final boolean hasBarcode;

	/**
	 * Create a card instance.
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
	 * @param hasBarcode
	 *            Whether this card has a barcode.
	 * @throws SimulationException
	 *             If type, number, or cardholder is null.
	 * @throws SimulationException
	 *             If hasChip is true but pin is null.
	 * @throws SimulationException
	 *             If hasBarcode is true, but any character in number is not a valid
	 *             digit (0-9).
	 */
	public Card(String type, String number, String cardholder, String cvv, String pin, boolean isTapEnabled,
		boolean hasChip, boolean hasBarcode) {
		if(type == null)
			throw new SimulationException(new NullPointerException("type is null"));

		if(number == null)
			throw new SimulationException(new NullPointerException("number is null"));

		if(cardholder == null)
			throw new SimulationException(new NullPointerException("cardholder is null"));

		if(hasChip && pin == null)
			throw new SimulationException(new NullPointerException("hasChip but pin is null"));

		this.type = type;
		this.number = number;
		this.cardholder = cardholder;
		this.cvv = cvv;
		this.pin = pin;
		this.isTapEnabled = isTapEnabled;
		this.hasChip = hasChip;
		this.hasBarcode = hasBarcode;

		if(hasBarcode) {
			int count = number.length();
			Numeral[] numerals = new Numeral[count];

			for(int i = 0; i < count; i++) {
				char digit = number.charAt(i);
				// Subtract one because the array is 0-indexed
				numerals[i] = Numeral.valueOf((byte)Character.getNumericValue(digit));
			}

			barcode = new Barcode(numerals);
		}
	}

	protected static final ThreadLocalRandom random = ThreadLocalRandom.current();
	private static final double PROBABILITY_OF_MAGNETIC_STRIPE_FAILURE = 0.01;
	private static final double PROBABILITY_OF_TAP_FAILURE = 0.005;
	private static final double PROBABILITY_OF_INSERT_FAILURE = 0.001;
	private static final double PROBABILITY_OF_MAGNETIC_STRIPE_CORRUPTION = 0.001;
	protected static final double PROBABILITY_OF_CHIP_CORRUPTION = 0.00001;

	@Override
	public final Barcode getBarcode() throws UnsupportedOperationException {
		if(!hasBarcode)
			throw new UnsupportedOperationException("This card does not possess a barcode");

		return barcode;
	}

	@Override
	public boolean hasBarcode() {
		return hasBarcode;
	}

	/**
	 * Simulates the action of swiping the card.
	 * 
	 * @return The card data.
	 * @throws IOException
	 *             If anything went wrong with the data transfer.
	 */
	public final CardSwipeData swipe() throws IOException {
		if(isBlocked)
			throw new BlockedCardException();

		if(random.nextDouble(0.0, 1.0) <= PROBABILITY_OF_MAGNETIC_STRIPE_FAILURE)
			throw new MagneticStripeFailureException();

		return new CardSwipeData();
	}

	/**
	 * Simulates the action of tapping the card.
	 * 
	 * @return The card data.
	 * @throws IOException
	 *             If anything went wrong with the data transfer.
	 */
	public final CardTapData tap() throws IOException {
		if(isBlocked)
			throw new BlockedCardException();

		if(isTapEnabled) {
			if(random.nextDouble(0.0, 1.0) <= PROBABILITY_OF_TAP_FAILURE)
				throw new TapFailureException();

			return new CardTapData();
		}

		return null;
	}

	/**
	 * Simulates the action of inserting the card.
	 * 
	 * @param pin
	 *            The personal identification number (PIN) being used as an
	 *            authentication.
	 * @return The card data.
	 * @throws IOException
	 *             If anything went wrong with the data transfer.
	 */
	public final CardInsertData insert(String pin) throws IOException {
		if(isBlocked)
			throw new BlockedCardException();

		if(hasChip) {
			if(random.nextDouble(0.0, 1.0) <= PROBABILITY_OF_INSERT_FAILURE)
				throw new ChipFailureException();

			return createCardInsertData(pin);
		}

		return null;
	}

	private String randomize(String original, double probability) {
		if(random.nextDouble(0.0, 1.0) <= probability) {
			int length = original.length();
			int index = random.nextInt(0, length);
			String first;

			if(index == 0)
				first = "";
			else
				first = original.substring(0, index);

			char second = original.charAt(index);
			second++;

			String third;

			if(index == length - 1)
				third = "";
			else
				third = original.substring(index + 1, length);

			return first + second + third;
		}

		return original;
	}

	/**
	 * The abstract base type of card data.
	 */
	public interface CardData {
		/**
		 * Gets the type of the card.
		 * 
		 * @return The type of the card.
		 */
		public String getType();

		/**
		 * Gets the number of the card.
		 * 
		 * @return The number of the card.
		 */
		public String getNumber();

		/**
		 * Gets the cardholder's name.
		 * 
		 * @return The cardholder's name.
		 */
		public String getCardholder();

		/**
		 * Gets the card verification value (CVV) of the card.
		 * 
		 * @return The CVV of the card.
		 * @throws UnsupportedOperationException
		 *             If this operation is unsupported by this object.
		 */
		public String getCVV();
	}

	/**
	 * The data from swiping a card.
	 */
	public class CardSwipeData implements CardData {
		@Override
		public String getType() {
			return randomize(type, PROBABILITY_OF_MAGNETIC_STRIPE_CORRUPTION);
		}

		@Override
		public String getNumber() {
			return randomize(number, PROBABILITY_OF_MAGNETIC_STRIPE_CORRUPTION);
		}

		@Override
		public String getCardholder() {
			return randomize(cardholder, PROBABILITY_OF_MAGNETIC_STRIPE_CORRUPTION);
		}

		@Override
		public String getCVV() {
			throw new UnsupportedOperationException();
		}
	}

	/**
	 * The data from tapping a card.
	 */
	public final class CardTapData implements CardData {
		@Override
		public String getType() {
			return randomize(type, PROBABILITY_OF_CHIP_CORRUPTION);
		}

		@Override
		public String getNumber() {
			return randomize(number, PROBABILITY_OF_CHIP_CORRUPTION);
		}

		@Override
		public String getCardholder() {
			return randomize(cardholder, PROBABILITY_OF_CHIP_CORRUPTION);
		}

		@Override
		public String getCVV() {
			return randomize(cvv, PROBABILITY_OF_CHIP_CORRUPTION);
		}
	}

	/**
	 * The data from inserting a card.
	 */
	public class CardInsertData implements CardData {
		CardInsertData(String pin) throws InvalidPINException {
			if(!testPIN(pin))
				throw new InvalidPINException();
		}

		@Override
		public final String getType() {
			return randomize(type, PROBABILITY_OF_CHIP_CORRUPTION);
		}

		@Override
		public final String getNumber() {
			return randomize(number, PROBABILITY_OF_CHIP_CORRUPTION);
		}

		@Override
		public final String getCardholder() {
			return randomize(cardholder, PROBABILITY_OF_CHIP_CORRUPTION);
		}

		@Override
		public final String getCVV() {
			return randomize(cvv, PROBABILITY_OF_CHIP_CORRUPTION);
		}

		private boolean testPIN(String pinToTest) {
			if(pin.equals(pinToTest)) {
				failedTrials = 0;
				return true;
			}

			if(++failedTrials >= 3)
				isBlocked = true;

			return false;
		}
	}

	/**
	 * Factory method for creating a CardInsertData object. The intention is to
	 * permit subclasses to specialize this functionality.
	 * 
	 * @param pin
	 *            The personal identification number in order to validate this
	 *            request.
	 * @return The object requested.
	 * @throws InvalidPINException
	 *             If the pin is not valid.
	 */
	public CardInsertData createCardInsertData(String pin) throws InvalidPINException {
		return new CardInsertData(pin);
	}
}
