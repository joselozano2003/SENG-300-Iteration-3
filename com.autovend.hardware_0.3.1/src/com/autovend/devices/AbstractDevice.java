package com.autovend.devices;

import java.io.Serializable;
import java.util.ArrayList;

import com.autovend.devices.observers.AbstractDeviceObserver;

/**
 * The abstract base class for all devices involved in the simulator.
 * <p>
 * This class utilizes the Observer design pattern. Subclasses inherit the
 * register method, but each must define its own notifyXXX methods.
 * </p>
 * <p>
 * Each device must possess an appropriate observer type, which extends
 * AbstractDeviceObserver; the type parameter T represents this observer.
 * <p>
 * <p>
 * Any individual device can be disabled, which means it will not permit
 * physical movements to be caused by the software. Any method that could cause
 * a physical movement will declare that it throws DisabledException.
 * </p>
 * 
 * @param <T>
 *            The type of observers used for this device. For a device whose
 *            class is X, its corresponding observer type would typically be
 *            XObserver.
 */
public abstract class AbstractDevice<T extends AbstractDeviceObserver> implements Serializable {
	private static final long serialVersionUID = -5835508997720707884L;

	/**
	 * A list of the registered observers on this device.
	 */
	protected ArrayList<T> observers = new ArrayList<>();

	/**
	 * Locates the indicated observer and removes it such that it will no longer be
	 * informed of events from this device. If the observer is not currently
	 * registered with this device, calls to this method will return false, but
	 * otherwise have no effect.
	 * 
	 * @param observer
	 *            The observer to remove.
	 * @return true if the observer was found and removed, false otherwise.
	 */
	public final boolean deregister(T observer) {
		return observers.remove(observer);
	}

	/**
	 * All observers registered with this device are removed. If there are none,
	 * calls to this method have no effect.
	 */
	public final void deregisterAll() {
		observers.clear();
	}

	/**
	 * Registers the indicated observer to receive event notifications from this
	 * device.
	 * 
	 * @param observer
	 *            The observer to be added.
	 */
	public final void register(T observer) {
		observers.add(observer);
	}

	private boolean disabled = false;

	/**
	 * Disables this device from receiving input and producing output. If the
	 * devices is currently enabled, this announces a "disabled" event to registered
	 * listeners.
	 */
	public final void disable() {
		if(disabled)
			return;

		disabled = true;

		for(T observer : observers)
			observer.reactToDisabledEvent(this);
	}

	/**
	 * Enables this device for receiving input and producing output. If the devices
	 * is currently disabled, this announces an "enabled" event to registered
	 * listeners.
	 */
	public final void enable() {
		if(!disabled)
			return;
		
		disabled = false;

		for(T observer : observers)
			observer.reactToEnabledEvent(this);
	}

	/**
	 * Returns whether this device is currently disabled from receiving input and
	 * producing output.
	 * 
	 * @return true if the device is disabled; false if the device is enabled.
	 */
	public final boolean isDisabled() {
		return disabled;
	}
}
