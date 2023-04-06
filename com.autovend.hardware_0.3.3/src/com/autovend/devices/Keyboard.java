package com.autovend.devices;

import com.autovend.devices.observers.KeyboardObserver;

/**
 * Represents a device with a set of keys that permit manual entry of text. This
 * simulation does not model the individual keys, as that would be excessively
 * complicated to implement and to use.
 */
public class Keyboard extends AbstractDevice<KeyboardObserver> {
	private static final long serialVersionUID = 5818039618559081511L;

	/**
	 * A simple simulation of someone typing on the keyboard. This method is for
	 * convenience, so that {@link #pressKey(char)} need not be called multiple
	 * times.
	 * 
	 * @param stringToType
	 *            A string whose characters represent keystrokes.
	 * @throws SimulationException
	 *             If the stringToType is null.
	 */
	public void type(String stringToType) {
		if(stringToType == null)
			throw new SimulationException(new NullPointerException("stringToType"));

		for(char c : stringToType.toCharArray())
			pressKey(c);
	}

	/**
	 * A simple simulation of someone typing on the keyboard. This method is for
	 * convenience, so that {@link #pressKey(char)} need not be called multiple
	 * times.
	 * 
	 * @param characters
	 *            An array of characters representing keystrokes.
	 * @throws SimulationException
	 *             If the characters argument is null.
	 */
	public void type(char[] characters) {
		if(characters == null)
			throw new SimulationException(new NullPointerException("characters"));

		for(char c : characters)
			pressKey(c);
	}

	/**
	 * A simple simulation of someone pressing a key on the keyboard. Announces
	 * "keyPressed" event.
	 * 
	 * @param character
	 *            A character representing a keystroke.
	 */
	public void pressKey(char character) {
		for(KeyboardObserver observer : observers)
			observer.reactToKeyPressedEvent(this, character);
	}
}
