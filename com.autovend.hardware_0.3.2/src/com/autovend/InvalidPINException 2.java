package com.autovend;

import java.io.IOException;

/**
 * Represents exceptions arising from entry of an invalid PIN.
 */
public class InvalidPINException extends IOException {
	private static final long serialVersionUID = 4801292319481292575L;

	/**
	 * Create an exception.
	 */
	public InvalidPINException() {}
}
