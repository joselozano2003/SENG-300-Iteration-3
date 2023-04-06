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

import com.autovend.devices.AbstractDevice;
import com.autovend.devices.observers.AbstractDeviceObserver;

/**
 * Ported from hardware and changed a bit.
 * 
 * This class represents the abstract interface for all software listeners.
 * All subclasses should add their own event notification methods, the first
 * parameter of which should always be the software affected.
 */
public interface AbstractFacadeListener {
	/**
	 * Announces that the indicated software failed to complete due to a hardware failure.
	 */
	public void reactToHardwareFailure();
	/**
	 * Announces that a device should be disabled.
	 * 
	 * @param device
	 *            The device to disable.
	 */
	public void reactToDisableDeviceRequest(AbstractDevice<? extends AbstractDeviceObserver> device);
	/**
	 * Announces that a device should be enabled.
	 * 
	 * @param device
	 *            The device to enable.
	 */
	public void reactToEnableDeviceRequest(AbstractDevice<? extends AbstractDeviceObserver> device);
	/**
	 * Announces that the entire station should be disabled.
	 */
	public void reactToDisableStationRequest();
	/**
	 * Announces that the entire station should be enabled.
	 */
	public void reactToEnableStationRequest();
}