package com.autovend.software.payment;

import java.math.BigDecimal;

import com.autovend.CreditCard;
import com.autovend.DebitCard;
import com.autovend.devices.CardReader;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.external.CardIssuer;

public class PayWithGiftCardTest {
	
	private SelfCheckoutStation station;
	private PayWithGiftCard payWithCard;
	private CardReader reader;
	
	private CardIssuer w;
	private CreditCard creditCard;
	private CardIssuer debit;
	private DebitCard debitCard;
	private CardIssuer unregisteredIssuer;
	private CreditCard unregisteredCard;
	private CreditCard blockedCard;
	
	// Values used for checking success/failure of events
	private BigDecimal paymentCounter = new BigDecimal("0");

}
