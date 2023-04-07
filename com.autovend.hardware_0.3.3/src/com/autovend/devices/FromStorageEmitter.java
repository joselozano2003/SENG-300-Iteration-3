package com.autovend.devices;

import com.autovend.Coin;

/**
 * A simple interface for devices that emit things.
 * 
 * @param <T>
 *            The type of the things to emit.
 */
interface FromStorageEmitter<T> {
	/**
	 * Instructs the device to emit one thing, meaning that the device stores a set
	 * of things and one of them is to be emitted.
	 * 
	 * @return true if the emission was successful; otherwise false.
	 * @throws DisabledException
	 *             If the device is disabled.
	 * @throws EmptyException
	 *             If the device is empty and cannot emit.
	 * @throws OverloadException
	 *             If the receiving device is already full.
	 */
	boolean emit() throws DisabledException, EmptyException, OverloadException;
}
