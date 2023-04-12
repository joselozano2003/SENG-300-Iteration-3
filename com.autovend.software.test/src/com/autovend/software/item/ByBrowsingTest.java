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
	 * Tests the event that a single PLUCoded Product was found and selected
	 */
	@Test
	public void testBrowsingASinglePLUProductEvent() {
		int expected = 1;
		PLUCodedProduct PLUProd123 = Setup.createPLUProduct1234(9.99, 4, true);
		
		instance.register(new ItemListenerStub() {
			@Override
			public void onItemAddedEvent(Product product, double quantity) {
				found++;
				if (found == 1) {
					assertEquals(PLUProd123, product);
				}
			}});
		
		byBrowsing.productFromVisualCatalogueSelected(PLUProd123);
		assertEquals(expected, found);
	}
	
	/**
	 * Tests the event that a single Barcoded Product was found and selected
	 */
	@Test
	public void testBrowsingASingleBarcodedProductEvent() {
		int expected = 1;
		BarcodedProduct BarcodedProd123 = Setup.createBarcodedProduct123(12.99, 3, true);
		
		instance.register(new ItemListenerStub() {
			@Override
			public void onItemAddedEvent(Product product, double quantity) {
				found++;
				if (found == 1) {
					assertEquals(BarcodedProd123, product);
				}
			}});
		
		byBrowsing.productFromVisualCatalogueSelected(BarcodedProd123);
		assertEquals(expected, found);
	}
	
	/**
	 * Tests the event that two products, Barcoded and PLUCoded, were found and selected
	 */
	@Test
	public void testBrowsingMultipleProductsEvent() {
		int expected = 2;
		BarcodedProduct BarcodedProd123 = Setup.createBarcodedProduct123(12.99, 3, true);
		PLUCodedProduct PLUProd56789 = Setup.createPLUProduct56789(7.99, 4, true);
		
		instance.register(new ItemListenerStub() {
			@Override
			public void onItemAddedEvent(Product product, double quantity) {
				found++;
				if (found == 1) {
					assertEquals(BarcodedProd123, product);
				}
				
				if (found == 2) {
					assertEquals(PLUProd56789, product);
				}
			}});
		
		byBrowsing.productFromVisualCatalogueSelected(BarcodedProd123);
		byBrowsing.productFromVisualCatalogueSelected(PLUProd56789);
		assertEquals(expected, found);
	}
	
	/**
	 * Tests the event that a null product was attempted to be selected and found
	 */
	@Test
	public void testBrowsingNullProductEvent() {
		int expected = 0;
		Product Prod = null;
		
		instance.register(new ItemListenerStub() {
			@Override
			// The inside of this code shouldn't get reached. As a null product should not
			// be browsed or announced, as desired
			public void onItemAddedEvent(Product product, double quantity) {
				found++;
			}});
		
		byBrowsing.productFromVisualCatalogueSelected(Prod);
		assertEquals(expected, found);
	}
}