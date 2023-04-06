package com.autovend.software.item;

import com.autovend.products.Product;
import com.autovend.software.AbstractFacadeListener;

public interface ItemAdditionListener extends AbstractFacadeListener {
	
	void reactToItemAddedEvent(Product product, double quantity);
	
}
