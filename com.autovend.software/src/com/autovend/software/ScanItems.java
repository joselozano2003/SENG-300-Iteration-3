/* P3-4 Group Members
 * 
 * Abdelrhafour, Achraf (30022366)
 * Campos, Oscar (30057153)
 * Cavilla, Caleb (30145972)
 * Crowell, Madeline (30069333)
 * Debebe, Abigia (30134608)
 * Dhuka, Sara Hazrat (30124117)
 * Drissi, Khalen (30133707)
 * Ferreira, Marianna (30147733)
 * Frey, Ben (30088566)
 * Himel, Tanvir (30148868)
 * Huayhualla Arce, Fabricio (30091238)
 * Kacmar, Michael (30113919)
 * Lee, Jeongah (30137463)
 * Li, Ran (10120152)
 * Lokanc, Sam (30114370)
 * Lozano Cetina, Jose Camilo (30144736)
 * Maahdie, Monmoy (30149094)
 * Malik, Akansha (30056048)
 * Mehedi, Abdullah (30154770)
 * Polton, Scott (30138102)
 * Rahman, Saadman (30153482)
 * Rodriguez, Gabriel (30162544)
 * Samin Rashid, Khondaker (30143490)
 * Sloan, Jaxon (30123845)
 * Tran, Kevin (30146900)
 */
package com.autovend.software;

import com.autovend.Barcode;
import com.autovend.BarcodedUnit;
import com.autovend.devices.AbstractDevice;
import com.autovend.devices.BarcodeScanner;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.BarcodeScannerObserver;
import com.autovend.external.ProductDatabases;
import com.autovend.products.BarcodedProduct;

public class ScanItems extends AddItem implements BarcodeScannerObserver{

    private SelfCheckoutStation selfCheckoutStation;
    private PurchasedItems purchaseList;

    /*
     * Constructor for ScanItems
     */
    public ScanItems(SelfCheckoutStation station) {
        super(station);
        PurchasedItems purchasedItems = new PurchasedItems();
        this.selfCheckoutStation = station;
        this.purchaseList = purchasedItems;
    }

    public PurchasedItems getPurchasedItems() {
        return this.purchaseList;
    }

    /*
     *  Performs actions after item has been scanned
     *
     */
    @Override
    public void reactToBarcodeScannedEvent(BarcodeScanner barcodeScanner, Barcode barcode) {

        // 2. Blocks the self-checkout station from further customer interaction
        barcodeScanner.disable();
        selfCheckoutStation.handheldScanner.disable();
        selfCheckoutStation.mainScanner.disable();

        // 3. System: Determines the characteristics (weight and cost) of the product associated with the given barcode.
        // 4. System: Updates the expected weight from the Bagging Area.
        BarcodedProduct itemProduct = ProductDatabases.BARCODED_PRODUCT_DATABASE.get(barcode);
        double itemWeight = itemProduct.getExpectedWeight();
        addBarcodedProduct(itemProduct);

        // 5. System: Signals to the Customer I/O to place the scanned item in the Bagging Area.
        BarcodedUnit scannedUnit = new BarcodedUnit(barcode, itemWeight);
        CustomerIOStub.itemHasBeenScannedEvent(this.selfCheckoutStation, scannedUnit);
    }

    /*
     *  Weight changed in bagging area. Compares the total weight of the products that have been scanned
     *  with the weight of the bagging area to confirm whether it is accurate
     *
     *  If they match, user can continue scanning, if not, scanner is disabled and other
     *  actions are needed.
     */

    // 6. Signals to the system that the weight has changed.

    @Override
    public void reactToEnabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {}
    @Override
    public void reactToDisabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {}

}
