package com.autovend.devices;

/**
 * Represents the situation when a device is emptied but an attempt is made to
 * remove something from it.
 */
public class EmptyException extends Exception {
	private static final long serialVersionUID = 3566954386000387724L;

	/**
	 * Create an exception with no message.
	 */
	public EmptyException() {
		super();
	}

	/**
	 * Create an exception with the indicated message.
	 * 
	 * @param message
	 *            The message to include in the exception.
	 */
	public EmptyException(String message) {
		super(message);
	}
}
