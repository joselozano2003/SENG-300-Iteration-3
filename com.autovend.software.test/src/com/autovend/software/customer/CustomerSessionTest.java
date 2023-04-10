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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import com.autovend.ReusableBag;
import com.autovend.products.BarcodedProduct;
import com.autovend.products.PLUCodedProduct;
import com.autovend.products.Product;
import com.autovend.software.item.ProductsDatabase2;
import com.autovend.software.test.Setup;

public class CustomerSessionTest {
	
	private CustomerSession session;
	private BarcodedProduct barcodedProduct;
	
	
	@Before
	public void setUp() throws Exception {
		session = new CustomerSession();
		barcodedProduct = Setup.createBarcodedProduct123(12, 5, true);
	}


	@Test
	public void isPaymentCompleteTest() {
		CustomerSession session = new CustomerSession();
		session.addPayment(BigDecimal.valueOf(20));
		session.addPayment(BigDecimal.valueOf(15));
		
		// Check if payment is marked as complete
		assertTrue(session.isPaymentComplete());
		
		// Add another payment and check if payment is still complete
		session.addPayment(BigDecimal.valueOf(5));
		assertTrue(session.isPaymentComplete());
		
		// Add payment less than the total cost and check if payment is incomplete
		session.addPayment(BigDecimal.valueOf(10));
		assertFalse(session.isPaymentComplete());
	}

	}