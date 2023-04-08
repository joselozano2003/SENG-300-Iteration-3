package com.autovend.software.test;

import java.math.BigDecimal;
import java.util.Currency;

import com.autovend.Bill;
import com.autovend.Coin;
import com.autovend.devices.BillDispenser;
import com.autovend.devices.CoinDispenser;
import com.autovend.devices.OverloadException;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.SimulationException;

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
		return new SelfCheckoutStation(currency, billDenoms, coinDenoms,
				scaleMaximumWeight, scaleSensitivity);
	}
	
	/**
	 * Fill the BillDispensers in a SelfCheckoutStation.
	 * @param station The station to fill dispensers
	 * @param amount The amount to fill each bill dispenser.
	 */
	public static void fillBillDispensers(SelfCheckoutStation station, int amount) {
		for (int i = 0; i < station.billDenominations.length; i++) {
			BillDispenser dispenser = station.billDispensers.get(station.billDenominations[i]);
			for (int j = 0; j < amount; j++) {
				Bill bill = new Bill(station.billDenominations[i], Setup.getCurrency());
				try {
					dispenser.load(bill);
				} catch (SimulationException | OverloadException e) {
				}
			}
		}
	}
	
	/**
	 * Fill the CoinDispensers in a SelfCheckoutStation.
	 * @param station The station to fill dispensers
	 * @param amount The amount to fill each coin dispenser.
	 */
	public static void fillCoinDispensers(SelfCheckoutStation station, int amount) {
		for (int i = 0; i < station.coinDenominations.size(); i++) {
			CoinDispenser dispenser = station.coinDispensers.get(station.coinDenominations.get(i));
			for (int j = 0; j < amount; j++) {
				Coin coin = new Coin(station.coinDenominations.get(i), Setup.getCurrency());
				try {
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

}
