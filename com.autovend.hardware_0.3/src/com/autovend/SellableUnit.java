package com.autovend;

import java.io.Serializable;

import com.autovend.devices.SimulationException;
import com.autovend.products.Product;

/**
 * Abstract base class of items for sale, each with a particular weight.
 */
public abstract class SellableUnit implements Serializable {
	private static final long serialVersionUID = 6198610879873594970L;
	private double weightInGrams;

	/**
	 * Constructs an item with the indicated weight.
	 * 
	 * @param weightInGrams
	 *            The weight of the item.
	 * @throws SimulationException
	 *             If the weight is &le;0.
	 */
	protected SellableUnit(double weightInGrams) {
		if(weightInGrams <= 0.0)
			throw new SimulationException(new IllegalArgumentException("The weight has to be positive."));

		this.weightInGrams = weightInGrams;
	}

	/**
	 * The weight of the item, in grams.
	 * 
	 * @return The weight in grams.
	 */
	public double getWeight() {
		return weightInGrams;
	}
}
