package com.autovend;

import com.autovend.devices.SimulationException;
import com.autovend.products.Product;

/**
 * Represents units for sale, each with a particular barcode and weight.
 */
public class BarcodedUnit extends SellableUnit {
	private static final long serialVersionUID = 3769652085437296243L;
	private Barcode barcode;

	/**
	 * Basic constructor.
	 * 
	 * @param barcode
	 *            The barcode for this unit..
	 * @param weightInGrams
	 *            The weight of this unit.
	 * @throws SimulationException
	 *             If the barcode is null.
	 * @throws SimulationException
	 *             If the weight is &le;0.
	 */
	public BarcodedUnit(Barcode barcode, double weightInGrams) {
		super(weightInGrams);

		if(barcode == null)
			throw new SimulationException(new NullPointerException("barcode is null"));

		this.barcode = barcode;
	}

	/**
	 * Gets the barcode of this unit.
	 * 
	 * @return The barcode.
	 */
	public Barcode getBarcode() {
		return barcode;
	}
}
