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
package com.autovend.software.test;

import static org.junit.Assert.*;


import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Currency;
import java.util.GregorianCalendar;

import com.autovend.*;
import com.autovend.external.ProductDatabases;
import com.autovend.software.ScanItems;
import com.autovend.software.WeightDiscrepancy;
import org.junit.*;
import org.junit.Before;
import org.junit.Test;

import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.SimulationException;
import com.autovend.external.CardIssuer;
import com.autovend.products.BarcodedProduct;
import com.autovend.software.PayWithCard;
import com.autovend.software.PurchasedItems;

public class PayWithCardTest{
	

	private SelfCheckoutStation scs;
	BigDecimal milkPrice = new BigDecimal(2.50);
	BigDecimal amountToPay;
	BigDecimal companyIssue;
    Numeral[] nMilk = {Numeral.one, Numeral.two, Numeral.three, Numeral.four};
    Barcode barcodeMilk = new Barcode(nMilk);
	BarcodedProduct milk = new BarcodedProduct(barcodeMilk,"milk description",milkPrice,2.00);
	
	
	Calendar exipery = new GregorianCalendar(2025, Calendar.OCTOBER, 31);
	CardIssuer company = new CardIssuer("TD");
	CardIssuer company2 = new CardIssuer("BMO");

	Currency currency = Currency.getInstance("CAD");
	int[] billDenominations = {1, 2, 5, 10};
	BigDecimal[] coinDenominations = {BigDecimal.TEN};
	private SelfCheckoutStation selfCheckoutStation;
	private BigDecimal price1, price2, price3;
	private Double weight1, weight2, weight3;
	private String description1, description2, description3;
	private BarcodedProduct itemProduct1, itemProduct2, itemProduct3;
	private BarcodedUnit unitItem1, unitItem2, unitItem3;
	private BigDecimal expectedCartPrice;
	private int maxScaleWeight, sensitivity;
	private double expectedBaggingWeight;
	private ScanItems scanItems;
	private WeightDiscrepancy weightDiscrepancy;
	private PurchasedItems itemsPurchased;
	private boolean scanFailed, cardFailed;

	// initializing some barcodes to use during tests
	Numeral[] n = {Numeral.one, Numeral.two, Numeral.three};
	Numeral[] m = {Numeral.two, Numeral.three, Numeral.one};
	Numeral[] k = {Numeral.three, Numeral.two, Numeral.one};
	Barcode b1 = new Barcode(n);
	Barcode b2 = new Barcode(m);
	Barcode b3 = new Barcode(k);
	
@Before
public void setUp() {
	
	Currency currency = Currency.getInstance("CAD");
	int[] billDom = {5,10,20};
	BigDecimal[] coinDom = {BigDecimal.valueOf(0.05), BigDecimal.valueOf(0.10),BigDecimal.valueOf(0.25)};
	scs = new SelfCheckoutStation(currency, billDom, coinDom, 10000,2);


	company.addCardData("1234567890123458","debit",exipery,"123",BigDecimal.valueOf(100));

	expectedCartPrice = new BigDecimal(0);
	expectedBaggingWeight = 0.0;

	// initialize a few prices
	price1 = new BigDecimal(2.00);
	price2 = new BigDecimal(3.00);
	price3 = new BigDecimal(4.50);

	//initialize a few weights
	weight1 = 2.0;
	weight2 = 3.5;
	weight3 = 12.0;

	// initialize a few descriptions
	description1 = "item1";
	description2 = "item2";
	description3 = "item3";

	scanFailed = false;
	cardFailed = false;

	//initialize some products
	itemProduct1 = new BarcodedProduct(b1, description1, price1, weight1);
	itemProduct2 = new BarcodedProduct(b2, description2, price2, weight2);
	itemProduct3 = new BarcodedProduct(b3, description3, price3, weight3);

	// initialize some units associated with the barcodes
	unitItem1 = new BarcodedUnit(b1, weight1);
	unitItem2 = new BarcodedUnit(b2, weight2);
	unitItem3 = new BarcodedUnit(b3, weight3);

	// add the products to the database to access
	ProductDatabases.BARCODED_PRODUCT_DATABASE.put(b1, itemProduct1);
	ProductDatabases.BARCODED_PRODUCT_DATABASE.put(b2, itemProduct2);
	ProductDatabases.BARCODED_PRODUCT_DATABASE.put(b3, itemProduct3);

	maxScaleWeight = 10;
	sensitivity = 1;

	// initialize purchased items constructor
	itemsPurchased = new PurchasedItems();

	// initialize constructor and add each product to the list of products being scanned
	scanItems = new ScanItems(scs);
	weightDiscrepancy = new WeightDiscrepancy(scs);



	//register the observers and enable card readers
	// TODO: register the observers and enable card readers



	//register the observer and enable scanners
	scs.mainScanner.register(scanItems);
	scs.mainScanner.enable();
	scs.handheldScanner.enable();
	scs.handheldScanner.register(scanItems);
	scs.baggingArea.register(weightDiscrepancy);

}
	
@After
public void tearDown() {

	PurchasedItems.reset();
}

/*
 * tests if paying with debit card tap works with sufficient balance
 */
@Test
public void testDebitTap() throws IOException {

	PayWithCard PayWithDebit = new PayWithCard(scs,company);
	scs.cardReader.register(PayWithDebit);
	while (scanFailed == false) {
		scanFailed = scs.mainScanner.scan(unitItem1);
	}
	scs.baggingArea.add(unitItem1);

	DebitCard DebitTap = new DebitCard("DEBIT", "0234567890223451", "debit", "123", "1234", true, true);
	company.addCardData("0234567890223451","DEBIT",exipery,"123",BigDecimal.valueOf(100));


	try {
		scs.cardReader.tap(DebitTap);
	} catch (SimulationException e) {
		e.printStackTrace();

		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	Assert.assertEquals(new BigDecimal(2), PurchasedItems.getAmountPaid());
	}

/*
 * tests if paying with debit card insert works with sufficient balance and valid pin
 */
@Test
public void testDebitInsert() throws IOException {
	
	PayWithCard PayWithDebit = new PayWithCard(scs,company);
	scs.cardReader.register(PayWithDebit);
	while (scanFailed == false) {
		scanFailed = scs.mainScanner.scan(unitItem1);
	}
	scs.baggingArea.add(unitItem1);

	DebitCard DebitInsert = new DebitCard("DEBIT", "0234567890223451", "debit", "123", "1234", true, true);
	company.addCardData("0234567890223451","DEBIT",exipery,"123",BigDecimal.valueOf(100));


	try {
		scs.cardReader.insert(DebitInsert,"1234");
	} catch (SimulationException e) {
		e.printStackTrace();

		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	Assert.assertEquals(new BigDecimal(2), PurchasedItems.getAmountPaid());
}
/*
 * tests if paying with debit card insert throws InvalidPINException when invalid pin present
 */
@Test(expected = InvalidPINException.class)
public void testDebitInsertWrongPin() throws IOException {
	PayWithCard PayWithDebit = new PayWithCard(scs,company);
	scs.cardReader.register(PayWithDebit);
	while (scanFailed == false) {
		scanFailed = scs.mainScanner.scan(unitItem1);
	}
	scs.baggingArea.add(unitItem1);

	DebitCard DebitWrong = new DebitCard("DEBIT", "0234567890223451", "debit", "123", "1234", true, true);
	company.addCardData("0234567890223451","DEBIT",exipery,"123",BigDecimal.valueOf(100));
	scs.cardReader.insert(DebitWrong, "12345");
}
		


/*
 * tests if paying with debit card swipe works with sufficient balance
 */
@Test
public void testDebitSwipe() throws IOException {
	PayWithCard PayWithDebit = new PayWithCard(scs,company);
	scs.cardReader.register(PayWithDebit);
	while (scanFailed == false) {
		scanFailed = scs.mainScanner.scan(unitItem1);
	}
	scs.baggingArea.add(unitItem1);

	DebitCard DebitSwipe = new DebitCard("DEBIT", "0234567890223451", "debit", "123", "1234", true, true);
	company.addCardData("0234567890223451","DEBIT",exipery,"123",BigDecimal.valueOf(100));

	try {
		scs.cardReader.swipe(DebitSwipe,null);
	} catch (SimulationException e) {
		e.printStackTrace();

		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	Assert.assertEquals(new BigDecimal(2), PurchasedItems.getAmountPaid());
}

/*
 * tests if paying with debit card tap does not works with insufficient balance
 */
@Test
public void testDebitTapNotEnough() throws IOException {
	PayWithCard PayWithDebit = new PayWithCard(scs,company);
	scs.cardReader.register(PayWithDebit);
	while (scanFailed == false) {
		scanFailed = scs.mainScanner.scan(unitItem1);
	}
	scs.baggingArea.add(unitItem1);

	DebitCard DebitTapWrong = new DebitCard("DEBIT", "0234567890223451", "debit", "123", "1234", true, true);
	company.addCardData("0234567890223451","DEBIT",exipery,"123",BigDecimal.valueOf(1));


	try {
		scs.cardReader.tap(DebitTapWrong);
	} catch (SimulationException e) {
		e.printStackTrace();

		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	Assert.assertEquals(new BigDecimal(2), PurchasedItems.getAmountLeftToPay());
	Assert.assertEquals(new BigDecimal(0), PurchasedItems.getAmountPaid());
	
}

/*
 * tests if paying with debit card insert does not works with insufficient balance
 */
@Test
public void testDebitInsertNotEnough() throws IOException {
	PayWithCard PayWithDebit = new PayWithCard(scs,company);
	scs.cardReader.register(PayWithDebit);
	while (scanFailed == false) {
		scanFailed = scs.mainScanner.scan(unitItem1);
	}
	scs.baggingArea.add(unitItem1);

	DebitCard DebitInsertWrong = new DebitCard("DEBIT", "0234567890223451", "debit", "123", "1234", true, true);
	company.addCardData("0234567890223451","DEBIT",exipery,"123",BigDecimal.valueOf(1));


	try {
		scs.cardReader.insert(DebitInsertWrong,"1234");
	} catch (SimulationException e) {
		e.printStackTrace();

		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	Assert.assertEquals(new BigDecimal(2), PurchasedItems.getAmountLeftToPay());
	Assert.assertEquals(new BigDecimal(0), PurchasedItems.getAmountPaid());
	
}

/*
 * tests if paying with debit card swipe does not works with insufficient balance
 */
@Test
public void testDebitSwipeNotEnough() throws IOException {
	PayWithCard PayWithDebit = new PayWithCard(scs,company);
	scs.cardReader.register(PayWithDebit);
	while (scanFailed == false) {
		scanFailed = scs.mainScanner.scan(unitItem1);
	}
	scs.baggingArea.add(unitItem1);

	DebitCard Debit1 = new DebitCard("DEBIT", "0234567890223451", "debit", "123", "1234", true, true);
	company.addCardData("0234567890223451","DEBIT",exipery,"123",BigDecimal.valueOf(1));


	try {
		scs.cardReader.swipe(Debit1, null);
	} catch (SimulationException e) {
		e.printStackTrace();

		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	Assert.assertEquals(new BigDecimal(2), PurchasedItems.getAmountLeftToPay());
	Assert.assertEquals(new BigDecimal(0), PurchasedItems.getAmountPaid());
	
}


/*
 * tests if paying with credit card tap works with enough credit
 */
@Test
public void testCreditTap() throws IOException {
	PayWithCard PayWithCredit = new PayWithCard(scs,company);
	scs.cardReader.register(PayWithCredit);
	while (scanFailed == false) {
		scanFailed = scs.mainScanner.scan(unitItem1);
	}
	scs.baggingArea.add(unitItem1);

	CreditCard CreditTap = new CreditCard("Credit", "0234567890223451", "credit", "123", "1234", true, true);
	company.addCardData("0234567890223451","Credit",exipery,"123",BigDecimal.valueOf(100));


	try {
		scs.cardReader.tap(CreditTap);
	} catch (SimulationException e) {
		e.printStackTrace();

		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	Assert.assertEquals(new BigDecimal(2), PurchasedItems.getAmountPaid());
}
/*
 * tests if paying with credit card insert works with enough credit and valid pin
 */
@Test 
public void testCreditInsert() throws IOException {
	PayWithCard PayWithCredit = new PayWithCard(scs,company);
	scs.cardReader.register(PayWithCredit);
	while (scanFailed == false) {
		scanFailed = scs.mainScanner.scan(unitItem1);
	}
	scs.baggingArea.add(unitItem1);

	CreditCard CreditInsert = new CreditCard("Credit", "0234567890223451", "credit", "123", "1234", true, true);
	company.addCardData("0234567890223451","Credit",exipery,"123",BigDecimal.valueOf(100));


	try {
		scs.cardReader.insert(CreditInsert, "1234");
	} catch (SimulationException e) {
		e.printStackTrace();

		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	Assert.assertEquals(new BigDecimal(2), PurchasedItems.getAmountPaid());
}

/*
 * tests if paying with credit card insert throws InvalidPINException when invalid pin present
 */
@Test (expected = InvalidPINException.class)
public void testCreditInsertWrongPin() throws IOException {
	PayWithCard PayWithCredit = new PayWithCard(scs,company);
	scs.cardReader.register(PayWithCredit);
	while (scanFailed == false) {
		scanFailed = scs.mainScanner.scan(unitItem1);
	}
	scs.baggingArea.add(unitItem1);

	CreditCard CreditWrong = new CreditCard("Credit", "0234567890223451", "credit", "123", "1234", true, true);
	company.addCardData("0234567890223451","Credit",exipery,"123",BigDecimal.valueOf(100));

	scs.cardReader.insert(CreditWrong, "12345");
	
}

/*
 * tests if paying with credit card swipe works with enough credit
 */
@Test
public void testCreditSwipe() throws IOException {
	PayWithCard PayWithCredit = new PayWithCard(scs,company);
	scs.cardReader.register(PayWithCredit);
	while (scanFailed == false) {
		scanFailed = scs.mainScanner.scan(unitItem1);
	}
	scs.baggingArea.add(unitItem1);

	CreditCard CreditSwipe = new CreditCard("Credit", "0234567890223451", "credit", "123", "1234", true, true);
	company.addCardData("0234567890223451","Credit",exipery,"123",BigDecimal.valueOf(100));


	try {
		scs.cardReader.swipe(CreditSwipe, null);
	} catch (SimulationException e) {
		e.printStackTrace();

		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	Assert.assertEquals(new BigDecimal(2), PurchasedItems.getAmountPaid());
}


/*
 * tests if paying with credit card tap does not works with not enough credit
 */
@Test
public void testCreditTapNotEnough() throws IOException {
	PayWithCard PayWithCredit = new PayWithCard(scs,company);
	scs.cardReader.register(PayWithCredit);
	while (scanFailed == false) {
		scanFailed = scs.mainScanner.scan(unitItem1);
	}
	scs.baggingArea.add(unitItem1);

	CreditCard Credit1 = new CreditCard("DEBIT", "0234567890223451", "debit", "123", "1234", true, true);
	company.addCardData("0234567890223451","DEBIT",exipery,"123",BigDecimal.valueOf(1));


	try {
		scs.cardReader.tap(Credit1);
	} catch (SimulationException e) {
		e.printStackTrace();

		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	Assert.assertEquals(new BigDecimal(2), PurchasedItems.getAmountLeftToPay());
	Assert.assertEquals(new BigDecimal(0), PurchasedItems.getAmountPaid());
	
}

/*
 * tests if paying with credit card insert does not works with not enough credit
 */
@Test
public void testCreditInsertNotEnough() throws IOException {
	PayWithCard PayWithCredit = new PayWithCard(scs,company);
	scs.cardReader.register(PayWithCredit);
	while (scanFailed == false) {
		scanFailed = scs.mainScanner.scan(unitItem1);
	}
	scs.baggingArea.add(unitItem1);

	CreditCard Credit2 = new CreditCard("DEBIT", "0234567890223451", "debit", "123", "1234", true, true);
	company.addCardData("0234567890223451","DEBIT",exipery,"123",BigDecimal.valueOf(1));


	try {
		scs.cardReader.insert(Credit2,"1234");
	} catch (SimulationException e) {
		e.printStackTrace();

		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	Assert.assertEquals(new BigDecimal(2), PurchasedItems.getAmountLeftToPay());
	Assert.assertEquals(new BigDecimal(0), PurchasedItems.getAmountPaid());
}

/*
 * tests if paying with credit card swipe does not works with not enough credit
 */
@Test
public void testCreditSwipeNotEnough() throws IOException {
	PayWithCard PayWithCredit = new PayWithCard(scs,company);
	scs.cardReader.register(PayWithCredit);
	while (scanFailed == false) {
		scanFailed = scs.mainScanner.scan(unitItem1);
	}
	scs.baggingArea.add(unitItem1);

	CreditCard CreditNotEnough = new CreditCard("DEBIT", "0234567890223451", "debit", "123", "1234", true, true);
	company.addCardData("0234567890223451","DEBIT",exipery,"123",BigDecimal.valueOf(1));


	try {
		scs.cardReader.swipe(CreditNotEnough,null);
	} catch (SimulationException e) {
		e.printStackTrace();

		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	Assert.assertEquals(new BigDecimal(2), PurchasedItems.getAmountLeftToPay());
	Assert.assertEquals(new BigDecimal(0), PurchasedItems.getAmountPaid());
}



}
