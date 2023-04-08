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
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.autovend.MembershipCard;
import com.autovend.devices.AbstractDevice;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.software.test.Setup;

public class MembershipFacadeTest {
	class ListenerStub implements MembershipListener {
		@Override
		public void reactToHardwareFailure() {fail();}
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
	
	private SelfCheckoutStation station;
	private MembershipFacade membershipFacade;
	private int found;
	
	@Before
	public void setup() {
		//Setup the class to test
		station = Setup.createSelfCheckoutStation();
		membershipFacade = new MembershipFacade(station);
	}
	
	@Test (expected = NullPointerException.class)
	public void testNullContruction() {
		new MembershipFacade(null);
	}
	
	/**
	 * Pre: Valid Membership card data is read.
	 * Expected: reactToValidMembershipEntered event is called.
	 */
	@Test
	public void testEventValidMembershipEntered() throws IOException {
		membershipFacade.register(new ListenerStub() {
			@Override
			public void reactToValidMembershipEntered(String number) {
				found++;
			}
		});
		//TODO: Add card to some database.
		MembershipCard card = new MembershipCard("type", "1234", "cardHolder", true);
		//NOTE: Might fail from random swipe failure. Should test in another way to avoid this.
		station.cardReader.swipe(card, null);
		
		assertEquals(1, found);
	}

}
