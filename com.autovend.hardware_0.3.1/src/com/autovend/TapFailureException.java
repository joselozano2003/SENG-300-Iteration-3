package com.autovend;

import java.io.IOException;

/**
 * Represents exceptions arising from failures of taps.
 */
public class TapFailureException extends IOException {
	private static final long serialVersionUID = -1944149258542339159L;

	/**
	 * Create an exception.
	 */
	public TapFailureException() {}
}
