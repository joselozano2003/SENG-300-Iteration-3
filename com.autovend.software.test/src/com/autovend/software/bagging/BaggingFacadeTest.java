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
package com.autovend.software.bagging;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;

import com.autovend.ReusableBag;
import com.autovend.SellableUnit;
import com.autovend.devices.OverloadException;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.software.test.Setup;
import com.autovend.software.ui.CustomerView;

public class BaggingFacadeTest {
	private SelfCheckoutStation station;
	private BaggingFacade baggingFacade;
	private int found;
	
	@Before
	public void setup() {
		//Setup the class to test
		station = Setup.createSelfCheckoutStation();
		baggingFacade = new BaggingFacade(station, new CustomerView());
		found = 0;
	}
	
	@Test (expected = NullPointerException.class)
	public void testContructorNullStation() {
		new BaggingFacade(null, new CustomerView());
	}
	
	@Test (expected = NullPointerException.class)
	public void testContructorNullBagDispenser() {
		new BaggingFacade(null, new CustomerView());
	}
	
	/**
	 * Pre: Zero bags to dispense.
	 * Expected: onBagsDispensedFailure
	 */
	@Test
	public void testEventDispenseZeroBagsFail() {
		baggingFacade.register(new ListenerStub() {
			@Override
			public void onBagsDispensedFailure(ReusableBagProduct bagProduct, int amount, int amountDispensed) {
				found++;
				assertEquals(0, amountDispensed);
			}
		});
		baggingFacade.dispenseBags(1);
		assertEquals(1, found);
	}
	
	/**
	 * Pre: One bag available, dispense one.
	 * Expected: onBagsDispensedEvent
	 * @throws OverloadException 
	 */
	@Test
	public void testEventDispenseOneBag() throws OverloadException {
		loadBags(1);
		baggingFacade.register(new ListenerStub() {
			@Override
			public void onBagsDispensedEvent(ReusableBagProduct bagProduct, int amount) {
				found++;
				assertEquals(1, amount);
			}
		});
		baggingFacade.dispenseBags(1);
		assertEquals(1, found);
	}
	
	/**
	 * Pre: Enough bags, dispense multiple.
	 * Expected: onBagsDispensedEvent announced once.
	 */
	@Test
	public void testEventDispenseManyBags() {
		int amountToDispense = 3;
		loadBags(amountToDispense);
		baggingFacade.register(new ListenerStub() {
			@Override
			public void onBagsDispensedEvent(ReusableBagProduct bagProduct, int amount) {
				found++;
				assertEquals(amountToDispense, amount);
			}
		});
		baggingFacade.dispenseBags(amountToDispense);
		assertEquals(1, found);
	}
	
	/**
	 * Pre: Short by one bag. 
	 * Expected: onBagsDispensedFailure, with the correct amount dispensed.
	 */
	@Test
	public void testEventDispenseBagsOneFail() {
		int amountToDispense = 2;
		loadBags(amountToDispense - 1);
		baggingFacade.register(new ListenerStub() {
			@Override
			public void onBagsDispensedFailure(ReusableBagProduct bagProduct, int amount, int amountDispensed) {
				found++;
				assertEquals(amountToDispense-1, amountDispensed);
			}
		});
		baggingFacade.dispenseBags(amountToDispense);
		assertEquals(1, found);
	}
	
	/**
	 * Expected: Nothing should happen.
	 */
	@Test
	public void testDispenseBagsNegative() {
		baggingFacade.register(new ListenerStub());
		baggingFacade.dispenseBags(-1);
	}
	
	/**
	 * Pre: The scale detects that the weight changed.
	 * Expected: onWeightChanged event is announced.
	 */
	@Test
	public void testEventWeightChanged() {
		double expectedWeight = 100.77;
		baggingFacade.register(new ListenerStub() {
			@Override
			public void onWeightChanged(double weightInGrams) {
				found++;
				assertEquals(expectedWeight, weightInGrams, 0.000);
			}
		});
		station.baggingArea.add(new Apple(expectedWeight));
		assertEquals(1, found);
	}
	
	/**
	 * Pre: The baggingArea detects a weight overload.
	 * Expected: onWeightChanged event is announced.
	 */
	@Test
	public void testEventWeightChangedOverload() {
		double expectedWeight = 99999;
		baggingFacade.register(new ListenerStub() {
			@Override
			public void onWeightChanged(double weightInGrams) {
				found++;
				assertTrue(weightInGrams < 0);
			}
		});
		station.baggingArea.add(new Apple(expectedWeight));
		assertEquals(1, found);
	}
	
	@SuppressWarnings("serial")
	class Apple extends SellableUnit {
		protected Apple(double weightInGrams) {
			super(weightInGrams);
		}
	}
	
	/**
	 * Assert that these hardware events don't announce to listeners.
	 */
	@Test
	public void testMethodsNoEvents() {
		baggingFacade.register(new ListenerStub());
		baggingFacade.reactToDisabledEvent(station.bagDispenser);
		baggingFacade.reactToEnabledEvent(station.bagDispenser);
		baggingFacade.reactToOutOfOverloadEvent(station.baggingArea);
	}
	
	/**
	 * Load bags into the bagDispenser.
	 * @param amount The amount of bags to load.
	 */
	private void loadBags(int amount) {
		for (int i = 0; i < amount; i++)
			try {
				station.bagDispenser.load(new ReusableBag());
			} catch (OverloadException e) {}
	}
	
	/*--------------- STUBS ---------------*/
	
	/**Stubs primarily check if/how many times observer events occurred.
	 * Tests should fail if an unexpected event is reported.
	 * Override any event in this stub that you don't want to fail.
	 */
	class ListenerStub implements BaggingEventListener {
		@Override
		public void reactToDisableStationRequest() {fail();}
		@Override
		public void reactToEnableStationRequest() {fail();}
		@Override
		public void onWeightChanged(double weightInGrams) {fail();}
		@Override
		public void onBagsDispensedEvent(ReusableBagProduct bagProduct, int amount) {fail();}
		@Override
		public void onBagsDispensedFailure(ReusableBagProduct bagProduct, int amount, int amountDispensed) {fail();}
	}

}
