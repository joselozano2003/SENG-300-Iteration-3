package com.autovend.software.item;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.autovend.devices.SelfCheckoutStation;
import com.autovend.products.PLUCodedProduct;
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
	private int found;
	
	/**
	 * Sets up the test suite. Runs before every method.
	 */
	@Before
	public void setup() {
		
		//Setup the class to test
		station = Setup.createSelfCheckoutStation();
		byPLUCode = new ByPLUCode(station, new CustomerView());
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
	 * Test ByPLUCode constructor with null parameter.
	 */
	@Test (expected = NullPointerException.class)
	public void testContructorNullStation() {
		new ByPLUCode(null, new CustomerView());
	}
	
	/**
	 * Test the event when a PLU Code has been entered 
	 */
	@Test
	public void testReactToPLUCodeEnteredEvent() {
		PLUCodedProduct PLUProd = Setup.createPLUProduct1234(11.99, 3, true);
		
		//byPLUCode.reactToPLUCodeEnteredEvent(PLUProd.getPLUCode(), 3);
		
		
		
	}
	
	

}
