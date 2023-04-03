package com.autovend.devices.observers;

import com.autovend.devices.ReceiptPrinter;

/**
 * Observes events emanating from a receipt printer.
 */
public interface ReceiptPrinterObserver extends AbstractDeviceObserver {
	/**
	 * Announces that the indicated printer is out of paper.
	 * 
	 * @param printer
	 *            The device from which the event emanated.
	 */
	void reactToOutOfPaperEvent(ReceiptPrinter printer);

	/**
	 * Announces that the indicated printer is out of ink.
	 * 
	 * @param printer
	 *            The device from which the event emanated.
	 */
	void reactToOutOfInkEvent(ReceiptPrinter printer);

	/**
	 * Announces that paper has been added to the indicated printer.
	 * 
	 * @param printer
	 *            The device from which the event emanated.
	 */
	void reactToPaperAddedEvent(ReceiptPrinter printer);

	/**
	 * Announces that ink has been added to the indicated printer.
	 * 
	 * @param printer
	 *            The device from which the event emanated.
	 */
	void reactToInkAddedEvent(ReceiptPrinter printer);
}
