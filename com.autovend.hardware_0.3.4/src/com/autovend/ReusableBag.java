package com.autovend;

/**
 * Represents a single, reusable bag for carrying groceries.
 */
public class ReusableBag extends SellableUnit {
	private static final long serialVersionUID = 5536362950488952784L;
	private static final double idealWeightInGrams = 5.0;

	/**
	 * Default constructor.
	 */
	public ReusableBag() {
		super(idealWeightInGrams);
	}
}
