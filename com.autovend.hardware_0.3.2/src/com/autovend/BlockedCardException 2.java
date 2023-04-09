package com.autovend;

import java.io.IOException;

/**
 * Represents exceptions arising from a blocked card.
 */
public class BlockedCardException extends IOException {
	private static final long serialVersionUID = -2534723446886146588L;

	/**
	 * Create an exception.
	 */
	public BlockedCardException() {}
}
