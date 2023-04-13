// P3-4 Group Members
//
// Abdelrhafour, Achraf (30022366)
// Campos, Oscar (30057153)
// Cavilla, Caleb (30145972)
// Crowell, Madeline (30069333)
// Debebe, Abigia (30134608)
// Dhuka, Sara Hazrat (30124117)
// Drissi, Khalen (30133707)
// Ferreira, Marianna (30147733)
// Frey, Ben (30088566)
// Himel, Tanvir (30148868)
// Huayhualla Arce, Fabricio (30091238)
// Kacmar, Michael (30113919)
// Lee, Jeongah (30137463)
// Li, Ran (10120152)
// Lokanc, Sam (30114370)
// Lozano Cetina, Jose Camilo (30144736)
// Maahdie, Monmoy (30149094)
// Malik, Akansha (30056048)
// Mehedi, Abdullah (30154770)
// Polton, Scott (30138102)
// Rahman, Saadman (30153482)
// Rodriguez, Gabriel (30162544)
// Samin Rashid, Khondaker (30143490)
// Sloan, Jaxon (30123845)
// Tran, Kevin (30146900)
//

package com.autovend.software.payment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import com.autovend.ChipFailureException;
import com.autovend.CreditCard;
import com.autovend.DebitCard;
import com.autovend.GiftCard;
import com.autovend.devices.BillDispenser;
import com.autovend.devices.CardReader;
import com.autovend.devices.CoinDispenser;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.external.CardIssuer;
import com.autovend.software.payment.PayWithCardTest.PaymentEventListenerStub;
import com.autovend.software.test.Setup;
import com.autovend.software.ui.CustomerView;

public class PayWithGiftCardTest {
	
	private SelfCheckoutStation station;
	private PayWithGiftCard payWithGiftCard;
	private CardReader reader;
	
	private GiftCard giftCard;
	private GiftCard notInDBCard;
	private GiftCard noMoney;
	private CardIssuer unregisteredIssuer;
	private CreditCard unregisteredCard;
	private CreditCard blockedCard;
	
	// Values used for checking success/failure of events
	private BigDecimal paymentCounter = new BigDecimal("0");
	private int paymentFailCounter = 0;
	private int paymentSuccessCounter = 0;
	private boolean removed = false;
	
	
	// Flag used to rerun methods prone to hardware failure
	private boolean flag;

	@Before
	public void setup() {
		//Setup the class to test
		station = Setup.createSelfCheckoutStation();
		payWithGiftCard = new PayWithGiftCard(station, new CustomerView());
		CardReader reader = station.cardReader;
		paymentFailCounter = 0;
		paymentSuccessCounter = 0;
		removed = false;
		flag = false;
		
		giftCard = new GiftCard("Gift Card", "00000", "1111", Setup.getCurrency(),BigDecimal.valueOf(100));
		notInDBCard = new GiftCard("No DB", "00001", "1112", Setup.getCurrency(),BigDecimal.valueOf(100));
		noMoney = new GiftCard("No Money", "00002", "1113", Setup.getCurrency(),BigDecimal.valueOf(1));
		
		GiftCardDatabase.addCard("00000", giftCard);
		GiftCardDatabase.addCard("00002", noMoney);
		
		
	}
	@Test (expected = NullPointerException.class)
	public void testContructorNullStation() {
		new PayWithGiftCard(null, new CustomerView());
	}
	
	@Test (expected = NullPointerException.class)
	public void testContructorNullView() {
		new PayWithGiftCard(station, null);
	}
	/**
	 * Insert a valid gift card into the station's reader,
	 * expecting a paymentAddedEvent and payment to be processed.
	 */
	@Test
	public void testInsertValidGiftCard() {
		payWithGiftCard.setAmountDue(BigDecimal.valueOf(5));
		payWithGiftCard.register(new PaymentEventListenerStub());
		CardReader reader = station.cardReader;
		while (!flag)
			try {
				reader.insert(giftCard, "1111");
				flag = true;
			} catch (Exception e) {}
		assertEquals(1, paymentSuccessCounter);
		assertEquals(BigDecimal.valueOf(5), paymentCounter);
	}
	@Test
	public void testNoDBGiftCard() {
		payWithGiftCard.setAmountDue(BigDecimal.valueOf(5));
		payWithGiftCard.register(new PaymentEventListenerStub());
		CardReader reader = station.cardReader;
		while (!flag)
			try {
				reader.insert(notInDBCard, "1112");
				flag = true;
			} catch (Exception e) {}
		assertEquals(1, paymentFailCounter);
	}
	@Test
	public void testNoMoney() {
		payWithGiftCard.setAmountDue(BigDecimal.valueOf(5));
		payWithGiftCard.register(new PaymentEventListenerStub());
		CardReader reader = station.cardReader;
		while (!flag)
			try {
				reader.insert(noMoney, "1113");
				flag = true;
			} catch (Exception e) {}
		assertEquals(1, paymentFailCounter);
	}
	@Test
	public void testChipFail() {

	}
	/*--------------- STUBS ---------------*/
	
	/**Stubs primarily check if/how many times observer events occurred.
	 * Tests should fail if an unexpected event is reported.
	 * Override any event in this stub that you don't want to fail.
	 */
	public class PaymentEventListenerStub implements PaymentEventListener {
		@Override
		public void reactToDisableStationRequest() {fail();}
		@Override
		public void reactToEnableStationRequest() {fail();}
		@Override
		public void onPaymentAddedEvent(BigDecimal amount) {
			paymentCounter = paymentCounter.add(amount);
			paymentSuccessCounter++;
		}
		@Override
		public void onPaymentFailure() {
			paymentFailCounter++;
		}
		@Override
		public void onChangeDispensedEvent(BigDecimal amount) {fail();}
		@Override
		public void onChangeDispensedFailure(BigDecimal totalChangeLeft) {fail();}
		@Override
		public void onLowCoins(CoinDispenser dispenser) {fail();}
		@Override
		public void onLowBills(BillDispenser dispenser) {fail();}
		@Override
		public void cardInserted() {
		}
		@Override
		public void cardRemoved() {
		}
	}
}
