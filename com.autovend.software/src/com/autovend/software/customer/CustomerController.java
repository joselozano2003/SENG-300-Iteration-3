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
import java.util.ArrayList;
import java.util.List;

import com.autovend.ReusableBag;
import com.autovend.devices.AbstractDevice;
import com.autovend.devices.ReusableBagDispenser;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.external.ProductDatabases;
import com.autovend.products.Product;
import com.autovend.software.bagging.BaggingEventListener;
import com.autovend.software.bagging.BaggingFacade;
import com.autovend.software.bagging.WeightDiscrepancyException;
import com.autovend.software.item.ItemEventListener;
import com.autovend.software.item.ItemFacade;
import com.autovend.software.payment.PaymentEventListener;
import com.autovend.software.payment.PaymentFacade;
import com.autovend.software.receipt.ReceiptEventListener;
import com.autovend.software.receipt.ReceiptFacade;
import com.autovend.software.item.ByBrowsing;
import com.autovend.software.item.ByTextSearch;

public class CustomerController
		implements BaggingEventListener, ItemEventListener, PaymentEventListener, ReceiptEventListener {

	private SelfCheckoutStation selfCheckoutStation;
	private ReusableBagDispenser bagDispener;
	private CustomerSession currentSession;

	private PaymentFacade paymentFacade;
	private ItemFacade itemFacade;

	private ReceiptFacade receiptPrinterFacade;
	private BaggingFacade baggingFacade;
	List<PaymentFacade> paymentMethods;
	List<ItemFacade> itemAdditionMethods;

	public enum State {
		INITIAL, ADDING_OWN_BAGS, ADDING_ITEMS, CHECKING_WEIGHT, PAYING, DISPENSING_CHANGE, PRINTING_RECEIPT, FINISHED,
		DISABLED,
	}

	private State currentState;

	public CustomerController(SelfCheckoutStation selfCheckoutStation, ReusableBagDispenser bagDispenser) {
		this.selfCheckoutStation = selfCheckoutStation;
		this.bagDispener = bagDispenser;
		this.currentState = State.INITIAL;
		this.paymentFacade = new PaymentFacade(selfCheckoutStation, false);
		this.itemFacade = new ItemFacade(selfCheckoutStation, false);
		this.receiptPrinterFacade = new ReceiptFacade(selfCheckoutStation);
		this.baggingFacade = new BaggingFacade(selfCheckoutStation, bagDispenser);

		// Register the CustomerController as a listener for the facades
		paymentFacade.register(this);
		paymentMethods = paymentFacade.getChildren();
		for (PaymentFacade child : paymentMethods) {
			child.register(this);
		}
		itemFacade.register(this);
		itemAdditionMethods = itemFacade.getChildren();
		for (ItemFacade child : itemAdditionMethods) {
			child.register(this);
		}
		receiptPrinterFacade.register(this);
		baggingFacade.register(this);
	}

	public void setState(State newState) {
		this.currentState = newState;

		switch (newState) {
		case INITIAL:
			selfCheckoutStation.baggingArea.disable();
			selfCheckoutStation.scale.disable();

			selfCheckoutStation.billInput.disable();
			selfCheckoutStation.billOutput.disable();

			selfCheckoutStation.coinSlot.disable();
			selfCheckoutStation.coinTray.disable();

			selfCheckoutStation.cardReader.disable();

			selfCheckoutStation.handheldScanner.disable();
			selfCheckoutStation.mainScanner.disable();

			selfCheckoutStation.printer.disable();
			break;
		case ADDING_OWN_BAGS:
			selfCheckoutStation.baggingArea.enable();
			selfCheckoutStation.scale.enable();
		case ADDING_ITEMS:
			selfCheckoutStation.baggingArea.enable();
			selfCheckoutStation.scale.enable();
			selfCheckoutStation.handheldScanner.enable();
			selfCheckoutStation.mainScanner.enable();
			break;
		case CHECKING_WEIGHT:
			selfCheckoutStation.handheldScanner.disable();
			selfCheckoutStation.mainScanner.disable();
			break;
		case PAYING:
			selfCheckoutStation.billInput.enable();
			selfCheckoutStation.coinSlot.enable();
			selfCheckoutStation.cardReader.enable();
			break;
		case DISPENSING_CHANGE:
			selfCheckoutStation.billInput.disable();
			selfCheckoutStation.coinSlot.disable();
			selfCheckoutStation.cardReader.disable();

			selfCheckoutStation.billOutput.enable();
			selfCheckoutStation.coinTray.enable();

			paymentFacade.dispenseChange(currentSession.getChangeDue());

			break;
		case PRINTING_RECEIPT:
			selfCheckoutStation.printer.enable();

			receiptPrinterFacade.printReceipt(currentSession.getShoppingCart());
			break;
		case FINISHED:
			startNewSession();
			break;
		case DISABLED:
			selfCheckoutStation.baggingArea.disable();
			selfCheckoutStation.scale.disable();

			selfCheckoutStation.billInput.disable();
			selfCheckoutStation.billOutput.disable();

			selfCheckoutStation.coinSlot.disable();
			selfCheckoutStation.coinTray.disable();

			selfCheckoutStation.cardReader.disable();

			selfCheckoutStation.handheldScanner.disable();
			selfCheckoutStation.mainScanner.disable();
			selfCheckoutStation.printer.disable();
			break;
		default:
			break;

		}
	}

	// In reaction to UI
	public void startNewSession() {
		currentSession = new CustomerSession();
	}

	// In reaction to UI
	public void startAddingOwnBags() {
		setState(State.ADDING_OWN_BAGS);
		// Signal customer to add their own bags (e.g., via customerIO)
	}

	// In reaction to UI
	public void finishAddingOwnBags() {
		// Require attendant approval before changing state
		// Signal attendant to approve the added bags (e.g., via attendantIO)
	}

	// In reaction to UI
	public void startAddingItems() {
		setState(State.ADDING_ITEMS);
	}

	// In reaction to UI
	public void addMoreItems() {
		if (currentState == State.PAYING) {
			setState(State.ADDING_ITEMS);
		}
	}

	// In reaction to UI
	public void startPaying() {
		setState(State.PAYING);
		BigDecimal amountDue = currentSession.getTotalCost();
		paymentFacade.setAmountDue(amountDue); // Used only for non-cash payments

	}

	// In reaction to UI
	public void purchaseBags(int amount) {
		baggingFacade.dispenseBags(amount);
	}
	
	// In reaction to UI
	/**
	 * When customer gui signals that a customer has selected a product from the screen, call this method.
	 * 
	 * @param product: the product the customer has selected
	 */
	public void productFromBrowsingSelected(Product product) {
		ByBrowsing browsing = (ByBrowsing)itemFacade.getChildren().get(1);
		browsing.productFromVisualCatalogueSelected(product);
	}
	
	//In reaction to UI
	/**
	 * When the attendant has typed in a string that they want to search, call this method.
	 * This method gives you an arraylist of products to display as the search result. 
	 * 
	 * @param attendantInputString: string attendant has typed in as input to search
	 * @return
	 */
	public ArrayList<Product> getProductsToDisplayAfterSearch(String attendantInputString){
		ByTextSearch textSearch = (ByTextSearch) itemFacade.getChildren().get(3);
		return textSearch.getProductsCorrespondingToTextSearch(attendantInputString);
	}
	
	//In reaction to UI
	/**
	 * When attendant gui signals that the attendant has selected a product from the text search to add to customer's shopping cart, call this method.
	 * 
	 * @param product: the product attendant has selected to add to customer's shopping cart
	 */
	public void productFromTextSearchSelected(Product product) {
		ByTextSearch textSearch = (ByTextSearch) itemFacade.getChildren().get(3);
		textSearch.productFromTextSearchSelected(product);
	}

	@Override
	public void onBagsDispensedEvent(int amount) {
		// I don't see a reusableBagProduct - just the sellable unit, where should we
		// set this price?
		// currentSession.addItemToCart(bag, amount);
	}

	@Override
	public void onBagsDispensedFailure(int amount) {
		// notify attendant
	}

	@Override
	public void reactToDisableDeviceRequest(AbstractDevice<? extends AbstractDeviceObserver> device) {
		// comes from the attendant
		// device.disable();

	}

	@Override
	public void reactToEnableDeviceRequest(AbstractDevice<? extends AbstractDeviceObserver> device) {
		// comes from the attendant
		// device.enable();

	}

	@Override
	public void reactToDisableStationRequest() {
		// comes from the attendant
	}

	@Override
	public void reactToEnableStationRequest() {
		// comes from the attendant
	}

	@Override
	public void reactToHardwareFailure() {
		// notify attendant
	}

	@Override
	public void onItemAddedEvent(Product product, double quantity) {
		currentSession.addItemToCart(product, quantity);
		setState(State.CHECKING_WEIGHT);

	}

	@Override
	public void onItemNotFoundEvent() {
		// notify attendant

	}

	@Override
	public void onPaymentAddedEvent(BigDecimal amount) {
		currentSession.addPayment(amount);
		boolean paymentComplete = currentSession.isPaymentComplete();
		if (paymentComplete) {
			setState(State.DISPENSING_CHANGE);

		}
	}

	@Override
	public void onPaymentFailure() {
		// display a try again message in UI and maybe keep track of the number of
		// successive failures in the session object
		// if currentSession.numberOfFailedPayments > 5 {notify attendant}
	}

	@Override
	public void onReceiptPrintedEvent(StringBuilder receiptText) {
		setState(State.FINISHED);

		// To "see" the receipt, uncomment the line below
		// System.out.println(receiptText.toString());

	}

	@Override
	public void onReceiptPrinterFixed() {
		setState(State.PRINTING_RECEIPT);

	}

	@Override
	public void onReceiptPrinterFailed() {
		setState(State.DISABLED);

	}

	@Override
	public void onChangeDispensedEvent() {
		setState(State.PRINTING_RECEIPT);
	}

	@Override
	public void onChangeDispensedFailure(BigDecimal totalChangeLeft) {
		// eventually method to dispense change left (not dispensed) should be called
		// here, or just tell
		// the attendant this value
		// paymentFacade.dispenseChange(totalChangeLeft);
	}

	@Override
	public void onWeightChanged(double weightInGrams) {
		boolean expectedWeightEqualsActual = (currentSession.getExpectedWeight() == weightInGrams);

		if (currentState == State.ADDING_OWN_BAGS) {
			// do nothing
		} else {
			if (expectedWeightEqualsActual) {
				setState(State.ADDING_ITEMS);
			} else {
				setState(State.DISABLED);
				// notify attendant

			}

		}

	}

	public CustomerSession getCurrentSession() {
		return this.currentSession;
	}

	public State getCurrentState() {
		return this.currentState;
	}

}
