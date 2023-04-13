package com.autovend.software.payment;

import static org.junit.Assert.fail;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

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
		GiftCardDatabase.addCard("00000", giftCard);
		
	}
	
	/**
	 * Insert a valid gift card into the station's reader,
	 * expecting a paymentAddedEvent and payment to be processed.
	 */
	@Test
	public void testInsertValidGiftCard() {
		
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
			// TODO Auto-generated method stub
			
		}
		@Override
		public void cardRemoved() {
			removed = true;
			
		}
	}
}
