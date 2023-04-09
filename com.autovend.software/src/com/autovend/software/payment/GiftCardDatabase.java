package com.autovend.software.payment;

import java.util.HashMap;
import java.util.Map;

import com.autovend.GiftCard;

public class GiftCardDatabase {
	public static Map<String, GiftCard> allGiftCards = new HashMap<String, GiftCard>();
	
	public static void addCard(String number, GiftCard card) {
		allGiftCards.put(number, card);
	}
	
	public static boolean isGiftCard(String num) {
		for (String number: allGiftCards.keySet()) {
			if (num.equals(number)) {
				return true;
			}
		}
		return false;
	}
	
	public static GiftCard getGiftCard(String num) {
		for (String number: allGiftCards.keySet()) {
			if (number.equals(num)) {
				return allGiftCards.get(number);
			}
		}
		return null;
	}
}