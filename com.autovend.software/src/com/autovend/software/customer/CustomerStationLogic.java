// P3-4 Group Members
//
// Abdelrhafour, Achraf (30022366)
// Campos, Oscar (30057153)
// Cavilla, Caleb (30145972)
// Crowell, Madeline (30069333)
// Debebe, Abigia (30134608)
// Dhuka, Sara Hazrat (30124117)
// Drissi, Khalen (30133707)
// Ferreira, Marianna (30147733)
// Frey, Ben (30088566)
// Himel, Tanvir (30148868)
// Huayhualla Arce, Fabricio (30091238)
// Kacmar, Michael (30113919)
// Lee, Jeongah (30137463)
// Li, Ran (10120152)
// Lokanc, Sam (30114370)
// Lozano Cetina, Jose Camilo (30144736)
// Maahdie, Monmoy (30149094)
// Malik, Akansha (30056048)
// Mehedi, Abdullah (30154770)
// Polton, Scott (30138102)
// Rahman, Saadman (30153482)
// Rodriguez, Gabriel (30162544)
// Samin Rashid, Khondaker (30143490)
// Sloan, Jaxon (30123845)
// Tran, Kevin (30146900)
//

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
