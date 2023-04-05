package com.autovend.software.controller;

import com.autovend.Barcode;
import com.autovend.PriceLookUpCode;
import com.autovend.devices.AbstractDevice;
import com.autovend.devices.BarcodeScanner;
import com.autovend.devices.ElectronicScale;
import com.autovend.devices.OverloadException;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.BarcodeScannerObserver;
import com.autovend.devices.observers.ElectronicScaleObserver;
import com.autovend.external.ProductDatabases;
import com.autovend.products.BarcodedProduct;
import com.autovend.products.PLUCodedProduct;
import com.autovend.software.model.CustomerSession;
import com.autovend.software.model.CustomerSession.State;

public class AddItemController implements BarcodeScannerObserver, ElectronicScaleObserver {

    private MainController mainController;
    private CustomerSession currentSession;

    public AddItemController(MainController mainController, CustomerSession currentSession) {
        this.mainController = mainController;
        this.currentSession = currentSession;
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
    public void reactToBarcodeScannedEvent(BarcodeScanner barcodeScanner, Barcode barcode) {
        BarcodedProduct barcodedProduct = ProductDatabases.BARCODED_PRODUCT_DATABASE.get(barcode);
        currentSession.addItem(barcodedProduct, 1);
        currentSession.setState(State.WEIGHING_ITEMS);

    }

    public void reactToPLUCodeEnteredEvent(PriceLookUpCode priceLookUpCode, double weightToPurchase) {
        PLUCodedProduct pluCodedProduct = ProductDatabases.PLU_PRODUCT_DATABASE.get(priceLookUpCode);
        currentSession.addItem(pluCodedProduct, weightToPurchase);
        currentSession.setState(State.WEIGHING_ITEMS);

    }

    @Override
    public void reactToWeightChangedEvent(ElectronicScale scale, double weightInGrams) {
        try {
            boolean weightIsValid = mainController.checkWeight();
            if (weightIsValid) {
                mainController.setState(State.ADDING_ITEMS);
            } else {
                mainController.setState(State.DISABLED);
            }
        } catch (OverloadException e) {

        }

    }

    @Override
    public void reactToOverloadEvent(ElectronicScale scale) {
        // TODO Auto-generated method stub

    }

    @Override
    public void reactToOutOfOverloadEvent(ElectronicScale scale) {
        // TODO Auto-generated method stub

    }

}
