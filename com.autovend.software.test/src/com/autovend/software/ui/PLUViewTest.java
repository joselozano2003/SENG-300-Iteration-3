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

package com.autovend.software.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;

import javax.swing.JTextField;

import org.junit.Before;
import org.junit.Test;

import com.autovend.Numeral;
import com.autovend.PriceLookUpCode;
import com.autovend.external.ProductDatabases;
import com.autovend.products.PLUCodedProduct;
import com.autovend.software.payment.PaymentEventListener;

/**
 * Automated JUnit4 tests for the PLUView class
 */
public class PLUViewTest {
	/**
	 * Plan:
	 * - Test constructor
	 * - Press addButton -> notifyItemAdded
	 * 		- Stub PLUViewObserver that is used for this
	 * - Press backButton -> stub ActionListener's actionPerformed (two stubs???)
	 * 		- Looks like we should not stub this ActionListener, but rather the CustomerUIEventListener
	 * - Getter methods are lower priority
	 */
	private PLUView pluView;
	private boolean PLUCodeWasEntered;
	private String PLUCodeEntered;
	
	@Before
	public void setup() {
		// Construct the PLUView
		pluView = new PLUView();
		
		// Create a test PLUCodedProduct
		Numeral[] code3 = { Numeral.one, Numeral.one, Numeral.one, Numeral.one };
		PriceLookUpCode pluCode = new PriceLookUpCode(code3);
		PLUCodedProduct pluProduct = new PLUCodedProduct(pluCode, "apples", new BigDecimal("1.00"));
		ProductDatabases.PLU_PRODUCT_DATABASE.put(pluCode, pluProduct);
		
		PLUCodeWasEntered = false;
		PLUCodeEntered = "";
	}
	
	/**
	 * Press the add button, expecting input text field to be parsed for
	 * a valid PLU code, and notifying a listener that an item was added.
	 */
	@Test
	public void testAddButtonValidPLUCode() {
		pluView.addObserver(new PLUViewObserverStub());
		JTextField textField = pluView.getInputField();
		// Set text field to match the PLU Code of the test product
		textField.setText("1111");
		pluView.getAddButton().doClick();
		assertEquals("1111", PLUCodeEntered);
		assertTrue(PLUCodeWasEntered);
	}
	
	/**
	 * Press the add button, expecting input text field to be parsed for
	 * an invalid PLU code, no listener events
	 */
	@Test
	public void testAddButtonInvalidPLUCode() {
		pluView.addObserver(new PLUViewObserverStub());
		JTextField textField = pluView.getInputField();
		textField.setText(null);
		pluView.getAddButton().doClick();
		assertFalse(PLUCodeWasEntered);
	}
	
	
	
	/*------------------------- Stubs ------------------------*/
	
	/**Stubs primarily check if/how many times observer events occurred.
	 * Tests should fail if an unexpected event is reported.
	 * Override any event in this stub that you don't want to fail.
	 */
	public class CustomerUIEventListenerStub implements CustomerUIEventListener {
		@Override
		public void onStartAddingItems() {}

		@Override
		public void onStartPaying() {}

		@Override
		public void onSelectAddItemByPLU() {}

		@Override
		public void onSelectAddItemByBrowsing() {}

		@Override
		public void onStartAddingOwnBags() {	}

		@Override
		public void onFinishAddingOwnBags() {}

		@Override
		public void onAddMembershipNumber() {}

		@Override
		public void onPurchaseBags(int amount) {	}

		@Override
		public void goBackToCheckout() {}

		@Override
		public void onSelectPaymentMethod(String payment) {	}

		@Override
		public void onBagApproval(int stationNumber) {	}

		@Override
		public void onBagRefusal(int stationNumber) {}

	}
	
	public class PLUViewObserverStub implements PLUViewObserver {
		@Override
		public void reactToPLUCodeEntered(String pluCode) {
			PLUCodeWasEntered = true;
			PLUCodeEntered = pluCode;
			
		}
	}
	
}
