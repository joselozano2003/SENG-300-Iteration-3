package com.autovend.software.membership;

import java.util.ArrayList;

public class MemberShipDatabase {

/**
 * Represents a cheap and dirty version of a set of databases that the
 * simulation can interact with.
 */
 
	/**
	 * Instances of this class are not needed, so the constructor is private.
	 */
	private MemberShipDatabase() {}
	
	/**
	 * The known membership numbers of users.
	 */
	public static ArrayList<String> MEMBERSHIP_DATABASE = new ArrayList<String>();
	
	
	public static boolean userExists(String number) {
		for (String key: MEMBERSHIP_DATABASE) {
			if (number.equals(key)) {
				return true;
			} 
		}
		return false;
	}
	
	

}

