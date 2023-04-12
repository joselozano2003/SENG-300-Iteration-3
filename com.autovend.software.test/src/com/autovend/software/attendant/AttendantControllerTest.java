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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.SupervisionStation;
import com.autovend.software.customer.CustomerController;
import com.autovend.software.customer.CustomerStationLogic;
import com.autovend.software.customer.CustomerController.State;
import com.autovend.software.test.Setup;

import auth.AttendantAccount;
import auth.AttendantAccountDatabases;

public class AttendantControllerTest {
	private AttendantController controller;
	private AttendantModel model;
	//private AttendantView view;
	private ArrayList<CustomerStationLogic> customerStationList;
	private SupervisionStation station;
	
	
	@Before
	public void setup() {
		//Setup the class to test
		model = new AttendantModel();
		//view = new AttendantView();
		station = new SupervisionStation();
		customerStationList = new ArrayList<CustomerStationLogic>();
		
		
		SelfCheckoutStation scs = Setup.createSelfCheckoutStation();
	    CustomerStationLogic stationLogic = CustomerStationLogic.installOn(scs);

	    // Add the CustomerStationLogic instance to the customerStationList
	    customerStationList.add(stationLogic);

	    controller = new AttendantController(station);
	}
	
	@Test (expected = NullPointerException.class)
	public void testContructionNullStation() {
		new AttendantController(null);
	}
	
	@Test (expected = NullPointerException.class)
	public void testContructionNullView() {
		new AttendantController(null);
	}
	
	@Test (expected = NullPointerException.class)
	public void testContructionNullList() {
		new AttendantController(null);
	}
	
	@Test
	public void SuccesfulLogInTest() {
		AttendantAccount demoAttendant = AttendantAccountDatabases.demoAccount();
		assertTrue(controller.startLogIn(demoAttendant));
	}

	@Test
	public void SuccesfulLogOutTest() {
		AttendantAccount demoAttendant = AttendantAccountDatabases.demoAccount();
		assertTrue(controller.startLogOut(demoAttendant));
	}

	@Test
	public void UnsuccesfulLogInTest() {
		AttendantAccount fakeAttendant = new AttendantAccount("Fake", "Fake");
		assertFalse(controller.startLogIn(fakeAttendant));
	}

	@Test
	public void UnsuccesfulLogOutTest() {
		AttendantAccount fakeAttendant = new AttendantAccount("Fake", "Fake");
		assertFalse(controller.startLogOut(fakeAttendant));
	}

	@Test
	public void SuccesfulAddingAndDeletingAccountTest() {
		AttendantAccount addedAccount = new AttendantAccount("addedDemo", "addedDemo");
		assertTrue(controller.startAddAccount(AttendantAccountDatabases.godAccount(), addedAccount));
		assertTrue(controller.startDeleteAccount(AttendantAccountDatabases.godAccount(), addedAccount));
	}

	@Test
	public void UnsuccesfulAddingAndDeletingAccountTest() {
		AttendantAccount addedAccount = new AttendantAccount("addedDemo", "addedDemo");
		assertFalse(controller.startAddAccount(AttendantAccountDatabases.demoAccount(), addedAccount));
		assertFalse(controller.startDeleteAccount(AttendantAccountDatabases.demoAccount(), addedAccount));
	}
	
	@Test
    public void testAddCustomerStation() {
        CustomerStationLogic stationLogic = CustomerStationLogic.installOn(Setup.createSelfCheckoutStation());
        controller.addCustomerStation(stationLogic);

        assertEquals(1, controller.getCustomerStationsManaged().size());
        assertEquals(stationLogic, controller.getCustomerStationsManaged().get(0));
    }

    @Test(expected = NullPointerException.class)
    public void testAddCustomerStationNull() {
        controller.addCustomerStation(null);
    }
	
    @Test
    public void testShutDownAndStartUpStation() {
        CustomerStationLogic stationLogic2 = CustomerStationLogic.installOn(Setup.createSelfCheckoutStation());
        controller.addCustomerStation(stationLogic2);
        
        controller.shutDownStation(0);
        assertEquals(CustomerController.State.SHUTDOWN, stationLogic2.getController().getCurrentState());

        controller.startUpStation(0);
        assertEquals(CustomerController.State.STARTUP, stationLogic2.getController().getCurrentState());
    }
	
    @Test
    public void testPermitStationUse() {
        CustomerStationLogic stationLogic = CustomerStationLogic.installOn(Setup.createSelfCheckoutStation());
        controller.addCustomerStation(stationLogic);

        assertEquals(CustomerController.State.DISABLED, stationLogic.getController().getCurrentState());

        controller.permitStationUse(0);
        assertEquals(CustomerController.State.ADDING_ITEMS, stationLogic.getController().getCurrentState());
    }

    @Test
    public void testDenyStationUse() {
        CustomerStationLogic stationLogic = CustomerStationLogic.installOn(Setup.createSelfCheckoutStation());
        controller.addCustomerStation(stationLogic);

        controller.permitStationUse(0);
        assertEquals(CustomerController.State.ADDING_ITEMS, stationLogic.getController().getCurrentState());

        controller.denyStationUse(0);
        assertEquals(CustomerController.State.DISABLED, stationLogic.getController().getCurrentState());
    }

    @Test
    public void testReEnableStationUse() {
        CustomerStationLogic stationLogic = CustomerStationLogic.installOn(Setup.createSelfCheckoutStation());
        controller.addCustomerStation(stationLogic);

        assertEquals(CustomerController.State.DISABLED, stationLogic.getController().getCurrentState());

        controller.permitStationUse(0);
        assertEquals(CustomerController.State.ADDING_ITEMS, stationLogic.getController().getCurrentState());

        controller.denyStationUse(0);
        assertEquals(CustomerController.State.DISABLED, stationLogic.getController().getCurrentState());

        controller.reEnableStationUse(0);
        assertEquals(CustomerController.State.ADDING_ITEMS, stationLogic.getController().getCurrentState());
    }

    @Test
    public void testGetCustomerStations() {
        CustomerStationLogic stationLogic = CustomerStationLogic.installOn(Setup.createSelfCheckoutStation());
        controller.addCustomerStation(stationLogic);

        assertEquals(1, controller.getCustomerStationsManaged().size());
        assertEquals(stationLogic, controller.getCustomerStationsManaged().get(0));
    }

}
