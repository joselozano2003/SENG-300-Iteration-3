package com.autovend.software.controller;

import java.math.BigDecimal;
import java.util.Currency;

import com.autovend.Card.CardData;
import com.autovend.devices.AbstractDevice;
import com.autovend.devices.BillValidator;
import com.autovend.devices.CardReader;
import com.autovend.devices.CoinValidator;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.BillValidatorObserver;
import com.autovend.devices.observers.CardReaderObserver;
import com.autovend.devices.observers.CoinValidatorObserver;
import com.autovend.software.model.CardPayment;
import com.autovend.software.model.CashPayment;
import com.autovend.software.model.CustomerSession;
import com.autovend.software.model.CustomerSession.State;

public class PaymentController implements BillValidatorObserver, CoinValidatorObserver, CardReaderObserver {

    private MainController mainController;
    private CustomerSession currentSession;

    public PaymentController(MainController mainController, CustomerSession currentSession) {
        this.currentSession = currentSession;
    }

    public void setPaymentMethod(String paymentMethodName) {
        switch (paymentMethodName) {
            case "Cash":
                currentSession.setPaymentMethod(new CashPayment(currentSession)); // You can replace this with the actual
                // cash received
                // value
                break;
            case "Card":
                currentSession.setPaymentMethod(new CardPayment(currentSession)); // Replace this with actual card details
                // entered by the
                // user
                break;
            default:
                currentSession.setPaymentMethod(null);
                break;
        }
    }

    public void checkPaymentComplete() {
        if (currentSession.isPaymentComplete()) {
            mainController.setState(State.PAYMENT_COMPLETE);
            BigDecimal changeDue = currentSession.getTotalPaid().subtract(currentSession.getTotalCost());
            if (changeDue.compareTo(BigDecimal.ZERO) > 0){
                mainController.setState(State.DISPENSE_CHANGE);
            }
        }
    }

    @Override
    public void reactToEnabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
        // TODO Auto-generated method stub

    }

    @Override
    public void reactToDisabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
        // TODO Auto-generated method stub

    }

    @Override
    public void reactToCardInsertedEvent(CardReader reader) {
        // update view

    }

    @Override
    public void reactToCardRemovedEvent(CardReader reader) {
        // update view

    }

    @Override
    public void reactToCardTappedEvent(CardReader reader) {
        // update view

    }

    @Override
    public void reactToCardSwipedEvent(CardReader reader) {
        // // update view

    }

    @Override
    public void reactToCardDataReadEvent(CardReader reader, CardData data) {
        if (currentSession.getPaymentMethod() instanceof CardPayment) {
            ((CardPayment) currentSession.getPaymentMethod()).setCardData(data);
            ((CardPayment) currentSession.getPaymentMethod()).processPayment(currentSession.getTotalCost());
            ;
            if (currentSession.isPaymentComplete()) {
                mainController.setState(State.PAYMENT_COMPLETE);
            }
        }

    }

    @Override
    public void reactToValidCoinDetectedEvent(CoinValidator validator, BigDecimal value) {
        if (currentSession.getPaymentMethod() instanceof CashPayment) {
            ((CashPayment) currentSession.getPaymentMethod()).processPayment(value);
            ;
            if (currentSession.isPaymentComplete()) {
                mainController.setState(State.PAYMENT_COMPLETE);
            }
        }

    }

    @Override
    public void reactToInvalidCoinDetectedEvent(CoinValidator validator) {
        // TODO Auto-generated method stub

    }

    @Override
    public void reactToValidBillDetectedEvent(BillValidator validator, Currency currency, int value) {
        if (currentSession.getPaymentMethod() instanceof CashPayment) {
            ((CashPayment) currentSession.getPaymentMethod()).processPayment(BigDecimal.valueOf(value));
            ;
            if (currentSession.isPaymentComplete()) {
                mainController.setState(State.PAYMENT_COMPLETE);
            }
        }

    }

    @Override
    public void reactToInvalidBillDetectedEvent(BillValidator validator) {
        // TODO Auto-generated method stub

    }

}
