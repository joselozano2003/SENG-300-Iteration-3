package com.autovend.devices.observers;

import com.autovend.Bill;
import com.autovend.devices.BillSlot;

/**
 * Observes events emanating from a bill slot.
 */
public interface BillSlotObserver extends AbstractDeviceObserver {
	/**
	 * An event announcing that a bill has been inserted.
	 * 
	 * @param slot
	 *            The device on which the event occurred.
	 */
	void reactToBillInsertedEvent(BillSlot slot);

	/**
	 * An event announcing that a bill has been returned to the user, dangling
	 * from the slot.
	 * 
	 * @param slot
	 *            The device on which the event occurred.
	 */
	void reactToBillEjectedEvent(BillSlot slot);

	/**
	 * An event announcing that a dangling bill has been removed by the user.
	 * 
	 * @param slot
	 *            The device on which the event occurred.
	 */
	void reactToBillRemovedEvent(BillSlot slot);
}
