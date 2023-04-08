package com.autovend.software.membership;

import com.autovend.Barcode;
import com.autovend.devices.AbstractDevice;
import com.autovend.devices.BarcodeScanner;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.BarcodeScannerObserver;
import com.autovend.external.ProductDatabases;

public class ScanMembership extends MembershipFacade implements BarcodeScannerObserver {
    public ScanMembership(SelfCheckoutStation station) {
        super(station);
    }

    @Override
    public void reactToEnabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {

    }

    @Override
    public void reactToDisabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {

    }

    @Override
    public void reactToBarcodeScannedEvent(BarcodeScanner barcodeScanner, Barcode barcode) {
        if(1 == 1){ //
            // try for membership
        }
    }
}
