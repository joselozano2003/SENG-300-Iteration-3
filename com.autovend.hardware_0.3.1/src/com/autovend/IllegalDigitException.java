package com.autovend;

/**
 * Signals that an illegal character has been used where a digit (0-9) was
 * expected.
 */
public class IllegalDigitException extends IllegalArgumentException {
	private static final long serialVersionUID = 4331710529464666978L;

	/**
	 * Constructs an exception with an error message.
	 * 
	 * @param message
	 *            The error message to display.
	 */
	public IllegalDigitException(String message) {
		super(message);
	}
}
