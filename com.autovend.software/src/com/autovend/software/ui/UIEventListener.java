package com.autovend.software.ui;

import auth.AttendantAccount;

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
