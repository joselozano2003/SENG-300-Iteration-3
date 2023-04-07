package com.autovend.software;

import java.util.Map;

import com.autovend.external.CardIssuer;

public class BankIO {
	
	private static Map<String, CardIssuer> cardIssuers;

	public static Map<String, CardIssuer> getCardIssuers() {
		return cardIssuers;
	}

	public static void setCardIssuers(Map<String, CardIssuer> cardIssuers) {
		BankIO.cardIssuers = cardIssuers;
	}

}
