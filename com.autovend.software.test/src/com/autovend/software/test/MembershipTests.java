
package com.autovend.software.test;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.autovend.software.Membership;
import com.autovend.software.PurchasedItems;

public class MembershipTests {

	
	Membership mem = new Membership();
	
	
@Before
public void setup() {
	
	Membership mem = new Membership();
	
}

@After
public void teardown() {
	mem = null;
}

/*
 * testing if a membershipcard of 10 digits is valid
 */
@Test
public void testMembershipisValid() throws Exception {
	String numberETen = "1234567890";
	mem.isValid(numberETen);		
	
}
/*
 * testing if a membershipcard of less than 10 digits is invalid
 */
@Test (expected = Exception.class)
public void testMembershipinValidLessthan10() throws Exception {
	String numberLTTen = "123456789";
	mem.isValid(numberLTTen);		
	
}
/*
 * testing if a membershipcard of greater than 10 digits is invalid
 */
@Test (expected = Exception.class)
public void testMembershipinValidGreaterthan10() throws Exception {
	String numberGTTen = "12345678900";
	mem.isValid(numberGTTen);		
	
}
/*
 * testing if a membershipcard of an invalid digit such as -1 is invalid while still being 10 digits
 */
@Test (expected = Exception.class)
public void testMembershipinValidOnlydigitAbove9() throws Exception {
	String numberGTTen = "-123456789";
	mem.isValid(numberGTTen);		
	
}
/*
 * testing if a membershipcard of an invalid digit such as 19 is invalid while still being 10 digits
 */
@Test (expected = Exception.class)
public void testMembershipinValidOnlydigitBelow0() throws Exception {
	String numberGTTen = "0123456 19";
	mem.isValid(numberGTTen);		
	
}



}
