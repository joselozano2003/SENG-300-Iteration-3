
package com.autovend.software;

import com.autovend.devices.SelfCheckoutStation;
import com.autovend.products.BarcodedProduct;
import com.autovend.software.observers.AddItemObserver;
import com.autovend.software.PurchasedItems;

public abstract class AddItem {
	
	SelfCheckoutStation check;
	
	public AddItem(SelfCheckoutStation scs) {
		this.check = scs;
	}

	protected void addBag(Bag bag) {
		PurchasedItems.addBag(bag);
	}

	protected void addBarcodedProduct(BarcodedProduct unit) {
		PurchasedItems.addProduct(unit);
	}
}
