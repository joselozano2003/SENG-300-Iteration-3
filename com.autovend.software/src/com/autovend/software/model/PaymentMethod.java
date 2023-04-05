package com.autovend.software.model;

import java.math.BigDecimal;

public abstract class PaymentMethod {
    CustomerSession currentSession;

    public PaymentMethod(CustomerSession currentSession) {
        this.currentSession = currentSession;
    }
    abstract void processPayment(BigDecimal amount);
    abstract String getPaymentMethodName();
}
