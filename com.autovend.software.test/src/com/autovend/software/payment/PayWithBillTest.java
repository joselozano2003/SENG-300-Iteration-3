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
import static org.junit.Assert.fail;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import com.autovend.devices.AbstractDevice;
import com.autovend.devices.BillDispenser;
import com.autovend.devices.CoinDispenser;
import com.autovend.devices.DisabledException;
import com.autovend.devices.OverloadException;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.software.test.Setup;
import com.autovend.software.ui.CustomerView;
import com.autovend.Bill;
import com.autovend.Coin;

public class PayWithBillTest {
	private SelfCheckoutStation station;
	private PayWithBill payWithBill;
	private BigDecimal paymentCounter = BigDecimal.valueOf(0);
	
	@Before
	public void setup() {
		//Setup the class to test
		station = Setup.createSelfCheckoutStation();
		payWithBill = new PayWithBill(station, new CustomerView());
		paymentCounter = BigDecimal.valueOf(0);
	}
	
	@Test (expected = NullPointerException.class)
	public void testContructorNullStation() {
		new PayWithBill(null, new CustomerView());
	}
	
	@Test (expected = NullPointerException.class)
	public void testContructorNullView() {
		new PayWithBill(station, null);
	}
	
	/**
	 * Things To Test
	 * 	- BillValidator reactions
	 * 		- reactToEnabled/DisabledEvents (expect nothing)
	 * 		- reactToValidBillDetectedEvent (expect an onPaymentAddedEvent)
	 * 		- reactToInvalidBillDetectedEvent (expect no listener event)
	 */

	/**
	 * Pass in a valid bill expecting payment equal to the bill's value to be added.
	 */
	@Test
	public void insertValidBill() {
		payWithBill.register(new PaymentEventListenerStub());
		Bill testBill = new Bill(5, Setup.getCurrency());
		try {
			station.billInput.accept(testBill);
		} catch (DisabledException | OverloadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(BigDecimal.valueOf(5), paymentCounter);
	}
	
	/**
	 * Pass in an invalid bill (not matching any of the system's bill denominations, 
	 * expecting no payment to be added at all.
	 */
	@Test
	public void insertInvalidBill() {
		payWithBill.register(new PaymentEventListenerStub());
		Bill invalidBill = new Bill(1, Setup.getCurrency());
		try {
			station.billInput.accept(invalidBill);
		} catch (DisabledException | OverloadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(BigDecimal.valueOf(0), paymentCounter);
	}
	
	/**
	 * Test that a low bills event is triggered when the number of bills in the dispenser is low enough.
	 */
	@Test
	public void testLowBills() {
		Setup.fillBillDispensers(station, 10);
		payWithBill.addAmountDue(BigDecimal.valueOf(1100));
		payWithBill.dispenseChange(paymentCounter);
	}
	
	/**
	 * Assert that these hardware events don't announce to listeners.
	 */
	@Test
	public void testMethodsNoEvent() {
		Bill testBill = new Bill(5, Setup.getCurrency());
		payWithBill.register(new PaymentEventListenerStub());
		//Will fail if any listener event is entered.
		payWithBill.reactToEnabledEvent(station.billValidator);
		payWithBill.reactToDisabledEvent(station.billValidator);
		payWithBill.reactToInvalidBillDetectedEvent(station.billValidator);
		for (int key : station.billDispensers.keySet()) {
			payWithBill.reactToBillsFullEvent(station.billDispensers.get(key));
			payWithBill.reactToBillsEmptyEvent(station.billDispensers.get(key));
			payWithBill.reactToBillAddedEvent(station.billDispensers.get(key), testBill);
			payWithBill.reactToBillsLoadedEvent(station.billDispensers.get(key), testBill);
			payWithBill.reactToBillsUnloadedEvent(station.billDispensers.get(key), testBill);
		}
	}
	
	/*--------------- STUBS ---------------*/
	
	/**Stubs primarily check if/how many times observer events occurred.
	 * Tests should fail if an unexpected event is reported.
	 * Override any event in this stub that you don't want to fail.
	 */
	public class PaymentEventListenerStub implements PaymentEventListener {
		@Override
		public void reactToDisableDeviceRequest(AbstractDevice<? extends AbstractDeviceObserver> device) {fail();}
		@Override
		public void reactToEnableDeviceRequest(AbstractDevice<? extends AbstractDeviceObserver> device) {fail();}
		@Override
		public void reactToDisableStationRequest() {fail();}
		@Override
		public void reactToEnableStationRequest() {fail();}
		@Override
		public void onPaymentAddedEvent(BigDecimal amount) {
			paymentCounter = paymentCounter.add(amount);
		}
		@Override
		public void onPaymentFailure() {fail();}
		@Override
		public void onChangeDispensedEvent() {fail();}
		@Override
		public void onChangeDispensedFailure(BigDecimal totalChangeLeft) {fail();}
		@Override
		public void onLowCoins(CoinDispenser dispenser, Coin coin) {fail();}
		@Override
		public void onLowBills(BillDispenser dispenser, Bill bill) {fail();}
	}
	
}
