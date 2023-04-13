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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

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
 * A test class that performs tests on the ItemFacade class.
 * @author Akansha
 *
 */
public class ItemFacadeTest {
	private SelfCheckoutStation station;
	private ItemFacade itemFacade;
	private int found;
	
	/**
	 * Sets up the test suite. Runs before every method.
	 */
	@Before
	public void setup() {
		//Setup the class to test
		station = Setup.createSelfCheckoutStation();
		itemFacade = new ItemFacade(station, new CustomerView(), false);
	}
	
	/**
	 * Cleans up the test suite. Runs after every test method.
	 */
	@After
	public void teardown() {
		station = null;
		itemFacade = null;
		found = 0;
	}
	
	/**
	 * Test ItemFacade constructor with null parameter.
	 */
	@Test (expected = NullPointerException.class)
	public void testContructorNullStation() {
		new ItemFacade(null, new CustomerView(), false);
	}
	
	@Test (expected = NullPointerException.class)
	public void testContructorNullView() {
		new ItemFacade(station, null, false);
	}
	
	/**
	 * Tests that ItemFacade getChildren list contains the correct number of children
	 */
	@Test
	public void testGetChildren() {
		int expectedSize = 4;
		int actualSize = itemFacade.getChildren().size();
		assertEquals(expectedSize, actualSize);
	}
	
	/**
	 * Test whether a null instance of ItemFacade is returned
	 */
	@Test
	public void testGetNullInstanceOfItemFacade() {
		itemFacade = null;
		assertEquals(itemFacade, itemFacade.getInstance());
	}
	
	/**
	 * Tests whether a non-null instance of ItemFacade is returned
	 */
	@Test
	public void testGetNonNullInstanceOfItemFacade() {
		ItemFacade itemFacade2 = new ItemFacade(station, new CustomerView(), false);
		itemFacade2 = itemFacade;
		
		assertEquals(itemFacade.getInstance(), itemFacade2.getInstance());	
	}	
}