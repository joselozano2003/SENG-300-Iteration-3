package com.autovend.software;

import com.autovend.BarcodedUnit;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.external.ProductDatabases;
import com.autovend.products.BarcodedProduct;

public class CustomerIOStub {
	
	public static void itemHasBeenScannedEvent(SelfCheckoutStation selfCheckoutStation, BarcodedUnit scannedUnit) {
		BarcodedProduct itemProduct = ProductDatabases.BARCODED_PRODUCT_DATABASE.get(scannedUnit.getBarcode());
	}
	
	

}
