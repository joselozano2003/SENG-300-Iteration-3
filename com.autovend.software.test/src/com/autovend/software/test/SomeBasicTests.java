package com.autovend.software.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
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
import com.autovend.PriceLookUpCode;
import com.autovend.ReusableBag;
import com.autovend.devices.BillDispenser;
import com.autovend.devices.CoinDispenser;
import com.autovend.devices.DisabledException;
import com.autovend.devices.OverloadException;
import com.autovend.devices.ReceiptPrinter;
import com.autovend.devices.ReusableBagDispenser;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.SimulationException;
import com.autovend.external.CardIssuer;
import com.autovend.external.ProductDatabases;
import com.autovend.products.BarcodedProduct;
import com.autovend.products.PLUCodedProduct;
import com.autovend.products.Product;
import com.autovend.software.BankIO;
import com.autovend.software.attendant.AttendantController;
import com.autovend.software.attendant.AttendantModel;
import com.autovend.software.bagging.ReusableBagProduct;
import com.autovend.software.bagging.WeightDiscrepancyException;
import com.autovend.software.customer.CustomerController;
import com.autovend.software.customer.CustomerController.State;
import com.autovend.software.customer.CustomerSession;
import com.autovend.software.customer.CustomerStationLogic;
import com.autovend.software.ui.CustomerView;
import com.autovend.software.ui.PLUView;

public class SomeBasicTests {

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
		billDenominations = new int[] { 5, 10, 20, 50, 100 };
		coinDenominations = new BigDecimal[] { BigDecimal.valueOf(0.05), BigDecimal.valueOf(0.10),
				BigDecimal.valueOf(0.25), BigDecimal.valueOf(1), BigDecimal.valueOf(2) };
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

		Numeral[] code1 = { Numeral.one, Numeral.two, Numeral.three, Numeral.four, Numeral.five, Numeral.six };
		Barcode barcode = new Barcode(code1);
		barcodeProduct = new BarcodedProduct(barcode, "product1", new BigDecimal("1.00"), 10);
		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcode, barcodeProduct);
		ProductDatabases.INVENTORY.put(barcodeProduct, 25);

		Numeral[] code2 = { Numeral.seven, Numeral.eight, Numeral.nine, Numeral.zero, Numeral.one, Numeral.two };
		Barcode barcode2 = new Barcode(code2);
		barcodeProduct2 = new BarcodedProduct(barcode2, "product2", new BigDecimal("2.50"), 15);
		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcode2, barcodeProduct2);
		ProductDatabases.INVENTORY.put(barcodeProduct2, 40);
		
		Numeral[] code3 = {Numeral.one, Numeral.one, Numeral.one, Numeral.one};
		PriceLookUpCode pluCode = new PriceLookUpCode(code3);
		
		pluProduct = new PLUCodedProduct(pluCode, "PLU Coded Grapes", new BigDecimal("1.00"));
		ProductDatabases.PLU_PRODUCT_DATABASE.put(pluCode, pluProduct);


		bagProduct = new ReusableBagProduct();

		credit = new CardIssuer("credit");
		BankIO.CARD_ISSUER_DATABASE.put("credit", credit);
		creditCard = new CreditCard("credit", "00000", "Some Guy", "902", "1111", true, true);
		credit.addCardData("00000", "Some Guy", date, "902", BigDecimal.valueOf(100));
		
		customerView = new CustomerView();
		
		customerSessionController = new CustomerController(selfCheckoutStation, bagDispenser, customerView);
		currentSession = customerSessionController.getCurrentSession();

		model = new AttendantModel();
	//	view = new AttendantView();
		customerStations = new ArrayList<>();
	//	attendantController = new AttendantController(model, view, customerStations);
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
			// Initialize ink amount to 100
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
	public void payWithValidCoin() throws DisabledException, OverloadException {
		customerSessionController.onStartAddingItems();


		selfCheckoutStation.mainScanner
				.scan(new BarcodedUnit(barcodeProduct.getBarcode(), barcodeProduct.getExpectedWeight()));

		customerSessionController.onStartPaying();

		Coin coin = new Coin(BigDecimal.valueOf(0.05), currency);
		selfCheckoutStation.coinSlot.accept(coin);

	//	assertEquals(coin.getValue(), currentSession.getTotalPaid());

	}

	@Test
	public void payWithValidBill() throws DisabledException, OverloadException, InterruptedException {
		Thread.sleep(5000);
		customerSessionController.onStartAddingItems();


		selfCheckoutStation.mainScanner
				.scan(new BarcodedUnit(barcodeProduct.getBarcode(), barcodeProduct.getExpectedWeight()));

		customerSessionController.onStartPaying();

		Bill bill = new Bill(10, currency);
		selfCheckoutStation.billInput.accept(bill);
		assertEquals(currentSession.getTotalPaid(), BigDecimal.valueOf(bill.getValue()));

	}

	@Test
	public void payWithCardTap() throws IOException, InterruptedException {
		Thread.sleep(5000);
		customerSessionController.onStartAddingItems();



		selfCheckoutStation.mainScanner
				.scan(new BarcodedUnit(barcodeProduct.getBarcode(), barcodeProduct.getExpectedWeight()));

		selfCheckoutStation.baggingArea
				.add(new BarcodedUnit(barcodeProduct.getBarcode(), barcodeProduct.getExpectedWeight()));

		selfCheckoutStation.mainScanner
				.scan(new BarcodedUnit(barcodeProduct.getBarcode(), barcodeProduct.getExpectedWeight()));

		selfCheckoutStation.baggingArea
				.add(new BarcodedUnit(barcodeProduct.getBarcode(), barcodeProduct.getExpectedWeight()));

		selfCheckoutStation.mainScanner
				.scan(new BarcodedUnit(barcodeProduct2.getBarcode(), barcodeProduct2.getExpectedWeight()));

		selfCheckoutStation.baggingArea
				.add(new BarcodedUnit(barcodeProduct2.getBarcode(), barcodeProduct2.getExpectedWeight()));


		customerSessionController.onStartPaying();

		selfCheckoutStation.cardReader.tap(creditCard);

		assertEquals(currentSession.getTotalCost(), currentSession.getTotalPaid());
		

	}

	@Test
	public void addItemByScanning() throws InterruptedException {
		Thread.sleep(5000);
		customerSessionController.onStartAddingItems();



		selfCheckoutStation.mainScanner
				.scan(new BarcodedUnit(barcodeProduct.getBarcode(), barcodeProduct.getExpectedWeight()));

		selfCheckoutStation.baggingArea
				.add(new BarcodedUnit(barcodeProduct.getBarcode(), barcodeProduct.getExpectedWeight()));


		selfCheckoutStation.mainScanner
				.scan(new BarcodedUnit(barcodeProduct.getBarcode(), barcodeProduct.getExpectedWeight()));


	}
	
	@Test
	public void addItemByPLUCode() throws InterruptedException {
		Thread.sleep(5000);
		customerSessionController.onStartAddingItems();

		customerSessionController.getCurrentView().pluView.notifyItemAdded("1111");	
	}

	@Test
	public void changeDispenserTest() throws InterruptedException {
		Thread.sleep(5000);



		selfCheckoutStation.mainScanner
				.scan(new BarcodedUnit(barcodeProduct.getBarcode(), barcodeProduct.getExpectedWeight()));

		selfCheckoutStation.baggingArea
				.add(new BarcodedUnit(barcodeProduct.getBarcode(), barcodeProduct.getExpectedWeight()));

		selfCheckoutStation.mainScanner
				.scan(new BarcodedUnit(barcodeProduct.getBarcode(), barcodeProduct.getExpectedWeight()));

		selfCheckoutStation.baggingArea
				.add(new BarcodedUnit(barcodeProduct.getBarcode(), barcodeProduct.getExpectedWeight()));

		selfCheckoutStation.mainScanner
				.scan(new BarcodedUnit(barcodeProduct2.getBarcode(), barcodeProduct2.getExpectedWeight()));

		selfCheckoutStation.baggingArea
				.add(new BarcodedUnit(barcodeProduct2.getBarcode(), barcodeProduct2.getExpectedWeight()));

		customerSessionController.onStartPaying();

		Bill tenDollarBill = new Bill(10, currency);
		try {
			selfCheckoutStation.billInput.accept(tenDollarBill);
		} catch (DisabledException | OverloadException e) {

		}

		// Dispense 1 $5 bill and 2 $0.25 coins
		assertEquals(selfCheckoutStation.billDispensers.get(5).size(), 99);
		assertEquals(selfCheckoutStation.coinDispensers.get(BigDecimal.valueOf(0.25)).size(), 98);

	}

	// @Test
	/*
	 * public void removeItem() { Barcode barcode = new Barcode(Numeral.eight,
	 * Numeral.one, Numeral.two, Numeral.three); BarcodedProduct product1 = new
	 * BarcodedProduct(barcode, "bar", new BigDecimal("10"), 1); PriceLookUpCode
	 * PLUCode = new PriceLookUpCode(Numeral.one, Numeral.two, Numeral.three,
	 * Numeral.four); PLUCodedProduct product2 = new PLUCodedProduct(PLUCode, "plu",
	 * new BigDecimal("20"));
	 * 
	 * customerSessionController.getItemFacade().addProduct(product1); Product
	 * addedPrdocut1 =
	 * customerSessionController.getItemFacade().getItemList().get(0);
	 * assertFalse(addedPrdocut1 == null);
	 * assertFalse(attendantController.startRemoveItem(customerSessionController.
	 * getItemFacade(), product2));
	 * assertTrue(attendantController.startRemoveItem(customerSessionController.
	 * getItemFacade(), product1)); assertEquals(0,
	 * customerSessionController.getItemFacade().getItemList().size());
	 * 
	 * customerSessionController.getItemFacade().addProduct(product2); Product
	 * addedPrdocut2 =
	 * customerSessionController.getItemFacade().getItemList().get(0);
	 * assertFalse(addedPrdocut2 == null);
	 * assertFalse(attendantController.startRemoveItem(customerSessionController.
	 * getItemFacade(), product1));
	 * assertTrue(attendantController.startRemoveItem(customerSessionController.
	 * getItemFacade(), product2)); assertEquals(0,
	 * customerSessionController.getItemFacade().getItemList().size()); }
	 */

	@Test
	public void weightChangedBaggingArea() throws InterruptedException {
		Thread.sleep(5000);


		// Scan first unit, and add to bagging
		selfCheckoutStation.mainScanner
				.scan(new BarcodedUnit(barcodeProduct.getBarcode(), barcodeProduct.getExpectedWeight()));

		selfCheckoutStation.baggingArea
				.add(new BarcodedUnit(barcodeProduct.getBarcode(), barcodeProduct.getExpectedWeight()));

		try {
			assertEquals(barcodeProduct.getExpectedWeight(), selfCheckoutStation.baggingArea.getCurrentWeight(), 0.1);
		} catch (OverloadException e) {

		}
		assertEquals(State.ADDING_ITEMS, customerSessionController.getCurrentState());

		// Scan second unit, and also add to bagging
		selfCheckoutStation.mainScanner
				.scan(new BarcodedUnit(barcodeProduct.getBarcode(), barcodeProduct.getExpectedWeight()));

		selfCheckoutStation.baggingArea
				.add(new BarcodedUnit(barcodeProduct.getBarcode(), barcodeProduct.getExpectedWeight()));

		try {
			assertEquals(2 * barcodeProduct.getExpectedWeight(), selfCheckoutStation.baggingArea.getCurrentWeight(),
					0.1);
		} catch (OverloadException e) {

		}

	}

	@Test
	public void weightDiscrepancyBaggingArea() {

		// Scan first unit, and add to bagging
		selfCheckoutStation.mainScanner
				.scan(new BarcodedUnit(barcodeProduct.getBarcode(), barcodeProduct.getExpectedWeight()));

		selfCheckoutStation.baggingArea
				.add(new BarcodedUnit(barcodeProduct.getBarcode(), barcodeProduct.getExpectedWeight() * 2));

		assertEquals(State.DISABLED, customerSessionController.getCurrentState());

	}

	@Test
	public void dispenseBagsTest() throws InterruptedException {
		Thread.sleep(5000);

		customerSessionController.onPurchaseBags(2);
		Thread.sleep(3000);

		selfCheckoutStation.baggingArea.add(new ReusableBag());
		selfCheckoutStation.baggingArea.add(new ReusableBag());

		assertEquals(1, currentSession.getShoppingCart().size());

		Thread.sleep(5000);

		customerSessionController.onStartPaying();

		Coin coin = new Coin(BigDecimal.valueOf(1), currency);
		selfCheckoutStation.coinSlot.accept(coin);

	}

}
