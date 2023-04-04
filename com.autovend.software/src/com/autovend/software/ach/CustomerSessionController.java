package com.autovend.software.ach;

import java.math.BigDecimal;
import java.util.Currency;

import com.autovend.Barcode;
import com.autovend.Card.CardData;
import com.autovend.PriceLookUpCode;
import com.autovend.devices.AbstractDevice;
import com.autovend.devices.BarcodeScanner;
import com.autovend.devices.BillValidator;
import com.autovend.devices.CardReader;
import com.autovend.devices.CoinValidator;
import com.autovend.devices.ElectronicScale;
import com.autovend.devices.EmptyException;
import com.autovend.devices.OverloadException;
import com.autovend.devices.ReceiptPrinter;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.observers.*;
import com.autovend.external.CardIssuer;
import com.autovend.external.ProductDatabases;
import com.autovend.products.BarcodedProduct;
import com.autovend.products.PLUCodedProduct;
import com.autovend.products.Product;
import com.autovend.software.ach.virtual.OutOfBagsException;
import com.autovend.software.ach.virtual.VirtualBagDispenser;
/*
 * Group Members: Achraf Abdelrhafour: (30022366), Marianna Ferreira (30147733),
 * Ryan Chrumka (30144174), Alireza Vafisani (30150496), Ali Savab Pour
 * (30154744), Aryan Nambiar (30140671), Shijia Wang (30018276), Carson Bergen
 * (30127827), Md Abdullah Mehedi Patwary (30154770), Vita Vysochina (30118374),
 * Michael Kacmar, (30113919), Deepshikha Dhammi (30140157)
 */

/*
 * This class, CustomerSessionController, is responsible for managing the customer session in a self-checkout station. It acts as a mediator between the self-checkout station devices and the CustomerSession. The class implements various observer interfaces to react to events triggered by devices such as the bill validator, coin validator, card reader, barcode scanner, electronic scale, and receipt printer.

The CustomerSessionController has an enumeration State which defines the various stages of a customer session: INITIAL, ADDING_ITEMS, CHECKING_WEIGHT, PAYING, DISABLE_STATION, and FINISHED.

The constructor of CustomerSessionController takes a SelfCheckoutStation, a ReusableBag, and a VirtualBagDispenser as arguments. It initializes the instance variables and calls the registerObservers() method to register itself as an observer to the appropriate devices.

The class has several public methods to control the customer session's state, such as startNewSession(), resetCurrentSession(), startPayingForItems(), addMoreItems(), and setState().

The class also implements various observer methods to handle events triggered by the self-checkout station's devices. For example, when a barcode is scanned, the reactToBarcodeScannedEvent() method is called, which adds the corresponding item to the customer session's cart.

Two additional methods, onAddOwnBagsSelection() and onPurchaseBagsSelection(int amount), handle user interactions related to adding their own bags or purchasing bags at the self-checkout station.

In summary, the CustomerSessionController class is responsible for managing the customer session during a self-checkout process and reacting to events triggered by the self-checkout station's devices.
 */
public class CustomerSessionController implements BillValidatorObserver, CoinValidatorObserver, CardReaderObserver,
		BarcodeScannerObserver, ElectronicScaleObserver, ReceiptPrinterObserver {

	private CustomerSession currentSession;
	private SelfCheckoutStation selfCheckoutStation;
	private VirtualBagDispenser bagDispenser;
	private BarcodedProduct reusableBagProduct;
	private State currentState;

	public enum State {
		INITIAL, ADDING_ITEMS, CHECKING_WEIGHT, PAYING, DISABLE_STATION, FINISHED

	}

	public CustomerSessionController(SelfCheckoutStation selfCheckoutStation, BarcodedProduct reusableBagProduct,
			VirtualBagDispenser bagDispenser) {
		this.reusableBagProduct = reusableBagProduct;
		this.selfCheckoutStation = selfCheckoutStation;
		this.bagDispenser = bagDispenser;
		setState(State.INITIAL);
		registerObservers();
	}

	private void registerObservers() {
		selfCheckoutStation.billValidator.register(this);
		selfCheckoutStation.coinValidator.register(this);
		selfCheckoutStation.cardReader.register(this);
		selfCheckoutStation.mainScanner.register(this);
		selfCheckoutStation.handheldScanner.register(this);
		selfCheckoutStation.baggingArea.register(this);
		selfCheckoutStation.printer.register(this);
	}

	public void startCheckout() {
		currentSession = new CustomerSession(selfCheckoutStation, reusableBagProduct, bagDispenser);
		setState(State.ADDING_ITEMS);
	}

	public void resetCurrentSession() {
		if (currentSession != null) {
			currentSession.resetSession();
			setState(State.INITIAL);
		}
	}

	public void startPayingForItems() {
		setState(State.PAYING);
	}

	public void addMoreItems() {
		if (currentState == State.PAYING) {
			setState(State.ADDING_ITEMS);
		}
	}

	public void setState(State newState) {
		this.currentState = newState;

		switch (newState) {
		case INITIAL:
			this.selfCheckoutStation.billInput.disable();
			this.selfCheckoutStation.cardReader.disable();
			this.selfCheckoutStation.coinSlot.disable();
			this.selfCheckoutStation.mainScanner.disable();
			this.selfCheckoutStation.handheldScanner.disable();
			this.selfCheckoutStation.printer.disable();
			this.selfCheckoutStation.baggingArea.disable();
			break;
		case ADDING_ITEMS:
			this.selfCheckoutStation.mainScanner.enable();
			this.selfCheckoutStation.handheldScanner.enable();
			this.selfCheckoutStation.baggingArea.enable();
			break;
		case CHECKING_WEIGHT:
			this.selfCheckoutStation.mainScanner.disable();
			this.selfCheckoutStation.handheldScanner.disable();
			break;
		case PAYING:
			this.selfCheckoutStation.billInput.enable();
			this.selfCheckoutStation.cardReader.enable();
			this.selfCheckoutStation.coinSlot.enable();
			break;
		case DISABLE_STATION:
			this.selfCheckoutStation.billInput.disable();
			this.selfCheckoutStation.cardReader.disable();
			this.selfCheckoutStation.coinSlot.disable();
			this.selfCheckoutStation.mainScanner.disable();
			this.selfCheckoutStation.handheldScanner.disable();
			this.selfCheckoutStation.printer.disable();
			this.selfCheckoutStation.baggingArea.disable();
			break;
		case FINISHED:
			this.selfCheckoutStation.printer.enable();
			this.selfCheckoutStation.billInput.disable();
			this.selfCheckoutStation.cardReader.disable();
			this.selfCheckoutStation.coinSlot.disable();
			this.selfCheckoutStation.mainScanner.disable();
			this.selfCheckoutStation.handheldScanner.disable();
			currentSession.printReceipt();
			currentSession.dispenseChange();
			resetCurrentSession();
			break;
		default:
			break;
		}
	}

	@Override
	public void reactToEnabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
		// TODO Auto-generated method stub

	}

	@Override
	public void reactToDisabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
		// TODO Auto-generated method stub

	}

	@Override
	public void reactToWeightChangedEvent(ElectronicScale scale, double weightInGrams) {
		boolean expectedMatchesActual = false;
		try {
			expectedMatchesActual = currentSession.verifyWeight();
			if (expectedMatchesActual) {
				setState(State.ADDING_ITEMS);
			} else {
				setState(State.DISABLE_STATION);
			}
		} catch (WeightDiscrepancyException e) {
			setState(State.DISABLE_STATION);
		}
	}

	@Override
	public void reactToOverloadEvent(ElectronicScale scale) {
		setState(State.DISABLE_STATION);

	}

	@Override
	public void reactToOutOfOverloadEvent(ElectronicScale scale) {
		setState(State.ADDING_ITEMS);

	}

	@Override
	public void reactToCardInsertedEvent(CardReader reader) {
		// eventually will display message to user, asking for PIN
	}

	@Override
	public void reactToCardRemovedEvent(CardReader reader) {
		// eventually will display message to user

	}

	@Override
	public void reactToCardTappedEvent(CardReader reader) {
		// eventually will display message to user

	}

	@Override
	public void reactToCardSwipedEvent(CardReader reader) {
		// eventually will display message to user

	}

	@Override
	public void reactToCardDataReadEvent(CardReader reader, CardData data) {
		if (currentState == State.PAYING) {
			currentSession.processCardPayment(data);
			boolean isPaymentSufficient = currentSession.isPaymentComplete();
			if (isPaymentSufficient) {
				setState(State.FINISHED);
			}
		}

	}

	@Override
	public void reactToValidCoinDetectedEvent(CoinValidator validator, BigDecimal value) {
		if (currentState == State.PAYING) {
			currentSession.processCashPayment(value);
			boolean isPaymentSufficient = currentSession.isPaymentComplete();
			if (isPaymentSufficient) {
				setState(State.FINISHED);
			}
		}

	}

	@Override
	public void reactToInvalidCoinDetectedEvent(CoinValidator validator) {
		// eventually will display message to user

	}

	@Override
	public void reactToValidBillDetectedEvent(BillValidator validator, Currency currency, int value) {
		if (currentState == State.PAYING) {
			currentSession.processCashPayment(BigDecimal.valueOf(value));
			boolean isPaymentSufficient = currentSession.isPaymentComplete();
			if (isPaymentSufficient) {
				setState(State.FINISHED);
			}
		}
	}

	@Override
	public void reactToInvalidBillDetectedEvent(BillValidator validator) {
		// eventually will display message to user

	}

	@Override
	public void reactToOutOfPaperEvent(ReceiptPrinter printer) {
		// handle use case here
		setState(State.DISABLE_STATION);

	}

	@Override
	public void reactToOutOfInkEvent(ReceiptPrinter printer) {
		setState(State.DISABLE_STATION);
	}

	@Override
	public void reactToPaperAddedEvent(ReceiptPrinter printer) {
		

	}

	@Override
	public void reactToInkAddedEvent(ReceiptPrinter printer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void reactToBarcodeScannedEvent(BarcodeScanner barcodeScanner, Barcode barcode) {
		if (currentState == State.ADDING_ITEMS && barcode != null) {

			BarcodedProduct barcodedProduct = ProductDatabases.BARCODED_PRODUCT_DATABASE.get(barcode);
			currentSession.addBarcodedItemToCart(barcodedProduct);
			setState(State.CHECKING_WEIGHT);
		}
	}

	// Eventually will override some event in UI
	public void reactToProductLookUpCodeEnteredEvent(PriceLookUpCode priceLookUpCode, double weight) {
		if (currentState == State.ADDING_ITEMS) {
			PLUCodedProduct pluCodedProduct = ProductDatabases.PLU_PRODUCT_DATABASE.get(priceLookUpCode);
			currentSession.addPLUCodedItemToCart(pluCodedProduct, weight);
			setState(State.CHECKING_WEIGHT);
		}
	}

	// Eventually will override some event in UI
	public void startAddingOwnBags() {
		if (currentState == State.ADDING_ITEMS) {
			// Temporarily disable weight discrepancy checking
			setState(State.DISABLE_STATION);
		}
	}

	// Eventually will override some event in UI
	public void finishAddingOwnBags() {
		if (currentState == State.DISABLE_STATION) {
			selfCheckoutStation.baggingArea.enable();

			double newWeight;
			try {
				newWeight = selfCheckoutStation.baggingArea.getCurrentWeight();
				currentSession.setExpectedWeight(newWeight);
			} catch (OverloadException e) {
			}
		}
	}

	// Carson**
	// Eventually will override some event in UI
	public boolean onPurchaseBagsSelection(int amount) throws OutOfBagsException {
		if (currentState == State.ADDING_ITEMS || currentState == State.PAYING) {
			if (ProductDatabases.BARCODED_PRODUCT_DATABASE.get(reusableBagProduct.getBarcode()) != reusableBagProduct) {
				BarcodedProduct product = ProductDatabases.BARCODED_PRODUCT_DATABASE.get(reusableBagProduct.getBarcode());
				reusableBagProduct = product;
				// Update the bag dispenser as well
				bagDispenser.updateReusableBagBarcode(reusableBagProduct.getBarcode());
			}
			if (bagDispenser.getBagsInSystem() <= 0) {
				throw new OutOfBagsException();
			} else if (bagDispenser.getBagsInSystem() < amount) {
				return false;
				/*
				 * Alert client that the number of bags stored in the machine is less than the
				 * amount desired Ideally, the system would ask if the current amount of bags is
				 * sufficient for the customer, and only if this were true, continue executing
				 * and dispensing bags. Otherwise, alerting the attendant.
				 * 
				 * This feature is purposely ignored as the client did not specifically ask for
				 * it, although the idea is perhaps a good addition to the software. Pseudocode:
				 * if (client wants to proceed) continue else throw new NotEnoughBagsException()
				 */
			}
			return currentSession.dispenseBags(amount);
		}
		return false;
	}

	public CustomerSession getCurrentSession() {
		return this.currentSession;
	}

	public State getCurrentState() {
		return this.currentState;
	}

}
