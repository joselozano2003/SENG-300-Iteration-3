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

package com.autovend.software.customer;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.autovend.devices.ReusableBagDispenser;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.software.test.Setup;
import com.autovend.software.ui.CustomerView;
import com.autovend.products.Product;
import com.autovend.software.bagging.ReusableBagProduct;
import com.autovend.software.customer.CustomerController.State;

import java.math.BigDecimal;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class CustomerControllerTest {
	private SelfCheckoutStation station;
	private CustomerController controller;
	private ReusableBagDispenser bagDispenser;
	private JPanel panel;
	private ReusableBagProduct bagProduct;
	
	private JFrame frame;
	
	
	@Before
	public void setup() {
		//Setup the class to test
		station = Setup.createSelfCheckoutStation();
		ReusableBagDispenser bagDispenser = new ReusableBagDispenser(10);
		controller = new CustomerController(station, bagDispenser, new CustomerView());
		panel = new JPanel(); 
		frame = new JFrame();
	}
	
	@Test (expected = NullPointerException.class)
	public void testConstructorNullStation() {
		new CustomerController(null, new ReusableBagDispenser(50), new CustomerView());
	}
	
	@Test (expected = NullPointerException.class)
	public void testConstructorNullBagDispenser() {
		new CustomerController(station, null, new CustomerView());
	}
	
	@Test (expected = NullPointerException.class)
	public void testConstructorNullView() {
		new CustomerController(station, null, new CustomerView());
	}
	
	
	//Adding empty test methods.
		
//    @Test
//    public void testStartNewSession() {
//        controller.startNewSession();
//        assertEquals(State.INITIAL, controller.getCurrentState());
//    }
//
    
    @Test
    public void testFinishAddingOwnBags() {
    	
    }
    

// Test for setting inital state
    @Test
    public void setStateInitialTest() {
    	controller.setState(State.INITIAL);
    	assertEquals(State.INITIAL, controller.getCurrentState());
    }
    @Test
    public void testStartAddingOwnBags() {
    	controller.onStartAddingOwnBags();
        assertEquals(State.ADDING_OWN_BAGS, controller.getCurrentState());
    }       
    @Test
    public void startPayingTest() {
    	controller.onStartPaying();
    	assertEquals(State.PAYING, controller.getCurrentState());
    }
    @Test
    public void startAddingItemsTest() {
    	controller.onStartAddingItems();
    	assertEquals(State.ADDING_ITEMS, controller.getCurrentState());
    }
    
    
    @Test
    public void setStatePrintingReceiptTest() {
    	controller.setState(State.PRINTING_RECEIPT);
    	assertEquals(State.PRINTING_RECEIPT, controller.getCurrentState());
    }
    
    @Test
    public void stateTransactionFinishedTest() {
    	controller.setState(State.FINISHED);
    	assertEquals(State.FINISHED, controller.getCurrentState());
    }
    @Test
    public void setStateDisabledTest() {
    	controller.setState(State.DISABLED);
    	assertEquals(State.DISABLED, controller.getCurrentState());
    }
    
    @Test
   public void setStateDispenseChangeTest() {
    	controller.setState(State.DISPENSING_CHANGE);
    	assertEquals(State.DISPENSING_CHANGE, controller.getCurrentState());
  }
    @Test
    public void finishAddingOwnBagTest() {
    	controller.onFinishAddingOwnBags();
    	assertEquals(State.DISABLED, controller.getCurrentState());
    }
    
    @Test
    public void setStateShutDownTest() {
    	controller.setState(State.SHUTDOWN);
    	assertEquals(State.SHUTDOWN, controller.getCurrentState());
    }
    @Test
    public void setStateStartUpTest() {
    	controller.setState(State.STARTUP);
    	assertEquals(State.STARTUP, controller.getCurrentState());
    }
    @Test
    public void startingNewSessionTest() {
    	controller.startNewSession();
    	assertEquals(State.INITIAL, controller.getCurrentState());
    }
    
//TEST TO SEE IF UPDATE VIEW IS WORKING PROPERLY
    //checking if the controller object's frame has the correct panel that is passed in, in the test case/
    @Test
    public void updateViewTest() {
    	controller.updateView(panel);
   	
    	frame = controller.getFrame();
    	assertTrue(frame.getContentPane().getComponents().length == 1);
    	assertEquals(frame.getContentPane().getComponents()[0], panel);
    }
    
// TESTING TO SEE IF IT CAN GO BACK TO CHECKOUT
    //Checking if the frame is updated with checkoutView (Panel type)
    @Test
    public void goBackToCheckOutTest() {
    	controller.goBackToCheckout();
    	
    	frame = controller.getFrame();
    	assertTrue(frame.getContentPane().getComponents().length == 1);
    	assertEquals(frame.getContentPane().getComponents()[0], controller.getCustomerView().checkoutView);
    }
    
// TESTING IF WEIGHT IS CHECKED DURING BAH DISPENSED EVENT
    @Test
    public void weightCheckDuringBagDispensedEventTest() {
    	bagProduct = new ReusableBagProduct();
    	controller.onBagsDispensedEvent(bagProduct, 3);
    	assertEquals(State.CHECKING_WEIGHT, controller.getCurrentState());
    }
// TESTING IF ITEM IS ADDED TO CART DURING BAG DISPENSED EVENT
    @Test
    public void addItemCheckDuringBagDispensedEventTest() {
    	bagProduct = new ReusableBagProduct(); 	
    	controller.onBagsDispensedEvent(bagProduct, 3);
    	
    	assertEquals(3.00, controller.getCurrentSession().getShoppingCart().get(bagProduct), 0.1);	
    }
    
// TESTING TO CHECK IF PLU VIEW PANEL IS OBTAINED IN FRAME
    @Test
    public void addItemByPLUViewTest() {
    	controller.onSelectAddItemByPLU();
    	
    	frame = controller.getFrame();
    	assertTrue(frame.getContentPane().getComponents().length == 1);
    	assertEquals(frame.getContentPane().getComponents()[0], controller.getCustomerView().pluView);
    }
 // TESTING TO CHECK IF BROWSING VIEW PANEL IS OBTAINED IN FRAME
    @Test
    public void browsingViewTest() {
    	controller.onSelectAddItemByBrowsing();
    	
    	frame = controller.getFrame();
    	assertTrue(frame.getContentPane().getComponents().length == 1);
    	assertEquals(frame.getContentPane().getComponents()[0], controller.getCustomerView().browsingView);
    }
 // TESTING TO CHECK IF MEMBERSHIP VIEW PANEL IS OBTAINED IN FRAME WHEN ADDING MEMBERSHIP
    @Test
    public void membershipViewTest() {
    	controller.onAddMembershipNumber();
    	
    	frame = controller.getFrame();
    	assertTrue(frame.getContentPane().getComponents().length == 1);
    	assertEquals(frame.getContentPane().getComponents()[0], controller.getCustomerView().membershipView);
    }
 // TESTING IF WEIGHT IS CHECKED DURING ITEM ADDED EVENT
    @Test
    public void weightCheckDuringItemAddEventTest() {
    	bagProduct = new ReusableBagProduct(); 
    	
    	controller.onItemAddedEvent(bagProduct, 5);
    	assertEquals(State.CHECKING_WEIGHT, controller.getCurrentState());
    }
 // TESTING IF ITEM IS ADDED TO CART DURING ADD ITEM EVENT
    @Test
    public void addItemCheckDuringAddItemEventTest() {
    	bagProduct = new ReusableBagProduct(); 	
    	
    	controller.onItemAddedEvent(bagProduct, 9);    	
    	assertEquals(9.00, controller.getCurrentSession().getShoppingCart().get(bagProduct), 0.1);	
    }
    
 /**   
    
    @Test
    public void testAddMoreItems() {
    	
    }
    
    @Test
    public void testStartPaying() {
		 
    	
    }
    
    @Test
    public void testPurchaseBags() {
    	
    }
    
    @Test
    public void testOnBagsDispensedEvent() {
    	
    }
    
    @Test
    public void testOnBagsDispensedFailure() {
    	
    }
    
    @Test 
    public void testReactToDisableDeviceRequest(){
    	
    }
    
    @Test
    public void testReactToEnableDeviceRequest() {
    	
    }
    
    @Test
    public void testReactToDisableStationRequest(){
    	
    }
    
    @Test
    public void testReactToEnableStationRequest() {
    	
    }
    
    @Test
    public void testReactToHardwareFailure() {
    	
    }
    
    @Test
    public void testOnItemAddedEvent() {
    	
    }
    
    @Test
    public void testOnItemNotFoundEvent() {
    	
    }
    
    @Test
    public void testOnPaymentAddedEvent() {
    	
    }
    
    @Test 
    public void testOnPaymentFailure() {
    	
    }
    
    @Test
    public void testOnReceiptPrintedEvent() {
    	
    }
    
    @Test 
    public void testOnReceiptPrinterFixed() {
    	
    }
    
    @Test
    public void testOnReceiptPrinterFailed() {
    	
    }
    
    @Test
    public void testOnChangeDispensedEvent() {
    	
    }
    
    @Test
    public void testOnChangeDispensedFailure() {
    	
    }
    
    @Test
    public void testOnWeightChanged() {
    	
    }
   **/
}
