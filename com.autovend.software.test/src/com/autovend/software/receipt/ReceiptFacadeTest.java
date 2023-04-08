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

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.autovend.devices.AbstractDevice;
import com.autovend.devices.ReceiptPrinter;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.ReceiptPrinterObserver;
import com.autovend.software.attendant.AttendantController;
import com.autovend.software.attendant.AttendantModel;
import com.autovend.software.attendant.AttendantView;
import com.autovend.software.customer.CustomerController;
import com.autovend.software.customer.CustomerSession;
import com.autovend.software.customer.CustomerStationLogic;
import com.autovend.software.test.Setup;

public class ReceiptFacadeTest {
	private List<ReceiptEventListener> testListeners;
	private SelfCheckoutStation station;
	private ReceiptFacade facade;
	
	@Before
	public void setup() {
		//Setup the class to test
		station = Setup.createSelfCheckoutStation();
		facade = new ReceiptFacade(station);
		testListeners = new ArrayList<>();
	}
	
	@Test (expected = NullPointerException.class)
	public void testNullConstruction() {
		new ReceiptFacade(null);
	}
	
	@Test
	public void testReactToHardwareFailure() {

	}
	
	@Test
	public void testLowInk() {
		
	}
	@Test
	public void testLowPaper() {
		
	}
	// ------------ STUBS --------------
	public class ReceiptEventListenerStub implements ReceiptEventListener {
		private boolean reactToHardwareFailureCalled = false;
		@Override
		public void reactToHardwareFailure() {
			reactToHardwareFailureCalled = true;
		}
		@Override
		public void reactToDisableDeviceRequest(AbstractDevice<? extends AbstractDeviceObserver> device) {}
		@Override
		public void reactToEnableDeviceRequest(AbstractDevice<? extends AbstractDeviceObserver> device) {}
		@Override
		public void reactToDisableStationRequest() {}
		@Override
		public void reactToEnableStationRequest() {}
		@Override
		public void onReceiptPrintedEvent() {}
		@Override
		public void onReceiptPrinterFailed() {}
		@Override
		public void onLowPaper() {}
		@Override
		public void onLowInk() {}
	
		public boolean isReactToHardwareFailureCalled() {
			return reactToHardwareFailureCalled;
		}
	}
	public class ReceiptPrinterObserverStub implements ReceiptPrinterObserver {
		@Override
		public void reactToEnabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {}
		@Override
		public void reactToDisabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {}
		@Override
		public void reactToOutOfPaperEvent(ReceiptPrinter printer) {}
		@Override
		public void reactToOutOfInkEvent(ReceiptPrinter printer) {}
		@Override
		public void reactToPaperAddedEvent(ReceiptPrinter printer) {}
		@Override
		public void reactToInkAddedEvent(ReceiptPrinter printer) {}
	}
}
