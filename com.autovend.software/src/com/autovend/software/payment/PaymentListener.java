package com.autovend.software.payment;

import java.math.BigDecimal;

import com.autovend.software.AbstractFacadeListener;

public interface PaymentListener extends AbstractFacadeListener {
	
	void onPaymentSuccessful(BigDecimal amount);
	
	void onPaymentFailure();
	
	void onChangeDispensed();
	
	void onChangeDispensedFailure();

}
