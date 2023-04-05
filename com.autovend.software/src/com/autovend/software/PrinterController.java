/* P3-4 Group Members
 * 
 * Abdelrhafour, Achraf (30022366)
 * Campos, Oscar (30057153)
 * Cavilla, Caleb (30145972)
 * Crowell, Madeline (30069333)
 * Debebe, Abigia (30134608)
 * Dhuka, Sara Hazrat (30124117)
 * Drissi, Khalen (30133707)
 * Ferreira, Marianna (30147733)
 * Frey, Ben (30088566)
 * Himel, Tanvir (30148868)
 * Huayhualla Arce, Fabricio (30091238)
 * Kacmar, Michael (30113919)
 * Lee, Jeongah (30137463)
 * Li, Ran (10120152)
 * Lokanc, Sam (30114370)
 * Lozano Cetina, Jose Camilo (30144736)
 * Maahdie, Monmoy (30149094)
 * Malik, Akansha (30056048)
 * Mehedi, Abdullah (30154770)
 * Polton, Scott (30138102)
 * Rahman, Saadman (30153482)
 * Rodriguez, Gabriel (30162544)
 * Samin Rashid, Khondaker (30143490)
 * Sloan, Jaxon (30123845)
 * Tran, Kevin (30146900)
 */
package com.autovend.software;

import com.autovend.devices.*;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.ReceiptPrinterObserver;
import com.autovend.products.BarcodedProduct;

import java.util.ArrayList;

public class PrinterController implements ReceiptPrinterObserver {

    private String receipt;
    private ReceiptPrinter printer;
    private int inkAdded;
    private int paperAdded;
    private int inkUsed;
    private int paperUsed;
    private boolean canPrint;
    private boolean hasInk;
    private boolean hasPaper;
    
    /**
     * Basic constructor.
     * @param printer The printer to control
     */
    public PrinterController(ReceiptPrinter printer) {
    	if (printer == null)
    		throw new NullPointerException();
        printer.register(this);
        inkAdded = 0;
        paperAdded = 0;
        inkUsed = 0;
        paperUsed = 0;
    }

    public void printReceipt() throws OverloadException, InsufficientResourcesException {
        ArrayList<BarcodedProduct> items = PurchasedItems.getListOfProducts();

        // Give the receipt a title
        String receiptTitle = String.format("%23s\n", "-----RECEIPT-----") + String.format("%-9s %20s\n", "ITEMS", "PRICE");

        // Body of the receipt where the items and prices are included
        StringBuilder receiptItems = new StringBuilder();

        // Iterates through the ArrayList and adds the items and their price to the receipt
        for (BarcodedProduct item : items) {
            String price = item.getPrice().toString();
            String description = item.getDescription();
            receiptItems.append(String.format("%-10s %18s$\n", description, price));
        }

        // End of the receipt, includes the total and change
        String receiptChangeAndTotal = String.format("\n%-10s %18s$\n", "TOTAL:", PurchasedItems.getTotalPrice().toString()) +
                String.format("%-10s %17s$", "Change Due:", PurchasedItems.getChange().toString());

        StringBuilder finalReceipt = new StringBuilder();
        finalReceipt.append(receiptTitle).append(receiptItems).append(receiptChangeAndTotal);

        canPrint = testResources(finalReceipt);
        if (canPrint) {
            try {
                // Print the receipt content, if it runs out of ink or paper then disable the printer
                for (int i = 0; i < finalReceipt.length(); i++) {
                    // These should notify an attendant and disable the printer
                    if (!hasPaper()) {
                        printer.disable();
                    }
                    if (!hasInk()) {
                        printer.disable();
                    }
                    printer.print(finalReceipt.charAt(i));
                }
            } catch (EmptyException e) {
                // Disable the printer and notify attendant
                printer.disable();
            }

            printer.cutPaper();
            receipt = printer.removeReceipt();
        }
        else {
            // Notify the user that there are not enough resources to print the receipt
            throw new InsufficientResourcesException("There are not enough resources to print the receipt.");
        }
    }

    public void insertPaper(int amount) throws OverloadException {
        paperAdded += amount;
        printer.addPaper(amount);
    }

    public void insertInk(int amount) throws OverloadException {
        inkAdded += amount;
        printer.addInk(amount);
    }

    public boolean testResources(StringBuilder sb) {
        int inkCount = 0;
        int paperCount = 0;
        for (int i = 0; i < sb.length(); i++) {
            char c = sb.charAt(i);
            if (!Character.isWhitespace(c)) {
                inkCount++;
            }
            if (c == '\n') {
                paperCount++;
            }
        }
        // Check if the printer has enough resources to print the receipt
        if ((inkCount + inkUsed < inkAdded) && (paperCount + paperUsed < paperAdded)) {
            inkUsed += inkCount;
            paperUsed += paperCount;
            return true;
        }
        else {
            return false;
        }
    }

	@Override
	public void reactToOutOfPaperEvent(ReceiptPrinter printer) {
		hasPaper = false;
		//Notify Attendant
		//Disable Station
	}
	
	@Override
	public void reactToOutOfInkEvent(ReceiptPrinter printer) {
		hasInk = false;
		//Notify Attendant
		//Disable Station
	}
	
	@Override
	public void reactToPaperAddedEvent(ReceiptPrinter printer) {
		hasPaper = true;
		//Enable Station
	}
	
	@Override
	public void reactToInkAddedEvent(ReceiptPrinter printer) {
		hasInk = true;
		//Enable Station
	}
	
	@Override
	public void reactToEnabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {}
	@Override
	public void reactToDisabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {}
	
	/*
	 * Getters and Setters
	 */
	
	public String getReceipt() {
    	return receipt;
    }
	
	public boolean hasPaper() {
    	return hasPaper;
    }
	
    public boolean hasInk() {
    	return hasInk;
    }
    
}