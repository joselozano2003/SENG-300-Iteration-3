package com.autovend.software.customer;

import java.math.BigDecimal;
import java.util.Currency;

import com.autovend.devices.SelfCheckoutStation;

public class CustomerStation {
	
	CustomerModel model;
	CustomerView view;
	CustomerController controller;
	
	public static CustomerStation InstallOn(SelfCheckoutStation station) {
		return new CustomerStation(station);
	}
	
	private CustomerStation(SelfCheckoutStation station) {
		model = new CustomerModel(station);
		view = new CustomerView();
		controller = new CustomerController(model, view);
	}
	
	public void turnOnDisplay() {
		view.setVisible(true);
	}
	
	/**
	 * Run independently from the attendant station.
	 * @param args
	 */
	public static void main(String[] args) {
		int[] billDenoms = {5, 10 , 15, 20, 50, 100};
		BigDecimal[] coinDenoms = {new BigDecimal("0.05"), new BigDecimal("0.10"), 
				new BigDecimal("0.25"), new BigDecimal("1"), new BigDecimal("2")};
		int scaleMaximumWeight = 50;
		int scaleSensitivity = 10;
		SelfCheckoutStation station = new SelfCheckoutStation(Currency.getInstance("CAD"),
				billDenoms, coinDenoms, scaleMaximumWeight, scaleSensitivity);
		
		CustomerStation self = CustomerStation.InstallOn(station);
		self.turnOnDisplay();
	}

}
