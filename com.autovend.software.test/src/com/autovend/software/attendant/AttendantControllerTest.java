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

import java.math.BigDecimal;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.autovend.BarcodedUnit;
import com.autovend.devices.ReusableBagDispenser;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.SupervisionStation;
import com.autovend.products.BarcodedProduct;
import com.autovend.software.customer.CustomerController;
import com.autovend.software.customer.CustomerSession;
import com.autovend.software.customer.CustomerStationLogic;
import com.autovend.software.customer.CustomerController.State;
import com.autovend.software.item.ProductsDatabase2;
import com.autovend.software.item.TextSearchProduct;
import com.autovend.software.test.Setup;
import com.autovend.software.ui.CustomerView;

import auth.AttendantAccount;
import auth.AttendantAccountDatabases;

public class AttendantControllerTest {
	private AttendantController controller;
	private AttendantModel model;
	//private AttendantView view;
	private ArrayList<CustomerStationLogic> customerStationList;
	private SupervisionStation station;
	
	private CustomerController customerController;
	private ReusableBagDispenser bagDispenser;
	public CustomerSession currentSession;
	
	private SelfCheckoutStation scs;
    private SupervisionStation supervisionStation;
    private AttendantController attendantController;
    
	
	
	@Before
	public void setup() {
		//Setup the class to test
		model = new AttendantModel();
		//view = new AttendantView();
		station = new SupervisionStation();
		customerStationList = new ArrayList<CustomerStationLogic>();
		
		
		scs = Setup.createSelfCheckoutStation();
	    CustomerStationLogic stationLogic = CustomerStationLogic.installOn(scs);

	    // Add the CustomerStationLogic instance to the customerStationList
	    customerStationList.add(stationLogic);

	    controller = new AttendantController(station);
	    
	    ReusableBagDispenser bagDispenser = new ReusableBagDispenser(10);
	    customerController = new CustomerController(scs, bagDispenser, new CustomerView());
	    customerController.startNewSession();
	    currentSession = customerController.getCurrentSession();
	}
	
	@Test (expected = NullPointerException.class)
	public void testContructionNullStation() {
		new AttendantController(station);
	}
	
	@Test (expected = NullPointerException.class)
	public void testContructionNullView() {
		new AttendantController(station);
	}
	
	@Test (expected = NullPointerException.class)
	public void testContructionNullList() {
		new AttendantController(station);
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
    public void testOnStationShutdown() {
        attendantController.onStationShutdown(1);
        assertEquals(State.SHUTDOWN, customerController.getCurrentState());
    }

    @Test(expected = NullPointerException.class)
    public void testAddCustomerStationNull() {
        controller.addCustomerStation(null);
    }
	
    @Test
    public void testShutDownAndStartUpStation() {
        CustomerStationLogic stationLogic = CustomerStationLogic.installOn(Setup.createSelfCheckoutStation());
        controller.addCustomerStation(stationLogic);
        
        controller.onStationShutdown(0);
        CustomerController.State state = stationLogic.getController().getCurrentState();
        assertEquals(CustomerController.State.SHUTDOWN, state);

//        controller.startUpStation(0);
//        assertEquals(CustomerController.State.STARTUP, state);
    }
	 
    @Test
    public void addInkToPrinter() {
        CustomerStationLogic station = new CustomerStationLogic(selfCheckoutStation);
        attendantController.addCustomerStation(station);
        attendantController.addInkToStation(0, 10);
        assertEquals(10, station.getController().inkAdded);
    }
    @Test
    public void addPaperToPrinter() {
        CustomerStationLogic station = new CustomerStationLogic(selfCheckoutStation);
        attendantController.addCustomerStation(station);
        attendantController.addPaperToStation(0, 10);
        assertEquals(10, station.getController().paperAdded);
    }

    @Test
    public void testShutDownStation(){
        CustomerStationLogic station = new CustomerStationLogic(selfCheckoutStation);
        attendantController.addCustomerStation(station);
        attendantController.shutDownStation(0);
        CustomerController.State state = station.getController().getCurrentState();
        assertEquals(CustomerController.State.SHUTDOWN, state);
    }

    @Test
    public void testStartUpStation(){
        CustomerStationLogic station = new CustomerStationLogic(selfCheckoutStation);
        attendantController.addCustomerStation(station);
        attendantController.shutDownStation(0);
        attendantController.startUpStation(0);
        CustomerController.State state = station.getController().getCurrentState();
        assertEquals(CustomerController.State.STARTUP, state);
    }

    @Test
    public void testDenyStationUse(){
        CustomerStationLogic station = new CustomerStationLogic(selfCheckoutStation);
        attendantController.addCustomerStation(station);
        attendantController.startUpStation(0);
        attendantController.permitStationUse(0);
        CustomerController.State state = station.getController().getCurrentState();
        assertEquals(CustomerController.State.DISABLED, state);
        attendantController.denyStationUse(0);
        state = station.getController().getCurrentState();
        assertEquals(CustomerController.State.DISABLED, state);
    }

    @Test
    public void testPermitStationUse(){
        CustomerStationLogic station = new CustomerStationLogic(selfCheckoutStation);
        attendantController.addCustomerStation(station);
        attendantController.startUpStation(0);
        attendantController.permitStationUse(0);
        CustomerController.State state = station.getController().getCurrentState();
        assertEquals(CustomerController.State.DISABLED, state);
        attendantController.addPaperToStation(0, 10);
        attendantController.addInkToStation(0, 10);
        attendantController.shutDownStation(0);
        attendantController.startUpStation(0);
        attendantController.permitStationUse(0);
        state = station.getController().getCurrentState();
        assertEquals(CustomerController.State.INITIAL, state);
    }

    @Test
    public void testRemoveItem() {
        CustomerStationLogic station = new CustomerStationLogic(selfCheckoutStation);
        attendantController.addCustomerStation(station);
        attendantController.startUpStation(0);
        attendantController.permitStationUse(0);
        attendantController.addPaperToStation(0, 10);
        attendantController.addInkToStation(0, 10);
        attendantController.startUpStation(0);
        attendantController.permitStationUse(0);
        CustomerController.State state = station.getController().getCurrentState();
        assertEquals(CustomerController.State.INITIAL, state);

        CustomerController customerController = station.getController();
        customerController.startNewSession();
        customerController.onStartAddingItems();
        selfCheckoutStation = customerController.getStation();

        boolean flag = false;
        while (!flag) {
            flag = selfCheckoutStation.mainScanner
                    .scan(new BarcodedUnit(barcodeProduct.getBarcode(), barcodeProduct.getExpectedWeight()));
        }
        selfCheckoutStation.baggingArea
                .add(new BarcodedUnit(barcodeProduct.getBarcode(), barcodeProduct.getExpectedWeight()));

        assertEquals(1, customerController.getCurrentSession().getTotalCost().intValue());

        flag = false;
        while (!flag) {
            flag = selfCheckoutStation.mainScanner
                    .scan(new BarcodedUnit(barcodeProduct2.getBarcode(), barcodeProduct2.getExpectedWeight()));
        }
        selfCheckoutStation.baggingArea
                .add(new BarcodedUnit(barcodeProduct2.getBarcode(), barcodeProduct2.getExpectedWeight()));

        assertEquals(3, customerController.getCurrentSession().getTotalCost().intValue());

        station.removeItemRequest(new BarcodedProduct(barcodeProduct2.getBarcode(), "product2", new BigDecimal("2"), 15), 1);

        attendantController.removeItemfromStation(0, new BarcodedProduct(barcodeProduct2.getBarcode(), "product2", new BigDecimal("2"), 15), 1);
        assertEquals(1, customerController.getCurrentSession().getTotalCost().intValue());
        }

    @Test
    public void addItemWithTextSearch(){
        CustomerStationLogic station = new CustomerStationLogic(selfCheckoutStation);
        attendantController.addCustomerStation(station);
        attendantController.startUpStation(0);
        attendantController.permitStationUse(0);
        attendantController.addPaperToStation(0, 10);
        attendantController.addInkToStation(0, 10);
        attendantController.startUpStation(0);
        attendantController.permitStationUse(0);

        CustomerController customerController = station.getController();
        customerController.startNewSession();
        customerController.onStartAddingItems();

        TextSearchProduct textSearchProduct = new TextSearchProduct("Banana", "banana", new BigDecimal("1"), 10);
        ProductsDatabase2.Products_Textsearch_Keywords_Database.put("Banana", textSearchProduct);
        attendantController.addItemToStationByTextSearch(0, "Banana", 1);
        assertEquals(1, customerController.getCurrentSession().getTotalCost().intValue());

    }

}
