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
	private TextSearchProduct txtSearchProd;
	
	/**
	 * Sets up the test suite. Runs before every method.
	 */
	@Before
	public void setup() {
		
		//Setup the class to test
		station = Setup.createSelfCheckoutStation();
		byTextSearch = new ByTextSearch(station, new CustomerView());
		instance = new ItemFacade(station, new CustomerView(), false);
		txtSearchProd = new TextSearchProduct("Fresh mozzerella cheese", "This is a good product", new BigDecimal
				("15.99"), 3.0);
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
		txtSearchProd = null;
		ProductsDatabase2.Products_Textsearch_Keywords_Database.clear();
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
	 * Tests that relevant search results are found for a single Product
	 */
	@Test
	public void testTextSearchForSingleBarcodedProduct() {
		int expectedSize = 1;
		
		ProductsDatabase2.Products_Textsearch_Keywords_Database.put("mozzerella", txtSearchProd); 
		
		ArrayList<Product> results = byTextSearch.getProductsCorrespondingToTextSearch("mozzerella");
		
		int actualSize = results.size();
		assertEquals(expectedSize, actualSize);
	}
	
	/**
	 * Tests that relevant search results are found multiple products
	 */
	@Test
	public void testTextSearchForMultipleProducts() {
		int expectedSize1 = 1;
		ProductsDatabase2.Products_Textsearch_Keywords_Database.put("mozzerella", txtSearchProd); 
		
		ArrayList<Product> results = byTextSearch.getProductsCorrespondingToTextSearch("mozzerella");
		
		int actualSize1 = results.size();
		
		TextSearchProduct txtSearchProd2 = new TextSearchProduct("Tide Sport Laundry Detergent", "Clean clothes nice", 
				new BigDecimal("27.99"), 8.0);
		
		int expectedSize2 = 1;
	
		ProductsDatabase2.Products_Textsearch_Keywords_Database.put("Sport detergent", txtSearchProd);
		
		ArrayList<Product> results2 = byTextSearch.getProductsCorrespondingToTextSearch("Sport");
		
		int actualSize2 = results2.size();
		
		// for first product
		assertEquals(expectedSize1, actualSize1);
		
		// for second product
		assertEquals(expectedSize2, actualSize2);
	}
	
	/**
	 * Tests that no relevant search results are found for a product
	 */
	@Test
	public void testTextSearchForNoProduct() {
		int expectedSize = 0;
		
		TextSearchProduct txtSearchProd2 = new TextSearchProduct("Driscol's organic raspberries", "good fruit", 
				new BigDecimal("27.99"), 8.0);
	
		ProductsDatabase2.Products_Textsearch_Keywords_Database.put("raspberries", txtSearchProd);
		
		ArrayList<Product> results = byTextSearch.getProductsCorrespondingToTextSearch("blue");
		
		int actualSize = results.size();
		
		assertEquals(expectedSize, actualSize);
	}
	
	/**
	 * Tests that a valid product has been selected from the text search
	 */
	@Test
	public void testValidProductSelected() {
		int expected = 1;
		
		BarcodedProduct barcodeProd123 = Setup.createBarcodedProduct123(9.99, 2, true);
		
		byTextSearch.register(new ItemListenerStub() {
			@Override
			public void onItemAddedEvent(Product product, double quantity) {
				found++;
			}});
		
		byTextSearch.productFromTextSearchSelected(barcodeProd123);
		assertEquals(expected, found);
	}
	
	/**
	 * Tests an attempt to select a null product from the text search
	 */
	@Test
	public void testNullProductSelected() {
		int expected = 0;
		
		BarcodedProduct barcodeProd123 = Setup.createBarcodedProduct123(9.99, 2, true);
		barcodeProd123 = null;
		
		byTextSearch.register(new ItemListenerStub() {
			@Override
			public void onItemAddedEvent(Product product, double quantity) {
				found++;
			}});
		
		byTextSearch.productFromTextSearchSelected(barcodeProd123);
		assertEquals(expected, found);
	}	
}
