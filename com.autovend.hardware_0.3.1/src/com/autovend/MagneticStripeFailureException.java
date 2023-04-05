package com.autovend;

import java.io.IOException;

/**
 * Represents exceptions arising from failures of the magnetic stripe.
 */
public class MagneticStripeFailureException extends IOException {
	private static final long serialVersionUID = 7683341395084278222L;

	/**
	 * Create an exception.
	 */
	public MagneticStripeFailureException() {}
}
