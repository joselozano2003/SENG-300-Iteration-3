package com.autovend.runnable;

import java.math.BigDecimal;
import java.util.Currency;

import javax.swing.JButton;

import com.autovend.Barcode;
import com.autovend.BarcodedUnit;
import com.autovend.Bill;
import com.autovend.Coin;
import com.autovend.Numeral;
import com.autovend.PriceLookUpCode;
import com.autovend.PriceLookUpCodedUnit;
import com.autovend.ReusableBag;
import com.autovend.devices.BillDispenser;
import com.autovend.devices.CoinDispenser;
import com.autovend.devices.OverloadException;
import com.autovend.devices.ReusableBagDispenser;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.SimulationException;
import com.autovend.external.ProductDatabases;
import com.autovend.products.BarcodedProduct;
import com.autovend.products.PLUCodedProduct;
import com.autovend.software.customer.CustomerController;
import com.autovend.software.item.ProductsDatabase2;
import com.autovend.software.ui.CustomerView;

public class WeightDiscrepancyDemo {

	public static void main(String[] args) throws InterruptedException, OverloadException {

		// Create 1 product
		Numeral[] code1 = { Numeral.one, Numeral.two, Numeral.three, Numeral.four, Numeral.five, Numeral.six };
		Barcode barcode = new Barcode(code1);
		BarcodedProduct barcodeProduct = new BarcodedProduct(barcode, "batteries", new BigDecimal("1.00"), 10);
		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcode, barcodeProduct);
		ProductDatabases.INVENTORY.put(barcodeProduct, 25);

		// Create another product
		Numeral[] code2 = { Numeral.seven, Numeral.eight, Numeral.nine, Numeral.zero, Numeral.one, Numeral.two };
		Barcode barcode2 = new Barcode(code2);
		BarcodedProduct barcodeProduct2 = new BarcodedProduct(barcode2, "gum", new BigDecimal("2.50"), 15);
		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcode2, barcodeProduct2);
		ProductDatabases.INVENTORY.put(barcodeProduct2, 40);

		Numeral[] code3 = { Numeral.one, Numeral.one, Numeral.one, Numeral.one };
		PriceLookUpCode pluCode = new PriceLookUpCode(code3);
		PLUCodedProduct pluProduct = new PLUCodedProduct(pluCode, "apples", new BigDecimal("1.00"));
		ProductDatabases.PLU_PRODUCT_DATABASE.put(pluCode, pluProduct);

		Numeral[] code4 = { Numeral.zero, Numeral.zero, Numeral.zero, Numeral.zero };
		PriceLookUpCode pluCode2 = new PriceLookUpCode(code4);
		PLUCodedProduct pluProduct2 = new PLUCodedProduct(pluCode2, "grapes", new BigDecimal("1.50"));
		ProductDatabases.PLU_PRODUCT_DATABASE.put(pluCode2, pluProduct2);

		Numeral[] code5 = { Numeral.two, Numeral.two, Numeral.two, Numeral.two };
		PriceLookUpCode pluCode3 = new PriceLookUpCode(code5);
		PLUCodedProduct pluProduct3 = new PLUCodedProduct(pluCode3, "bananas", new BigDecimal("1.00"));
		ProductDatabases.PLU_PRODUCT_DATABASE.put(pluCode3, pluProduct3);

		Numeral[] code6 = { Numeral.three, Numeral.three, Numeral.three, Numeral.three };
		PriceLookUpCode pluCode4 = new PriceLookUpCode(code6);
		PLUCodedProduct pluProduct4 = new PLUCodedProduct(pluCode4, "kiwis", new BigDecimal("3.00"));
		ProductDatabases.PLU_PRODUCT_DATABASE.put(pluCode4, pluProduct4);

		Numeral[] code7 = { Numeral.four, Numeral.four, Numeral.four, Numeral.four };
		PriceLookUpCode pluCode5 = new PriceLookUpCode(code7);
		PLUCodedProduct pluProduct5 = new PLUCodedProduct(pluCode5, "pears", new BigDecimal("1.00"));
		ProductDatabases.PLU_PRODUCT_DATABASE.put(pluCode5, pluProduct5);

		Numeral[] code8 = { Numeral.five, Numeral.five, Numeral.five, Numeral.five };
		PriceLookUpCode pluCode6 = new PriceLookUpCode(code8);
		PLUCodedProduct pluProduct6 = new PLUCodedProduct(pluCode6, "oranges", new BigDecimal("1.25"));
		ProductDatabases.PLU_PRODUCT_DATABASE.put(pluCode6, pluProduct6);

		ProductsDatabase2.Products_In_Visual_Catalogue_Database.put(new JButton(), pluProduct);
		ProductsDatabase2.Products_In_Visual_Catalogue_Database.put(new JButton(), pluProduct2);
		ProductsDatabase2.Products_In_Visual_Catalogue_Database.put(new JButton(), pluProduct3);
		ProductsDatabase2.Products_In_Visual_Catalogue_Database.put(new JButton(), pluProduct4);
		ProductsDatabase2.Products_In_Visual_Catalogue_Database.put(new JButton(), pluProduct5);
		ProductsDatabase2.Products_In_Visual_Catalogue_Database.put(new JButton(), pluProduct6);

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

		for (int i = 0; i < billDenoms.length; i++) {
			BillDispenser billDispenser = station.billDispensers.get(billDenoms[i]);
			for (int j = 0; j < 100; j++) {
				Bill bill = new Bill(billDenoms[i], Currency.getInstance("CAD"));
				try {
					billDispenser.load(bill);
				} catch (SimulationException | OverloadException e) {
				}
			}
		}
		// Add 100 coins to each dispenser

		for (int i = 0; i < coinDenoms.length; i++) {
			CoinDispenser coinDispenser = station.coinDispensers.get(coinDenoms[i]);
			for (int j = 0; j < 100; j++) {
				Coin coin = new Coin(coinDenoms[i], Currency.getInstance("CAD"));
				try {
					coinDispenser.load(coin);
				} catch (SimulationException | OverloadException e) {

				}
			}
		}

		station.printer.addInk(1000);
		station.printer.addPaper(1000);

		CustomerView customerView = new CustomerView();

		CustomerController customerController = new CustomerController(station, dispenser, customerView);
		Thread.sleep(3000);

		customerView.startView.notifyAddMembershipButtonPressed();

		Thread.sleep(3000);
		customerView.membershipView.getInputField().setText("101010101");
		Thread.sleep(3000);
		customerView.membershipView.notifyMembershipNumberEntered("101010101");
		customerView.membershipView.notifyGoBackToCheckout();

		Thread.sleep(4000);
		customerView.startView.notifyStartButtonPressed();
		Thread.sleep(4000);

		boolean scan1 = false;
		while (!scan1) {
			scan1 = station.mainScanner
					.scan(new BarcodedUnit(barcodeProduct.getBarcode(), barcodeProduct.getExpectedWeight()));
		}
		station.baggingArea.add(new BarcodedUnit(barcodeProduct.getBarcode(), barcodeProduct.getExpectedWeight()));

		Thread.sleep(2000);

		scan1 = false;
		while (scan1 == false) {
			scan1 = station.mainScanner
					.scan(new BarcodedUnit(barcodeProduct2.getBarcode(), barcodeProduct2.getExpectedWeight()));
		}

		station.baggingArea.add(new BarcodedUnit(barcodeProduct2.getBarcode(), barcodeProduct2.getExpectedWeight()));

		Thread.sleep(2000);

		scan1 = false;
		while (scan1 == false) {
			scan1 = station.mainScanner
					.scan(new BarcodedUnit(barcodeProduct2.getBarcode(), barcodeProduct2.getExpectedWeight()));
		}

		station.baggingArea.add(new BarcodedUnit(barcodeProduct2.getBarcode(), barcodeProduct2.getExpectedWeight()));

		Thread.sleep(4000);
		customerView.checkoutView.notifyAddItemByPLUButtonPressed();
		Thread.sleep(2000);
		customerView.pluView.getInputField().setText("0000");
		Thread.sleep(2000);
		customerView.pluView.getAddButton().doClick();
		Thread.sleep(2000);
		customerView.pluView.getDoneButton().doClick();
		Thread.sleep(2000);

		PriceLookUpCodedUnit pluUnit1 = new PriceLookUpCodedUnit(pluCode2, 5);
		station.scale.add(pluUnit1);
		Thread.sleep(2000);
		station.scale.remove(pluUnit1);

		// Commenting out the line below will fix the weight discrepancy
		// station.baggingArea.add(pluUnit1);

		customerView.checkoutView.notifyAddItemByBrowsingButtonPressed();

		Thread.sleep(3000);
		customerView.browsingView.notifyProductSelected(pluProduct4);
		Thread.sleep(3000);

		customerView.browsingView.notifyGoBackToCheckout();

		PriceLookUpCodedUnit pluUnit2 = new PriceLookUpCodedUnit(pluCode4, 10);
		station.scale.add(pluUnit2);
		//
		Thread.sleep(2000);
		station.scale.remove(pluUnit2);
		Thread.sleep(3000);

		station.baggingArea.add(pluUnit2);

		customerView.checkoutView.notifyPurchaseBagsButtonPressed();
		Thread.sleep(1000);
		customerView.checkoutView.notifyPurchaseBagsButtonPressed();
		Thread.sleep(1000);
		customerView.checkoutView.notifyPurchaseBagsButtonPressed();

	}

}
