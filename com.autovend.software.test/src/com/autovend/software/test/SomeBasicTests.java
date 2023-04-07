package com.autovend.software.test;

import static org.junit.Assert.*;

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
import com.autovend.Numeral;
import com.autovend.devices.BillDispenser;
import com.autovend.devices.CoinDispenser;
import com.autovend.devices.DisabledException;
import com.autovend.devices.OverloadException;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.SimulationException;
import com.autovend.external.ProductDatabases;
import com.autovend.products.BarcodedProduct;
import com.autovend.products.PLUCodedProduct;
import com.autovend.software.customer.CustomerController;
import com.autovend.software.customer.CustomerSession;

public class SomeBasicTests {

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

		customerSessionController = new CustomerController(selfCheckoutStation);
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
	public void payWithValidCoin() throws DisabledException, OverloadException {
		customerSessionController.startPaying();

		Coin coin = new Coin(BigDecimal.valueOf(0.05), currency);
		selfCheckoutStation.coinSlot.accept(coin);

		assertEquals(currentSession.getTotalPaid(), coin.getValue());

	}

	@Test
	public void payWithValidBill() throws DisabledException, OverloadException {
		customerSessionController.startPaying();

		Bill bill = new Bill(10, currency);
		selfCheckoutStation.billInput.accept(bill);
		assertEquals(currentSession.getTotalPaid(), BigDecimal.valueOf(bill.getValue()));

	}

	@Test
	public void addItemByScanning() {
		customerSessionController.startAddingItems();

		selfCheckoutStation.mainScanner
				.scan(new BarcodedUnit(barcodeProduct.getBarcode(), barcodeProduct.getExpectedWeight()));
	    assertEquals(1, (double) currentSession.getShoppingCart().get(barcodeProduct), 0.01);
	    
		selfCheckoutStation.mainScanner
				.scan(new BarcodedUnit(barcodeProduct.getBarcode(), barcodeProduct.getExpectedWeight()));
		assertEquals(2, (double) currentSession.getShoppingCart().get(barcodeProduct), 0.01);

	}
	
	@Test 
	public void payWithCard() {
		
	}

}
