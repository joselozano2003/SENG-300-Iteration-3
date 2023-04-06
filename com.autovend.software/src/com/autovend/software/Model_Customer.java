package com.autovend.software;

import com.autovend.devices.SelfCheckoutStation;

public class Model_Customer {
    SelfCheckoutStation station;
    private State state;

    public Model_Customer(SelfCheckoutStation station) {
        this.station = station;
        state = State.INITIAL;
    }

    //private Map<Product, Double> shoppingCart;

    private double expectedWeight = 0.0;

    private BigDecimal totalPaid;
    //private PaymentMethod paymentMethod;

    public enum State {
        INITIAL, ADDING_ITEMS, WEIGHING_ITEMS, PAYMENT_METHOD_SELECTION, PAYMENT_PROCESSING, PAYMENT_COMPLETE, DISPENSE_CHANGE,
        RECEIPT_PRINTING, FINISHED, DISABLED,
    }
}
