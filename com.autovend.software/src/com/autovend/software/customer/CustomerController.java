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

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.autovend.Bill;
import com.autovend.Coin;
import com.autovend.ReusableBag;
import com.autovend.devices.AbstractDevice;
import com.autovend.devices.BillDispenser;
import com.autovend.devices.CoinDispenser;
import com.autovend.devices.ReusableBagDispenser;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.external.ProductDatabases;
import com.autovend.products.BarcodedProduct;
import com.autovend.products.Product;
import com.autovend.software.AbstractFacade;
import com.autovend.software.bagging.BaggingEventListener;
import com.autovend.software.bagging.BaggingFacade;
import com.autovend.software.bagging.ReusableBagProduct;
import com.autovend.software.bagging.WeightDiscrepancyException;
import com.autovend.software.item.ItemEventListener;
import com.autovend.software.item.ItemFacade;
import com.autovend.software.membership.MembershipFacade;
import com.autovend.software.membership.MembershipListener;
import com.autovend.software.payment.PaymentEventListener;
import com.autovend.software.payment.PaymentFacade;
import com.autovend.software.receipt.ReceiptEventListener;
import com.autovend.software.receipt.ReceiptFacade;
import com.autovend.software.ui.CustomerUIEventListener;
import com.autovend.software.ui.CustomerView;
import com.autovend.software.ui.PLUView;
import com.autovend.software.ui.PaymentView;

import auth.AttendantAccount;

public class CustomerController extends AbstractFacade<CustomerControllerListener> implements BaggingEventListener, ItemEventListener, PaymentEventListener,
		ReceiptEventListener, MembershipListener, CustomerUIEventListener {

	private SelfCheckoutStation selfCheckoutStation;
	private ReusableBagDispenser bagDispener;
	private CustomerSession currentSession;

	private PaymentFacade paymentFacade;
	private ItemFacade itemFacade;

	private ReceiptFacade receiptPrinterFacade;
	private BaggingFacade baggingFacade;
	private MembershipFacade membershipFacade;
	List<PaymentFacade> paymentMethods;
	List<ItemFacade> itemAdditionMethods;
	private int inkUsed, paperUsed;
	public int inkAdded, paperAdded;
	boolean cardInserted;
	private boolean isMembership;

	final int ALERT_THRESHOLD = 10;

	private CustomerView customerView;
	private JFrame frame;
	public State stateSave;
	
//  private AttendantController attendantListener;  //

	public enum State {
		INITIAL, ENTER_MEMBERSHIP, ADDING_OWN_BAGS, ADDING_ITEMS, CHECKING_WEIGHT, PAYING, DISPENSING_CHANGE,
		PRINTING_RECEIPT, FINISHED, DISABLED, SHUTDOWN, STARTUP
	}

	private State currentState;

	public CustomerController(SelfCheckoutStation selfCheckoutStation, ReusableBagDispenser bagDispenser,
			CustomerView customerView) {
		super(selfCheckoutStation, customerView);
		this.selfCheckoutStation = selfCheckoutStation;
		this.bagDispener = bagDispenser;
		this.customerView = customerView;
		
		this.currentSession = new CustomerSession();


		this.currentState = State.INITIAL;
		this.paymentFacade = new PaymentFacade(selfCheckoutStation, false, customerView);
		this.itemFacade = new ItemFacade(selfCheckoutStation, customerView, false);
		this.receiptPrinterFacade = new ReceiptFacade(selfCheckoutStation, customerView);
		this.baggingFacade = new BaggingFacade(selfCheckoutStation, customerView);

		this.membershipFacade = new MembershipFacade(selfCheckoutStation, customerView);

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
		membershipFacade.register(this);

		// Register the CustomerController to listen to views
		customerView.startView.register(this);
		customerView.checkoutView.register(this);
		customerView.membershipView.register(this);
		customerView.paymentView.register(this);
		customerView.pluView.register(this);
		customerView.browsingView.register(this);

		// Make view visible
		selfCheckoutStation.screen.setVisible(true);
		selfCheckoutStation.screen.getFrame().add(customerView.startView);

		inkUsed = 0;
		paperUsed = 0;
		inkAdded = 0;
		paperAdded = 0;

		cardInserted = false;

	}
	
	/*  public void register(AttendantController attendantListener){
	 * 	  this.attendantListener = attendantListener();
	 *  }
	 * 
	 *  public void register(AttendantController attendantListener){
	 *    this.attendantListener = null;
	 *   }
	 */

	public void setState(State newState) {
		this.stateSave = currentState;
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
		case ENTER_MEMBERSHIP:
			selfCheckoutStation.handheldScanner.enable();
			selfCheckoutStation.mainScanner.enable();
			break;
		case ADDING_ITEMS:
			selfCheckoutStation.baggingArea.enable();
			selfCheckoutStation.scale.enable();
			selfCheckoutStation.handheldScanner.enable();
			selfCheckoutStation.mainScanner.enable();
			break;
		case CHECKING_WEIGHT:

			break;
		case PAYING:
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
			onTransactionFinished();
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
		case SHUTDOWN:
				selfCheckoutStation.baggingArea.disable();
				selfCheckoutStation.scale.disable();
				selfCheckoutStation.baggingArea.disable();
				selfCheckoutStation.screen.disable();
				selfCheckoutStation.printer.disable();
				selfCheckoutStation.cardReader.disable();
				selfCheckoutStation.mainScanner.disable();
				selfCheckoutStation.handheldScanner.disable();
				selfCheckoutStation.billInput.disable();
				selfCheckoutStation.billOutput.disable();
				selfCheckoutStation.billStorage.disable();
				selfCheckoutStation.coinSlot.disable();
				selfCheckoutStation.coinTray.disable();
				selfCheckoutStation.coinStorage.disable();
				selfCheckoutStation.coinValidator.disable();
				break;
		case STARTUP:
				selfCheckoutStation.baggingArea.enable();
				selfCheckoutStation.scale.enable();
				selfCheckoutStation.baggingArea.enable();
				selfCheckoutStation.printer.enable();
				selfCheckoutStation.cardReader.enable();
				selfCheckoutStation.mainScanner.enable();
				selfCheckoutStation.handheldScanner.enable();
				selfCheckoutStation.billInput.enable();
				selfCheckoutStation.billOutput.enable();
				selfCheckoutStation.billStorage.enable();
				selfCheckoutStation.coinSlot.enable();
				selfCheckoutStation.coinTray.enable();
				selfCheckoutStation.coinStorage.enable();
				selfCheckoutStation.coinValidator.enable();
				selfCheckoutStation.screen.disable();
				// everything is on but screen is off, so it cannot be interacted with
				break;
		default:
			break;

		}
	}

	public void updateView(JPanel newView) {
		frame = selfCheckoutStation.screen.getFrame();
		frame.getContentPane().removeAll();
		frame.getContentPane().add(newView);
		frame.revalidate();
		frame.repaint();

	}

	public void startNewSession() {
		currentSession = new CustomerSession();
		setState(State.INITIAL);
	}

	public void onTransactionFinished() {
		try {
			Thread.sleep(600);
		} catch (InterruptedException e) {

		}

		// updateView(customerView.startView);

	}

	@Override
	public void onStartAddingItems() {
		currentSession = new CustomerSession();
		setState(State.ADDING_ITEMS);
		updateView(customerView.checkoutView);
	}

	@Override
	public void goBackToCheckout() {
		updateView(customerView.checkoutView);
		customerView.checkoutView.updateShoppingCart(currentSession);
	}

	@Override
	public void onStartPaying() {
		setState(State.PAYING);
		BigDecimal amountDue = currentSession.getTotalCost();
		paymentFacade.setAmountDue(amountDue); // Used only for non-cash payments
		updateView(customerView.paymentView);
		customerView.paymentView.updateAmountDue(amountDue.toString());
	}

	@Override
	public void onSelectPaymentMethod(String payment) {
		if (payment == "Debit/Credit Card" || payment == "Gift Card") {

			selfCheckoutStation.cardReader.enable();
		} else {
			selfCheckoutStation.billInput.enable();
			selfCheckoutStation.coinSlot.enable();
		}
		currentSession.setPaymentMethod(payment);
	}

	@Override
	public void onStartAddingOwnBags() {
		setState(State.ADDING_OWN_BAGS);

	}

	@Override
	public void onFinishAddingOwnBags() {
		setState(State.DISABLED);
		
		// attendantListener.notifyFinishAddingOwnBagsRequest();
		
		// Require attendant approval before changing state
		// Signal attendant to approve the added bags (e.g., via attendantIO)
	}

	@Override
	public void onPurchaseBags(int amount) {
		baggingFacade.dispenseBags(amount);
	}

	@Override
	public void onBagsDispensedEvent(ReusableBagProduct bagProduct, int amount) {

		currentSession.addItemToCart(bagProduct, amount);
		customerView.checkoutView.updateShoppingCart(currentSession);

		setState(State.CHECKING_WEIGHT);
	}

	@Override
	public void onBagsDispensedFailure(ReusableBagProduct bagProduct, int amountDispensed, int amountLeft) {
		// attendantListener.notifyBagsDispensedFailure();
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
	public void onSelectAddItemByPLU() {
		updateView(customerView.pluView);
	}

	@Override
	public void onSelectAddItemByBrowsing() {
		updateView(customerView.browsingView);
	}

	@Override
	public void onAddMembershipNumber() {
		updateView(customerView.membershipView);
	}

	@Override
	public void onItemAddedEvent(Product product, double quantity) {
		currentSession.addItemToCart(product, quantity);
		customerView.checkoutView.updateShoppingCart(currentSession);
		setState(State.CHECKING_WEIGHT);
	}

	@Override
	public void onItemNotFoundEvent() {
		// attendantListener.notifyItemNotFound();

	}

	@Override
	public void onPaymentAddedEvent(BigDecimal amount) {
		currentSession.addPayment(amount);
		boolean paymentComplete = currentSession.isPaymentComplete();
		if (paymentComplete) {
			customerView.paymentView.updateAmountDue("0.00");
			customerView.paymentView.updateChangeDue(currentSession.getChangeDue().toString());

			setState(State.DISPENSING_CHANGE);
		} else {
			customerView.paymentView.updateAmountDue(currentSession.getAmountLeft().toString());

		}
	}

	@Override
	public void onPaymentFailure() {
		currentSession.addFailedPayment();
		if (currentSession.getNumberOfFailedPayments() > 5) {
			// attendantListener.notifyMaxPaymentFailuresReached();
		}
	}

	@Override
	public void onReceiptPrintedEvent(StringBuilder receiptText) {

		setState(State.FINISHED);
		customerView.paymentView.updateReceipt(receiptText.toString());

	}

	@Override
	public void onReceiptPrinterFixed() {
		setState(State.PRINTING_RECEIPT);

	}

	@Override
	public void onReceiptPrinterFailed() {
		// attendantListener.notifyReceiptFailure();

		setState(State.DISABLED);
	}

	@Override
	public void onChangeDispensedEvent(BigDecimal amount) {

		setState(State.PRINTING_RECEIPT);
	}

	@Override
	public void onChangeDispensedFailure(BigDecimal totalChangeLeft) {		
		// attendantListener.notifyRemainingChangeLeft(totalChangeLeft);

	}

	@Override
	public void onWeightChanged(double weightInGrams) {
		boolean expectedWeightEqualsActual = (currentSession.getExpectedWeight() == weightInGrams);

		if (currentState == State.ADDING_OWN_BAGS) {
			// do nothing
		} else {
			if (weightInGrams >= 0 && expectedWeightEqualsActual) {
				setState(State.ADDING_ITEMS);
			} else {
				setState(State.DISABLED);
				customerView.checkoutView.displayWeightDiscrepancy();
				// attendantListener.notifyWeightDiscrepancy();
			}

		}

	}



	@Override
	public void reactToValidMembershipEntered(String number) {
		currentSession.addMembershipNumber(number);
	}

	@Override
	public void reactToInvalidMembershipEntered() {
		// attendantListener.notifyInvalidMembershipNumberEntered();
	}

	@Override
	public void reactToInvalidBarcode(BarcodedProduct barcodedProduct, int i) {
		// 

	}

	@Override
	public void onLowCoins(CoinDispenser dispenser) {
		// attendantListener.notifyAdjustCoinsForChange(this.customerController, dispenser);

	}

	@Override
	public void onLowBills(BillDispenser dispenser) {
		// attendantListener.notifyAdjustCoinsForChange(this.selfCheckoutStation, dispenser);
	}

	@Override
	public void cardInserted() {
		cardInserted = true;
	}

	@Override
	public void cardRemoved() {
		cardInserted = false;
	}

	@Override
	public void onBagApproval(int stationNumber) {
		// setState(STATE.ADDING_ITEMS);
	}

	@Override
	public void onBagRefusal(int stationNumber) {
		// setState(STATE.DISABLED); <- Should already be disabled (See: Adding Own Bags Use Case)
	}
	
	public CustomerSession getCurrentSession() {
		return this.currentSession;
	}
	
	public SelfCheckoutStation getStation() {
		return this.selfCheckoutStation;
	}

	public CustomerView getCurrentView() {
		return this.customerView;
	}

	public State getCurrentState() {
		return this.currentState;
	}

	public int getInkUsed(StringBuilder sb){
		int inkCount = 0;
		for (int i = 0; i < sb.length(); i++) {
			char c = sb.charAt(i);
			if (!Character.isWhitespace(c)) {
				inkCount++;
			}
		}
		return inkCount;
	}

	public int getPaperUsed(StringBuilder sb){
		int paperCount = 0;
		for (int i = 0; i < sb.length(); i++) {
			char c = sb.charAt(i);
			if (c == '\n') {
				paperCount++;
			}
		}
		return paperCount;
	}

	public void checkLevels(){
		int inkLevel = inkAdded - inkUsed;
		int paperLevel = paperAdded - paperUsed;

		if (inkLevel < ALERT_THRESHOLD) {
			setState(State.DISABLED);
			for (CustomerControllerListener listener : listeners) {
				listener.reactToLowInkAlert();
			}
		}

		if (paperLevel < ALERT_THRESHOLD){
			setState(State.DISABLED);
			for (CustomerControllerListener listener : listeners) {
				listener.reactToLowPaperAlert();
			}
		}
	}
	public JFrame getFrame() {
		return frame;
	}
	public CustomerView getCustomerView() {
		return customerView;
	}

	public boolean getCardInserted() {
		
		return cardInserted;
	}

}
