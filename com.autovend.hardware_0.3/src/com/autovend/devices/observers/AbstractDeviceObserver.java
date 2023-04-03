package com.autovend.devices.observers;

import com.autovend.devices.AbstractDevice;

/**
 * This class represents the abstract interface for all device listeners. All
 * subclasses should add their own event notification methods, the first
 * parameter of which should always be the device affected.
 */
public interface AbstractDeviceObserver {
	/**
	 * Announces that the indicated device has been enabled.
	 * 
	 * @param device
	 *            The device that has been enabled.
	 */
	public void reactToEnabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device);

	/**
	 * Announces that the indicated device has been disabled.
	 * 
	 * @param device
	 *            The device that has been enabled.
	 */
	public void reactToDisabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device);
}
