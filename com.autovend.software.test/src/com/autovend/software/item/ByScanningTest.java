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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.autovend.Barcode;
import com.autovend.Numeral;
import com.autovend.devices.AbstractDevice;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.products.BarcodedProduct;
import com.autovend.products.Product;
import com.autovend.software.test.Setup;

/**
 * A test class that performs tests on the ByScanning class. 
 * @author Akansha
 *
 */
public class ByScanningTest {
	private SelfCheckoutStation station;
	private ByScanning byScanning;
	private int found;
	
	/**
	 * Sets up the test suite. Runs before every method.
	 */
	@Before
	public void setup() {
		
		//Setup the class to test
		station = Setup.createSelfCheckoutStation();
		byScanning = new ByScanning(station);
	}
	
	/**
	 * Cleans up the test suite. Runs after every test method.
	 */
	@After
	public void teardown() {
		station = null;
		byScanning = null;
		found = 0;
	}
	
	/**
	 * Test ByScanning constructor with null parameter.
	 */
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
	
	// This test fails currently
	@Test
	public void testEventItemNotFound() {
		int expected = 1;
		BarcodedProduct barcodedProduct0 = Setup.createBarcodedProduct123(27.99, 10, false);
		
		byScanning.register(new ItemListenerStub() {
			@Override
			public void onItemNotFoundEvent() {
				found++;
			}
		});
		byScanning.reactToBarcodeScannedEvent(station.mainScanner, barcodedProduct0.getBarcode());
		assertEquals(expected, found);
	}
	
	/**
	 * Tests an attempt to add a null item.
	 */
	@Test
	public void testEventAttemptingToAddNullItem() {
		int expected = 1;
		BarcodedProduct barcodedProduct1 = null;
		
		byScanning.register(new ItemListenerStub() {
			@Override
			public void reactToInvalidBarcode(BarcodedProduct barcodedProduct, int i) {
				assertEquals(barcodedProduct1, barcodedProduct);
				found++;
			}
		});
		byScanning.reactToBarcodeScannedEvent(station.mainScanner, null);
		assertEquals(expected, found);
	}
	
	// This test fails, code inside override doesn't get reached
	// not sure if I did the stub right
	@Test
	public void testEventDisabled() {
		int expected = 1;
		BarcodedProduct barcodedProduct1 = Setup.createBarcodedProduct123(12.91, 5, true);
		
		byScanning.register(new ItemListenerStub() {
			@Override
			public void reactToDisableDeviceRequest(AbstractDevice<? extends AbstractDeviceObserver> device) {
				found++;
			}
		});
		
		station.mainScanner.disable();
		byScanning.reactToDisabledEvent(station.mainScanner);
		assertEquals(expected, found);
	}
	
	// This test fails, code inside override doesn't get reached
	// not sure if I did the stub right
	@Test
	public void testEventEnabled() {
		int expected = 1;
		BarcodedProduct barcodedProduct2 = Setup.createBarcodedProduct123(12.91, 5, true);
		
		byScanning.register(new ItemListenerStub() {
			@Override
			public void reactToEnableDeviceRequest(AbstractDevice<? extends AbstractDeviceObserver> device) {
				found++;
			}
		});
		
		station.mainScanner.enable();
		byScanning.reactToEnabledEvent(station.mainScanner);
		assertEquals(expected, found);
	}
	
	/**
	 * Tests when a valid item is added and attempting to add a null item, right after. 
	 */
	@Test
	public void testEventAddValidItemAndNullItem() {
		int expected = 2;
		BarcodedProduct barcodedProduct3 = Setup.createBarcodedProduct123(12.91, 5, true);
		BarcodedProduct barcodedProduct4 = null;
		
		byScanning.register(new ItemListenerStub() {
			@Override
			public void onItemAddedEvent(Product product, double quantity) {
				assertEquals(barcodedProduct3, product);
				found++;
			}
			
			@Override 
			public void reactToInvalidBarcode(BarcodedProduct barcodedProduct, int i) {
				assertEquals(barcodedProduct4, barcodedProduct);
				found++;
		}});
		
		byScanning.reactToBarcodeScannedEvent(station.mainScanner, barcodedProduct3.getBarcode());
		byScanning.reactToBarcodeScannedEvent(station.mainScanner, null);
		assertEquals(expected, found);
	}
	
	// Not too sure how to test for hardware failure!
	// This test fails bc of that
	@Test
	public void testEventHardwareFailure() {
		int expected = 1;
		byScanning.register(new ItemListenerStub() {
			@Override
			public void reactToHardwareFailure() {
				found++;
			}
		});
		
		station.mainScanner.disable();
		station.handheldScanner.disable();
		
		byScanning = new ByScanning(station);
		
		assertEquals(expected, found);
	}
}
