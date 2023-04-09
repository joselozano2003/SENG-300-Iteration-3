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
package com.autovend.software.receipt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.autovend.Barcode;
import com.autovend.devices.AbstractDevice;
import com.autovend.devices.EmptyException;
import com.autovend.devices.OverloadException;
import com.autovend.devices.ReceiptPrinter;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.ReceiptPrinterObserver;
import com.autovend.products.BarcodedProduct;
import com.autovend.products.PLUCodedProduct;
import com.autovend.products.Product;
import com.autovend.software.test.Setup;

public class ReceiptFacadeTest {
	private List<ReceiptEventListener> testListeners;
	private SelfCheckoutStation station;
	private ReceiptFacade facade;
	private BarcodedProduct bcProduct1;
	private BarcodedProduct bcProduct2;
	private PLUCodedProduct pluProduct1;
	private PLUCodedProduct pluProduct2;
	
	@Before
	public void setup() {
		//Setup the class to test
		station = Setup.createSelfCheckoutStation();
		facade = new ReceiptFacade(station);
		testListeners = new ArrayList<>();
		
		bcProduct1 = Setup.createBarcodedProduct123(5.00, 69, false);
		bcProduct2 = Setup.createBarcodedProduct456(4.20, 99, false);
		
		pluProduct1 = Setup.createPLUProduct1234(5.00, 69, false);
		pluProduct2 = Setup.createPLUProduct5678(4.20, 99, false);
	}
	
	@Test (expected = NullPointerException.class)
	public void testNullConstruction() {
		new ReceiptFacade(null);
	}
	@Test
	public void testDisabled() {
		station.printer.disable();
		assertTrue(station.printer.isDisabled());
	}
	@Test
	public void testEnabled() {
		station.printer.disable();
		station.printer.enable();
		assertFalse(station.printer.isDisabled());
	}
	@Test
	public void testPrintReceiptBarcode(){
		try {
			station.printer.addInk(100);
		} catch (OverloadException e) {}
		try {
			station.printer.addPaper(100);
		} catch (OverloadException e) {}
		StringBuilder expected = new StringBuilder();	
		
		Map<Product, Double> cart = new HashMap<>();		
		cart.put(bcProduct1, 1.00);
		cart.put(bcProduct2, 2.00);
		
		expected.append("Receipt:\n");	
		expected.append("Product One x 1.00 5.00\n");
		expected.append("Product Two x 2.00 8.40\n");
		
		facade.printReceipt(cart);
		String actual = station.printer.removeReceipt();
		
		System.out.println(expected.toString());
		System.out.println(actual);
		
		boolean receiptMatch = false;
		if (actual.equals(expected.toString())) {
			receiptMatch = true;
		}else {
			receiptMatch = false;
		}
		
		assertTrue(receiptMatch);
	}
	@Test
	public void testPrintReceiptPLU(){
		try {
			station.printer.addInk(100);
		} catch (OverloadException e) {}
		try {
			station.printer.addPaper(100);
		} catch (OverloadException e) {}
		StringBuilder expected = new StringBuilder();	
		
		Map<Product, Double> cart = new HashMap<>();		
		cart.put(pluProduct1, 1.00);
		cart.put(pluProduct2, 2.00);
		
		expected.append("Receipt:\n");	
		expected.append("Product One x 1.00 5.00\n");
		expected.append("Product Two x 2.00 8.40\n");
		
		facade.printReceipt(cart);
		String actual = station.printer.removeReceipt();
		
		//System.out.println(expected.toString());
		//System.out.println(actual);
		
		boolean receiptMatch = false;
		if (actual.equals(expected.toString())) {
			receiptMatch = true;
		}else {
			receiptMatch = false;
		}
		
		assertTrue(receiptMatch);
	}
	
	public void testLowInk() {
		try {
			station.printer.addInk(1);
		} catch (OverloadException e) {}
		try {
			station.printer.addPaper(100);
		} catch (OverloadException e) {}
		
		Map<Product, Double> cart = new HashMap<>();		
		cart.put(bcProduct1, 1.00);
		cart.put(bcProduct2, 2.00);
		
		facade.printReceipt(cart);
		
	}
	@Test
	public void testLowPaper() {
		try {
			station.printer.addInk(100);
		} catch (OverloadException e) {}
		try {
			station.printer.addPaper(1);
		} catch (OverloadException e) {}
		
		Map<Product, Double> cart = new HashMap<>();		
		cart.put(bcProduct1, 1.00);
		cart.put(bcProduct2, 2.00);
		
		facade.printReceipt(cart);
	}
	
	/*--------------- STUBS ---------------*/
	
	/**Stubs primarily check if/how many times observer events occurred.
	 * Tests should fail if an unexpected event is reported.
	 * Override any event in this stub that you don't want to fail.
	 */
	public class ReceiptEventListenerStub implements ReceiptEventListener {
		@Override
		public void reactToHardwareFailure() {fail();}
		@Override
		public void reactToDisableDeviceRequest(AbstractDevice<? extends AbstractDeviceObserver> device) {fail();}
		@Override
		public void reactToEnableDeviceRequest(AbstractDevice<? extends AbstractDeviceObserver> device) {fail();}
		@Override
		public void reactToDisableStationRequest() {fail();}
		@Override
		public void reactToEnableStationRequest() {fail();}
		@Override
		public void onReceiptPrinterFailed() {fail();}
		@Override
		public void onReceiptPrintedEvent(StringBuilder receiptText) {fail();}
		@Override
		public void onReceiptPrinterFixed() {fail();}
	}
}
