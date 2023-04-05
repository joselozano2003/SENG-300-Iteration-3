
package com.autovend.software;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.autovend.Bill;
import com.autovend.Coin;
import com.autovend.devices.*;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.BillDispenserObserver;
import com.autovend.devices.observers.BillValidatorObserver;
import com.autovend.devices.observers.CoinDispenserObserver;
import com.autovend.devices.observers.CoinValidatorObserver;
import com.autovend.devices.observers.ElectronicScaleObserver;
import com.autovend.devices.SelfCheckoutStation;

public class WeightDiscrepancy implements ElectronicScaleObserver{
    private SelfCheckoutStation selfCheckoutStation;

    public WeightDiscrepancy(SelfCheckoutStation selfCheckoutStation)
    {
        this.selfCheckoutStation = selfCheckoutStation;
        selfCheckoutStation.baggingArea.register(this);
    }
    @Override
    public void reactToWeightChangedEvent(ElectronicScale scale, double weightInGrams) {
        // Exception 1. Weight Discrepancy
        // stays disabled if the weight of the bagging area does not match the expected weight
            if(PurchasedItems.getTotalExpectedWeight() == weightInGrams) {
                selfCheckoutStation.handheldScanner.enable();
                selfCheckoutStation.mainScanner.enable();
            }
            else
            {
                selfCheckoutStation.handheldScanner.disable();
                selfCheckoutStation.mainScanner.disable();
                // throw exception to IO?
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
    public void reactToOverloadEvent(ElectronicScale scale) {
        // TODO Auto-generated method stub
        selfCheckoutStation.handheldScanner.disable();
        selfCheckoutStation.mainScanner.disable();
        // throw exception to IO?
    }

    @Override
    public void reactToOutOfOverloadEvent(ElectronicScale scale) {
        // TODO Auto-generated method stub
            selfCheckoutStation.handheldScanner.enable();
            selfCheckoutStation.mainScanner.enable();
    }
}
