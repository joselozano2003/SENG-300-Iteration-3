package com.autovend.software.test;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.autovend.Barcode;
import com.autovend.BarcodedUnit;
import com.autovend.Bill;
import com.autovend.Coin;
import com.autovend.CreditCard;
import com.autovend.Numeral;
import com.autovend.ReusableBag;
import com.autovend.devices.BillDispenser;
import com.autovend.devices.CoinDispenser;
import com.autovend.devices.OverloadException;
import com.autovend.devices.ReceiptPrinter;
import com.autovend.devices.ReusableBagDispenser;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.SimulationException;
import com.autovend.external.CardIssuer;
import com.autovend.external.ProductDatabases;
import com.autovend.products.BarcodedProduct;
import com.autovend.products.PLUCodedProduct;
import com.autovend.software.BankIO;
import com.autovend.software.bagging.ReusableBagProduct;
import com.autovend.software.customer.CustomerController;
import com.autovend.software.customer.CustomerSession;
import com.autovend.software.customer.CustomerStationLogic;
import com.autovend.software.item.ProductsDatabase2;
import com.autovend.software.item.TextSearchProduct;
import com.autovend.software.ui.AttendantView;

public class AttendantControllerTest {

    public SelfCheckoutStation selfCheckoutStation;
    public ReusableBagDispenser bagDispenser;
    public CustomerController customerSessionController;
    public CustomerSession currentSession;

    public AttendantModel model;
    public AttendantView view;
    public List<CustomerStationLogic> customerStations;
    public AttendantController attendantController;

    public int[] billDenominations;
    public BigDecimal[] coinDenominations;
    public Currency currency;
    public int scaleMaximumWeight;
    public int scaleSensitivity;

    public BarcodedProduct barcodeProduct;
    public BarcodedProduct barcodeProduct2;
    public ReusableBagProduct bagProduct;

    public PLUCodedProduct pluProduct;
    public CardIssuer credit;
    public CreditCard creditCard;

    @Before
    public void setUp() throws Exception {

        Calendar date = Calendar.getInstance();
        date.set(Calendar.YEAR, 2024);
        date.set(Calendar.MONTH, 7);
        date.set(Calendar.DAY_OF_MONTH, 4);

        // Variables for SelfCheckoutStation constructor
        billDenominations = new int[]{5, 10, 20, 50, 100};
        coinDenominations = new BigDecimal[]{BigDecimal.valueOf(0.05), BigDecimal.valueOf(0.10),
                BigDecimal.valueOf(0.25), BigDecimal.valueOf(1), BigDecimal.valueOf(2)};
        currency = Currency.getInstance("CAD");

        scaleMaximumWeight = 20;
        scaleSensitivity = 1;

        selfCheckoutStation = new SelfCheckoutStation(currency, billDenominations, coinDenominations,
                scaleMaximumWeight, scaleSensitivity);

        bagDispenser = new ReusableBagDispenser(100);
        int n = 0;
        while (n < 100) {
            bagDispenser.load(new ReusableBag());
            n++;
        }

        Numeral[] code1 = {Numeral.one, Numeral.two, Numeral.three, Numeral.four, Numeral.five, Numeral.six};
        Barcode barcode = new Barcode(code1);
        barcodeProduct = new BarcodedProduct(barcode, "product1", new BigDecimal("1.00"), 10);
        ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcode, barcodeProduct);
        ProductDatabases.INVENTORY.put(barcodeProduct, 25);

        Numeral[] code2 = {Numeral.seven, Numeral.eight, Numeral.nine, Numeral.zero, Numeral.one, Numeral.two};
        Barcode barcode2 = new Barcode(code2);
        barcodeProduct2 = new BarcodedProduct(barcode2, "product2", new BigDecimal("2"), 15);
        ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcode2, barcodeProduct2);
        ProductDatabases.INVENTORY.put(barcodeProduct2, 40);

        bagProduct = new ReusableBagProduct();

        credit = new CardIssuer("credit");
        BankIO.CARD_ISSUER_DATABASE.put("credit", credit);
        creditCard = new CreditCard("credit", "00000", "Some Guy", "902", "1111", true, true);
        credit.addCardData("00000", "Some Guy", date, "902", BigDecimal.valueOf(100));

        customerSessionController = new CustomerController(selfCheckoutStation, bagDispenser);
        customerSessionController.startNewSession();
        currentSession = customerSessionController.getCurrentSession();

        model = new AttendantModel();
        view = new AttendantView();
        customerStations = new ArrayList<>();
        attendantController = new AttendantController(model, view);
        // Add 100 bills to each dispenser
        for (int i = 0; i < billDenominations.length; i++) {
            BillDispenser dispenser = selfCheckoutStation.billDispensers.get(billDenominations[i]);
            for (int j = 0; j < 100; j++) {
                Bill bill = new Bill(billDenominations[i], currency);
                try {
                    dispenser.load(bill);
                } catch (SimulationException | OverloadException e) {
                }
            }
        }
        // Add 100 coins to each dispenser

        for (int i = 0; i < coinDenominations.length; i++) {
            CoinDispenser dispenser = selfCheckoutStation.coinDispensers.get(coinDenominations[i]);
            for (int j = 0; j < 100; j++) {
                Coin coin = new Coin(coinDenominations[i], currency);
                try {
                    dispenser.load(coin);
                } catch (SimulationException | OverloadException e) {

                }
            }
        }

        // Load printer with ink and paper
        ReceiptPrinter printer = selfCheckoutStation.printer;
        try {
            // Initialize ink amount to 1000
            printer.addInk(100);
            // Initialize paper amount to 100
            printer.addPaper(100);
        } catch (SimulationException | OverloadException e) {
        }

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testConstructor() {
        AttendantController controller = new AttendantController(model, view);
    }

    @Test (expected = NullPointerException.class)
    public void testConstructorNullModel() {
        AttendantController controller = new AttendantController(null, view);
    }

    @Test (expected = NullPointerException.class)
    public void testConstructorNullModel2() {
        AttendantController controller = new AttendantController(model, null);
    }

    @Test
    public void testAddCustomerStation() {
        CustomerStationLogic station = new CustomerStationLogic(selfCheckoutStation);
        attendantController.addCustomerStation(station);
        assertEquals(1, attendantController.getCustomerStationsManaged().size());
    }

    @Test
    public void addInkToPrinter() {
        CustomerStationLogic station = new CustomerStationLogic(selfCheckoutStation);
        attendantController.addCustomerStation(station);
        attendantController.addInkToStation(0, 10);
        assertEquals(10, station.getController().inkAdded);
    }
    @Test
    public void addPaperToPrinter() {
        CustomerStationLogic station = new CustomerStationLogic(selfCheckoutStation);
        attendantController.addCustomerStation(station);
        attendantController.addPaperToStation(0, 10);
        assertEquals(10, station.getController().paperAdded);
    }

    @Test
    public void testShutDownStation(){
        CustomerStationLogic station = new CustomerStationLogic(selfCheckoutStation);
        attendantController.addCustomerStation(station);
        attendantController.shutDownStation(0);
        CustomerController.State state = station.getController().getCurrentState();
        assertEquals(CustomerController.State.SHUTDOWN, state);
    }

    @Test
    public void testStartUpStation(){
        CustomerStationLogic station = new CustomerStationLogic(selfCheckoutStation);
        attendantController.addCustomerStation(station);
        attendantController.shutDownStation(0);
        attendantController.startUpStation(0);
        CustomerController.State state = station.getController().getCurrentState();
        assertEquals(CustomerController.State.STARTUP, state);
    }

    @Test
    public void testDenyStationUse(){
        CustomerStationLogic station = new CustomerStationLogic(selfCheckoutStation);
        attendantController.addCustomerStation(station);
        attendantController.startUpStation(0);
        attendantController.permitStationUse(0);
        CustomerController.State state = station.getController().getCurrentState();
        assertEquals(CustomerController.State.DISABLED, state);
        attendantController.denyStationUse(0);
        state = station.getController().getCurrentState();
        assertEquals(CustomerController.State.DISABLED, state);
    }

    @Test
    public void testPermitStationUse(){
        CustomerStationLogic station = new CustomerStationLogic(selfCheckoutStation);
        attendantController.addCustomerStation(station);
        attendantController.startUpStation(0);
        attendantController.permitStationUse(0);
        CustomerController.State state = station.getController().getCurrentState();
        assertEquals(CustomerController.State.DISABLED, state);
        attendantController.addPaperToStation(0, 10);
        attendantController.addInkToStation(0, 10);
        attendantController.shutDownStation(0);
        attendantController.startUpStation(0);
        attendantController.permitStationUse(0);
        state = station.getController().getCurrentState();
        assertEquals(CustomerController.State.INITIAL, state);
    }

    @Test
    public void testRemoveItem() {
        CustomerStationLogic station = new CustomerStationLogic(selfCheckoutStation);
        attendantController.addCustomerStation(station);
        attendantController.startUpStation(0);
        attendantController.permitStationUse(0);
        attendantController.addPaperToStation(0, 10);
        attendantController.addInkToStation(0, 10);
        attendantController.startUpStation(0);
        attendantController.permitStationUse(0);
        CustomerController.State state = station.getController().getCurrentState();
        assertEquals(CustomerController.State.INITIAL, state);

        CustomerController customerController = station.getController();
        customerController.startNewSession();
        customerController.onStartAddingItems();
        selfCheckoutStation = customerController.getStation();

        boolean flag = false;
        while (!flag) {
            flag = selfCheckoutStation.mainScanner
                    .scan(new BarcodedUnit(barcodeProduct.getBarcode(), barcodeProduct.getExpectedWeight()));
        }
        selfCheckoutStation.baggingArea
                .add(new BarcodedUnit(barcodeProduct.getBarcode(), barcodeProduct.getExpectedWeight()));

        assertEquals(1, customerController.getCurrentSession().getTotalCost().intValue());

        flag = false;
        while (!flag) {
            flag = selfCheckoutStation.mainScanner
                    .scan(new BarcodedUnit(barcodeProduct2.getBarcode(), barcodeProduct2.getExpectedWeight()));
        }
        selfCheckoutStation.baggingArea
                .add(new BarcodedUnit(barcodeProduct2.getBarcode(), barcodeProduct2.getExpectedWeight()));

        assertEquals(3, customerController.getCurrentSession().getTotalCost().intValue());

        station.removeItemRequest(new BarcodedProduct(barcodeProduct2.getBarcode(), "product2", new BigDecimal("2"), 15), 1);

        attendantController.removeItemfromStation(0, new BarcodedProduct(barcodeProduct2.getBarcode(), "product2", new BigDecimal("2"), 15), 1);
        assertEquals(1, customerController.getCurrentSession().getTotalCost().intValue());
        }

    @Test
    public void addItemWithTextSearch(){
        CustomerStationLogic station = new CustomerStationLogic(selfCheckoutStation);
        attendantController.addCustomerStation(station);
        attendantController.startUpStation(0);
        attendantController.permitStationUse(0);
        attendantController.addPaperToStation(0, 10);
        attendantController.addInkToStation(0, 10);
        attendantController.startUpStation(0);
        attendantController.permitStationUse(0);

        CustomerController customerController = station.getController();
        customerController.startNewSession();
        customerController.onStartAddingItems();

        TextSearchProduct textSearchProduct = new TextSearchProduct("Banana", "banana", new BigDecimal("1"), 10);
        ProductsDatabase2.Products_Textsearch_Keywords_Database.put("Banana", textSearchProduct);
        attendantController.addItemToStationByTextSearch(0, "Banana", 1);
        assertEquals(1, customerController.getCurrentSession().getTotalCost().intValue());

    }
    }