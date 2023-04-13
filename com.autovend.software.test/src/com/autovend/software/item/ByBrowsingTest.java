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
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.autovend.devices.SelfCheckoutStation;
import com.autovend.products.BarcodedProduct;
import com.autovend.products.PLUCodedProduct;
import com.autovend.products.Product;
import com.autovend.software.test.Setup;
import com.autovend.software.ui.CustomerView;

/**
 * A test class that performs tests on the ByBrowsing class.
 * @author Akansha
 *
 */
public class ByBrowsingTest {
	private SelfCheckoutStation station;
	private ByBrowsing byBrowsing;
	private ItemFacade instance;
	private int found;
	
	/**
	 * Sets up the test suite. Runs before every method.
	 */
	@Before
	public void setup() {
		
		//Setup the class to test
		station = Setup.createSelfCheckoutStation();
		byBrowsing = new ByBrowsing(station, new CustomerView());
		instance = new ItemFacade(station, new CustomerView(), false);
	}
	
	/**
	 * Cleans up the test suite. Runs after every test method.
	 */
	@After
	public void teardown() {
		station = null;
		byBrowsing = null;
		instance = null;
		found = 0;
	}
	
	/**
	 * Test ByBrowsing constructor with null station parameter.
	 */
	@Test (expected = NullPointerException.class)
	public void testContructorNullStation() {
		new ByBrowsing(null, new CustomerView());
	}
	
	/**
	 * Test ByBrowsing constructor with null customer view parameter.
	 */
	@Test (expected = NullPointerException.class)
	public void testContructorNullView() {
		new ByBrowsing(station, null);
	}
	
	/**
	 * Tests the event that a single product enforced a weight change
	 */
	@Test
	public void testBrowingProductWeightChange() {
		int expected = 1;
		PLUCodedProduct PLUProd123 = Setup.createPLUProduct1234(9.99, 4, true);
		
		byBrowsing.register(new ItemListenerStub() {
			@Override
			public void onItemAddedEvent(Product product, double quantity) {
				found++;
				if (found == 1) {
					assertEquals(PLUProd123, product);
				}
			}});
		
		byBrowsing.reactToProductSelected(PLUProd123);
		byBrowsing.reactToWeightChangedEvent(station.scale, 8);
		assertEquals(expected, found);
	}
	
	/**
	 * Tests the event that a null product did not enforce a weight change
	 */
	@Test
	public void testBrowsingNullProductNoWeightChange() {
		int expected = 0;
		Product nullProd = null;
		
		byBrowsing.register(new ItemListenerStub() {
			@Override
			public void onItemAddedEvent(Product product, double quantity) {
				found++;
			}});
		
		byBrowsing.reactToProductSelected(nullProd);
		byBrowsing.reactToWeightChangedEvent(station.scale, 0);
		assertEquals(expected, found);
	}
	
	/**
	 * Tests the event that a single product did not enforce a weight change
	 */
	@Test
	public void testBrowsingProductNoWeightChange() {
		int expected = 0;
		PLUCodedProduct PLUProd123 = Setup.createPLUProduct1234(9.99, 4, true);
		
		byBrowsing.register(new ItemListenerStub() {
			@Override
			public void onItemAddedEvent(Product product, double quantity) {
				found++;
			}});
		
		byBrowsing.reactToProductSelected(PLUProd123);
		byBrowsing.reactToWeightChangedEvent(station.scale, 0);
		assertEquals(expected, found);
	}
	
	/**
	 * Tests the event that a null product tried to enforce a weight change
	 * but it didn't actually occur
	 */
	@Test
	public void testBrowsingNullProductWeightChange() {
		int expected = 0;
		Product nullProd = null;
		
		byBrowsing.register(new ItemListenerStub() {
			@Override
			public void onItemAddedEvent(Product product, double quantity) {
				found++;
			}});
		
		byBrowsing.reactToProductSelected(nullProd);
		byBrowsing.reactToWeightChangedEvent(station.scale, 5);
		assertEquals(expected, found);
	}
	
	/**
	 * Assert that these hardware events don't announce to listeners.
	 */
	@Test
	public void testMethodsNoEvents() {
		int expected = 4;
		//Will fail if any listener event is entered.
		byBrowsing.register(new ItemListenerStub() {
			@Override
			public void reactToDisableStationRequest() {
				found++;
			}
			@Override
			public void reactToEnableStationRequest() {
				found++;
			}
			@Override
			public void onItemAddedEvent(Product product, double quantity) {
				found++;
			}
			@Override
			public void onItemNotFoundEvent() {
				found++;
			}
			@Override
			public void reactToInvalidBarcode(BarcodedProduct barcodedProduct, int i) {
				found++;
			}	
		});
		byBrowsing.reactToEnabledEvent(station.scale);
		byBrowsing.reactToDisabledEvent(station.scale);
		byBrowsing.reactToOverloadEvent(station.scale);
		byBrowsing.reactToOutOfOverloadEvent(station.scale);
		assertNotEquals(expected, found);		
	}
}