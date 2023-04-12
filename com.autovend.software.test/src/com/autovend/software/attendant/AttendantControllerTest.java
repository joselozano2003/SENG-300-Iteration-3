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
package com.autovend.software.attendant;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.autovend.software.customer.CustomerController;
import com.autovend.software.customer.CustomerStationLogic;
import com.autovend.software.test.Setup;

import auth.AttendantAccount;

public class AttendantControllerTest {
	private AttendantController controller;
	private AttendantModel model;
	private AttendantView view;
	private ArrayList<CustomerStationLogic> customerStationList;
	
	@Before
	public void setup() {
		//Setup the class to test
		model = new AttendantModel();
		view = new AttendantView();
		customerStationList = new ArrayList<CustomerStationLogic>();
		customerStationList.add(CustomerStationLogic.installOn(Setup.createSelfCheckoutStation()));
		controller = new AttendantController(model, view);
	}
	
	@Test (expected = NullPointerException.class)
	public void testContructionNullStation() {
		new AttendantController(null, view);
	}
	
	@Test (expected = NullPointerException.class)
	public void testContructionNullView() {
		new AttendantController(model, null);
	}
	
	@Test
	public void testAddCustomerStation() {
		int initialSize = controller.getCustomerStations().size();
	    CustomerStationLogic stationLogic = CustomerStationLogic.installOn(Setup.createSelfCheckoutStation());
	    controller.addCustomerStation(stationLogic);
	    assertEquals(initialSize + 1, controller.getCustomerStations().size());
	}
	
	@Test
	public void testShutDownAndStartUpStation() {
		CustomerStationLogic stationLogic = CustomerStationLogic.installOn(Setup.createSelfCheckoutStation());
		controller.addCustomerStation(stationLogic);
		controller.shutDownStation(0);
		assertEquals(CustomerController.State.SHUTDOWN, stationLogic.getController().getState());
		
		controller.startUpStation(0);
		assertEquals(CustomerController.State.STARTUP, stationLogic.getController().getState());
	}
	
	@Test
	public void testPermitStationUse() {
	    CustomerStationLogic stationLogic = CustomerStationLogic.installOn(Setup.createSelfCheckoutStation());
	    controller.addCustomerStation(stationLogic);

	    // Initially, the station should be in DISABLED state
	    assertEquals(CustomerController.State.DISABLED, stationLogic.getController().getState());

	    // Permit station use and check if the state changes to INITIAL
	    controller.permitStationUse(0);
	    assertEquals(CustomerController.State.INITIAL, stationLogic.getController().getState());
	}

	 
	@Test
	public void testDenyStationUse() {
	    CustomerStationLogic stationLogic = CustomerStationLogic.installOn(Setup.createSelfCheckoutStation());
	    controller.addCustomerStation(stationLogic);

	    // Permit station use to set the state to INITIAL
	    controller.permitStationUse(0);
	    assertEquals(CustomerController.State.INITIAL, stationLogic.getController().getState());

	    // Deny station use and check if the state changes back to DISABLED
	    controller.denyStationUse(0);
	    assertEquals(CustomerController.State.DISABLED, stationLogic.getController().getState());
	}
	


}
