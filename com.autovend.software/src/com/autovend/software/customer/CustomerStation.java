package com.autovend.software.customer;

import java.math.BigDecimal;
import java.util.Currency;

import com.autovend.devices.SelfCheckoutStation;
import com.autovend.software.item.ItemFacade;
import com.autovend.software.membership.MembershipFacade;
import com.autovend.software.payment.PaymentFacade;
import com.autovend.software.receipt.ReceiptFacade;
import com.autovend.software.weight.WeightFacade;

public class CustomerStation {
	
	CustomerModel model;
	CustomerView view;
	CustomerController controller;
	
	public static CustomerStation InstallOn(SelfCheckoutStation station) {
		return new CustomerStation(station);
	}
	
	private CustomerStation(SelfCheckoutStation station) {
		//Initiate facades.
		ItemFacade item = new ItemFacade(station);
		PaymentFacade payment = new PaymentFacade(station);
		ReceiptFacade receipt = new ReceiptFacade(station);
		WeightFacade bagging = new WeightFacade(station);
		MembershipFacade membership = new MembershipFacade(station);
		//Initiate station with MVC design.
		model = new CustomerModel(item, payment, receipt, bagging, membership);
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
