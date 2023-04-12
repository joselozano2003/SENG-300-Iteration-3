package com.autovend.software.ui;

import com.autovend.products.Product;

/**
 * BrowsingViewObserver is an interface for classes that need to be notified when a Product
 * is selected in a browsing view. Classes implementing this interface should provide
 * an implementation for the reactToProductSelected() method.
 */


public interface BrowsingViewObserver {
	
    public void reactToProductSelected(Product product);

}
