package com.autovend.devices;

import com.autovend.Coin;

/**
 * A simple interface for devices that accept things.
 * 
 * @param <T> The type of the things to accept.
 */
interface Acceptor<T> {
	/**
	 * Instructs the device to take the thing as input.
	 * 
	 * @param thing
	 *            The thing to be taken as input.
	 * @return true if the acceptance was successful; otherwise false.
	 * @throws OverloadException
	 *             If the device does not have enough space for the thing.
	 * @throws DisabledException
	 *             If the device is disabled.
	 */
	public boolean accept(T thing) throws OverloadException, DisabledException;

	/**
	 * Checks whether the device has enough space to expect one more thing. If this
	 * method returns true, an immediate call to accept should not throw
	 * CapacityExceededException, unless an asynchronous addition has occurred in
	 * the meantime.
	 * 
	 * @return true If there is space; otherwise, false.
	 */
	boolean hasSpace();
}
