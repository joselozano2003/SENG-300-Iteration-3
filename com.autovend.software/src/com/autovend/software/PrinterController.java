

package com.autovend.software;
import com.autovend.devices.*;
import com.autovend.products.BarcodedProduct;

import java.util.ArrayList;

public class PrinterController {

    private String receipt;
    private final ReceiptPrinterObserverStub rpo;
    ReceiptPrinter printer;
    int inkAdded;
    int paperAdded;
    int inkUsed;
    int paperUsed;
    boolean canPrint;


    public PrinterController(SelfCheckoutStation station) {
        printer = station.printer;
        rpo = new ReceiptPrinterObserverStub();
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
        for (BarcodedProduct item : items){
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
                    if (!rpo.hasPaper()) {
                        printer.disable();
                    }
                    if (!rpo.hasInk()) {
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

    public String getReceipt() { return receipt; }

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

}