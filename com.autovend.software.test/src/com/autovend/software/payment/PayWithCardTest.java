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

import static org.junit.Assert.fail;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import com.autovend.Bill;
import com.autovend.Coin;
import com.autovend.devices.AbstractDevice;
import com.autovend.devices.BillDispenser;
import com.autovend.devices.CoinDispenser;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.software.test.Setup;
import com.autovend.software.ui.CustomerView;

public class PayWithCardTest {
	private SelfCheckoutStation station;
	private PayWithCard payWithCard;
	
	@Before
	public void setup() {
		//Setup the class to test
		station = Setup.createSelfCheckoutStation();
		payWithCard = new PayWithCard(station, new CustomerView());
	}
	
	@Test (expected = NullPointerException.class)
	public void testContructorNullStation() {
		new PayWithCard(null, new CustomerView());
	}
	
	@Test (expected = NullPointerException.class)
	public void testContructorNullView() {
		new PayWithCard(station, null);
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
		public void onPaymentAddedEvent(BigDecimal amount) {fail();}
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
