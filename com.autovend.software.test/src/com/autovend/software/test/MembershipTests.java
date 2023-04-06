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

import java.math.BigDecimal;
import java.util.Currency;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.autovend.devices.SelfCheckoutStation;
import com.autovend.software.membership.MembershipFacade;

public class MembershipTests {
	SelfCheckoutStation station;
	MembershipFacade mem;

	@Before
	public void setup() {
		//Setup and create station
		int[] billDenoms = {5, 10 , 15, 20, 50, 100};
		BigDecimal[] coinDenoms = {new BigDecimal("0.05"), new BigDecimal("0.10"), 
				new BigDecimal("0.25"), new BigDecimal("1"), new BigDecimal("2")};
		int scaleMaximumWeight = 50;
		int scaleSensitivity = 10;
		station = new SelfCheckoutStation(Currency.getInstance("CAD"),
				billDenoms, coinDenoms, scaleMaximumWeight, scaleSensitivity);
		mem = new MembershipFacade(station);
	}

	@After
	public void teardown() {
		mem = null;
	}

	@Test
	public void test() {
		mem.register(null);
	}
	
//	/*
//	 * testing if a membershipcard of 10 digits is valid
//	 */
//	@Test
//	public void testMembershipisValid() throws Exception {
//		String numberETen = "1234567890";
//		mem.isValid(numberETen);
//	}
//
//	/*
//	 * testing if a membershipcard of less than 10 digits is invalid
//	 */
//	@Test(expected = Exception.class)
//	public void testMembershipinValidLessthan10() throws Exception {
//		String numberLTTen = "123456789";
//		mem.isValid(numberLTTen);
//	}
//
//	/*
//	 * testing if a membershipcard of greater than 10 digits is invalid
//	 */
//	@Test(expected = Exception.class)
//	public void testMembershipinValidGreaterthan10() throws Exception {
//		String numberGTTen = "12345678900";
//		mem.isValid(numberGTTen);
//	}
//
//	/*
//	 * testing if a membershipcard of an invalid digit such as -1 is invalid while
//	 * still being 10 digits
//	 */
//	@Test(expected = Exception.class)
//	public void testMembershipinValidOnlydigitAbove9() throws Exception {
//		String numberGTTen = "-123456789";
//		mem.isValid(numberGTTen);
//	}
//
//	/*
//	 * testing if a membershipcard of an invalid digit such as 19 is invalid while
//	 * still being 10 digits
//	 */
//	@Test(expected = Exception.class)
//	public void testMembershipinValidOnlydigitBelow0() throws Exception {
//		String numberGTTen = "0123456 19";
//		mem.isValid(numberGTTen);
//	}

}
