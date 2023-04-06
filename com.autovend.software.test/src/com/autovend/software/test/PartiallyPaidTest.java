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
//import static org.junit.Assert.*;
//
//import java.io.IOException;
//import java.math.BigDecimal;
//import java.util.Calendar;
//import java.util.Currency;
//import java.util.GregorianCalendar;
//
//import com.autovend.*;
//import com.autovend.external.ProductDatabases;
//import com.autovend.software.*;
//import org.junit.*;
//import org.junit.Before;
//import org.junit.Test;
//
//import com.autovend.devices.SelfCheckoutStation;
//import com.autovend.devices.SimulationException;
//import com.autovend.external.CardIssuer;
//import com.autovend.products.BarcodedProduct;
//
//public class PartiallyPaidTest {
//
//	private SelfCheckoutStation scs;
//	BigDecimal milkPrice = new BigDecimal(2.50);
//	BigDecimal amountToPay;
//	BigDecimal companyIssue;
//	Numeral[] nMilk = { Numeral.one, Numeral.two, Numeral.three, Numeral.four };
//	Barcode barcodeMilk = new Barcode(nMilk);
//	BarcodedProduct milk = new BarcodedProduct(barcodeMilk, "milk description", milkPrice, 2.00);
//
//	Calendar exipery = new GregorianCalendar(2025, Calendar.OCTOBER, 31);
//	CardIssuer company = new CardIssuer("TD");
//	CardIssuer company2 = new CardIssuer("BMO");
//
//	Currency currency = Currency.getInstance("CAD");
//	int[] billDenominations = { 1, 2, 5, 10 };
//	BigDecimal[] coinDenominations = { BigDecimal.TEN };
//	private BigDecimal price1, price2;
//	private Double weight1, weight2;
//	private String description1, description2;
//	private BarcodedProduct itemProduct1, itemProduct2, itemProduct3;
//	private BarcodedUnit unitItem1, unitItem2;
//	private ScanItems scanItems;
//	private WeightDiscrepancy weightDiscrepancy;
//	Numeral[] n = { Numeral.one, Numeral.two, Numeral.three };
//	Numeral[] m = { Numeral.two, Numeral.three, Numeral.one };
//	Numeral[] k = { Numeral.three, Numeral.two, Numeral.one };
//	Barcode b1 = new Barcode(n);
//	Barcode b2 = new Barcode(m);
//	Barcode b3 = new Barcode(k);
//	// initializing some barcodes to use during tests
//
//	@Before
//	public void setUp() {
//
//		Currency currency = Currency.getInstance("CAD");
//		int[] billDom = { 5, 10, 20 };
//		BigDecimal[] coinDom = { BigDecimal.valueOf(0.05), BigDecimal.valueOf(0.10), BigDecimal.valueOf(0.25) };
//		scs = new SelfCheckoutStation(currency, billDom, coinDom, 10000, 1);
//
//		company.addCardData("1234567890123458", "debit", exipery, "123", BigDecimal.valueOf(100));
//
//		BigDecimal expectedCartPrice = new BigDecimal(0);
//		double expectedBaggingWeight = 0.0;
//
//		// initialize a few prices8
//		price1 = new BigDecimal(2.00);
//		price2 = new BigDecimal(3.00);
//
//		// initialize a few weights
//		weight1 = 2.0;
//		weight2 = 3.5;
//
//		// initialize a few descriptions
//		description1 = "item1";
//		description2 = "item2";
//
//		// initialize some products
//		itemProduct1 = new BarcodedProduct(b1, description1, price1, weight1);
//		itemProduct2 = new BarcodedProduct(b2, description2, price2, weight2);
//		// itemProduct3 = new BarcodedProduct(b3, description3, price3, weight3);
//
//		// initialize some units associated with the barcodes
//		unitItem1 = new BarcodedUnit(b1, weight1);
//		unitItem2 = new BarcodedUnit(b2, weight2);
//		// unitItem3 = new BarcodedUnit(b3, weight3);
//
//		// add the products to the database to access
//		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(b1, itemProduct1);
//		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(b2, itemProduct2);
//		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(b3, itemProduct3);
//
//		int maxScaleWeight = 10;
//		int sensitivity = 1;
//
//		// initialize purchased items constructor
//		PurchasedItems itemsPurchased = new PurchasedItems();
//
//		// initialize constructor and add each product to the list of products being
//		// scanned
//		scanItems = new ScanItems(scs);
//		weightDiscrepancy = new WeightDiscrepancy(scs);
//
//		// register the observers and enable card readers
//		// TODO: register the observers and enable card readers
//
//		// register the observer and enable scanners
//		scs.mainScanner.register(scanItems);
//		scs.mainScanner.enable();
//		scs.handheldScanner.enable();
//		scs.handheldScanner.register(scanItems);
//		scs.baggingArea.register(weightDiscrepancy);
//	}
//
//	@After
//	public void tearDown() {
//		PurchasedItems.reset();
//	}
//
//	@Test
//	public void testPartiallyWithCard() {
//		PayWithCard PayWithCredit = new PayWithCard(scs, company);
//		scs.cardReader.register(PayWithCredit);
//		scs.mainScanner.scan(unitItem1);
//		scs.baggingArea.add(unitItem1);
//
//		CreditCard CreditTap = new CreditCard("Credit", "0234567890223451", "credit", "123", "1234", true, true);
//		company.addCardData("0234567890223451", "Credit", exipery, "123", BigDecimal.valueOf(100));
//
//		try {
//			scs.cardReader.tap(CreditTap);
//		} catch (SimulationException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			throw new RuntimeException(e);
//		}
//
//		assertTrue(PurchasedItems.isPaid());
//
//		scs.mainScanner.scan(unitItem2);
//		scs.baggingArea.add(unitItem2);
//
//		assertFalse(PurchasedItems.isPaid());
//	}
//
//	@Test
//	public void testPartiallyWithCard2() {
//		PayWithCard PayWithCredit2 = new PayWithCard(scs, company);
//		scs.cardReader.register(PayWithCredit2);
//		scs.mainScanner.scan(unitItem1);
//		scs.baggingArea.add(unitItem1);
//
//		CreditCard CreditTap2 = new CreditCard("Credit", "0234567890223451", "credit", "123", "1234", true, true);
//		company.addCardData("0234567890223451", "Credit", exipery, "123", BigDecimal.valueOf(100));
//
//		try {
//			scs.cardReader.tap(CreditTap2);
//		} catch (SimulationException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			throw new RuntimeException(e);
//		}
//
//		assertTrue(PurchasedItems.isPaid());
//
//		scs.mainScanner.scan(unitItem2);
//		scs.baggingArea.add(unitItem2);
//
//		assertFalse(PurchasedItems.isPaid());
//
//		try {
//			scs.cardReader.tap(CreditTap2);
//		} catch (SimulationException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			throw new RuntimeException(e);
//		}
//
//		assertTrue(PurchasedItems.isPaid());
//	}
//
//}
