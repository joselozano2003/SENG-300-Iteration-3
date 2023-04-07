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
import com.autovend.software.payment.WithGiftCard;

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
//		CardData data = card.createCardInsertData("2001");

		WithGiftCard readerob = new WithGiftCard(scs);
		readerob.setValue(new BigDecimal("50"));

		scs.cardReader.register(readerob);
		
		try {
			scs.cardReader.insert(card, "2001");
			assertEquals(new BigDecimal("0"), readerob.value);
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
//		CardData data = card.createCardInsertData("2001");

		WithGiftCard readerob = new WithGiftCard(scs);
		readerob.setValue(new BigDecimal("50"));
		
		scs.cardReader.register(readerob);
		
		try {
			scs.cardReader.insert(card, "2001");
			assertEquals(readerob.value, new BigDecimal("0"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	
	@Test
	public void testLessFunds() throws InvalidPINException {
		Currency currency = Currency.getInstance("CAD");
		int[] billDom = {5,10,20};
		BigDecimal[] coinDom = {BigDecimal.valueOf(0.05), BigDecimal.valueOf(0.10),BigDecimal.valueOf(0.25)};
		scs = new SelfCheckoutStation(currency, billDom, coinDom, 10000,2);
		
		GiftCard card = new GiftCard("Gift", "12345678", "2001", currency, new BigDecimal("10"));
//		CardData data = card.createCardInsertData("2001");

		WithGiftCard readerob = new WithGiftCard(scs);
		readerob.setValue(new BigDecimal("50"));

		scs.cardReader.register(readerob);
		
		try {
			scs.cardReader.insert(card, "2001");
			assertEquals(readerob.value, new BigDecimal("50"));
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
//		CardData data = card.createCardInsertData("2001");

		WithGiftCard readerob = new WithGiftCard(scs);
		
		scs.cardReader.register(readerob);
		readerob.setValue(new BigDecimal("50"));

		try {
			scs.cardReader.insert(card, "200");
			assertFalse(true);
		} catch (InvalidPINException ipe) {
			assertTrue(true);
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	

}

