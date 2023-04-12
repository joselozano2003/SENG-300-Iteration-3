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

import java.util.ArrayList;

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
 * A test class that performs tests on the ByTextSearch class.
 * @author Akansha
 *
 */
public class ByTextSearchTest {
	private SelfCheckoutStation station;
	private ByTextSearch byTextSearch;
	private ItemFacade instance;
	private int found;
	
	/**
	 * Sets up the test suite. Runs before every method.
	 */
	@Before
	public void setup() {
		
		//Setup the class to test
		station = Setup.createSelfCheckoutStation();
		byTextSearch = new ByTextSearch(station, new CustomerView());
		instance = new ItemFacade(station, new CustomerView(), false);
	}
	
	/**
	 * Cleans up the test suite. Runs after every test method.
	 */
	@After
	public void teardown() {
		station = null;
		byTextSearch = null;
		instance = null;
		found = 0;
	}
	
	/**
	 * Test ByTextSearch constructor with null station parameter.
	 */
	@Test (expected = NullPointerException.class)
	public void testContructorNullStation() {
		new ByTextSearch(null, new CustomerView());
	}
	
	/**
	 * Test ByTextSearch constructor with null customer view parameter.
	 */
	@Test (expected = NullPointerException.class)
	public void testContructorNullView() {
		new ByTextSearch(station, null);
	}
	
	/**
	 * Tests that relevant search results are found for a single Barcoded Product
	 */
	@Test
	public void testTextSearchForSingleBarcodedProduct() {
		int expectedSize = 1;
		BarcodedProduct barcodedProd123 = Setup.createBarcodedProduct123(33.33, 5, false);
		
		ProductsDatabase2.Products_Textsearch_Keywords_Database.put("Product one", barcodedProd123);
		
		ArrayList<Product> results = byTextSearch.getProductsCorrespondingToTextSearch("one");
		
		int actualSize = results.size();
		assertEquals(expectedSize, actualSize);
	}
	
	/**
	 * Tests that relevant search results are found for a single PLUCoded Product
	 */
	@Test
	public void testTextSearchForSinglePLUCodedProduct() {
		int expectedSize = 1;
		PLUCodedProduct PLUProd123 = Setup.createPLUProduct1234(12.97, 2, false);
		
		ProductsDatabase2.Products_Textsearch_Keywords_Database.put("Imported: Aged Mozzerella Cheese", PLUProd123);
		
		ArrayList<Product> results = byTextSearch.getProductsCorrespondingToTextSearch("Mozzerella");
		
		int actualSize = results.size();
		assertEquals(expectedSize, actualSize);
	}
	
	/**
	 * Tests that relevant search results are found multiple products
	 */
	@Test
	public void testTextSearchForMultipleProducts() {
		int expectedSize1 = 1;
		PLUCodedProduct PLUProd123 = Setup.createPLUProduct1234(12.97, 2, false);
		ProductsDatabase2.Products_Textsearch_Keywords_Database.put("Imported: Aged Mozzerella Cheese", PLUProd123);
		ArrayList<Product> results1 = byTextSearch.getProductsCorrespondingToTextSearch("Mozzerella");
		int actualSize1 = results1.size();
		
		int expectedSize2 = 1;
		BarcodedProduct barcodedProd123 = Setup.createBarcodedProduct123(27.99, 8, false);
		ProductsDatabase2.Products_Textsearch_Keywords_Database.put("Tide Sport Laundry Detergent", barcodedProd123);	
		ArrayList<Product> results2 = byTextSearch.getProductsCorrespondingToTextSearch("Sport");
		int actualSize2 = results2.size();
		
		assertEquals(expectedSize1, actualSize1);
		assertEquals(expectedSize2, actualSize2);
	}
	
	/**
	 * Tests that a relevant search result was found for a product
	 * and the event announcement of product being selected from the search results
	 */
	@Test
	public void testEventTextSearchProductSelected() {
		int expectedEvent = 1;
		
		int expectedSize = 1;
		BarcodedProduct barcodedProd123 = Setup.createBarcodedProduct123(27.99, 8, false);
		ProductsDatabase2.Products_Textsearch_Keywords_Database.put("Tide Sport Laundry Detergent", barcodedProd123);	
		ArrayList<Product> results = byTextSearch.getProductsCorrespondingToTextSearch("Sport");
		int actualSize = results.size();
		
		instance.register(new ItemListenerStub() {
			@Override
			public void onItemAddedEvent(Product product, double quantity) {
				found++;
				if (found == 1) {
					assertEquals(barcodedProd123, product);
				}
			}});
		
		byTextSearch.productFromTextSearchSelected(barcodedProd123);
		
		assertEquals(expectedSize, actualSize);
		assertEquals(expectedEvent, found);	
	}
	
	/**
	 * Tests that a relevant search result was found for a product
	 * and no event announcement of a product being selected from the search results
	 */
	@Test
	public void testEventTextSearchNoProductSelected() {
		int expectedEvent = 0;
		
		int expectedSize = 1;
		BarcodedProduct barcodedProd123 = Setup.createBarcodedProduct123(7.99, 8, false);
		ProductsDatabase2.Products_Textsearch_Keywords_Database.put("Driscol's organic raspberries", barcodedProd123);	
		ArrayList<Product> results = byTextSearch.getProductsCorrespondingToTextSearch("rasp");
		int actualSize = results.size();
		
		
		// Calling method without a registered listener
		byTextSearch.productFromTextSearchSelected(barcodedProd123);
		
		assertEquals(expectedSize, actualSize);
		assertEquals(expectedEvent, found);	
	}	
}
