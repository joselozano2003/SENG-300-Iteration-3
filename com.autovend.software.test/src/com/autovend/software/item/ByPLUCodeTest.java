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

import com.autovend.devices.ElectronicScale;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.products.BarcodedProduct;
import com.autovend.products.PLUCodedProduct;
import com.autovend.products.Product;
import com.autovend.software.test.Setup;
import com.autovend.software.ui.CustomerView;

/**
 * A test class that performs tests on the ByPLUCode class.
 * @author Akansha
 *
 */
public class ByPLUCodeTest {
	private SelfCheckoutStation station;
	private ByPLUCode byPLUCode;
	private ItemFacade instance;
	private int found;
	
	/**
	 * Sets up the test suite. Runs before every method.
	 */
	@Before
	public void setup() {
		
		//Setup the class to test
		station = Setup.createSelfCheckoutStation();
		byPLUCode = new ByPLUCode(station, new CustomerView());
		instance = new ItemFacade(station, new CustomerView(), false);
	}
	
	/**
	 * Cleans up the test suite. Runs after every test method.
	 */
	@After
	public void teardown() {
		station = null;
		byPLUCode = null;
		instance = null;
		found = 0;
	}
	
	/**
	 * Test ByPLUCode constructor with null station parameter.
	 */
	@Test (expected = NullPointerException.class)
	public void testContructorNullStation() {
		new ByPLUCode(null, new CustomerView());
	}
	
	/**
	 * Test ByPLUCode constructor with null customer view parameter.
	 */
	@Test (expected = NullPointerException.class)
	public void testContructorNullView() {
		new ByPLUCode(station, null);
	}
	
	/**
	 * Tests the processing of a single PLUCoded Product
	 */
	@Test
	public void testSingleValidPLUProcessing() {
		int expected = 1;
		PLUCodedProduct PLUProd123 = Setup.createPLUProduct1234(11.99, 3, true);
		
		byPLUCode.reactToPLUCodeEntered("1234");
		
		byPLUCode.register(new ItemListenerStub() {
			@Override
			public void onItemAddedEvent(Product product, double quantity) {
				found++;
				if (found == 1) {
					assertEquals(PLUProd123, product);
				}
			}});
		
		byPLUCode.processPLUInput("1234", 2);
		assertEquals(expected, found);
	}
	
	/**
	 * Tests the processing of a multiple PLUCoded Products
	 */
	@Test
	public void testMultipleValidPLUProcessing() {
		int expected = 2;
		PLUCodedProduct PLUProd123 = Setup.createPLUProduct1234(11.99, 3, true);
		PLUCodedProduct PLUProd456 = Setup.createPLUProduct56789(3.57, 1, true);
		
		byPLUCode.register(new ItemListenerStub() {
			@Override
			public void onItemAddedEvent(Product product, double quantity) {
				found++;
				if (found == 1) {
					assertEquals(PLUProd123, product);
				}
				
				if (found == 2) {
					assertEquals(PLUProd456, product);
				}
			}});
		
		byPLUCode.processPLUInput("1234", 2);
		byPLUCode.processPLUInput("56789", 1);
		assertEquals(expected, found);
	}
	
	/**
	 * Tests the event when the weight is changed during a PLUCode lookup
	 */
	@Test
	public void testEventReactToWeightChanged() {
		int expected = 1;
		PLUCodedProduct PLUProd123 = Setup.createPLUProduct1234(11.99, 3, true);
		byPLUCode.reactToPLUCodeEntered("1234");
		
		byPLUCode.register(new ItemListenerStub() {
			@Override
			public void onItemAddedEvent(Product product, double quantity) {
				found++;
				if (found == 1) {
					assertEquals(PLUProd123, product);
				}
			}});
		
		byPLUCode.reactToWeightChangedEvent(station.scale, 6);
		assertEquals(expected, found);
	}
	
	/**
	 * Tests the event when the weight is not changed during a PLUCode lookup
	 */
	@Test
	public void testEventReactToNOWeightChange() {
		int expected = 0;
		PLUCodedProduct PLUProd123 = Setup.createPLUProduct1234(11.99, 3, true);
		byPLUCode.reactToPLUCodeEntered("1234");
		
		byPLUCode.register(new ItemListenerStub() {
			@Override
			public void onItemAddedEvent(Product product, double quantity) {
				found++;
				if (found == 1) {
					assertEquals(PLUProd123, product);
				}
			}});
		
		byPLUCode.reactToWeightChangedEvent(station.scale, 0);
		assertEquals(expected, found);
	}
	
	/**
	 * Tests the event when the weight is changed during a PLUCode lookup for a null product
	 */
	@Test
	public void testEventReactToWeightChangeNullProd() {
		int expected = 0;
		PLUCodedProduct PLUProd123 = Setup.createPLUProduct1234(11.99, 3, true);
		
		byPLUCode.register(new ItemListenerStub() {
			@Override
			public void onItemAddedEvent(Product product, double quantity) {
				found++;
				if (found == 1) {
					assertEquals(PLUProd123, product);
				}
			}});
		
		byPLUCode.reactToWeightChangedEvent(station.scale, 7);
		assertEquals(expected, found);
	}
	/**
	 * Assert that these hardware events don't announce to listeners.
	 */
	@Test
	public void testMethodsNoEvents() {
		int expected = 4;
		//Will fail if any listener event is entered.
		byPLUCode.register(new ItemListenerStub() {
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

		byPLUCode.register(new ItemListenerStub());
		byPLUCode.reactToEnabledEvent(station.scale);
		byPLUCode.reactToDisabledEvent(station.scale);
		byPLUCode.reactToOverloadEvent(station.scale);
		byPLUCode.reactToOutOfOverloadEvent(station.scale);
		assertNotEquals(expected, found);		
	}
}
