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
package com.autovend.software.ui;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Automated JUnit4 tests for the MembershipView class
 * @author Akansha
 */
public class MembershipViewTest {
	private MembershipView membershipView;
	private int found;
	private String MembershipCodeEntered;
	
	/**
	 * Sets up the test suite. Runs before every method.
	 */
	@Before
	public void setup() {
		membershipView = new MembershipView();
		MembershipCodeEntered = "00000";
		found = 0;
		
	}
	
	/**
	 * Cleans up the test suite. Runs after every test method.
	 */
	@After
	public void teardown() {
		membershipView = null;
		found = 0;	
	}
	
	/**
	 * Tests that the observer is notified of the event that a membership number is entered
	 */
	@Test
	public void testEventNotifyMembershipNumberEntered() {
		int expected = 1;
		
		MembershipCodeEntered = "123456";
		membershipView.addObserver(new MembershipViewObserverStub());
		membershipView.notifyMembershipNumberEntered("123456");
		assertEquals(expected, found);	
	}
	
	/**
	 * Tests that the listener is notified of the event when the user requests
	 * to go back to checkout
	 */
	@Test
	public void testEventNotifyGoBackToCheckout() {
		int expected = 1;
		membershipView.register(new CustomerUIEventListenerStub());
		membershipView.notifyGoBackToCheckout();
		assertEquals(expected, found);	
	}
	
	/*------------------------- Stubs ------------------------*/
	
	/**Stubs primarily check if/how many times observer events occurred.
	 * Tests should fail if an unexpected event is reported.
	 * Override any event in this stub that you don't want to fail.
	 */
	public class CustomerUIEventListenerStub implements CustomerUIEventListener {
		@Override
		public void onStartAddingItems() {}

		@Override
		public void onStartPaying() {}

		@Override
		public void onSelectAddItemByPLU() {}

		@Override
		public void onSelectAddItemByBrowsing() {}

		@Override
		public void onStartAddingOwnBags() {	}

		@Override
		public void onFinishAddingOwnBags() {}

		@Override
		public void onAddMembershipNumber() {}

		@Override
		public void onPurchaseBags(int amount) {	}

		@Override
		public void goBackToCheckout() {
			found++;
		}

		@Override
		public void onSelectPaymentMethod(String payment) {	}

		@Override
		public void onBagApproval(int stationNumber) {	}

		@Override
		public void onBagRefusal(int stationNumber) {}

	}
	
	public class MembershipViewObserverStub implements MembershipViewObserver {

		@Override
		public void reactToMembershipCodeEntered(String membershipNumber) {
			found++;	
		}
	}
}
