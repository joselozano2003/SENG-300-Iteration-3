package com.autovend.software.test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.autovend.BarcodedUnit;
import com.autovend.Bill;
import com.autovend.Coin;
import com.autovend.CreditCard;
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
import com.autovend.software.BankIO;
import com.autovend.software.attendant.AttendantController;
import com.autovend.software.attendant.AttendantModel;
import com.autovend.software.attendant.AttendantView;
import com.autovend.software.customer.CustomerController;
import com.autovend.software.customer.CustomerController.State;
import com.autovend.software.customer.CustomerSession;
import com.autovend.software.customer.CustomerStationLogic;

public class SomeBasicTests {

	private SelfCheckoutStation station;
	private ReusableBagDispenser bagDispenser;
	private CustomerController customerSessionController;
	private CustomerSession currentSession;

	private AttendantModel model;
	private AttendantView view;
	private List<CustomerStationLogic> customerStations;
	private AttendantController attendantController;

	private BarcodedProduct barcodeProduct;
	private BarcodedProduct barcodeProduct2;

	private PLUCodedProduct pluProduct;
	private CardIssuer credit;
	private CreditCard creditCard;

	@Before
	public void setUp() throws Exception {

		Calendar date = Calendar.getInstance();
		date.set(Calendar.YEAR, 2024);
		date.set(Calendar.MONTH, 7);
		date.set(Calendar.DAY_OF_MONTH, 4);

		station = Setup.createSelfCheckoutStation();

		bagDispenser = new ReusableBagDispenser(100);
		
		//
		barcodeProduct = Setup.createBarcodedProduct123(1.00, 15, true);
		barcodeProduct2 = Setup.createBarcodedProduct456(2.50, 40, true);

		credit = new CardIssuer("credit");
		BankIO.CARD_ISSUER_DATABASE.put("credit", credit);
		creditCard = new CreditCard("credit", "00000", "Some Guy", "902", "1111", true, true);
		credit.addCardData("00000", "Some Guy", date, "902", BigDecimal.valueOf(100));

		customerSessionController = new CustomerController(station, bagDispenser);
		customerSessionController.startNewSession();
		currentSession = customerSessionController.getCurrentSession();

		model = new AttendantModel();
		view = new AttendantView();
		customerStations = new ArrayList<>();
		attendantController = new AttendantController(model, view, customerStations);
		
		// Add 100 bills to each dispenser
		Setup.fillBillDispensers(station, 100);
		// Add 100 coins to each dispenser
		Setup.fillCoinDispensers(station, 100);
		
		ReceiptPrinter printer = station.printer;
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
		ProductDatabases.BARCODED_PRODUCT_DATABASE.clear();
		ProductDatabases.INVENTORY.clear();
	}

	@Test
	public void payWithValidCoin() throws DisabledException, OverloadException {

		customerSessionController.startAddingItems();

		station.mainScanner
				.scan(new BarcodedUnit(barcodeProduct.getBarcode(), barcodeProduct.getExpectedWeight()));

		customerSessionController.startPaying();

		Coin coin = new Coin(BigDecimal.valueOf(0.05), Setup.getCurrency());
		station.coinSlot.accept(coin);

		assertEquals(coin.getValue(), currentSession.getTotalPaid());

	}

	@Test
	public void payWithValidBill() throws DisabledException, OverloadException {

		customerSessionController.startAddingItems();

		station.mainScanner
				.scan(new BarcodedUnit(barcodeProduct.getBarcode(), barcodeProduct.getExpectedWeight()));

		customerSessionController.startPaying();

		Bill bill = new Bill(10, Setup.getCurrency());
		station.billInput.accept(bill);
		assertEquals(currentSession.getTotalPaid(), BigDecimal.valueOf(bill.getValue()));

	}

	@Test
	public void payWithCardTap() throws IOException {
		customerSessionController.startAddingItems();

		station.mainScanner
				.scan(new BarcodedUnit(barcodeProduct.getBarcode(), barcodeProduct.getExpectedWeight()));

		station.baggingArea
				.add(new BarcodedUnit(barcodeProduct.getBarcode(), barcodeProduct.getExpectedWeight()));

		station.mainScanner
				.scan(new BarcodedUnit(barcodeProduct.getBarcode(), barcodeProduct.getExpectedWeight()));

		station.baggingArea
				.add(new BarcodedUnit(barcodeProduct.getBarcode(), barcodeProduct.getExpectedWeight()));

		station.mainScanner
				.scan(new BarcodedUnit(barcodeProduct2.getBarcode(), barcodeProduct2.getExpectedWeight()));

		station.baggingArea
				.add(new BarcodedUnit(barcodeProduct2.getBarcode(), barcodeProduct2.getExpectedWeight()));

		customerSessionController.startPaying();

		station.cardReader.tap(creditCard);

		assertEquals(currentSession.getTotalCost(), currentSession.getTotalPaid());

	}

	@Test
	public void addItemByScanning() {
		customerSessionController.startAddingItems();

		station.mainScanner
				.scan(new BarcodedUnit(barcodeProduct.getBarcode(), barcodeProduct.getExpectedWeight()));

		station.baggingArea
				.add(new BarcodedUnit(barcodeProduct.getBarcode(), barcodeProduct.getExpectedWeight()));

		assertEquals(1, (double) currentSession.getShoppingCart().get(barcodeProduct), 0.01);

		station.mainScanner
				.scan(new BarcodedUnit(barcodeProduct.getBarcode(), barcodeProduct.getExpectedWeight()));

		assertEquals(2, (double) currentSession.getShoppingCart().get(barcodeProduct), 0.01);

	}

	@Test
	public void changeDispenserTest() {

		customerSessionController.startAddingItems();

		station.mainScanner
				.scan(new BarcodedUnit(barcodeProduct.getBarcode(), barcodeProduct.getExpectedWeight()));

		station.baggingArea
				.add(new BarcodedUnit(barcodeProduct.getBarcode(), barcodeProduct.getExpectedWeight()));

		station.mainScanner
				.scan(new BarcodedUnit(barcodeProduct.getBarcode(), barcodeProduct.getExpectedWeight()));

		station.baggingArea
				.add(new BarcodedUnit(barcodeProduct.getBarcode(), barcodeProduct.getExpectedWeight()));

		station.mainScanner
				.scan(new BarcodedUnit(barcodeProduct2.getBarcode(), barcodeProduct2.getExpectedWeight()));

		station.baggingArea
				.add(new BarcodedUnit(barcodeProduct2.getBarcode(), barcodeProduct2.getExpectedWeight()));

		customerSessionController.startPaying();

		Bill tenDollarBill = new Bill(10, Setup.getCurrency());
		try {
			station.billInput.accept(tenDollarBill);
		} catch (DisabledException | OverloadException e) {

		}

		// Dispense 1 $5 bill and 2 $0.25 coins
		assertEquals(99, station.billDispensers.get(5).size());
		assertEquals(98, station.coinDispensers.get(BigDecimal.valueOf(0.25)).size());

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
	public void weightChangedBaggingArea() {
		customerSessionController.startAddingItems();

		// Scan first unit, and add to bagging
		station.mainScanner
				.scan(new BarcodedUnit(barcodeProduct.getBarcode(), barcodeProduct.getExpectedWeight()));

		station.baggingArea
				.add(new BarcodedUnit(barcodeProduct.getBarcode(), barcodeProduct.getExpectedWeight()));

		try {
			assertEquals(barcodeProduct.getExpectedWeight(), station.baggingArea.getCurrentWeight(), 0.1);
		} catch (OverloadException e) {

		}
		assertEquals(State.ADDING_ITEMS, customerSessionController.getCurrentState());

		// Scan second unit, and also add to bagging
		station.mainScanner
				.scan(new BarcodedUnit(barcodeProduct.getBarcode(), barcodeProduct.getExpectedWeight()));

		station.baggingArea
				.add(new BarcodedUnit(barcodeProduct.getBarcode(), barcodeProduct.getExpectedWeight()));

		try {
			assertEquals(2 * barcodeProduct.getExpectedWeight(), station.baggingArea.getCurrentWeight(),
					0.1);
		} catch (OverloadException e) {

		}

	}

	@Test
	public void weightDiscrepancyBaggingArea() {
		customerSessionController.startAddingItems();

		// Scan first unit, and add to bagging
		station.mainScanner
				.scan(new BarcodedUnit(barcodeProduct.getBarcode(), barcodeProduct.getExpectedWeight()));

		station.baggingArea
				.add(new BarcodedUnit(barcodeProduct.getBarcode(), barcodeProduct.getExpectedWeight() * 2));

		assertEquals(State.DISABLED, customerSessionController.getCurrentState());

	}

}
