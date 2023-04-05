
package com.autovend.software;

import com.autovend.devices.AbstractDevice;
import com.autovend.devices.ReceiptPrinter;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.ReceiptPrinterObserver;

public class ReceiptPrinterObserverStub implements ReceiptPrinterObserver {
    private boolean hasInk;
    private boolean hasPaper;

    @Override
    public void reactToEnabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {}

    @Override
    public void reactToDisabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {}

    @Override
    public void reactToOutOfPaperEvent(ReceiptPrinter printer) { hasPaper = false; }

    @Override
    public void reactToOutOfInkEvent(ReceiptPrinter printer) { hasInk = false; }

    @Override
    public void reactToPaperAddedEvent(ReceiptPrinter printer) { hasPaper = true; }

    @Override
    public void reactToInkAddedEvent(ReceiptPrinter printer) { hasInk = true; }

    public boolean hasPaper() { return hasPaper; }

    public boolean hasInk() { return hasInk; }
}
