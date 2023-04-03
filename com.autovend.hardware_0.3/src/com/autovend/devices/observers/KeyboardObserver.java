package com.autovend.devices.observers;

import com.autovend.devices.Keyboard;

/**
 * Observes events emanating from a keyboard.
 */
public interface KeyboardObserver extends AbstractDeviceObserver {
	/**
	 * Announces that the indicated key has been pressed on the indicated keyboard.
	 * 
	 * @param k
	 *            The keyboard where the event occurred.
	 * @param c
	 *            The key that was pressed on that keyboard.
	 */
	public void reactToKeyPressedEvent(Keyboard k, char c);
}
