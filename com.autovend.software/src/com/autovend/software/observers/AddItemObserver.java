
package com.autovend.software.observers;

import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.software.AddItem;

public interface AddItemObserver extends AbstractDeviceObserver {

    /**
     * Announces that a barcoded product has been added to the cart.
     *
     * @param addItem
     *             The AddItem object that added the product.
     */
    void reactToAddedBarcodedProduct(AddItem addItem);

    /**
     * Announces that a non-barcoded product has been added to the cart.
     *
     * @param addItem
     *             The AddItem object that added the product.
     */

    void reactToAddedNonBarcodedProduct(AddItem addItem);


    /**
     * Announces that a product has been removed from the cart.
     *
     * @param addItem
     *             The AddItem object that removed the product.
     */
    void reactToRemovedProduct(AddItem addItem);

    /**
     * Announces that a product has been removed from the cart.
     *
     * @param addItem
     *             The AddItem object that removed the product.
     */
    void reactToRemovedAllProducts(AddItem addItem);
}
