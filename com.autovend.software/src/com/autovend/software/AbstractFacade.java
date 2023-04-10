/* P3-4 Group Members
 * 
 * Abdelrhafour, Achraf (30022366)
 * Campos, Oscar (30057153)
 * Cavilla, Caleb (30145972)
 * Crowell, Madeline (30069333)
 * Debebe, Abigia (30134608)
 * Dhuka, Sara Hazrat (30124117)
 * Drissi, Khalen (30133707)
 * Ferreira, Marianna (30147733)
 * Frey, Ben (30088566)
 * Himel, Tanvir (30148868)
 * Huayhualla Arce, Fabricio (30091238)
 * Kacmar, Michael (30113919)
 * Lee, Jeongah (30137463)
 * Li, Ran (10120152)
 * Lokanc, Sam (30114370)
 * Lozano Cetina, Jose Camilo (30144736)
 * Maahdie, Monmoy (30149094)
 * Malik, Akansha (30056048)
 * Mehedi, Abdullah (30154770)
 * Polton, Scott (30138102)
 * Rahman, Saadman (30153482)
 * Rodriguez, Gabriel (30162544)
 * Samin Rashid, Khondaker (30143490)
 * Sloan, Jaxon (30123845)
 * Tran, Kevin (30146900)
 */
package com.autovend.software;

import java.io.Serializable;
import java.util.ArrayList;

import com.autovend.devices.SelfCheckoutStation;
import com.autovend.software.customer.CustomerView;

/**
 * Ported from hardware and changed a bit for software use.
 * 
 * The abstract base class for all software involved in the station.
 * <p>
 * This class utilizes the listener design pattern. Subclasses inherit the
 * register method, but each must define its own notifyXXX methods.
 * </p>
 * <p>
 * Each software must possess an appropriate listener type, which extends
 * Abstractsoftwarelistener; the type parameter T represents this listener.
 * <p>
 * </p>
 * 
 * @param <T>
 *            The type of listeners used for this software. For a software whose
 *            class is X, its corresponding listener type would typically be
 *            Xlistener.
 */
public abstract class AbstractFacade<T extends AbstractEventListener> implements Serializable {
	private static final long serialVersionUID = -5835508997720707884L;
	/**
	 * The SelfCheckoutStation to perform logic on.
	 */
	protected final SelfCheckoutStation station;
	/**
	 * A list of the registered listeners on this software.
	 */
	
	protected final CustomerView customerView;


	
	protected ArrayList<T> listeners = new ArrayList<>();

	/**
	 * Locates the indicated listener and removes it such that it will no longer be
	 * informed of events from this software. If the listener is not currently
	 * registered with this software, calls to this method will return false, but
	 * otherwise have no effect.
	 * 
	 * @param listener
	 *            The listener to remove.
	 * @return true if the listener was found and removed, false otherwise.
	 */
	public final boolean deregister(T listener) {
		return listeners.remove(listener);
	}

	/**
	 * All listeners registered with this software are removed. If there are none,
	 * calls to this method have no effect.
	 */
	public final void deregisterAll() {
		listeners.clear();
	}

	/**
	 * Registers the indicated listener to receive event notifications from this
	 * software.
	 * 
	 * @param listener
	 *            The listener to be added.
	 */
	public final void register(T listener) {
		listeners.add(listener);
	}
	
	/**
	 * Basic constructor.
	 * 
	 * @param station
	 */
	protected AbstractFacade(SelfCheckoutStation station, CustomerView customerView) {
		if (station == null || customerView == null)
			throw new NullPointerException("SelfCheckoutStation cannot be null");
		this.station = station;
		this.customerView = customerView;
	}
	
}
