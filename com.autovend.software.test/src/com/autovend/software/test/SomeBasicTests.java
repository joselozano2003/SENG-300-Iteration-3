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
import com.autovend.ReusableBag;
import com.autovend.devices.DisabledException;
import com.autovend.devices.OverloadException;
import com.autovend.devices.ReceiptPrinter;
import com.autovend.devices.ReusableBagDispenser;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.SimulationException;
import com.autovend.external.CardIssuer;
import com.autovend.external.ProductDatabases;
import com.autovend.products.BarcodedProduct;
import com.autovend.software.BankIO;
import com.autovend.software.attendant.AttendantController;
import com.autovend.software.attendant.AttendantModel;
import com.autovend.software.attendant.AttendantView;
import com.autovend.software.customer.CustomerController;
import com.autovend.software.customer.CustomerController.State;
import com.autovend.software.customer.CustomerSession;
import com.autovend.software.customer.CustomerStationLogic;

public class SomeBasicTests {

	private SelfCheckoutStation selfCheckoutStation;
	private ReusableBagDispenser bagDispenser;
	private CustomerController customerSessionController;
	private CustomerSession currentSession;

	private AttendantModel model;
	private AttendantView view;
	private List<CustomerStationLogic> customerStations;
	
	private BarcodedProduct barcodeProduct;
	private BarcodedProduct barcodeProduct2;
	//private ReusableBagProduct bagProduct;

	//private PLUCodedProduct pluProduct;
	private CardIssuer credit;
	private CreditCard creditCard;

	@Before
	public void setUp() throws Exception {

		Calendar date = Calendar.getInstance();
		date.set(Calendar.YEAR, 2024);
		date.set(Calendar.MONTH, 7);
		date.set(Calendar.DAY_OF_MONTH, 4);

		selfCheckoutStation = Setup.createSelfCheckoutStation();

		bagDispenser = new ReusableBagDispenser(100);
		int n = 0;
		while (n < 100) {
			bagDispenser.load(new ReusableBag());
			n++;
		}
		//bagProduct = new ReusableBagProduct();

		barcodeProduct = Setup.createBarcodedProduct123(1.00, 500, false);
		barcodeProduct2 = Setup.createBarcodedProduct456(2.50, 750, false);
		ProductDatabases.INVENTORY.put(barcodeProduct, 10);
		ProductDatabases.INVENTORY.put(barcodeProduct, 36);

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
		AttendantController attendantController = new AttendantController(model, view, customerStations);
		
		// Add 100 bills to each dispenser
		Setup.fillBillDispensers(selfCheckoutStation, 100);
		// Add 100 coins to each dispenser
		Setup.fillCoinDispensers(selfCheckoutStation, 100);

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
	public void payWithValidCoin() throws DisabledException, OverloadException {

		customerSessionController.startAddingItems();

		selfCheckoutStation.mainScanner
				.scan(new BarcodedUnit(barcodeProduct.getBarcode(), barcodeProduct.getExpectedWeight()));

		customerSessionController.startPaying();

		Coin coin = new Coin(BigDecimal.valueOf(0.05), Setup.getCurrency());
		selfCheckoutStation.coinSlot.accept(coin);

		assertEquals(coin.getValue(), currentSession.getTotalPaid());

	}

	@Test
	public void payWithValidBill() throws DisabledException, OverloadException {

		customerSessionController.startAddingItems();
		boolean flag = false;
		while (!flag){
			flag = selfCheckoutStation.mainScanner
					.scan(new BarcodedUnit(barcodeProduct.getBarcode(), barcodeProduct.getExpectedWeight()));
		}
		customerSessionController.startPaying();

		Bill bill = new Bill(10, Setup.getCurrency());
		selfCheckoutStation.billInput.accept(bill);
		assertEquals(currentSession.getTotalPaid(), BigDecimal.valueOf(bill.getValue()));

	}

	@Test
	public void payWithCardTap() throws IOException {
		customerSessionController.startAddingItems();

		boolean flag = false;
		while (!flag){
			flag = selfCheckoutStation.mainScanner
					.scan(new BarcodedUnit(barcodeProduct.getBarcode(), barcodeProduct.getExpectedWeight()));
		}

		selfCheckoutStation.baggingArea
				.add(new BarcodedUnit(barcodeProduct.getBarcode(), barcodeProduct.getExpectedWeight()));

		flag = false;
		while (!flag) {
			flag = selfCheckoutStation.mainScanner
					.scan(new BarcodedUnit(barcodeProduct.getBarcode(), barcodeProduct.getExpectedWeight()));
		}

		selfCheckoutStation.baggingArea
				.add(new BarcodedUnit(barcodeProduct.getBarcode(), barcodeProduct.getExpectedWeight()));

		flag = false;
		while (!flag) {
			flag = selfCheckoutStation.mainScanner
					.scan(new BarcodedUnit(barcodeProduct2.getBarcode(), barcodeProduct2.getExpectedWeight()));
		}

		selfCheckoutStation.baggingArea
				.add(new BarcodedUnit(barcodeProduct2.getBarcode(), barcodeProduct2.getExpectedWeight()));

		customerSessionController.startPaying();

		selfCheckoutStation.cardReader.tap(creditCard);

		assertEquals(currentSession.getTotalCost(), currentSession.getTotalPaid());

	}

	@Test
	public void addItemByScanning() {
		customerSessionController.startAddingItems();

		boolean flag = false;
		while (!flag){
			flag = selfCheckoutStation.mainScanner
					.scan(new BarcodedUnit(barcodeProduct.getBarcode(), barcodeProduct.getExpectedWeight()));
		}

		selfCheckoutStation.baggingArea
				.add(new BarcodedUnit(barcodeProduct.getBarcode(), barcodeProduct.getExpectedWeight()));

		assertEquals(1, currentSession.getShoppingCart().get(barcodeProduct), 0.01);

		flag = false;
		while (!flag){
			flag = selfCheckoutStation.mainScanner
					.scan(new BarcodedUnit(barcodeProduct.getBarcode(), barcodeProduct.getExpectedWeight()));
		}

		assertEquals(2, (double) currentSession.getShoppingCart().get(barcodeProduct), 0.01);

	}

	@Test
	public void changeDispenserTest() {

		customerSessionController.startAddingItems();

		boolean flag = false;
		while (!flag){
			flag = selfCheckoutStation.mainScanner
					.scan(new BarcodedUnit(barcodeProduct.getBarcode(), barcodeProduct.getExpectedWeight()));
		}

		selfCheckoutStation.baggingArea
				.add(new BarcodedUnit(barcodeProduct.getBarcode(), barcodeProduct.getExpectedWeight()));

		flag = false;
		while (!flag){
			flag = selfCheckoutStation.mainScanner
					.scan(new BarcodedUnit(barcodeProduct.getBarcode(), barcodeProduct.getExpectedWeight()));
		}

		selfCheckoutStation.baggingArea
				.add(new BarcodedUnit(barcodeProduct.getBarcode(), barcodeProduct.getExpectedWeight()));

		flag = false;
		while (!flag){
			flag = selfCheckoutStation.mainScanner
					.scan(new BarcodedUnit(barcodeProduct2.getBarcode(), barcodeProduct2.getExpectedWeight()));
		}
		selfCheckoutStation.baggingArea
				.add(new BarcodedUnit(barcodeProduct2.getBarcode(), barcodeProduct2.getExpectedWeight()));

		customerSessionController.startPaying();

		Bill tenDollarBill = new Bill(10, Setup.getCurrency());
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
	public void weightChangedBaggingArea() {
		customerSessionController.startAddingItems();

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
		customerSessionController.startAddingItems();

		// Scan first unit, and add to bagging
		selfCheckoutStation.mainScanner
				.scan(new BarcodedUnit(barcodeProduct.getBarcode(), barcodeProduct.getExpectedWeight()));

		selfCheckoutStation.baggingArea
				.add(new BarcodedUnit(barcodeProduct.getBarcode(), barcodeProduct.getExpectedWeight() * 2));

		assertEquals(State.DISABLED, customerSessionController.getCurrentState());

	}

	@Test
	public void dispenseBagsTest() throws InterruptedException {
		customerSessionController.purchaseBags(2);
		Thread.sleep(1);

		selfCheckoutStation.baggingArea.add(new ReusableBag());
		selfCheckoutStation.baggingArea.add(new ReusableBag());

		assertEquals(1, currentSession.getShoppingCart().size());

		Thread.sleep(1);

		customerSessionController.startPaying();

		Coin coin = new Coin(BigDecimal.valueOf(1), Setup.getCurrency());
		selfCheckoutStation.coinSlot.accept(coin);

	}

}
