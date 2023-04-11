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
import com.autovend.software.ui.CustomerView;
import com.autovend.software.ui.PLUView;
import com.autovend.software.ui.PaymentView;
import com.autovend.software.ui.UIEventListener;

public class CustomerController implements BaggingEventListener, ItemEventListener, PaymentEventListener,
		ReceiptEventListener, MembershipListener, UIEventListener {

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

	private CustomerView customerView;

	public enum State {

		INITIAL, SCANNING_MEMBERSHIP, ADDING_OWN_BAGS, ADDING_ITEMS, CHECKING_WEIGHT, PAYING, DISPENSING_CHANGE,
		PRINTING_RECEIPT, FINISHED, DISABLED

	}

	private State currentState;

	public CustomerController(SelfCheckoutStation selfCheckoutStation, ReusableBagDispenser bagDispenser,
			CustomerView customerView) {
		this.selfCheckoutStation = selfCheckoutStation;
		this.bagDispener = bagDispenser;
		this.customerView = customerView;

		this.currentState = State.INITIAL;
		this.paymentFacade = new PaymentFacade(selfCheckoutStation, false, customerView);
		this.itemFacade = new ItemFacade(selfCheckoutStation, customerView, false);
		this.receiptPrinterFacade = new ReceiptFacade(selfCheckoutStation, customerView);
		this.baggingFacade = new BaggingFacade(selfCheckoutStation, bagDispenser, customerView);

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
		customerView.pluView.register(this);

		// Make view visible
		selfCheckoutStation.screen.setVisible(true);
		selfCheckoutStation.screen.getFrame().add(customerView.startView);

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
		case SCANNING_MEMBERSHIP:
			break;
		case ADDING_ITEMS:
			selfCheckoutStation.baggingArea.enable();
			selfCheckoutStation.scale.enable();
			selfCheckoutStation.handheldScanner.enable();
			selfCheckoutStation.mainScanner.enable();
			break;
		case CHECKING_WEIGHT:
			// selfCheckoutStation.handheldScanner.disable();
			// selfCheckoutStation.mainScanner.disable();
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
		default:
			break;

		}
	}

	public void updateView(JPanel newView) {
		JFrame frame = selfCheckoutStation.screen.getFrame();
		frame.getContentPane().removeAll();
		frame.getContentPane().add(newView);
		frame.revalidate();
		frame.repaint();

	}

	private void onTransactionFinished() {
		updateView(customerView.startView);

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
		customerView.paymentView.setAmountDueLabelText(amountDue.toString());
	}

	@Override
	public void onStartAddingOwnBags() {
		setState(State.ADDING_OWN_BAGS);
	}

	@Override
	public void onFinishAddingOwnBags() {
		setState(State.DISABLED);
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
	public void onBagsDispensedFailure(ReusableBagProduct bagProduct, int amount) {
		// TODO Auto-generated method stub

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
	public void onSelectAddItemByPLU() {
		updateView(customerView.pluView);

	}

	@Override
	public void onItemAddedEvent(Product product, double quantity) {

		currentSession.addItemToCart(product, quantity);
		System.out.println("hey");
		customerView.checkoutView.updateShoppingCart(currentSession);

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
		currentSession.addFailedPayment();
		if (currentSession.getNumberOfFailedPayments() > 5) {
			// notify attendant
		}

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

	public CustomerView getCurrentView() {
		return this.customerView;
	}

	public State getCurrentState() {
		return this.currentState;
	}

	@Override
	public void reactToValidMembershipEntered(String number) {
		currentSession.addMembershipNumber(number);

	}

	@Override
	public void reactToInvalidMembershipEntered() {

	}

	@Override
	public void reactToInvalidBarcode(BarcodedProduct barcodedProduct, int i) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLowCoins(CoinDispenser dispenser, Coin coin) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLowBills(BillDispenser dispenser, Bill bill) {
		// TODO Auto-generated method stub

	}

}
