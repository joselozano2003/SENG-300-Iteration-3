
package com.autovend.software;

import java.util.Scanner;


public class Membership {

    //Membership number needs to be a string of length 10 and should only have digits from 0 to 9
    //this is the criteria for membership number to be considered valid
    public static void isValid(String membershipNumber) throws Exception {
    	if (membershipNumber.length() != 10) {
    		throw new Exception("Membership Number should be of 10 digits long");
    	}
        for (char c : membershipNumber.toCharArray()) {
            if(c < '0' || c > '9') {
                throw new Exception("Membership number should only contain digits from 0 to 9.");
            }
        }

    }
    
  }
    
