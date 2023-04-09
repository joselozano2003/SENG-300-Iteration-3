package com.autovend.software.test;

import java.math.BigDecimal;
import java.util.Currency;

import com.autovend.Barcode;
import com.autovend.Bill;
import com.autovend.Coin;
import com.autovend.Numeral;
import com.autovend.devices.BillDispenser;
import com.autovend.devices.CoinDispenser;
import com.autovend.devices.OverloadException;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.SimulationException;
import com.autovend.external.ProductDatabases;
import com.autovend.products.BarcodedProduct;

public class Setup {
	/**
	 * Creates a new SelfCheckoutStation as required for setting up most tests.
	 * 
	 * The station will be created with the following input:
	 * billDenoms = int[] {5, 10, 20, 50, 100};
	 * coinDenoms = BigDecimal[] {0.05, 0.10, 0.25, 1.00, 2.00};
	 * currency = Currency.getInstance("CAD");
	 * scaleMaximumWeight = 500;
	 * scaleSensitivity = 10;
	 * 
	 * @return The newly created SelfCheckoutStation
	 */
	public static SelfCheckoutStation createSelfCheckoutStation() {
		// Variables for SelfCheckoutStation constructor
		int[] billDenoms = new int[] {5, 10, 20, 50, 100};
		BigDecimal[] coinDenoms = new BigDecimal[] { BigDecimal.valueOf(0.05), BigDecimal.valueOf(0.10),
				BigDecimal.valueOf(0.25), BigDecimal.valueOf(1.00), BigDecimal.valueOf(2.00)};
		Currency currency = Currency.getInstance("CAD");
		int scaleMaximumWeight = 500;
		int scaleSensitivity = 10;
		return new SelfCheckoutStation(currency, billDenoms, coinDenoms, scaleMaximumWeight, scaleSensitivity);
	}
	
	/**
	 * Fill all the BillDispensers in a SelfCheckoutStation.
	 * @param station The station to fill dispensers
	 * @param amount The amount of bills to fill in each bill dispenser.
	 */
	public static void fillBillDispensers(SelfCheckoutStation station, int amount) {
		if (station == null)
			throw new NullPointerException("Could not fill bill dispensers since station is null");
		for (int i = 0; i < station.billDenominations.length; i++) {
			BillDispenser dispenser = station.billDispensers.get(station.billDenominations[i]);
			for (int j = 0; j < amount; j++) {
				try {
					Bill bill = new Bill(station.billDenominations[i], Setup.getCurrency());
					dispenser.load(bill);
				} catch (SimulationException | OverloadException e) {
				}
			}
		}
	}
	
	/**
	 * Fill all the CoinDispensers in a SelfCheckoutStation.
	 * @param station The station to fill dispensers
	 * @param amount The amount of coins to fill in each coin dispenser.
	 */
	public static void fillCoinDispensers(SelfCheckoutStation station, int amount) {
		if (station == null)
			throw new NullPointerException("Could not fill coin dispensers since station is null");
		for (int i = 0; i < station.coinDenominations.size(); i++) {
			CoinDispenser dispenser = station.coinDispensers.get(station.coinDenominations.get(i));
			for (int j = 0; j < amount; j++) {
				try {
					Coin coin = new Coin(station.coinDenominations.get(i), Setup.getCurrency());
					dispenser.load(coin);
				} catch (SimulationException | OverloadException e) {
				}
			}
		}
	}
	
	/**
	 * @return An instance of "CAD" currency.
	 */
	public static Currency getCurrency() {
		return Currency.getInstance("CAD");
	}
	
	/**Creates a BarcodedProduct with the barcode "123" and puts it in the ProductDatabases when specified.
	 * @param price The price per-unit of the product, which will be converted to BigDecimal.
	 * @param weight The expected weight of one unit of the product.
	 * @param putInDatabase When true, the BarcodedProduct is put into the ProductDatabases
	 * and the ProductDatabases inventory is increased by one.
	 * @return The newly created product.
	 */
	public static BarcodedProduct createBarcodedProduct123(double price, int weight, boolean putInDatabase) {
		Barcode barcode123 = new Barcode(Numeral.one, Numeral.two, Numeral.three);
		BigDecimal bigPrice = BigDecimal.valueOf(price);
		BarcodedProduct barcodedProduct1 = new BarcodedProduct(barcode123, "Product One", bigPrice, weight);
		if (putInDatabase) {
			ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcode123, barcodedProduct1);
			ProductDatabases.INVENTORY.put(barcodedProduct1, 1);
		}
		return barcodedProduct1;
	}
	
	/**Creates a BarcodedProduct with the barcode "456" and puts it in the ProductDatabases when specified.
	 * @param price The price per-unit of the product, which will be converted to BigDecimal.
	 * @param weight The expected weight of one unit of the product.
	 * @param putInDatabase When true, the BarcodedProduct is put into the ProductDatabases
	 * and the ProductDatabases inventory is increased by four.
	 * @return The newly created product.
	 */
	public static BarcodedProduct createBarcodedProduct456(double price, int weight, boolean putInDatabase) {
		Barcode barcode456 = new Barcode(Numeral.four, Numeral.five, Numeral.six);
		BigDecimal bigPrice = BigDecimal.valueOf(price);
		BarcodedProduct barcodedProduct2 = new BarcodedProduct(barcode456, "Product Two", bigPrice, weight);
		if (putInDatabase) {
			ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcode456, barcodedProduct2);
			ProductDatabases.INVENTORY.put(barcodedProduct2, 4);
		}
		return barcodedProduct2;
	}

}
