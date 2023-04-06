package com.autovend.software.controller;

import com.autovend.devices.AbstractDevice;
import com.autovend.devices.ReceiptPrinter;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.ReceiptPrinterObserver;
import com.autovend.software.ach.CustomerSessionController;

public class PrinterController implements ReceiptPrinterObserver {

    private MainController mainController;
    private CustomerSessionController currentSession;

    public PrinterController(MainController mainController, CustomerSessionController currentSession) {
        this.mainController = mainController;
        this.currentSession = currentSession;
    }

    @Override
    public void reactToEnabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {

    }

    @Override
    public void reactToDisabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {

    }

    @Override
    public void reactToOutOfPaperEvent(ReceiptPrinter printer) {
        // handle use case here
        currentSession.setState(CustomerSessionController.State.DISABLE_STATION);
    }

    @Override
    public void reactToOutOfInkEvent(ReceiptPrinter printer) {
        currentSession.setState(CustomerSessionController.State.DISABLE_STATION);
    }

    @Override
    public void reactToPaperAddedEvent(ReceiptPrinter printer) {

    }

    @Override
    public void reactToInkAddedEvent(ReceiptPrinter printer) {

    }
}
