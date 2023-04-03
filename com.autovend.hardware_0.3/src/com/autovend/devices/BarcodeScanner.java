package com.autovend.devices;

import java.util.Random;

import com.autovend.Barcode;
import com.autovend.BarcodedUnit;
import com.autovend.SellableUnit;
import com.autovend.devices.observers.BarcodeScannerObserver;

/**
 * A complex device hidden behind a simple simulation. They can scan and that is
 * about all.
 */
public class BarcodeScanner extends AbstractDevice<BarcodeScannerObserver> {
	private static final long serialVersionUID = 4255357977384600359L;

	/**
	 * Create a barcode scanner.
	 */
	public BarcodeScanner() {}

	private Random random = new Random();
	private static final int PROBABILITY_OF_FAILED_SCAN = 10; /* out of 100 */

	/**
	 * Simulates the customer's action of scanning an item. If the scan is
	 * successful, this method returns true, and all registered observers will
	 * receive a "barcodeScanned" event; otherwise, this method returns false and no
	 * event is announced.
	 * 
	 * @param item
	 *            The item to scan. Of course, it will only work if the item has a
	 *            barcode, and maybe not even then.
	 * @return true if the scan is successful; otherwise false.
	 * @throws SimulationException
	 *             If item is null.
	 */
	public boolean scan(SellableUnit item) {
		if(isDisabled())
			throw new DisabledException();

		if(item == null)
			throw new SimulationException(new NullPointerException("item is null"));

		if(item instanceof BarcodedUnit && random.nextInt(100) >= PROBABILITY_OF_FAILED_SCAN) {
			for(BarcodeScannerObserver observer : observers)
				// the barcode is immutable, no need to copy
				observer.reactToBarcodeScannedEvent(this, ((BarcodedUnit)item).getBarcode());

			return true;
		}

		return false;
	}
}
