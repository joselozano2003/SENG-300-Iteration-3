package com.autovend.software.item;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.autovend.devices.ElectronicScale;
import com.autovend.devices.SelfCheckoutStation;
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
		
		instance.register(new ItemListenerStub() {
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
		
		instance.register(new ItemListenerStub() {
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
	
	/*
	 * Attempt to getting a PLUCodedProduct with a null PLUCode but it won't be possible..
	 */
	
	/*
	@Test
	public void testValidAndInvalidPLUProcessing() {
		int expected = 2;
		PLUCodedProduct PLUProd123 = Setup.createPLUProduct1234(11.99, 3, true);
		PLUCodedProduct PLUProd456 = Setup.createPLUProduct56789(3.57, 1, false);
		
		instance.register(new ItemListenerStub() {
			@Override
			public void onItemAddedEvent(Product product, double quantity) {
				found++;
				if (found == 1) {
					assertEquals(PLUProd123, product);
				}
			}
			
			@Override 
			public void onItemNotFoundEvent() {
				found++;
		}});
		
		byPLUCode.processPLUInput("1234", 2);
		byPLUCode.processPLUInput("56789", 1);
		
		assertEquals(expected, found);
	}
	*/
	
	/**
	 * Tests the event when the weight is changed during a PLUCode scan
	 */
	@Test
	public void testEventReactToWeightChanged() {
		int expected = 1;
		PLUCodedProduct PLUProd123 = Setup.createPLUProduct1234(11.99, 3, true);
		byPLUCode.reactToPLUCodeEntered("1234");
		
		instance.register(new ItemListenerStub() {
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
	 * Assert that these hardware events don't announce to listeners.
	 */
	@Test
	public void testMethodsNoEvents() {
		//Will fail if any listener event is entered.
		instance.register(new ItemListenerStub());
		byPLUCode.reactToEnabledEvent(station.scale);
		byPLUCode.reactToDisabledEvent(station.scale);
		byPLUCode.reactToOverloadEvent(station.scale);
		byPLUCode.reactToOutOfOverloadEvent(station.scale);
		
		assertEquals(byPLUCode.getListeners(), instance.getListeners());	
	}

}
