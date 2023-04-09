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
package com.autovend.software.item;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import com.autovend.Barcode;
import com.autovend.Numeral;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.products.BarcodedProduct;
import com.autovend.products.Product;
import com.autovend.software.test.Setup;

public class ByScanningTest {
	private SelfCheckoutStation station;
	private ByScanning byScanning;
	private int found;
	
	@Before
	public void setup() {
		
		//Setup the class to test
		station = Setup.createSelfCheckoutStation();
		byScanning = new ByScanning(station);
	}
	
	@Test (expected = NullPointerException.class)
	public void testNullContruction() {
		new ByScanning(null);
	}
	
	/**
	 * Pre: Call item facade to add two different products.
	 * Expected: onItemAddedEvent is called with correct product each time.
	 */
	@Test
	public void testEventItemAdded() {
		BarcodedProduct barcodedProduct123 = Setup.createBarcodedProduct123(1.00, 15, true);
		BarcodedProduct barcodedProduct456 = Setup.createBarcodedProduct456(5.5, 20, true);
		
		byScanning.register(new ItemListenerStub() {
			@Override
			public void onItemAddedEvent(Product product, double quantity) {
				found++;
				if (found == 1)
					assertEquals(barcodedProduct123, product);
				if (found == 2)
					assertEquals(barcodedProduct456, product);
			}});
		byScanning.reactToBarcodeScannedEvent(station.mainScanner, barcodedProduct123.getBarcode());
		byScanning.reactToBarcodeScannedEvent(station.mainScanner, barcodedProduct456.getBarcode());
		assertEquals(2, found);
	}

}
