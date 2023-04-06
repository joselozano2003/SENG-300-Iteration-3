///* P3-4 Group Members
// *
// * Abdelrhafour, Achraf (30022366)
// * Campos, Oscar (30057153)
// * Cavilla, Caleb (30145972)
// * Crowell, Madeline (30069333)
// * Debebe, Abigia (30134608)
// * Dhuka, Sara Hazrat (30124117)
// * Drissi, Khalen (30133707)
// * Ferreira, Marianna (30147733)
// * Frey, Ben (30088566)
// * Himel, Tanvir (30148868)
// * Huayhualla Arce, Fabricio (30091238)
// * Kacmar, Michael (30113919)
// * Lee, Jeongah (30137463)
// * Li, Ran (10120152)
// * Lokanc, Sam (30114370)
// * Lozano Cetina, Jose Camilo (30144736)
// * Maahdie, Monmoy (30149094)
// * Malik, Akansha (30056048)
// * Mehedi, Abdullah (30154770)
// * Polton, Scott (30138102)
// * Rahman, Saadman (30153482)
// * Rodriguez, Gabriel (30162544)
// * Samin Rashid, Khondaker (30143490)
// * Sloan, Jaxon (30123845)
// * Tran, Kevin (30146900)
// */
//package com.autovend.software.test;
//
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.Currency;
//
//import com.autovend.software.WeightDiscrepancy;
//import org.junit.After;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//
//import com.autovend.Barcode;
//import com.autovend.BarcodedUnit;
//import com.autovend.Numeral;
//import com.autovend.devices.DisabledException;
//import com.autovend.devices.OverloadException;
//import com.autovend.devices.SelfCheckoutStation;
//import com.autovend.devices.SimulationException;
//import com.autovend.external.ProductDatabases;
//import com.autovend.products.BarcodedProduct;
//import com.autovend.software.PurchasedItems;
//import com.autovend.software.ScanItems;
//
//public class ScanItemsTest {
//
//	Currency currency = Currency.getInstance("CAD");
//	int[] billDenominations = {1, 2, 5, 10};
//	BigDecimal[] coinDenominations = {BigDecimal.TEN};
//	private SelfCheckoutStation selfCheckoutStation;
//	private BigDecimal price1, price2, price3;
//	private Double weight1, weight2, weight3;
//	private String description1, description2, description3;
//	private BarcodedProduct itemProduct1, itemProduct2, itemProduct3;
//	private BarcodedUnit unitItem1, unitItem2, unitItem3;
//	private BigDecimal expectedCartPrice;
//	private int maxScaleWeight, sensitivity;
//	private double expectedBaggingWeight;
//	private ScanItems scanItems;
//	private WeightDiscrepancy weightDiscrepancy;
//	private ArrayList<BarcodedProduct> itemList;
//	private boolean scanFailed1, scanFailed2, scanFailed3;
//
//	// initializing some barcodes to use during tests
//	Numeral[] n = {Numeral.one, Numeral.two, Numeral.three};
//	Numeral[] m = {Numeral.two, Numeral.three, Numeral.one};
//	Numeral[] k = {Numeral.three, Numeral.two, Numeral.one};
//	Barcode b1 = new Barcode(n);
//	Barcode b2 = new Barcode(m);
//	Barcode b3 = new Barcode(k);
//
//	@Before
//	public void setUp() {
//		// expectedCartPrice at start is 0 before scanning anything
//		expectedCartPrice = new BigDecimal(0);
//		expectedBaggingWeight = 0.0;
//		itemList = new ArrayList<BarcodedProduct>();
//
//		// initialize a few prices
//		price1 = new BigDecimal(2.00);
//		price2 = new BigDecimal(3.00);
//		price3 = new BigDecimal(4.50);
//
//		//initialize a few weights
//		weight1 = 2.0;
//		weight2 = 3.5;
//		weight3 = 12.0;
//
//		// initialize a few descriptions
//		description1 = "item1";
//		description2 = "item2";
//		description3 = "item3";
//
//		scanFailed1 = false;
//		scanFailed2 = false;
//		scanFailed3 = false;
//
//		//initialize some products
//		itemProduct1 = new BarcodedProduct(b1, description1, price1, weight1);
//		itemProduct2 = new BarcodedProduct(b2, description2, price2, weight2);
//		itemProduct3 = new BarcodedProduct(b3, description3, price3, weight3);
//
//		// initialize some units associated with the barcodes
//		unitItem1 = new BarcodedUnit(b1, weight1);
//		unitItem2 = new BarcodedUnit(b2, weight2);
//		unitItem3 = new BarcodedUnit(b3, weight3);
//
//		// add the products to the database to access
//		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(b1, itemProduct1);
//		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(b2, itemProduct2);
//		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(b3, itemProduct3);
//
//		maxScaleWeight = 10;
//		sensitivity = 1;
//
//		// create the station
//		selfCheckoutStation = new SelfCheckoutStation(currency, billDenominations, coinDenominations, maxScaleWeight, sensitivity);
//
//		// initialize constructor and add each product to the list of products being scanned
//		scanItems = new ScanItems(selfCheckoutStation);
//		weightDiscrepancy = new WeightDiscrepancy(selfCheckoutStation);
//
//		//register the observer and enable scanners
//		selfCheckoutStation.mainScanner.enable();
//		selfCheckoutStation.mainScanner.register(scanItems);
//		selfCheckoutStation.handheldScanner.enable();
//		selfCheckoutStation.handheldScanner.register(scanItems);
//		selfCheckoutStation.baggingArea.register(weightDiscrepancy);
//
//		//selfCheckoutStation.baggingArea.register(scanItems);
//	}
//
//	@After
//	public void tearDown() {
//		selfCheckoutStation = null;
//		PurchasedItems.reset();
//	}
//
//	@Test
//	public void testExpectedPrice() {
//		// while the item keeps failing the scan, keep trying to scan until successful
//		while (scanFailed1 == false) {
//			scanFailed1 = selfCheckoutStation.mainScanner.scan(unitItem1);
//		}
//		// update expectedCartPrice
//		expectedCartPrice = expectedCartPrice.add(itemProduct1.getPrice());
//		selfCheckoutStation.baggingArea.add(unitItem1);
//
//		while (scanFailed2 == false) {
//			// scan second item, scanner should be enabled again in ScanItems
//			scanFailed2 = selfCheckoutStation.mainScanner.scan(unitItem2);
//		}
//
//		// update cart price
//		expectedCartPrice = expectedCartPrice.add(itemProduct2.getPrice());
//		selfCheckoutStation.baggingArea.add(unitItem2);
//
//		Assert.assertEquals(expectedCartPrice, PurchasedItems.getTotalPrice());
//	}
//
//	// make sure the baggingArea total weight matches the expected weight
//	@Test
//	public void testExpectedWeight() {
//		// while the scan keeps failing, keep trying to scan until it is successful
//		while (scanFailed1 == false) {
//			scanFailed1 = selfCheckoutStation.mainScanner.scan(unitItem1);
//		}
//
//		// update expectedCartWeight
//		selfCheckoutStation.baggingArea.add(unitItem1);
//		expectedBaggingWeight = expectedBaggingWeight + (itemProduct1.getExpectedWeight());
//
//		// while the scan keeps failing, keep trying to scan until it is successful.
//		while (scanFailed2 == false){
//			// scan second item, scanner should be enabled again in ScanItems
//			scanFailed2 = selfCheckoutStation.mainScanner.scan(unitItem2);
//		}
//
//		// update cart weight
//		selfCheckoutStation.baggingArea.add(unitItem2);
//		expectedBaggingWeight = expectedBaggingWeight + (itemProduct2.getExpectedWeight());
//
//		// see if the bagging area weight matches the expected weight
//		try {
//			Assert.assertEquals(expectedBaggingWeight, selfCheckoutStation.baggingArea.getCurrentWeight(),0);
//		}
//		catch (OverloadException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//
//
//	// tests what happens when the same item is scanned twice and added to bagging area twice
//	// should give a simulation exception as an item cannot be added to bagging area twice
//	@Test (expected = SimulationException.class)
//	public void testSameItemScannedTwice() {
//		while (scanFailed1 == false) {
//		scanFailed1 = selfCheckoutStation.mainScanner.scan(unitItem1);
//		}
//		selfCheckoutStation.baggingArea.add(unitItem1);
//		while (scanFailed2 == false) {
//		scanFailed2 = selfCheckoutStation.mainScanner.scan(unitItem1);
//		}
//		selfCheckoutStation.baggingArea.add(unitItem1);
//	}
//
//	// check this test over
//	// reactToOverloadEvent should happen
//	@Test
//	public void testMaxWeightExceeded() throws OverloadException {
//		while (scanFailed1 == false) {
//		scanFailed1 = selfCheckoutStation.mainScanner.scan(unitItem3);
//		}
//		selfCheckoutStation.baggingArea.add(unitItem3);
//	}
//
//	// test what happens when scanner is disabled
//	// expected should be a DisabledException
//	@Test (expected = DisabledException.class)
//	public void testScannerDisabled() {
//		selfCheckoutStation.mainScanner.disable();
//		selfCheckoutStation.handheldScanner.disable();
//		selfCheckoutStation.mainScanner.scan(unitItem1);
//		selfCheckoutStation.baggingArea.add(unitItem1);
//	}
//
//	// test if scanner is re-enabled after a successful scan and bag
//	@Test
//	public void testSuccessfulScanAndBag() {
//		while (scanFailed1 == false) {
//		scanFailed1 = selfCheckoutStation.mainScanner.scan(unitItem1);
//		}
//		selfCheckoutStation.baggingArea.add(unitItem1);
//		while (scanFailed2 == false) {
//		scanFailed2 = selfCheckoutStation.handheldScanner.scan(unitItem2);
//		}
//		selfCheckoutStation.baggingArea.add(unitItem2);
//
//		boolean mainScannerIsDisabled = selfCheckoutStation.mainScanner.isDisabled();
//		boolean handheldScannerIsDisabled = selfCheckoutStation.handheldScanner.isDisabled();
//
//		Assert.assertEquals(false, mainScannerIsDisabled);
//		Assert.assertEquals(false, handheldScannerIsDisabled);
//	}
//
//	// test whether the products are added to the arraylist correctly
//	@Test
//	public void testArrayListOfProducts() {
//		while (scanFailed1 == false) {
//		scanFailed1 = selfCheckoutStation.mainScanner.scan(unitItem1);
//		}
//		selfCheckoutStation.baggingArea.add(unitItem1);
//		while (scanFailed2 == false) {
//		scanFailed2 = selfCheckoutStation.mainScanner.scan(unitItem2);
//		}
//		selfCheckoutStation.baggingArea.add(unitItem2);
//		Assert.assertEquals(itemProduct1, PurchasedItems.getListOfProducts().get(0));
//		Assert.assertEquals(itemProduct2, PurchasedItems.getListOfProducts().get(1));
//	}
//
//	@Test
//	public void paidFullyItShouldNotLetYouAddMore() {
//
//	}
//
//	@Test
//	public void paidPartiallyItShouldLetYouAddMore() {
//
//	}
//
//}
