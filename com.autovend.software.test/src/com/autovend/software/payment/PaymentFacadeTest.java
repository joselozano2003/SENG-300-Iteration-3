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
package com.autovend.software.payment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Executable;
import java.math.BigDecimal;
import java.security.cert.CertificateParsingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.autovend.Barcode;
import com.autovend.Bill;
import com.autovend.Coin;
import com.autovend.CreditCard;
import com.autovend.Numeral;
import com.autovend.devices.AbstractDevice;
import com.autovend.devices.BillDispenser;
import com.autovend.devices.CoinDispenser;
import com.autovend.devices.OverloadException;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.SimulationException;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.BillDispenserObserver;
import com.autovend.devices.observers.CoinDispenserObserver;
import com.autovend.external.CardIssuer;
import com.autovend.external.ProductDatabases;
import com.autovend.products.BarcodedProduct;
import com.autovend.products.PLUCodedProduct;
import com.autovend.software.BankIO;
import com.autovend.software.attendant.AttendantController;
import com.autovend.software.attendant.AttendantModel;
import com.autovend.software.attendant.AttendantView;
import com.autovend.software.customer.CustomerController;
import com.autovend.software.customer.CustomerSession;
import com.autovend.software.customer.CustomerStationLogic;

/**
 * Automated JUnit4 test suite for the 
 * PaymentFacade.java class.
 *
 */
public class PaymentFacadeTest {
	public SelfCheckoutStation selfCheckoutStation;
	public CustomerController customerSessionController;
	public CustomerSession currentSession;

	public AttendantModel model;
	public AttendantView view;
	public List<CustomerStationLogic> customerStations;
	public AttendantController attendantController;

	public int[] billDenominations;
	public BigDecimal[] coinDenominations;
	public Currency currency;
	public int scaleMaximumWeight;
	public int scaleSensitivity;

	public BarcodedProduct barcodeProduct;
	public PLUCodedProduct pluProduct;
	public CardIssuer credit;
	public CreditCard creditCard;
	
	PaymentFacade parentFacade;
	PaymentFacade childFacade;
	
	BigDecimal changeCounter = new BigDecimal("0");
	int changeDispensedFailCounter = 0;
	int changeDispensedCounter = 0;

	@Before
	public void setUp() throws Exception {
		Calendar date = Calendar.getInstance();
		date.set(Calendar.YEAR, 2024);
		date.set(Calendar.MONTH, 7);
		date.set(Calendar.DAY_OF_MONTH, 4);

		// Variables for SelfCheckoutStation constructor
		billDenominations = new int[] { 5, 10, 20, 50, 100 };
		coinDenominations = new BigDecimal[] { BigDecimal.valueOf(0.05), BigDecimal.valueOf(0.10),
				BigDecimal.valueOf(0.25), BigDecimal.valueOf(1), BigDecimal.valueOf(2) };
		currency = Currency.getInstance("CAD");

		scaleMaximumWeight = 20;
		scaleSensitivity = 1;

		selfCheckoutStation = new SelfCheckoutStation(currency, billDenominations, coinDenominations,
				scaleMaximumWeight, scaleSensitivity);

		Numeral[] code1 = { Numeral.one, Numeral.two, Numeral.three, Numeral.four, Numeral.five, Numeral.six };
		Barcode barcode = new Barcode(code1);
		barcodeProduct = new BarcodedProduct(barcode, "product 1", new BigDecimal("1.00"), 10);
		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcode, barcodeProduct);
		ProductDatabases.INVENTORY.put(barcodeProduct, 25);

		credit = new CardIssuer("credit");
		BankIO.CARD_ISSUER_DATABASE.put("credit", credit);
		creditCard = new CreditCard("credit", "00000", "Some Guy", "902", "1111", true, true);
		credit.addCardData("00000", "Some Guy", date, "902", BigDecimal.valueOf(100));

		customerSessionController = new CustomerController(selfCheckoutStation);
		customerSessionController.startNewSession();
		currentSession = customerSessionController.getCurrentSession();

		model = new AttendantModel();
		view = new AttendantView();
		customerStations = new ArrayList<>();
		attendantController = new AttendantController(model, view, customerStations);
		// Add 100 bills to each dispenser
		for (int i = 0; i < billDenominations.length; i++) {
			BillDispenser dispenser = selfCheckoutStation.billDispensers.get(billDenominations[i]);
			for (int j = 0; j < 100; j++) {
				Bill bill = new Bill(billDenominations[i], currency);
				try {
					dispenser.load(bill);
				} catch (SimulationException | OverloadException e) {
				}
			}
		}
		// Add 100 coins to each dispenser
		for (int i = 0; i < coinDenominations.length; i++) {
			CoinDispenser dispenser = selfCheckoutStation.coinDispensers.get(coinDenominations[i]);
			for (int j = 0; j < 100; j++) {
				Coin coin = new Coin(coinDenominations[i], currency);
				try {
					dispenser.load(coin);
				} catch (SimulationException | OverloadException e) {

				}
			}
		}
	}
	
	@After
	public void tearDown() {
		changeCounter = BigDecimal.ZERO;
		int changeDispensedFailCounter = 0;
		int changeDispensedCounter = 0;
	}
	
	/**
	 * TODO Things to Test:
	 * ? - Constructor when it is/isn't a child
	 * X - Make use of amountDue methods, getters
	 * - Dispense change
	 * 		X - Try dispensing 0 change/negative change
	 * 		X - Try dispensing a large amount of change (requiring a mix of bills and coins)
	 * 		- Try when device(s) disabled/enabled (make use of enum/states)
	 * 		X - Edge case -> Change due is less than smallest coin denomination
	 * 			X - Register a PaymentEventListener observer stub
	 */
	
	/**
	 * Test methods to add and get the amount due.
	 */
	@Test
	public void addAmountDueTest() {
		PaymentFacade parentFacade = new PaymentFacade(selfCheckoutStation, false);
		BigDecimal amountToAdd = BigDecimal.valueOf(12.50);
		parentFacade.addAmountDue(amountToAdd);
		BigDecimal actual = parentFacade.getAmountDue();
		BigDecimal expected = BigDecimal.valueOf(12.50);
		assertEquals(expected, actual);
	}
	
	/**
	 * Test methods to subtract and get the amount due.
	 */
	@Test
	public void subtractAmountDueTest() {
		PaymentFacade parentFacade = new PaymentFacade(selfCheckoutStation, false);
		parentFacade.addAmountDue(BigDecimal.valueOf(25.50));
		parentFacade.subtractAmountDue(BigDecimal.valueOf(10.50));
		BigDecimal actual = parentFacade.getAmountDue();
		BigDecimal expected = BigDecimal.valueOf(15.0);
		assertEquals(expected, actual);
	}
	
	/**
	 * Test dispenseChange() when a negative input is given.
	 * Expect an IllegalArgumentException to be thrown.
	 */
	@Test (expected = IllegalArgumentException.class)
	public void testNegativeChange() {
		parentFacade = new PaymentFacade(selfCheckoutStation, false);
		parentFacade.dispenseChange(BigDecimal.valueOf(-1));
	}
	
	/**
	 * Test dispenseChange() when a input equal to zero is given.
	 * Expect no change dispensed and no exception thrown.
	 */
	@Test
	public void testZeroChange() {
		BillDispenserObserverStub bdstub = new BillDispenserObserverStub();
		selfCheckoutStation.billDispensers.forEach((k,v) -> v.register(bdstub));
		CoinDispenserObserverStub cdstub = new CoinDispenserObserverStub();
		selfCheckoutStation.coinDispensers.forEach((k,v) -> v.register(cdstub));
		
		parentFacade = new PaymentFacade(selfCheckoutStation, false);
		parentFacade.dispenseChange(BigDecimal.valueOf(0));
		assertTrue(changeCounter.equals(BigDecimal.valueOf(0)));
	}
	
	/**
	 * Test dispenseChange() with an input requiring
	 * a mix of bills and coins to be dispensed.
	 */
	@Test
	public void testMultipleChange() {
		BillDispenserObserverStub bdstub = new BillDispenserObserverStub();
		selfCheckoutStation.billDispensers.forEach((k,v) -> v.register(bdstub));
		CoinDispenserObserverStub cdstub = new CoinDispenserObserverStub();
		selfCheckoutStation.coinDispensers.forEach((k,v) -> v.register(cdstub));
		
		parentFacade = new PaymentFacade(selfCheckoutStation, false);
		parentFacade.dispenseChange(BigDecimal.valueOf(16.55));
		assertTrue(changeCounter.equals(BigDecimal.valueOf(16.55)));
	}
	
	/**
	 * Test dispenseChange() when the input requires a coin to be
	 * dispensed with a denomination lower than the smallest available one
	 */
	@Test
	public void testChangeLowerThanDenominations() {
		parentFacade = new PaymentFacade(selfCheckoutStation, false);
		parentFacade.register(new PaymentEventListenerStub());
		parentFacade.dispenseChange(BigDecimal.valueOf(0.04));
		assertEquals(1, changeDispensedFailCounter);
	}
	
	
	
	/*------------------------- Stubs ------------------------*/
	// Stubs primarily to check if/how many times observer events occurred
	
	public class PaymentEventListenerStub implements PaymentEventListener {

		@Override
		public void reactToHardwareFailure() {}

		@Override
		public void reactToDisableDeviceRequest(AbstractDevice<? extends AbstractDeviceObserver> device) {}

		@Override
		public void reactToEnableDeviceRequest(AbstractDevice<? extends AbstractDeviceObserver> device) {}

		@Override
		public void reactToDisableStationRequest() {	}

		@Override
		public void reactToEnableStationRequest() {}

		@Override
		public void onPaymentAddedEvent(BigDecimal amount) {}

		@Override
		public void onPaymentReturnedEvent(BigDecimal amount) {}

		@Override
		public void onPaymentFailure() {}

		@Override
		public void onPaymentSuccessful(BigDecimal value) {}

		@Override
		public void onChangeDispensedEvent() {
			changeDispensedCounter++;
		}

		@Override
		public void onChangeDispensedFailure() {
			changeDispensedFailCounter++;
		}

		@Override
		public void onCardRemovedEvent() {}
		
	}
	
	public class CoinDispenserObserverStub implements CoinDispenserObserver {
		boolean coinWasAdded = false;
		boolean coinWasRemoved = false;
		@Override
		public void reactToEnabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {}
		@Override
		public void reactToDisabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {}
		@Override
		public void reactToCoinsFullEvent(CoinDispenser dispenser) {}
		@Override
		public void reactToCoinsEmptyEvent(CoinDispenser dispenser) {}
		@Override
		public void reactToCoinAddedEvent(CoinDispenser dispenser, Coin coin) {
			coinWasAdded = true;
		}
		@Override
		public void reactToCoinRemovedEvent(CoinDispenser dispenser, Coin coin) {
			coinWasRemoved = true;
			BigDecimal coinValueInt = coin.getValue();
			changeCounter = changeCounter.add(coinValueInt);
		}
		@Override
		public void reactToCoinsLoadedEvent(CoinDispenser dispenser, Coin... coins) {}
		@Override
		public void reactToCoinsUnloadedEvent(CoinDispenser dispenser, Coin... coins) {}
	}
	
	public class BillDispenserObserverStub implements BillDispenserObserver {
		boolean billWasDispensed = false;

		@Override
		public void reactToEnabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {}
		@Override
		public void reactToDisabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {}
		@Override
		public void reactToBillsFullEvent(BillDispenser dispenser) {}
		@Override
		public void reactToBillsEmptyEvent(BillDispenser dispenser) {}
		@Override
		public void reactToBillAddedEvent(BillDispenser dispenser, Bill bill) {}
		@Override
		public void reactToBillRemovedEvent(BillDispenser dispenser, Bill bill) {
			billWasDispensed = true;
			int billValueInt = bill.getValue();
			BigDecimal value = new BigDecimal(billValueInt);
			changeCounter = changeCounter.add(value);
		}
		@Override
		public void reactToBillsLoadedEvent(BillDispenser dispenser, Bill... bills) {}
		@Override
		public void reactToBillsUnloadedEvent(BillDispenser dispenser, Bill... bills) {}
		
	}
}
