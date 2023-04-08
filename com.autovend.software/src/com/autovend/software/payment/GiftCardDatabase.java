package com.autovend.software.payment;

import java.util.ArrayList;
import java.util.Map;

import com.autovend.GiftCard;

public class GiftCardDatabase {
	public static ArrayList<String> allGiftCards = new ArrayList<String>();
	
	public static void addCard(String number) {
		allGiftCards.add(number);
	}
	
	public static boolean isGiftCard(String num) {
		for (String number: allGiftCards) {
			if (num.equals(number)) {
				return true;
			}
		}
		return false;
	}
}