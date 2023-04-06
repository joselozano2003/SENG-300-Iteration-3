package com.autovend;

import com.autovend.devices.SimulationException;
import com.autovend.products.Product;

/**
 * Represents items for sale, each with a particular barcode and weight.
 */
public class PriceLookUpCodedUnit extends SellableUnit {
	private static final long serialVersionUID = 6482658489467330015L;
	private PriceLookUpCode pluCode;

	/**
	 * Basic constructor.
	 * 
	 * @param pluCode
	 *            The price look up (PLU) code for the unit.
	 * @param weightInGrams
	 *            The weight of the unit.
	 */
	public PriceLookUpCodedUnit(PriceLookUpCode pluCode, double weightInGrams) {
		super(weightInGrams);
		
		if(pluCode == null)
			throw new SimulationException(new NullPointerException("pluCode is null"));
		
		this.pluCode = pluCode;
	}

	/**
	 * Gets the price look up (PLU) code of this item.
	 * 
	 * @return The PLU code.
	 */
	public PriceLookUpCode getPLUCode() {
		return pluCode;
	}
}
