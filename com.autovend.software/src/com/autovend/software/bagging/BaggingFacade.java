package com.autovend.software.bagging;

import com.autovend.devices.AbstractDevice;
import com.autovend.devices.ElectronicScale;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.ElectronicScaleObserver;
import com.autovend.software.AbstractFacade;
import com.autovend.software.customer.CustomerSession;

public class BaggingFacade extends AbstractFacade<BaggingListener> {
    private SelfCheckoutStation selfCheckoutStation;
    private CustomerSession currentSession;

    private class InnerListener implements ElectronicScaleObserver {

        @Override
        public void reactToEnabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
            // TODO Auto-generated method stub
        }

        @Override
        public void reactToDisabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
            // TODO Auto-generated method stub
        }

        @Override
        public void reactToWeightChangedEvent(ElectronicScale scale, double weightInGrams) {
            for (BaggingListener listener : listeners) {
                listener.onWeightChanged(weightInGrams);
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

    public BaggingFacade(SelfCheckoutStation selfCheckoutStation, CustomerSession currentSession) {
        this.selfCheckoutStation = selfCheckoutStation;
        this.currentSession = currentSession;

        selfCheckoutStation.baggingArea.register(new InnerListener());
    }
}

