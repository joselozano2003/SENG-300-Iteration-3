package com.autovend.software.ui;

import auth.AttendantAccount;

/**
 * The UIEventListener interface defines the contract for objects that need to be
 * notified when user interface events occur. Classes implementing this interface
 * should provide an implementation for each of the defined methods.
 */

public interface UIEventListener {

	
	public void onStartAddingItems();
	
	public void onStartPaying();
	
	public void onSelectAddItemByPLU();
	
	public void onSelectAddItemByBrowsing();
	
	public void onStartAddingOwnBags();
	
	public void onFinishAddingOwnBags();
	
	public void onPurchaseBags(int amount);

	public void goBackToCheckout();

	public void onSelectPaymentMethod(String payment);

	public void onOverride(int stationNumber);
	
	public void onBagApproval(int stationNumber);

	public void onBagRefusal(int stationNumber);
	
	public void onAttendantLoginAttempt(AttendantAccount account);

	

}
