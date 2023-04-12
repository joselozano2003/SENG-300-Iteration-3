package com.autovend.runnable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import com.autovend.Barcode;
import com.autovend.BarcodedUnit;
import com.autovend.Numeral;
import com.autovend.PriceLookUpCode;
import com.autovend.PriceLookUpCodedUnit;
import com.autovend.ReusableBag;
import com.autovend.devices.OverloadException;
import com.autovend.devices.ReusableBagDispenser;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.external.ProductDatabases;
import com.autovend.products.BarcodedProduct;
import com.autovend.products.PLUCodedProduct;
import com.autovend.software.customer.CustomerController;
import com.autovend.software.customer.CustomerStationLogic;
import com.autovend.software.ui.CustomerView;

public class Main {

	public static void main(String[] args) throws InterruptedException, OverloadException {

		int[] billDenoms = { 5, 10, 15, 20, 50, 100 };
		BigDecimal[] coinDenoms = { new BigDecimal("0.05"), new BigDecimal("0.10"), new BigDecimal("0.25"),
				new BigDecimal("1"), new BigDecimal("2") };
		int scaleMaximumWeight = 1000;
		int scaleSensitivity = 1;
		SelfCheckoutStation station = new SelfCheckoutStation(Currency.getInstance("CAD"), billDenoms, coinDenoms,
				scaleMaximumWeight, scaleSensitivity);
		ReusableBagDispenser dispenser = new ReusableBagDispenser(100);
		
		int n = 0;
		while (n < 100) {
			dispenser.load(new ReusableBag());
			n++;
		}
		
		CustomerView customerView = new CustomerView();

		//CustomerController customerController = new CustomerController(station, dispenser, customerView);

		// Create 1 product
		Numeral[] code1 = { Numeral.one, Numeral.two, Numeral.three, Numeral.four, Numeral.five, Numeral.six };
		Barcode barcode = new Barcode(code1);
		BarcodedProduct barcodeProduct = new BarcodedProduct(barcode, "product1", new BigDecimal("1.00"), 10);
		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcode, barcodeProduct);
		ProductDatabases.INVENTORY.put(barcodeProduct, 25);

		// Create another product
		Numeral[] code2 = { Numeral.seven, Numeral.eight, Numeral.nine, Numeral.zero, Numeral.one, Numeral.two };
		Barcode barcode2 = new Barcode(code2);
		BarcodedProduct barcodeProduct2 = new BarcodedProduct(barcode2, "product2", new BigDecimal("2.50"), 15);
		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcode2, barcodeProduct2);
		ProductDatabases.INVENTORY.put(barcodeProduct2, 40);

		Numeral[] code3 = { Numeral.one, Numeral.one, Numeral.one, Numeral.one };
		PriceLookUpCode pluCode = new PriceLookUpCode(code3);

		PLUCodedProduct pluProduct = new PLUCodedProduct(pluCode, "product3", new BigDecimal("1.00"));
		ProductDatabases.PLU_PRODUCT_DATABASE.put(pluCode, pluProduct);

		Numeral[] code4 = { Numeral.zero, Numeral.zero, Numeral.zero, Numeral.zero };
		PriceLookUpCode pluCode2 = new PriceLookUpCode(code4);

		PLUCodedProduct pluProduct2 = new PLUCodedProduct(pluCode2, "grapes", new BigDecimal("2.00"));
		ProductDatabases.PLU_PRODUCT_DATABASE.put(pluCode2, pluProduct2);
		/*
		 * 
		 
		Thread.sleep(6000);
		station.mainScanner.scan(new BarcodedUnit(barcodeProduct.getBarcode(), barcodeProduct.getExpectedWeight()));
		station.baggingArea.add(new BarcodedUnit(barcodeProduct.getBarcode(), barcodeProduct.getExpectedWeight()));

		station.mainScanner.scan(new BarcodedUnit(barcodeProduct2.getBarcode(), barcodeProduct2.getExpectedWeight()));
		station.baggingArea.add(new BarcodedUnit(barcodeProduct2.getBarcode(), barcodeProduct2.getExpectedWeight()));

		station.mainScanner.scan(new BarcodedUnit(barcodeProduct2.getBarcode(), barcodeProduct2.getExpectedWeight()));
		station.baggingArea.add(new BarcodedUnit(barcodeProduct2.getBarcode(), barcodeProduct.getExpectedWeight()));

		Thread.sleep(10000);
		station.scale.add(new PriceLookUpCodedUnit(pluCode2, 999));
		
		*/

	}

}
