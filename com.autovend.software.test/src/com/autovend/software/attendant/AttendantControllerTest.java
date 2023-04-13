package com.autovend.software.attendant;

import com.autovend.*;
import com.autovend.devices.*;
import com.autovend.external.CardIssuer;
import com.autovend.external.ProductDatabases;
import com.autovend.products.BarcodedProduct;
import com.autovend.products.PLUCodedProduct;
import com.autovend.software.BankIO;
import com.autovend.software.attendant.AttendantController;
import com.autovend.software.attendant.AttendantModel;
import com.autovend.software.bagging.ReusableBagProduct;
import com.autovend.software.customer.CustomerController;
import com.autovend.software.customer.CustomerSession;
import com.autovend.software.customer.CustomerStationLogic;
import com.autovend.software.ui.AttendantView;
import com.autovend.software.ui.CustomerView;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class AttendantControllerTest {

    public SelfCheckoutStation selfCheckoutStation;
    public ReusableBagDispenser bagDispenser;
    public CustomerController customerSessionController;
    public CustomerSession currentSession;

    public AttendantModel model;
    //	public AttendantView view;
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

    //	public PLUView pluView;
    public CustomerView customerView;


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
            customerView = new CustomerView();
            customerSessionController = new CustomerController(selfCheckoutStation, bagDispenser, customerView);
            customerSessionController.startNewSession();
            currentSession = customerSessionController.getCurrentSession();
            SupervisionStation supervisionStation = new SupervisionStation();

            model = new AttendantModel();
            AttendantView view = new AttendantView(8);
            customerStations = new ArrayList<>();
            attendantController = new AttendantController(supervisionStation, view);
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

            AttendantView stationView = new AttendantView(1);


            SupervisionStation attendantStation = new SupervisionStation();
            attendantStation.screen.getFrame().add(stationView.loginView);
            attendantStation.screen.getFrame().setVisible(true);


            int[] billDenoms = { 5, 10, 15, 20, 50, 100 };
            BigDecimal[] coinDenoms = { new BigDecimal("0.05"), new BigDecimal("0.10"), new BigDecimal("0.25"),
                    new BigDecimal("1"), new BigDecimal("2") };
            int scaleMaximumWeight = 1000;
            int scaleSensitivity = 1;
            SelfCheckoutStation station = new SelfCheckoutStation(Currency.getInstance("CAD"), billDenoms, coinDenoms,
                    scaleMaximumWeight, scaleSensitivity);

            ReusableBagDispenser dispenser = new ReusableBagDispenser(100);

            int p = 0;
            while (p < 100) {
                dispenser.load(new ReusableBag());
                p++;
            }

            for (int i = 0; i < billDenoms.length; i++) {
                BillDispenser billDispenser = station.billDispensers.get(billDenoms[i]);
                for (int j = 0; j < 100; j++) {
                    Bill bill = new Bill(billDenoms[i], Currency.getInstance("CAD"));
                    try {
                        billDispenser.load(bill);
                    } catch (SimulationException | OverloadException e) {
                    }
                }
            }
            // Add 100 coins to each dispenser

            for (int i = 0; i < coinDenoms.length; i++) {
                CoinDispenser coinDispenser = station.coinDispensers.get(coinDenoms[i]);
                for (int j = 0; j < 100; j++) {
                    Coin coin = new Coin(coinDenoms[i], Currency.getInstance("CAD"));
                    try {
                        coinDispenser.load(coin);
                    } catch (SimulationException | OverloadException e) {

                    }
                }
            }

            station.printer.addInk(1000);
            station.printer.addPaper(1000);
    }


    @After
    public void tearDown() {
    }

    @Test
    public void testRemoveItem() {
        attendantController.addCustomerStation(customerSessionController);
        customerSessionController.setState(CustomerController.State.INITIAL);
        customerSessionController.setState(CustomerController.State.ADDING_ITEMS);
        selfCheckoutStation = customerSessionController.getStation();

        boolean flag = false;
        while (!flag) {
            flag = selfCheckoutStation.mainScanner
                    .scan(new BarcodedUnit(barcodeProduct.getBarcode(), barcodeProduct.getExpectedWeight()));
        }

        selfCheckoutStation.baggingArea.add(new BarcodedUnit(barcodeProduct.getBarcode(), barcodeProduct.getExpectedWeight()));

        assertEquals(1, customerSessionController.getCurrentSession().getTotalCost().intValue());

        flag = false;
        while (!flag) {
            flag = selfCheckoutStation.mainScanner
                    .scan(new BarcodedUnit(barcodeProduct2.getBarcode(), barcodeProduct2.getExpectedWeight()));
        }
        selfCheckoutStation.baggingArea.add(new BarcodedUnit(barcodeProduct2.getBarcode(), barcodeProduct2.getExpectedWeight()));

        selfCheckoutStation.baggingArea
                .add(new BarcodedUnit(barcodeProduct2.getBarcode(), barcodeProduct2.getExpectedWeight()));

        assertEquals(3, customerSessionController.getCurrentSession().getTotalCost().intValue());

        attendantController.removeItemfromStation(0, new BarcodedProduct(barcodeProduct2.getBarcode(), "product2", new BigDecimal("2"), 15), 1);
        assertEquals(1, customerSessionController.getCurrentSession().getTotalCost().intValue());
    }
}