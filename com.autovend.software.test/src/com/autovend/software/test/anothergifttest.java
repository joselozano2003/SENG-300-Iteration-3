package com.autovend.software.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.autovend.BarcodedUnit;
import com.autovend.GiftCard;
import com.autovend.InvalidPINException;
import com.autovend.devices.ReusableBagDispenser;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.products.BarcodedProduct;
import com.autovend.products.PLUCodedProduct;
import com.autovend.software.customer.CustomerController;
import com.autovend.software.payment.GiftCardDatabase;
import com.autovend.software.customer.CustomerSession;

public class anothergifttest {

	private SelfCheckoutStation selfCheckoutStation;
	private ReusableBagDispenser bagDispenser;

	private CustomerController customerSessionController;
	private CustomerSession currentSession;
	
	private BarcodedProduct barcodeProduct;
	private PLUCodedProduct pluProduct;
	private GiftCard giftCard;

	@Before
	public void setUp() throws Exception {

		Calendar date = Calendar.getInstance();
		date.set(Calendar.YEAR, 2024);
		date.set(Calendar.MONTH, 7);
		date.set(Calendar.DAY_OF_MONTH, 4);

		selfCheckoutStation = Setup.createSelfCheckoutStation();
		bagDispenser = new ReusableBagDispenser(100);

		barcodeProduct = Setup.createBarcodedProduct123(1.00, 500, true);

		giftCard = new GiftCard("Gift", "12345678", "2001", Setup.getCurrency(), new BigDecimal("100"));
		GiftCardDatabase.addCard("12345678", giftCard);

		customerSessionController = new CustomerController(selfCheckoutStation, bagDispenser);
		customerSessionController.startNewSession();
		currentSession = customerSessionController.getCurrentSession();

		// Add 100 bills to each dispenser
		Setup.fillBillDispensers(selfCheckoutStation, 100);
		// Add 100 coins to each dispenser
		Setup.fillCoinDispensers(selfCheckoutStation, 100);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void payWithGiftCardPass() throws IOException {
		customerSessionController.startAddingItems();

		selfCheckoutStation.mainScanner
				.scan(new BarcodedUnit(barcodeProduct.getBarcode(), barcodeProduct.getExpectedWeight()));

		customerSessionController.startPaying();

		GiftCard card = GiftCardDatabase.getGiftCard("12345678");
		selfCheckoutStation.cardReader.insert(card, "2001");

		assertEquals(currentSession.getTotalPaid(), currentSession.getTotalCost());

	}

	@Test
	public void payWithGiftCardFail() throws IOException {
		giftCard = new GiftCard("Gift", "12345678", "2001", Setup.getCurrency(), new BigDecimal("0.1"));
		GiftCardDatabase.addCard("12345678", giftCard);

		customerSessionController.startAddingItems();

		selfCheckoutStation.mainScanner
				.scan(new BarcodedUnit(barcodeProduct.getBarcode(), barcodeProduct.getExpectedWeight()));

		customerSessionController.startPaying();

		GiftCard card = GiftCardDatabase.getGiftCard("12345678");
		selfCheckoutStation.cardReader.insert(card, "2001");

		assertEquals(currentSession.getTotalPaid(), new BigDecimal("0"));

	}

	@Test
	public void payWithGiftCardBadPin() throws IOException {
		customerSessionController.startAddingItems();

		selfCheckoutStation.mainScanner
				.scan(new BarcodedUnit(barcodeProduct.getBarcode(), barcodeProduct.getExpectedWeight()));

		customerSessionController.startPaying();
		GiftCard card = GiftCardDatabase.getGiftCard("12345678");

		try {
			selfCheckoutStation.cardReader.insert(card, "123");
		} catch (InvalidPINException ipe) {
			assertEquals(currentSession.getTotalPaid(), new BigDecimal("0"));
		}

	}

	@Test
	public void payWithGiftCardNotInDatabase() throws IOException {
		giftCard = new GiftCard("Gift", "87654321", "2001", Setup.getCurrency(), new BigDecimal("100"));

		customerSessionController.startAddingItems();

		selfCheckoutStation.mainScanner
				.scan(new BarcodedUnit(barcodeProduct.getBarcode(), barcodeProduct.getExpectedWeight()));

		customerSessionController.startPaying();

		selfCheckoutStation.cardReader.insert(giftCard, "2001");

		assertEquals(currentSession.getTotalPaid(), new BigDecimal("0"));

	}

}