package com.autovend.software.controller;

import com.autovend.devices.OverloadException;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.software.model.CustomerSession;
import com.autovend.software.model.CustomerSession.State;
import com.autovend.software.view.MainView;

public class MainController {

    private CustomerSession currentSession;
    private MainView checkoutView;
    private SelfCheckoutStation selfCheckoutStation;

    private PaymentController paymentController;
    private AddItemController addItemController;


    public MainController(SelfCheckoutStation selfCheckoutStation, MainView checkoutView) {
        this.selfCheckoutStation = selfCheckoutStation;
        this.checkoutView = checkoutView;
        this.paymentController = new PaymentController(this,currentSession);
        this.addItemController = new AddItemController(this,currentSession);
    }


    public void setState(State newState) {
        currentSession.setState(newState);

        switch (newState) {
            case INITIAL:
                this.selfCheckoutStation.billInput.disable();
                this.selfCheckoutStation.cardReader.disable();
                this.selfCheckoutStation.coinSlot.disable();
                this.selfCheckoutStation.mainScanner.disable();
                this.selfCheckoutStation.handheldScanner.disable();
                this.selfCheckoutStation.printer.disable();
                this.selfCheckoutStation.baggingArea.disable();
                break;
            case ADDING_ITEMS:
                this.selfCheckoutStation.mainScanner.enable();
                this.selfCheckoutStation.handheldScanner.enable();
                this.selfCheckoutStation.baggingArea.enable();
                break;
            case WEIGHING_ITEMS:
                this.selfCheckoutStation.mainScanner.disable();
                this.selfCheckoutStation.handheldScanner.disable();
                break;
            case PAYMENT_PROCESSING:
                this.selfCheckoutStation.billInput.enable();
                this.selfCheckoutStation.cardReader.enable();
                this.selfCheckoutStation.coinSlot.enable();
                break;
            case DISABLED:
                this.selfCheckoutStation.billInput.disable();
                this.selfCheckoutStation.cardReader.disable();
                this.selfCheckoutStation.coinSlot.disable();
                this.selfCheckoutStation.mainScanner.disable();
                this.selfCheckoutStation.handheldScanner.disable();
                this.selfCheckoutStation.printer.disable();
                this.selfCheckoutStation.baggingArea.disable();
                break;
            case PAYMENT_COMPLETE:
                this.selfCheckoutStation.billInput.disable();
                this.selfCheckoutStation.cardReader.disable();
                this.selfCheckoutStation.coinSlot.disable();
                this.selfCheckoutStation.mainScanner.disable();
                this.selfCheckoutStation.handheldScanner.disable();
                dispenseChange();
                break;
            case RECEIPT_PRINTING:
                this.selfCheckoutStation.printer.enable();
            default:
                break;
        }
    }

    private void dispenseChange() {

    }


    public void start() {
        setState(State.INITIAL);
    }

    public boolean checkWeight() throws OverloadException {
        return currentSession.getExpectedWeight() == selfCheckoutStation.baggingArea.getCurrentWeight();
    }



}
