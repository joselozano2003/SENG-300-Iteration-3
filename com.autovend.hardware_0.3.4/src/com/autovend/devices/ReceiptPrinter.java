package com.autovend.devices;

import com.autovend.devices.observers.ReceiptPrinterObserver;

/**
 * Represents printers used for printing receipts. A printer has a finite amount
 * of paper (measured in lines that can be printed) and ink (measured in
 * characters that can be printed).
 * <p>
 * Since this is a simulation, each character is assumed to require the same
 * amount of ink (except blanks and newlines) and the font size is fixed.
 * </p>
 */
public class ReceiptPrinter extends AbstractDevice<ReceiptPrinterObserver> {
	private static final long serialVersionUID = 2485932101191989634L;
	private int charactersOfInkRemaining = 0;
	private int linesOfPaperRemaining = 0;
	private StringBuilder sb = new StringBuilder();
	private int charactersOnCurrentLine = 0;

	/**
	 * The maximum amount of ink that the printer can hold.
	 */
	public static final int MAXIMUM_INK = 1 << 20;

	/**
	 * The maximum amount of paper that the printer can hold.
	 */
	public static final int MAXIMUM_PAPER = 1 << 10;

	/**
	 * Represents the maximum number of characters that can fit on one line of the
	 * receipt. This is a simulation, so the font is assumed monospaced and of fixed
	 * size.
	 */
	public final static int CHARACTERS_PER_LINE = 60;

	/**
	 * Creates a receipt printer.
	 */
	public ReceiptPrinter() {}

	/**
	 * Prints a single character to the receipt. Whitespace characters are ignored,
	 * with the exception of ' ' (blank) and '\n', which signals to move to the
	 * start of the next line. If the printer is now out of ink, announces an
	 * "outOfInk" event. If the printer is now out of paper, announces an
	 * "outOfPaper" event.
	 * 
	 * @param c
	 *            The character to print.
	 * @throws SimulationException
	 *             If there is no ink or no paper in the printer.
	 * @throws OverloadException
	 *             If the extra character would spill off the end of the line.
	 * @throws EmptyException
	 *             if there is insufficient paper or ink to print the character.
	 */
	public void print(char c) throws OverloadException, EmptyException {
		if(c == '\n') {
			if(linesOfPaperRemaining > 0) {
				--linesOfPaperRemaining;
				charactersOnCurrentLine = 0;
			}
			else
				throw new EmptyException("There is no paper in the printer.");
		}
		else if(c != ' ' && Character.isWhitespace(c))
			return;
		else if(charactersOnCurrentLine == CHARACTERS_PER_LINE)
			throw new OverloadException("The line is too long. Add a newline");
		else if(!Character.isWhitespace(c)) {
			if(charactersOfInkRemaining == 0)
				throw new EmptyException("There is no ink in the printer.");

			charactersOfInkRemaining--;
		}
		else
			charactersOnCurrentLine++;

		sb.append(c);

		if(charactersOfInkRemaining == 0)
			for(ReceiptPrinterObserver observer : observers)
				observer.reactToOutOfInkEvent(this);

		if(linesOfPaperRemaining == 0)
			for(ReceiptPrinterObserver observer : observers)
				observer.reactToOutOfPaperEvent(this);
	}

	/**
	 * The receipt is finished printing, so cut it so that the customer can easily
	 * remove it. Failure to cut the paper means that the receipt will not be
	 * retrievable by the customer.
	 */
	public void cutPaper() {
		lastReceipt = sb.toString();
	}

	private String lastReceipt = null;

	/**
	 * Simulates the customer removing the receipt. Failure to cut the receipt
	 * first, or to always remove the receipt means that the customer will end up
	 * with other customers' receipts too!
	 * 
	 * @return The receipt if it has been cut; otherwise, null.
	 */
	public String removeReceipt() {
		String receipt = lastReceipt;

		if(lastReceipt != null) {
			lastReceipt = null;
			sb = new StringBuilder();
		}

		return receipt;
	}

	/**
	 * Adds ink to the printer. Simulates a human doing the adding.
	 * 
	 * @param quantity
	 *            The quantity of characters-worth of ink to add.
	 * @throws SimulationException
	 *             If the quantity is negative.
	 * @throws OverloadException
	 *             If the total of the existing ink plus the added quantity is
	 *             greater than the printer's capacity.
	 */
	public void addInk(int quantity) throws OverloadException {
		if(quantity < 0)
			throw new SimulationException("Are you trying to remove ink?");

		if(charactersOfInkRemaining + quantity > MAXIMUM_INK)
			throw new OverloadException("You spilled a bunch of ink!");

		if(quantity > 0) {
			charactersOfInkRemaining += quantity;

			for(ReceiptPrinterObserver observer : observers)
				observer.reactToInkAddedEvent(this);
		}
	}

	/**
	 * Adds paper to the printer. Simulates a human doing the adding.
	 * 
	 * @param units
	 *            The quantity of lines-worth of paper to add.
	 * @throws SimulationException
	 *             If the quantity is negative.
	 * @throws OverloadException
	 *             If the total of the existing paper plus the added quantity is
	 *             greater than the printer's capacity.
	 */
	public void addPaper(int units) throws OverloadException {
		if(units < 0)
			throw new SimulationException("Are you trying to remove paper?");

		if(linesOfPaperRemaining + units > MAXIMUM_PAPER)
			throw new OverloadException("You may have broken the printer, jamming so much in there!");

		if(units > 0) {
			linesOfPaperRemaining += units;

			for(ReceiptPrinterObserver observer : observers)
				observer.reactToPaperAddedEvent(this);
		}
	}
}
