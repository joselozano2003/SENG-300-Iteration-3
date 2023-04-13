package com.autovend.devices;

import com.autovend.Coin;

/**
 * A simple interface for devices that emit things.
 * 
 * @param <T>
 *            The type of the things to emit.
 */
interface FlowThroughEmitter<T> {
	/**
	 * Instructs the device to emit a specific thing, meaning that the device is
	 * being handed this thing to pass onwards.
	 * 
	 * @param thing
	 *            The thing to emit.
	 * @return true if the emission was successful; otherwise false.
	 * @throws DisabledException
	 *             If the device is disabled.
	 * @throws OverloadException
	 *             If the receiving device is already full.
	 */
	public boolean emit(T thing) throws DisabledException, OverloadException;
}
