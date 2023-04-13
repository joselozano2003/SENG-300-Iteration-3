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
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;

import com.autovend.Bill;
import com.autovend.ChipFailureException;
import com.autovend.Coin;
import com.autovend.CreditCard;
import com.autovend.DebitCard;
import com.autovend.devices.AbstractDevice;
import com.autovend.devices.BillDispenser;
import com.autovend.devices.CardReader;
import com.autovend.devices.CoinDispenser;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.external.CardIssuer;
import com.autovend.software.BankIO;
import com.autovend.software.payment.PaymentFacadeTest.PaymentEventListenerStub;
import com.autovend.software.test.Setup;
import com.autovend.software.ui.CustomerView;

public class PayWithCardTest {
	private SelfCheckoutStation station;
	private PayWithCard payWithCard;
	private CardReader reader;
	
	private CardIssuer credit;
	private CreditCard creditCard;
	private CardIssuer debit;
	private DebitCard debitCard;
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
		payWithCard = new PayWithCard(station, new CustomerView());
		CardReader reader = station.cardReader;
		PaymentEventListenerStub stub = new PaymentEventListenerStub();
		flag = false;
		
		// Setup date to use for cards
		Calendar date = Calendar.getInstance();
		date.set(Calendar.YEAR, 2024);
		date.set(Calendar.MONTH, 7);
		date.set(Calendar.DAY_OF_MONTH, 4);
		
		// Create some card issuers, cards and register them
		credit = new CardIssuer("credit");
		BankIO.CARD_ISSUER_DATABASE.put("credit", credit);
		creditCard = new CreditCard("credit", "00000", "Some Guy", "902", "1111", true, true);
		credit.addCardData("00000", "Some Guy", date, "902", BigDecimal.valueOf(100));
		
		debit = new CardIssuer("debit");
		BankIO.CARD_ISSUER_DATABASE.put("debit", debit);
		debitCard = new DebitCard("debit", "00001", "Some Other Guy", "209", "1112", true, true);
		debit.addCardData("00001", "Some Other Guy", date, "209", BigDecimal.valueOf(500));
		
		// Card Issuer and Card that will not be registered in database to cause issues
		unregisteredIssuer = new CardIssuer("Do not register");
		unregisteredCard = new CreditCard("credit", "00003", "Some Guy 2", "903", "1113", true, true);
		
		// Create a card that is blocked by an issuer
		blockedCard = new CreditCard("credit2", "00004", "Some Guy 3", "904", "1114", true, true);
		BankIO.CARD_ISSUER_DATABASE.put("credit2", credit);
		credit.block("00004");
	}
	
	@Test (expected = NullPointerException.class)
	public void testContructorNullStation() {
		new PayWithCard(null, new CustomerView());
	}
	
	@Test (expected = NullPointerException.class)
	public void testContructorNullView() {
		new PayWithCard(station, null);
	}
	
	/**
	 * Plans for Testing Cards
	 * 		- At minimum just test a card of type "credit" for a cardissuer w/ name "credit"
	 * 			- Since the implementation is the same, but optimally do both debit and credit (since they are split in UCs)
	 * 		- Try inserting, tapping, and swiping each card for both debit and credit (all should prompt CardDataReadEvent if valid)
	 * 			- Try using a third card that is not registered to an issuer, expecting onPaymentFailure()
	 * 			- Try using a blocked card so that the holdNumber returned is "-1" (or maybe a negative amount due?), expecting onPaymentFailure()
	 * 			- Also expect an onPaymentFailure() if transaction was not posted
	 * 			- Otherwise, expect an onPaymentAddedEvent with the amount passed in
	 */
	
	/**
	 * Attempt payment with a card whose issuer is missing from the BankIO database.
	 * Expect a payment failure event to be announced.
	 */
	@Test
	public void PayWithUnregisteredCard() {
		CardReader reader = station.cardReader;
		payWithCard.setAmountDue(BigDecimal.valueOf(5));
		payWithCard.register(new PaymentEventListenerStub());
		while (!flag)
			try {
				reader.insert(unregisteredCard, "1113");
				flag = true;
			} catch (IOException e) {
				reader.remove();
			}
		assertEquals(1, paymentFailCounter);
	}
	
	/**
	 * Pay with a valid credit card, by tapping.
	 * Expect onPaymentAddedEvent passing in amount paid.
	 */
	@Test
	public void PayWithValidCreditInsert() {
		payWithCard.setAmountDue(BigDecimal.valueOf(5));
		payWithCard.register(new PaymentEventListenerStub());
		CardReader reader = station.cardReader;
		while (!flag)
			try {
				reader.insert(creditCard, "1111");
				flag = true;
			} catch (IOException e) {
				reader.remove();
			}
		assertEquals(1, paymentSuccessCounter);
		assertEquals(BigDecimal.valueOf(5), paymentCounter);
	}
	
	/**
	 * Pay with a valid credit card, by tap.
	 * Expect onPaymentAddedEvent passing in amount paid.
	 */
	@Test
	public void PayWithValidCreditTap() {
		payWithCard.setAmountDue(BigDecimal.valueOf(5));
		payWithCard.register(new PaymentEventListenerStub());
		CardReader reader = station.cardReader;
		while (!flag)
			try {
				reader.tap(creditCard);
				flag = true;
			} catch (IOException e) {}
		assertEquals(1, paymentSuccessCounter);
		assertEquals(BigDecimal.valueOf(5), paymentCounter);
	}
	
	/**
	 * Pay with a valid credit card, by tap.
	 * Expect onPaymentAddedEvent passing in amount paid.
	 */
	@Test
	public void PayWithValidCreditSwipe() {
		payWithCard.setAmountDue(BigDecimal.valueOf(5));
		payWithCard.register(new PaymentEventListenerStub());
		CardReader reader = station.cardReader;
		while (!flag)
			try {
				reader.swipe(creditCard, null);
				flag = true;
			} catch (IOException e) {}
		assertEquals(1, paymentSuccessCounter);
		assertEquals(BigDecimal.valueOf(5), paymentCounter);
	}
	
	
	@Test 
	public void testCardRemoved() {
		payWithCard.setAmountDue(BigDecimal.valueOf(5));
		payWithCard.register(new PaymentEventListenerStub());
		CardReader reader = station.cardReader;
		while (!flag)
			try {
				reader.insert(creditCard, "1111");
				reader.remove();
				flag = true;
			} catch (IOException e) {
				reader.remove();
			}
		assertTrue(removed);
	}
	
	@Test
	public void testCreditBlocked() {
		payWithCard.setAmountDue(BigDecimal.valueOf(5));
		payWithCard.register(new PaymentEventListenerStub());
		CardReader reader = station.cardReader;
		while (!flag)
			try {
				reader.insert(blockedCard, "1114");
				reader.remove();
				flag = true;
			} catch (IOException e) {
				reader.remove();
			}
		assertEquals(1, paymentFailCounter);
	}
	
	@Test ()
	public void testNullIssuerFoundPayment() {
		BankIO.CARD_ISSUER_DATABASE.clear();;
		CreditCard testCard = new CreditCard("", "00004", "Some Guy 3", "904", "1114", true, true);
		payWithCard.setAmountDue(BigDecimal.valueOf(5));
		payWithCard.register(new PaymentEventListenerStub());
		CardReader reader = station.cardReader;
		while (!flag)
			try {
				reader.insert(testCard, "1114");
				reader.remove();
				flag = true;
			} catch (IOException e) {
				reader.remove();
			}
		assertEquals(1, paymentFailCounter);
	}
	
	/**
	 * Assert that these hardware events don't announce to listeners.
	 */
	@Test
	public void testMethodsNoEvents() {
		payWithCard.register(new PaymentEventListenerStub());
		payWithCard.reactToDisabledEvent(reader);
		payWithCard.reactToEnabledEvent(reader);
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