/* P3-4 Group Members
 * 
 * Abdelrhafour, Achraf (30022366)
 * Campos, Oscar (30057153)
 * Cavilla, Caleb (30145972)
 * Crowell, Madeline (30069333)
 * Debebe, Abigia (30134608)
 * Dhuka, Sara Hazrat (30124117)
 * Drissi, Khalen (30133707)
 * Ferreira, Marianna (30147733)
 * Frey, Ben (30088566)
 * Himel, Tanvir (30148868)
 * Huayhualla Arce, Fabricio (30091238)
 * Kacmar, Michael (30113919)
 * Lee, Jeongah (30137463)
 * Li, Ran (10120152)
 * Lokanc, Sam (30114370)
 * Lozano Cetina, Jose Camilo (30144736)
 * Maahdie, Monmoy (30149094)
 * Malik, Akansha (30056048)
 * Mehedi, Abdullah (30154770)
 * Polton, Scott (30138102)
 * Rahman, Saadman (30153482)
 * Rodriguez, Gabriel (30162544)
 * Samin Rashid, Khondaker (30143490)
 * Sloan, Jaxon (30123845)
 * Tran, Kevin (30146900)
 */
package com.autovend.software.customer;

import java.math.BigDecimal;
import java.util.List;

import com.autovend.devices.AbstractDevice;
import com.autovend.devices.OverloadException;
import com.autovend.devices.ReceiptPrinter;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.products.Product;
import com.autovend.software.bagging.BaggingEventListener;
import com.autovend.software.bagging.BaggingFacade;
import com.autovend.software.item.ItemEventListener;
import com.autovend.software.item.ItemFacade;
import com.autovend.software.payment.PaymentEventListener;
import com.autovend.software.payment.PaymentFacade;
import com.autovend.software.receipt.ReceiptEventListener;
import com.autovend.software.receipt.ReceiptFacade;

public class CustomerController
		implements BaggingEventListener, ItemEventListener, PaymentEventListener, ReceiptEventListener {

	private SelfCheckoutStation selfCheckoutStation;
	private CustomerSession currentSession;

	private PaymentFacade paymentFacade;
	private ItemFacade itemAdditionFacade;
	private ReceiptFacade receiptPrinterFacade;
	private BaggingFacade baggingFacade;
	List<PaymentFacade> paymentMethods;
	List<ItemFacade> itemAdditionMethods;

	public enum State {
		INITIAL, ADDING_ITEMS, CHECKING_WEIGHT, PAYING, DISPENSING_CHANGE, PRINTING_RECEIPT, FINISHED, DISABLED,
	}

	private State currentState;

	public CustomerController(SelfCheckoutStation selfCheckoutStation) {
		this.selfCheckoutStation = selfCheckoutStation;
		this.currentState = State.INITIAL;
		this.paymentFacade = new PaymentFacade(selfCheckoutStation, false);
		this.itemAdditionFacade = new ItemFacade(selfCheckoutStation, false);
		this.receiptPrinterFacade = new ReceiptFacade(selfCheckoutStation);
		this.baggingFacade = new BaggingFacade(selfCheckoutStation);

		// Register the CustomerController as a listener for the facades
		paymentFacade.register(this);
		paymentMethods = paymentFacade.getChildren();
		for (PaymentFacade child : paymentMethods) {
			child.register(this);
		}
		itemAdditionFacade.register(this);
		itemAdditionMethods = itemAdditionFacade.getChildren();
		for (ItemFacade child : itemAdditionMethods) {
			child.register(this);
		}
		receiptPrinterFacade.register(this);
		baggingFacade.register(this);
	}
	
	public void setState(State newState) {
		this.currentState = newState;
		
		switch(newState) {
		case INITIAL:
			break;
		case ADDING_ITEMS:
			break;
		case CHECKING_WEIGHT:
			break;
		case PAYING:
			break;
		case DISPENSING_CHANGE:
			break;
		case PRINTING_RECEIPT:
			break;
		case FINISHED:
			break;
		case DISABLED:
			break;
		default:
			break;
		
		}
	}

	public void startNewSession() {
		currentSession = new CustomerSession();
	}

	public void startAddingItems() {
	}

	public void startPaying() {
		BigDecimal amountDue = currentSession.getAmountLeft();
		paymentFacade.addAmountDue(amountDue);
		
	}

	@Override
	public void reactToDisableDeviceRequest(AbstractDevice<? extends AbstractDeviceObserver> device) {
		// device.disable();

	}

	@Override
	public void reactToEnableDeviceRequest(AbstractDevice<? extends AbstractDeviceObserver> device) {
		// device.enable();

	}

	@Override
	public void reactToDisableStationRequest() {

	}

	@Override
	public void reactToEnableStationRequest() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onItemAddedEvent(Product product, double quantity) {
		currentSession.addItemToCart(product, quantity);
		setState(State.CHECKING_WEIGHT);

	}

	@Override
	public void onPaymentAddedEvent(BigDecimal amount) {
		currentSession.addPayment(amount);
	}

	@Override
	public void onPaymentFailure() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onReceiptPrintedEvent() {

	}

	@Override
	public void onChangeDispensedEvent() {

	}

	@Override
	public void onChangeDispensedFailure() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLowInk() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLowPaper() {

	}

	@Override
	public void onWeightChanged(double weightInGrams) {
		boolean expectedWeightEqualsActual = (currentSession.getExpectedWeight() == weightInGrams);
		if (expectedWeightEqualsActual) {
			setState(State.ADDING_ITEMS);
		} else {
			setState(State.DISABLED);
		}

	}

	@Override
	public void reactToHardwareFailure() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPaymentReturnedEvent(BigDecimal amount) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onReceiptPrinterFailed() {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void onItemNotFoundEvent() {
		// TODO Auto-generated method stub

	}

	public CustomerSession getCurrentSession() {
		return this.currentSession;
	}
	
	public State getCurrentState() {
		return this.currentState;
	}

	@Override
	public void onPaymentSuccessful(BigDecimal value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCardRemovedEvent() {
		// TODO Auto-generated method stub
		
	}
	

	

}
