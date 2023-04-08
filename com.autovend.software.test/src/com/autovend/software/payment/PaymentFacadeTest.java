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
import org.junit.Before;
import org.junit.Test;

import com.autovend.devices.SelfCheckoutStation;
import com.autovend.software.test.Setup;
import java.math.BigDecimal;

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
	private int changeDispensedCounter = 0;

	@Before
	public void setup() throws Exception {
		station = Setup.createSelfCheckoutStation();
		paymentFacade = new PaymentFacade(station, false);
		
		// Add 100 bills to each dispenser
		Setup.fillBillDispensers(station, 100);
		// Add 100 coins to each dispenser
		Setup.fillCoinDispensers(station, 100);
	}
	
	@After
	public void teardown() {
		changeCounter = BigDecimal.ZERO;
		changeDispensedFailCounter = 0;
		changeDispensedCounter = 0;
	}
	
	/**
	 * Things to Test:
	 * TODO ? - Constructor when it is/isn't a child
	 * X - Make use of amountDue methods, getters
	 * - Dispense change
	 * 		X - Try dispensing 0 change/negative change
	 * 		X - Try dispensing a large amount of change (requiring a mix of bills and coins)
	 * 		- Try when device(s) disabled/enabled (make use of enum/states)
	 * 		TODO ? - Edge case -> Change due is less than smallest coin denomination
	 * 			X - Register a PaymentEventListener observer stub
	 */
	
	/**
	 * Test constructor with a null station input.
	 */
	@Test (expected = NullPointerException.class)
	public void testNullContruction() {
		new PaymentFacade(null, false);
	}
	
	/**
	 * Test methods to add and get the amount due.
	 */
	@Test
	public void addAmountDueTest() {
		PaymentFacade paymentFacade = new PaymentFacade(station, false);
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
		PaymentFacade paymentFacade = new PaymentFacade(station, false);
		paymentFacade.addAmountDue(BigDecimal.valueOf(25.50));
		paymentFacade.subtractAmountDue(BigDecimal.valueOf(10.50));
		BigDecimal actual = paymentFacade.getAmountDue();
		BigDecimal expected = BigDecimal.valueOf(15.0);
		assertEquals(expected, actual);
	}
	
	/**
	 * Test dispenseChange() when a negative input is given.
	 * Expect an IllegalArgumentException to be thrown.
	 */
	@Test (expected = IllegalArgumentException.class)
	public void testNegativeChange() {
		paymentFacade = new PaymentFacade(station, false);
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
		
		paymentFacade = new PaymentFacade(station, false);
		paymentFacade.dispenseChange(BigDecimal.valueOf(0));
		assertTrue(changeCounter.equals(BigDecimal.valueOf(0)));
	}
	
	/**
	 * Test dispenseChange() with an input requiring
	 * a mix of bills and coins to be dispensed.
	 */
	@Test
	public void testMultipleChange() {
		BillDispenserObserverStub bdstub = new BillDispenserObserverStub();
		station.billDispensers.forEach((k,v) -> v.register(bdstub));
		CoinDispenserObserverStub cdstub = new CoinDispenserObserverStub();
		station.coinDispensers.forEach((k,v) -> v.register(cdstub));
		
		paymentFacade = new PaymentFacade(station, false);
		paymentFacade.dispenseChange(BigDecimal.valueOf(16.55));
		assertTrue(changeCounter.equals(BigDecimal.valueOf(16.55)));
	}
	
	/**
	 * Test dispenseChange() when the input requires a coin to be
	 * dispensed with a denomination lower than the smallest available one
	 */
	@Test
	public void testChangeLowerThanDenominations() {
		paymentFacade = new PaymentFacade(station, false);
		paymentFacade.register(new PaymentEventListenerStub());
		paymentFacade.dispenseChange(BigDecimal.valueOf(0.04));
		// TODO assertEquals(1, changeDispensedFailCounter);
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
		public void onPaymentFailure() {}

		@Override
		public void onChangeDispensedEvent() {
			changeDispensedCounter++;
		}

		@Override
		public void onChangeDispensedFailure(BigDecimal totalChangeLeft) {
			changeDispensedFailCounter++;
		}
		
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
