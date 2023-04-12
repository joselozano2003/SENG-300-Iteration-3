package com.autovend.software.customer;

import java.math.BigDecimal;
import java.util.Currency;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.autovend.devices.SelfCheckoutStation;
import com.autovend.products.Product;
import com.autovend.software.AbstractFacade;
import com.autovend.software.bagging.BaggingFacade;
import com.autovend.software.item.ItemFacade;
import com.autovend.software.membership.MembershipFacade;
import com.autovend.software.payment.PaymentFacade;
import com.autovend.software.receipt.ReceiptFacade;
import com.autovend.software.ui.CustomerView;

public class CustomerStationLogic extends AbstractFacade<CustomerStationListener> implements CustomerControllerListener{

	//CustomerModel model;
	CustomerView view;
	CustomerController controller;

	public static CustomerStationLogic installOn(SelfCheckoutStation station) {
		return new CustomerStationLogic(station);
	}

	public CustomerStationLogic(SelfCheckoutStation station) {
		super(station, new CustomerView());
		view = new CustomerView();
	}



	public CustomerController getController() {
		return this.controller;
	}

	@Override
	public void reactToDisableStationRequest() {

	}

	@Override
	public void reactToEnableStationRequest() {

	}

	@Override
	public void reactToLowInkAlert() {

	}

	@Override
	public void reactToLowPaperAlert() {

	}

	// TODO: Connect to event from GUI
	// React when the customer requests to remove an item.
	// Sends request to the attendant

	@Override
	public void removeItemRequest(Product product, double quantity, CustomerController controller) {
		for (CustomerStationListener listener : listeners) {
			listener.reactToRemoveItemRequest(product, quantity, this);
		}
	}

}
