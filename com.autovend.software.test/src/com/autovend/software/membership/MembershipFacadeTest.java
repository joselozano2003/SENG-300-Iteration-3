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
package com.autovend.software.membership;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.autovend.BarcodedUnit;
import com.autovend.MembershipCard;
import com.autovend.devices.AbstractDevice;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.products.BarcodedProduct;
import com.autovend.software.membership.MembershipFacade.InnerListener;
import com.autovend.software.test.Setup;
import com.autovend.software.ui.CustomerView;

public class MembershipFacadeTest {
	private SelfCheckoutStation station;
	private MembershipFacade membershipFacade;
	private boolean flag;
	private int found;
	
	@Before
	public void setup() {
		//Setup the class to test
		station = Setup.createSelfCheckoutStation();
		membershipFacade = new MembershipFacade(station, new CustomerView());
		flag = false;
		found = 0;
	}
	
	@Test (expected = NullPointerException.class)
	public void testContructorNullStation() {
		new MembershipFacade(null, new CustomerView());
	}
	
	@Test (expected = NullPointerException.class)
	public void testContructorNullView() {
		new MembershipFacade(null, new CustomerView());
	}
	
	/**
	 * Pre: Valid Membership card is scanned.
	 * Expected: reactToValidMembershipEntered event is called.
	 */
	@Test
	public void testEventValidMembershipScan() throws IOException {
		membershipFacade.register(new ListenerStub() {
			@Override
			public void reactToValidMembershipEntered(String number) {
				found++;
				assertEquals("1234", number);
			}
		});
		MembershipCard card = new MembershipCard("membership", "1234", "cardHolder", true);
		MemberShipDatabase.MEMBERSHIP_DATABASE.add("1111");
		while (!flag)
			flag = station.mainScanner.scan(card);
		assertEquals(1, found);
		assertTrue(membershipFacade.membershipEntered());
	}
	
	/**
	 * Pre: Membership card is scanned, but card number not in database.
	 * Expected: reactToInvalidMembershipEntered event is called.
	 */
	@Test
	public void testEventInvalidScan() throws IOException {
		membershipFacade.register(new ListenerStub() {
			@Override
			public void reactToInvalidMembershipEntered() {
				found++;
			}
		});
		MembershipCard card = new MembershipCard("membership", "9999", "cardHolder", true);
		MemberShipDatabase.MEMBERSHIP_DATABASE.add("1111");
		while (!flag)
			flag = station.mainScanner.scan(card);
		assertEquals(1, found);
		assertFalse(membershipFacade.membershipEntered());
	}
	
	/**
	 * Pre: Product in database is scanned.
	 * Expected: no event announced.
	 */
	@Test
	public void testEventInvalidScan2() throws IOException {
		membershipFacade.register(new ListenerStub());
		BarcodedProduct p = Setup.createBarcodedProduct123(5.55, 55, true);
		while (!flag)
			flag = station.mainScanner.scan(new BarcodedUnit(p.getBarcode(), p.getExpectedWeight()));
		assertFalse(membershipFacade.membershipEntered());
	}
	
	/**
	 * Pre: Valid Membership card data is read through swipe.
	 * Expected: reactToValidMembershipEntered event is called.
	 */
	@Test
	public void testEventValidMembershipSwipe() throws IOException {
		membershipFacade.register(new ListenerStub() {
			@Override
			public void reactToValidMembershipEntered(String number) {
				found++;
				assertEquals("1234", number);
			}
		});
		MembershipCard card = new MembershipCard("membership", "1234", "cardHolder", true);
		MemberShipDatabase.MEMBERSHIP_DATABASE.add("1234");
		while (!flag)
			try {
				station.cardReader.swipe(card, null);
				flag = true;
			} catch (Exception e) {}
		assertEquals(1, found);
		assertTrue(membershipFacade.membershipEntered());
	}
	
	/**
	 * Pre: Membership card is swiped, but card number not in database.
	 * Expected: reactToInvalidMembershipEntered event is called.
	 */
	@Test
	public void testEventInvalidSwipe() throws IOException {
		membershipFacade.register(new ListenerStub() {
			@Override
			public void reactToInvalidMembershipEntered() {
				found++;
			}
		});
		MembershipCard card = new MembershipCard("membership", "9999", "cardHolder", true);
		MemberShipDatabase.MEMBERSHIP_DATABASE.add("1111");
		while (!flag)
			try {
				station.cardReader.swipe(card, null);
				flag = true;
			} catch (Exception e) {}
		assertEquals(1, found);
		assertFalse(membershipFacade.membershipEntered());
	}
	
	/**
	 * Pre: Membership card is swiped, but card number not in database.
	 * Expected: reactToInvalidMembershipEntered event is called.
	 */
	@Test
	public void testEventInvalidSwipe2() throws IOException {
		membershipFacade.register(new ListenerStub());
		MembershipCard card = new MembershipCard("NOT membership", "9999", "cardHolder", true);
		MemberShipDatabase.MEMBERSHIP_DATABASE.add("1111");
		while (!flag)
			try {
				station.cardReader.swipe(card, null);
				flag = true;
			} catch (Exception e) {}
		assertFalse(membershipFacade.membershipEntered());
	}
	
	/**
	 * Pre: Valid Membership card is entered through GUI keyboard or touch screen.
	 * Expected: reactToValidMembershipEntered event is called.
	 */
	@Test
	public void testEventValidMembershipPLU() {
		membershipFacade.register(new ListenerStub() {
			@Override
			public void reactToValidMembershipEntered(String number) {
				found++;
				assertEquals("1234", number);
			}
		});
		//MembershipCard card = new MembershipCard("membership", "1234", "cardHolder", true);
		MemberShipDatabase.MEMBERSHIP_DATABASE.add("1234");
		InnerListener inner = membershipFacade.new InnerListener();
		inner.reactToCodeInputEvent("1234");
		assertEquals(1, found);
		assertTrue(membershipFacade.membershipEntered());
	}
	
	/**
	 * Pre: Membership card is entered through GUI keyboard or
	 * touch screen, but the card number is not in the database.
	 * Expected: reactToInvalidMembershipEntered event is called.
	 */
	@Test
	public void testEventInvalidKey() throws IOException {
		membershipFacade.register(new ListenerStub() {
			@Override
			public void reactToInvalidMembershipEntered() {
				found++;
			}
		});
		//MembershipCard card = new MembershipCard("membership", "9999", "cardHolder", true);
		MemberShipDatabase.MEMBERSHIP_DATABASE.add("1111");
		InnerListener inner = membershipFacade.new InnerListener();
		inner.reactToCodeInputEvent("9999");
		assertEquals(1, found);
		assertFalse(membershipFacade.membershipEntered());
	}
	
	/**
	 * Assert that these hardware events don't announce to listeners.
	 */
	@Test
	public void testMethodsNoEvents() {
		InnerListener inner = membershipFacade.new InnerListener();
		//Will fail if any listener event is entered.
		inner.reactToDisabledEvent(station.mainScanner);
		inner.reactToEnabledEvent(station.mainScanner);
		inner.reactToCardRemovedEvent(station.cardReader);
		inner.reactToCardInsertedEvent(station.cardReader);
		inner.reactToCardSwipedEvent(station.cardReader);
		inner.reactToCardTappedEvent(station.cardReader);
	}
	
	/*--------------- STUBS ---------------*/
	
	/**Stubs primarily check if/how many times observer events occurred.
	 * Tests should fail if an unexpected event is reported.
	 * Override any event in this stub that you don't want to fail.
	 */
	class ListenerStub implements MembershipListener {
		@Override
		public void reactToDisableDeviceRequest(AbstractDevice<? extends AbstractDeviceObserver> device) {fail();}
		@Override
		public void reactToEnableDeviceRequest(AbstractDevice<? extends AbstractDeviceObserver> device) {fail();}
		@Override
		public void reactToDisableStationRequest() {fail();}
		@Override
		public void reactToEnableStationRequest() {fail();}
		@Override
		public void reactToValidMembershipEntered(String number) {fail();}
		@Override
		public void reactToInvalidMembershipEntered() {fail();}
	}

}