package com.autovend.software.ui;

/**
 * The PLUViewObserver interface defines the contract for objects that need to be
 * notified when a PLU code is entered in the PLUView. Classes implementing this
 * interface should provide an implementation for the reactToPLUCodeEntered method.
 */

public interface PLUViewObserver {
	
    public void reactToPLUCodeEntered(String pluCode);
}
