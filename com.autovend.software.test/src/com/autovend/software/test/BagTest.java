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
package com.autovend.software.test;

import static org.junit.Assert.*;
import java.math.BigDecimal;
import org.junit.*;
import com.autovend.devices.SimulationException;
import com.autovend.software.*;
public class BagTest {
	Bag bag;
    Bag reusableBagSmall;
    Bag reusableBagMedium;
    Bag reusableBagLarge;

    Bag disposableBagSmall;
    Bag disposableBagMedium;
    Bag disposableBagLarge;
	//sets up a Bag for testing
	@Before
	public void setup() {
        reusableBagSmall = new Bag("Small Reusable Bag", new BigDecimal("0.25"), 0.1);
        reusableBagMedium = new Bag("Medium Reusable Bag", new BigDecimal("0.50"), 0.2);
        reusableBagLarge = new Bag("Large Reusable Bag", new BigDecimal("1"), 0.3);

        disposableBagSmall = new Bag("Small Disposable Bag", new BigDecimal("0.25"), 0.1);
        disposableBagMedium = new Bag("Medium Disposable Bag", new BigDecimal("0.50"), 0.2);
        disposableBagLarge = new Bag("Large Disposable Bag", new BigDecimal("1"), 0.3);
	}
	
	@After
	public void tearDown() {
		bag = null;
		reusableBagLarge = null;
		reusableBagMedium = null;
		reusableBagSmall = null;
		disposableBagLarge = null;
		disposableBagMedium = null;
		disposableBagSmall = null;
		PurchasedItems.reset();
	}
	
	// tests if the bag was constructed correctly
	@Test
	public void validBagConstructorTest() {
        assertEquals(reusableBagSmall.getDescription(), "Small Reusable Bag");
        assertEquals(reusableBagSmall.getPrice(), new BigDecimal("0.25"));
        assertEquals(reusableBagSmall.getWeight(), 0.1, 0.0001);

		assertEquals(reusableBagMedium.getDescription(), "Medium Reusable Bag");
		assertEquals(reusableBagMedium.getPrice(), new BigDecimal("0.50"));
		assertEquals(reusableBagMedium.getWeight(), 0.2, 0.0001);

		assertEquals(reusableBagLarge.getDescription(), "Large Reusable Bag");
		assertEquals(reusableBagLarge.getPrice(), new BigDecimal("1"));
		assertEquals(reusableBagLarge.getWeight(), 0.3, 0.0001);

		assertEquals(disposableBagSmall.getDescription(), "Small Disposable Bag");
		assertEquals(disposableBagSmall.getPrice(), new BigDecimal("0.25"));
		assertEquals(disposableBagSmall.getWeight(), 0.1, 0.0001);

		assertEquals(disposableBagMedium.getDescription(), "Medium Disposable Bag");
		assertEquals(disposableBagMedium.getPrice(), new BigDecimal("0.50"));
		assertEquals(disposableBagMedium.getWeight(), 0.2, 0.0001);

		assertEquals(disposableBagLarge.getDescription(), "Large Disposable Bag");
		assertEquals(disposableBagLarge.getPrice(), new BigDecimal("1"));
		assertEquals(disposableBagLarge.getWeight(), 0.3, 0.0001);
    }

	@Test
	public void invalidBagConstructorTest() {
		boolean failed = false;
		try {
			bag = new Bag(null, new BigDecimal("0.50"), 5);
		} catch (IllegalArgumentException e) {
			failed = true;
		}
		assertTrue(failed);

		failed = false;
		try {
			bag = new Bag("test bag", null, 5);
		} catch (SimulationException e) {
			failed = true;
		}
		assertTrue(failed);

		failed = false;

		try {
			bag = new Bag("test bag", new BigDecimal("0.50"), -5);
		} catch (IllegalArgumentException e) {
			failed = true;
		}
		assertTrue(failed);
	}

}
