package com.autovend;

import java.io.IOException;

/**
 * Represents exceptions arising from failures of the chip.
 */
public class ChipFailureException extends IOException {
	private static final long serialVersionUID = 6154978861411818621L;

	/**
	 * Create an exception.
	 */
	public ChipFailureException() {}
}
