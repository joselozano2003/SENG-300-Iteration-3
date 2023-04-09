package com.autovend;

/**
 * Abstract type representing objects that potentially possess a barcode.
 */
public interface IBarcoded {
	/**
	 * If this object possesses a barcode.
	 * 
	 * @return true if the object possesses a barcode; false, otherwise.
	 */
	public boolean hasBarcode();

	/**
	 * Obtains the barcode of this object, if it has one.
	 * 
	 * @return The barcode for this object.
	 * @throws UnsupportedOperationException
	 *             If the object does not possess a barcode.
	 * @see #hasBarcode
	 */
	public Barcode getBarcode();
}
