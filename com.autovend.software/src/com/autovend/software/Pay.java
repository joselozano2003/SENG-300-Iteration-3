

package com.autovend.software;

import java.math.BigDecimal;

import com.autovend.devices.AbstractDevice;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.SimulationException;
import com.autovend.software.observers.PayObserver;


public abstract class Pay extends AbstractDevice<PayObserver> {
    protected SelfCheckoutStation station;
    protected BigDecimal amountDue;


    public Pay(SelfCheckoutStation station) {
        if (station == null) {
            throw new SimulationException(new NullPointerException("Station cannot be null."));
        }
        this.station = station;
        amountDue = PurchasedItems.getAmountLeftToPay();
    }

    protected void pay(BigDecimal amountToPay) {
        PurchasedItems.addAmountPaid(amountToPay);
        BigDecimal amountPaid = PurchasedItems.getAmountPaid();
    	if (amountPaid.equals(amountDue)) {
    		for (PayObserver observer : observers) {
    			observer.reactToSufficientPaymentEvent(this);
    		}
    	}
    }

    public BigDecimal getAmountDue() {
        return PurchasedItems.getAmountLeftToPay();
    }
}
