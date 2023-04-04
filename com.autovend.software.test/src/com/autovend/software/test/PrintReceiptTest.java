
package com.autovend.software.test;

import com.autovend.Barcode;
import com.autovend.BarcodedUnit;
import com.autovend.Numeral;
import com.autovend.devices.DisabledException;
import com.autovend.devices.EmptyException;
import com.autovend.devices.OverloadException;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.external.ProductDatabases;
import com.autovend.products.BarcodedProduct;
import com.autovend.software.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;

import static org.junit.Assert.*;

public class PrintReceiptTest {
    Currency currency = Currency.getInstance("CAD");
    int[] billDenominations = {1, 2, 5, 10};
    BigDecimal[] coinDenominations = {BigDecimal.TEN};

    private SelfCheckoutStation station;
    private BigDecimal price1, price2, price3;
    private Double weight1, weight2, weight3;
    private String description1, description2, description3;
    private BarcodedProduct itemProduct1, itemProduct2, itemProduct3;
    private BarcodedUnit unitItem1, unitItem2, unitItem3;
    private int maxScaleWeight, sensitivity;
    private PrinterController receiptController;
    boolean scanFailed1, scanFailed2, scanFailed3;
    private ArrayList<BarcodedProduct> itemList;
    private ScanItems scanItems;
    private WeightDiscrepancy weightDiscrepancy;
    private ReceiptPrinterObserverStub observer;
    private PurchasedItems itemsPurchased;

    // initializing some barcodes to use during tests
    Numeral[] n = {Numeral.one, Numeral.two, Numeral.three};
    Numeral[] m = {Numeral.two, Numeral.three, Numeral.one};
    Numeral[] k = {Numeral.three, Numeral.two, Numeral.one};
    Barcode b1 = new Barcode(n);
    Barcode b2 = new Barcode(m);
    Barcode b3 = new Barcode(k);
    @Before
    public void setUp() {
        currency = Currency.getInstance("CAD");
        itemList = new ArrayList<BarcodedProduct>();

        // initialize a few prices
        price1 = new BigDecimal(2.00);
        price2 = new BigDecimal(3.00);
        price3 = new BigDecimal(4.50);

        //initialize a few weights
        weight1 = 2.0;
        weight2 = 3.5;
        weight3 = 12.0;

        // initialize a few descriptions
        description1 = "Banana";
        description2 = "Apple";
        description3 = "Pineapple";

        //initialize some products
        itemProduct1 = new BarcodedProduct(b1, description1, price1, weight1);
        itemProduct2 = new BarcodedProduct(b2, description2, price2, weight2);
        itemProduct3 = new BarcodedProduct(b3, description3, price3, weight3);

        // initialize some units associated with the barcodes
        unitItem1 = new BarcodedUnit(b1, weight1);
        unitItem2 = new BarcodedUnit(b2, weight2);
        unitItem3 = new BarcodedUnit(b3, weight3);

        scanFailed1 = false;
        scanFailed2 = false;
        scanFailed3 = false;


        // add the products to the database to access
        ProductDatabases.BARCODED_PRODUCT_DATABASE.put(b1, itemProduct1);
        ProductDatabases.BARCODED_PRODUCT_DATABASE.put(b2, itemProduct2);
        ProductDatabases.BARCODED_PRODUCT_DATABASE.put(b3, itemProduct3);

        maxScaleWeight = 50;
        sensitivity = 1;

        // create the station
        station = new SelfCheckoutStation(currency, billDenominations, coinDenominations, maxScaleWeight, sensitivity);

        // initialize purchased items constructor

        // initialize constructor and add each product to the list of products being scanned
        scanItems = new ScanItems(station);
        weightDiscrepancy = new WeightDiscrepancy(station);

        //register the observer and enable scanners
        observer = new ReceiptPrinterObserverStub();
        station.printer.register(observer);
        station.mainScanner.register(scanItems);
        station.mainScanner.enable();
        station.handheldScanner.enable();
        station.handheldScanner.register(scanItems);

        station.baggingArea.register(weightDiscrepancy);

        receiptController = new PrinterController(station);
    }

    @After
    public void tearDown() {
        receiptController = null;
        observer = null;
        station = null;
        PurchasedItems.reset();
    }

    @Test
    public void workingReceiptTest() throws OverloadException, InsufficientResourcesException {

        receiptController.insertPaper(100);
        receiptController.insertInk(100);

        StringBuilder expectedReceipt = new StringBuilder();
        expectedReceipt.append(String.format("%23s\n","-----RECEIPT-----"));
        expectedReceipt.append(String.format("%-9s %20s\n", "ITEMS", "PRICE"));

        while (scanFailed1 == false) {
            scanFailed1 = station.mainScanner.scan(unitItem1);
        }
        station.baggingArea.add(unitItem1);
        while (scanFailed2 == false) {
            scanFailed2 = station.mainScanner.scan(unitItem2);
        }
        station.baggingArea.add(unitItem2);
        while (scanFailed3 == false) {
            scanFailed3 = station.mainScanner.scan(unitItem3);
        }
        station.baggingArea.add(unitItem3);

        for (BarcodedProduct item : scanItems.getPurchasedItems().getListOfProducts()){
            String price = item.getPrice().toString();
            String description = item.getDescription();
            expectedReceipt.append(String.format("%-10s %18s$\n", description, price));
        }

        expectedReceipt.append(String.format("\n%-10s %18s$\n", "TOTAL:",itemsPurchased.getTotalPrice().toString()));
        expectedReceipt.append(String.format("%-10s %17s$", "Change Due:", itemsPurchased.getChange().toString()));
        String expectedReceiptString = expectedReceipt.toString();

        receiptController.printReceipt();
        String receipt = receiptController.getReceipt();

        assertEquals(receipt, expectedReceiptString);
    }



    @Test (expected = InsufficientResourcesException.class)
    public void noSufficientPaperPrintTest() throws OverloadException, InsufficientResourcesException {
        receiptController.insertPaper(1);
        receiptController.insertInk(100);

        while (scanFailed1 == false) {
            scanFailed1 = station.mainScanner.scan(unitItem1);
        }
        station.baggingArea.add(unitItem1);
        while (scanFailed2 == false) {
            scanFailed2 = station.mainScanner.scan(unitItem2);
        }
        station.baggingArea.add(unitItem2);

        receiptController.printReceipt();
    }

    @Test (expected = InsufficientResourcesException.class)
    public void noSufficientInkPrintTest() throws OverloadException, InsufficientResourcesException {
        station.printer.addInk(10);
        station.printer.addPaper(100);

        while (scanFailed1 == false) {
            scanFailed1 = station.mainScanner.scan(unitItem1);
        }
        station.baggingArea.add(unitItem1);
        while (scanFailed2 == false) {
            scanFailed2 = station.mainScanner.scan(unitItem2);
        }
        station.baggingArea.add(unitItem2);

        receiptController.printReceipt();
        assertFalse(observer.hasInk());
    }
}