package com.autovend.software.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Currency;

import org.junit.Test;

import com.autovend.Card.CardData;
import com.autovend.GiftCard;
import com.autovend.InvalidPINException;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.software.payment.GiftCardDatabase;
import com.autovend.software.payment.PayWithGiftCard;
import com.autovend.software.payment.PaymentFacade;

public class tempTestGift {
	SelfCheckoutStation scs; 
	
	@Test
	public void testPass() throws InvalidPINException {
		GiftCardDatabase.addCard("12345678");
		Currency currency = Currency.getInstance("CAD");
		int[] billDom = {5,10,20};
		BigDecimal[] coinDom = {BigDecimal.valueOf(0.05), BigDecimal.valueOf(0.10),BigDecimal.valueOf(0.25)};
		scs = new SelfCheckoutStation(currency, billDom, coinDom, 10000,2);
		
		GiftCard card = new GiftCard("Gift", "12345678", "2001", currency, new BigDecimal("100"));

		PayWithGiftCard readerob = new PayWithGiftCard(scs);
		readerob.addAmountDue(new BigDecimal("50"));

		scs.cardReader.register(readerob);
		
		try {
			scs.cardReader.insert(card, "2001");
			assertEquals(new BigDecimal("0"), readerob.getAmountDue());
			scs.cardReader.remove();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testTwoPurchases() throws InvalidPINException {
		GiftCardDatabase.addCard("12345678");

		Currency currency = Currency.getInstance("CAD");
		int[] billDom = {5,10,20};
		BigDecimal[] coinDom = {BigDecimal.valueOf(0.05), BigDecimal.valueOf(0.10),BigDecimal.valueOf(0.25)};
		scs = new SelfCheckoutStation(currency, billDom, coinDom, 10000,2);
		
		GiftCard card = new GiftCard("Gift", "12345678", "2001", currency, new BigDecimal("100"));

		PayWithGiftCard readerob = new PayWithGiftCard(scs);
		readerob.addAmountDue(new BigDecimal("50"));
		
		scs.cardReader.register(readerob);
		
		try {
			scs.cardReader.insert(card, "2001");
			assertEquals(new BigDecimal("0"), readerob.getAmountDue());
			scs.cardReader.remove();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		readerob.addAmountDue(new BigDecimal("30"));
		try {
			scs.cardReader.insert(card, "2001");
			assertEquals(new BigDecimal("0"), readerob.getAmountDue());
			scs.cardReader.remove();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		assertEquals(new BigDecimal("0"), readerob.getAmountDue());

	}


	
	@Test
	public void testLessFunds() throws InvalidPINException {
		Currency currency = Currency.getInstance("CAD");
		int[] billDom = {5,10,20};
		BigDecimal[] coinDom = {BigDecimal.valueOf(0.05), BigDecimal.valueOf(0.10),BigDecimal.valueOf(0.25)};
		scs = new SelfCheckoutStation(currency, billDom, coinDom, 10000,2);
		
		GiftCard card = new GiftCard("Gift", "12345678", "2001", currency, new BigDecimal("10"));

		PayWithGiftCard readerob = new PayWithGiftCard(scs);
		readerob.addAmountDue(new BigDecimal("50"));

		scs.cardReader.register(readerob);
		
		try {
			scs.cardReader.insert(card, "2001");
			assertEquals(new BigDecimal("50"), readerob.getAmountDue());
			scs.cardReader.remove();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	@Test
	public void testWrongPin() {
		Currency currency = Currency.getInstance("CAD");
		int[] billDom = {5,10,20};
		BigDecimal[] coinDom = {BigDecimal.valueOf(0.05), BigDecimal.valueOf(0.10),BigDecimal.valueOf(0.25)};
		scs = new SelfCheckoutStation(currency, billDom, coinDom, 10000,2);
		
		GiftCard card = new GiftCard("Gift", "12345678", "2001", currency, new BigDecimal("100"));

		PayWithGiftCard readerob = new PayWithGiftCard(scs);
		readerob.addAmountDue(new BigDecimal("50"));
		
		scs.cardReader.register(readerob);

		try {
			scs.cardReader.insert(card, "200");
			assertFalse(true);
			scs.cardReader.remove();
		} catch (InvalidPINException ipe) {
			assertTrue(true);
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	

}

