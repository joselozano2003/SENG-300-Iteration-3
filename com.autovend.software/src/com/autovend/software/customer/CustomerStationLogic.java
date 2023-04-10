package com.autovend.software.customer;

import java.math.BigDecimal;
import java.util.Currency;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.autovend.devices.AbstractDevice;
import com.autovend.devices.ReusableBagDispenser;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.products.Product;
import com.autovend.software.AbstractEventListener;
import com.autovend.software.AbstractFacade;

public class CustomerStationLogic extends AbstractFacade<CustomerStationListener> implements CustomerControllerListener{

	CustomerView view;
	CustomerController controller;
	SelfCheckoutStation station;

	public static CustomerStationLogic installOn(SelfCheckoutStation station) {
		return new CustomerStationLogic(station);
	}

	private CustomerStationLogic(SelfCheckoutStation station) {
		super(station);
		// Initiate facades.
		this.station = station;
		controller = new CustomerController(station, new ReusableBagDispenser(20));
		//ItemFacade item = new ItemFacade(station, false);
		//PaymentFacade payment = new PaymentFacade(station, false);
		//ReceiptFacade receipt = new ReceiptFacade(station);
	//	BaggingFacade bagging = new BaggingFacade(station);
		//MembershipFacade membership = new MembershipFacade(station);
		// Initiate station with MVC design.
		view = new CustomerView();

	}
	public CustomerController getController() {
		return this.controller;
	}



	public void turnOnDisplay() {
		view.setVisible(true);
	}

	/**
	 * Run independently from the attendant station.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		int[] billDenoms = { 5, 10, 15, 20, 50, 100 };
		BigDecimal[] coinDenoms = { new BigDecimal("0.05"), new BigDecimal("0.10"), new BigDecimal("0.25"),
				new BigDecimal("1"), new BigDecimal("2") };
		int scaleMaximumWeight = 50;
		int scaleSensitivity = 10;
		SelfCheckoutStation station = new SelfCheckoutStation(Currency.getInstance("CAD"), billDenoms, coinDenoms,
				scaleMaximumWeight, scaleSensitivity);

		CustomerStationLogic self = CustomerStationLogic.installOn(station);
		self.turnOnDisplay();
	}

	// TODO: Connect to event from GUI
	// React when the customer requests to remove an item.
	// Sends request to the attendant
	public void removeItemRequest(Product product, double quantity) {
		for (CustomerStationListener listener : listeners) {
			listener.reactToRemoveItemRequest(product, quantity, this);
		}
	}


	@Override
	public void reactToHardwareFailure() {

	}

	@Override
	public void reactToDisableDeviceRequest(AbstractDevice<? extends AbstractDeviceObserver> device) {

	}

	@Override
	public void reactToEnableDeviceRequest(AbstractDevice<? extends AbstractDeviceObserver> device) {

	}

	@Override
	public void reactToDisableStationRequest() {

	}

	@Override
	public void reactToEnableStationRequest() {

	}

	@Override
	public void reactToLowInkAlert() {
		for (CustomerStationListener listener : listeners) {
			listener.lowInkAlert(this);
		}
	}

	@Override
	public void reactToLowPaperAlert() {
		for (CustomerStationListener listener : listeners) {
			listener.lowPaperAlert(this);
		}
	}
}
