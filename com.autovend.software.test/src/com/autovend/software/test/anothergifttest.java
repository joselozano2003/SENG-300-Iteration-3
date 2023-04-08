package com.autovend.software.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Currency;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.autovend.Barcode;
import com.autovend.BarcodedUnit;
import com.autovend.Bill;
import com.autovend.Coin;
import com.autovend.CreditCard;
import com.autovend.GiftCard;
import com.autovend.InvalidPINException;
import com.autovend.Numeral;
import com.autovend.devices.BillDispenser;
import com.autovend.devices.CoinDispenser;
import com.autovend.devices.DisabledException;
import com.autovend.devices.OverloadException;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.SimulationException;
import com.autovend.external.CardIssuer;
import com.autovend.external.ProductDatabases;
import com.autovend.products.BarcodedProduct;
import com.autovend.products.PLUCodedProduct;
import com.autovend.software.BankIO;
import com.autovend.software.customer.CustomerController;
import com.autovend.software.customer.CustomerController.State;
import com.autovend.software.payment.GiftCardDatabase;
import com.autovend.software.customer.CustomerSession;

public class anothergifttest {

	public SelfCheckoutStation selfCheckoutStation;
	public CustomerController customerSessionController;
	public CustomerSession currentSession;

	public int[] billDenominations;
	public BigDecimal[] coinDenominations;
	public Currency currency;
	public int scaleMaximumWeight;
	public int scaleSensitivity;

	public BarcodedProduct barcodeProduct;
	public PLUCodedProduct pluProduct;
	public GiftCard giftCard;

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

		Numeral[] code1 = { Numeral.one, Numeral.two, Numeral.three, Numeral.four, Numeral.five, Numeral.six };
		Barcode barcode = new Barcode(code1);
		barcodeProduct = new BarcodedProduct(barcode, "product 1", new BigDecimal("1.00"), 10);
		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcode, barcodeProduct);
		ProductDatabases.INVENTORY.put(barcodeProduct, 25);
		
		giftCard = new GiftCard("Gift", "12345678", "2001", currency, new BigDecimal("100"));
		GiftCardDatabase.addCard("12345678", giftCard);

		customerSessionController = new CustomerController(selfCheckoutStation);
		customerSessionController.startNewSession();
		currentSession = customerSessionController.getCurrentSession();

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
		giftCard = new GiftCard("Gift", "12345678", "2001", currency, new BigDecimal("0.1"));
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
		giftCard = new GiftCard("Gift", "87654321", "2001", currency, new BigDecimal("100"));

		customerSessionController.startAddingItems();

		selfCheckoutStation.mainScanner
				.scan(new BarcodedUnit(barcodeProduct.getBarcode(), barcodeProduct.getExpectedWeight()));

		customerSessionController.startPaying();
		
		selfCheckoutStation.cardReader.insert(giftCard, "2001");

		assertEquals(currentSession.getTotalPaid(), new BigDecimal("0"));

	}

}