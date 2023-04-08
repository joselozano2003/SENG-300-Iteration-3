package com.autovend.software.test;

import java.math.BigDecimal;
import java.util.Currency;

import com.autovend.devices.SelfCheckoutStation;

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

}
