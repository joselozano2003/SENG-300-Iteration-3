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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import com.autovend.devices.SelfCheckoutStation;
import com.autovend.software.test.Setup;
import com.autovend.software.ui.CustomerView;

import java.math.BigDecimal;
import java.util.List;

import org.junit.After;

import com.autovend.Bill;
import com.autovend.Coin;
import com.autovend.devices.AbstractDevice;
import com.autovend.devices.BillDispenser;
import com.autovend.devices.CoinDispenser;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.BillDispenserObserver;
import com.autovend.devices.observers.CoinDispenserObserver;
/**
 * Automated JUnit4 test suite for the 
 * PaymentFacade.java class.
 *
 */
public class PaymentFacadeTest {
	private PaymentFacade paymentFacade;
	SelfCheckoutStation station;
	
	private BigDecimal changeCounter = new BigDecimal("0");
	private int changeDispensedFailCounter = 0;
	private boolean changeDispensedCounter = false;

	@Before
	public void setup() throws Exception {
		station = Setup.createSelfCheckoutStation();
		paymentFacade = new PaymentFacade(station, false, new CustomerView());
		
		// Add 100 bills to each dispenser
		Setup.fillBillDispensers(station, 100);
		// Add 100 coins to each dispenser
		Setup.fillCoinDispensers(station, 100);
	}
	
	@After
	public void teardown() {
		changeCounter = BigDecimal.ZERO;
		changeDispensedFailCounter = 0;
		changeDispensedCounter = false;
	}
	

	
	/**
	 * Test constructor with a null station input.
	 */
	@Test (expected = NullPointerException.class)
	public void testContructorNullStation() {
		new PaymentFacade(null, false, new CustomerView());
	}
	
	@Test (expected = NullPointerException.class)
	public void testContructorNullView() {
		new PaymentFacade(station, false, null);
	}
	
	/**
	 * Test methods to add and get the amount due.
	 */
	@Test
	public void addAmountDueTest() {
		BigDecimal amountToAdd = BigDecimal.valueOf(12.50);
		paymentFacade.addAmountDue(amountToAdd);
		BigDecimal actual = paymentFacade.getAmountDue();
		BigDecimal expected = BigDecimal.valueOf(12.50);
		assertEquals(expected, actual);
	}
	
	/**
	 * Test methods to subtract and get the amount due.
	 */
	@Test
	public void subtractAmountDueTest() {
		paymentFacade.addAmountDue(BigDecimal.valueOf(25.50));
		paymentFacade.subtractAmountDue(BigDecimal.valueOf(10.50));
		BigDecimal actual = paymentFacade.getAmountDue();
		BigDecimal expected = BigDecimal.valueOf(15.0);
		assertEquals(expected, actual);
	}
	
	/**
	 * Test setAmountDue (and getAmountDue to check).
	 */
	@Test
	public void setAmountDueTest() {
		paymentFacade.setAmountDue(BigDecimal.valueOf(15.27));
		assertEquals(BigDecimal.valueOf(15.27), paymentFacade.getAmountDue());
	}
	
	/**
	 * Test dispenseChange() when a negative input is given.
	 * Expect an IllegalArgumentException to be thrown.
	 */
	@Test (expected = IllegalArgumentException.class)
	public void testNegativeChange() {
		paymentFacade.dispenseChange(BigDecimal.valueOf(-1));
	}
	
	/**
	 * Test dispenseChange() when a input equal to zero is given.
	 * Expect no change dispensed and no exception thrown.
	 */
	@Test
	public void testZeroChange() {
		BillDispenserObserverStub bdstub = new BillDispenserObserverStub();
		station.billDispensers.forEach((k,v) -> v.register(bdstub));
		CoinDispenserObserverStub cdstub = new CoinDispenserObserverStub();
		station.coinDispensers.forEach((k,v) -> v.register(cdstub));
		paymentFacade.dispenseChange(BigDecimal.valueOf(0));
		assertTrue(changeCounter.equals(BigDecimal.valueOf(0)));
		assertFalse(changeDispensedCounter);
	}
	
	/**
	 * Test dispenseChange() with an input requiring
	 * a mix of bills and coins to be dispensed.
	 */
	@Test
	public void testMultipleChange() {
		paymentFacade.register(new PaymentEventListenerStub());
		BillDispenserObserverStub bdstub = new BillDispenserObserverStub();
		station.billDispensers.forEach((k,v) -> v.register(bdstub));
		CoinDispenserObserverStub cdstub = new CoinDispenserObserverStub();
		station.coinDispensers.forEach((k,v) -> v.register(cdstub));
		paymentFacade.dispenseChange(BigDecimal.valueOf(16.55));
		assertTrue(changeCounter.equals(BigDecimal.valueOf(16.55)));
		assertTrue(changeDispensedCounter);
	}
	
	/**
	 * Test dispenseChange() when the input requires a coin to be
	 * dispensed with a denomination lower than the smallest available one.
	 * Expect the smallest available coin denomination to be dispensed.
	 */
	@Test
	public void testChangeLowerThanDenominations() {
		paymentFacade.register(new PaymentEventListenerStub());
		paymentFacade.dispenseChange(BigDecimal.valueOf(0.04));
		// TODO assertTrue(changeCounter.equals(BigDecimal.valueOf(0.05)));
	}
	
	// Tests for children, instances
	
	/**
	 * Check to see that a parent PaymentFacade instance properly 
	 * returns a list including a coin, bill, and card PaymentFacade children.
	 */
	@Test
	public void getChildrenOfParentTest() {
		List<PaymentFacade> children = paymentFacade.getChildren();
		boolean containsPayWithCoin = (children.get(0) instanceof PayWithCoin);
		boolean containsPayWithBill = (children.get(1) instanceof PayWithBill);
		boolean containsPayWithCard = (children.get(2) instanceof PayWithCard);
		assertTrue(containsPayWithCoin);
		assertTrue(containsPayWithBill);
		assertTrue(containsPayWithCard);
	}
	
	/**
	 * Check the return value of getChildren() when the PaymentFacade instance is a child of 
	 * another PaymentFacade instance, and expect the list of children to be null. 
	 */
	@Test
	public void getChildrenOfChildTest() {
		List<PaymentFacade> children = paymentFacade.getChildren();
		PaymentFacade child = children.get(0); // Doesn't matter which child instance is used
		assertEquals(null, child.getChildren());
	}
	
	/**
	 * Get the instance of the payment facade when it is null
	 */
	@Test
	public void getInstanceTest() {
		assertEquals(null, paymentFacade.getInstance());
	}
	
	/**
	 * Attempt to dispense change when the bill
	 * dispenser is disabled, expecting a failure event to be announced.
	 */
	@Test
	public void changeWhenBillDispenserDisabledTest() {
		SelfCheckoutStation station = Setup.createSelfCheckoutStation();
		PaymentFacade paymentFacade = new PaymentFacade(station, false, new CustomerView());
		paymentFacade.register(new PaymentEventListenerStub());
		station.billDispensers.get(5).disable();
		paymentFacade.dispenseChange(BigDecimal.valueOf(5.00));
		assertEquals(1, changeDispensedFailCounter);
	}
	
	/**
	 * Attempt to dispense change when the coin
	 * dispenser is disabled, expecting a failure event to be announced.
	 */
	@Test
	public void changeWhenCoinDispenserDisabledTest() {
		SelfCheckoutStation station = Setup.createSelfCheckoutStation();
		PaymentFacade paymentFacade = new PaymentFacade(station, false, new CustomerView());
		paymentFacade.register(new PaymentEventListenerStub());
		station.coinDispensers.get(BigDecimal.valueOf(0.05)).disable();
		paymentFacade.dispenseChange(BigDecimal.valueOf(0.05));
		assertEquals(1, changeDispensedFailCounter);
	}
	
	/**
	 * Attempt to dispense change when the bill
	 * dispenser is disabled, expecting a failure event to be announced.
	 */
	@Test
	public void changeWhenBillDispenserDisabledNoListenerTest() {
		SelfCheckoutStation station = Setup.createSelfCheckoutStation();
		PaymentFacade paymentFacade = new PaymentFacade(station, false, new CustomerView());
		station.billDispensers.get(5).disable();
		paymentFacade.dispenseChange(BigDecimal.valueOf(5));
		assertEquals(0, changeDispensedFailCounter);
	}
	
	/**
	 * Attempt to dispense change when the coin
	 * dispenser is disabled, expecting a failure event to be announced.
	 */
	@Test
	public void changeWhenCoinDispenserDisabledNoListenerTest() {
		SelfCheckoutStation station = Setup.createSelfCheckoutStation();
		PaymentFacade paymentFacade = new PaymentFacade(station, false, new CustomerView());
		station.coinDispensers.get(BigDecimal.valueOf(0.05)).disable();
		paymentFacade.dispenseChange(BigDecimal.valueOf(0.05));
		assertEquals(0, changeDispensedFailCounter);
	}
	
	/*------------------------- Stubs ------------------------*/
	
	/**Stubs primarily check if/how many times observer events occurred.
	 * Tests should fail if an unexpected event is reported.
	 * Override any event in this stub that you don't want to fail.
	 */
	public class PaymentEventListenerStub implements PaymentEventListener {
		public void reactToDisableStationRequest() {fail();}
		@Override
		public void reactToEnableStationRequest() {fail();}
		@Override
		public void onPaymentAddedEvent(BigDecimal amount) {fail();}
		@Override
		public void onPaymentFailure() {fail();}

		@Override
		public void onChangeDispensedFailure(BigDecimal totalChangeLeft) {
			changeDispensedFailCounter++;
		}
		@Override
		public void onLowCoins(CoinDispenser dispenser) {fail();}
		@Override
		public void onLowBills(BillDispenser dispenser) {fail();}
		@Override
		public void onChangeDispensedEvent(BigDecimal amount) {
			//changeCounter = changeCounter.add(amount);
			changeDispensedCounter = true;
			}
		@Override
		public void cardInserted() {fail();}
		@Override
		public void cardRemoved() {fail();}
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
