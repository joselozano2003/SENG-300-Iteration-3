package com.autovend.software.model;

import java.math.BigDecimal;

public class CashPayment extends PaymentMethod {
    CustomerSession currentSession;
    BigDecimal totalPaid = BigDecimal.ZERO;

    public CashPayment(CustomerSession currentSession) {
        super (currentSession);
    }

    @Override
    public void processPayment(BigDecimal amount) {
        currentSession.addPayment(amount);
    }

    @Override
    String getPaymentMethodName() {
        // TODO Auto-generated method stub
        return "Cash";
    }






}