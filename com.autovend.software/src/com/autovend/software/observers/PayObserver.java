
package com.autovend.software.observers;

import com.autovend.devices.BillDispenser;
import com.autovend.devices.BillSlot;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.software.Pay;

public interface PayObserver extends AbstractDeviceObserver {

    /**
     * Announces that the customer has provided sufficient funds to cover the
     * total cost of the items in their cart.
     *
     * @param payObject
     *             The payment processor that has processed these funds.
     */
    void reactToSufficientPaymentEvent(Pay payObject);


    /**
     * Announces that a bill has been emitted as change for customer to
     * retrieve.
     *
     * @param payObject
     *             The payment processor that has processed these funds.
     * @param slot
     *             The slot from which the bill was emitted.
     */
    void reactToBillChangeProducedEvent(Pay payObject, BillSlot slot);


    /**
     * Announces that the payment processor object was unable to dispense enough
     * change due to lack of change in dispenser.
     *
     * @param payObject
     *             The payment processor that was dispensing change.
     * @param dispenser
     *             The dispenser that is out of change.
     */
    void reactToInsufficientChangeEvent(Pay payObject, BillDispenser dispenser);
}
