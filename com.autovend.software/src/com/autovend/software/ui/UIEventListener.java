package com.autovend.software.ui;

public interface UIEventListener {

	
	public void onStartAddingItems();
	
	public void onStartPaying();
	
	public void onSelectAddItemByPLU();
	
	public void onStartAddingOwnBags();
	
	public void onFinishAddingOwnBags();
	
	public void onPurchaseBags(int amount);

	public void goBackToCheckout();

	
	




	

}
