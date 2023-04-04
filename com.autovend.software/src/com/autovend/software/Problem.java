
package com.autovend.software;

import java.util.ArrayList;
import java.math.BigDecimal;
public class Problem{
	public ArrayList<BigDecimal> set;
	public BigDecimal sum;
	
	public Problem(ArrayList<BigDecimal> inputList, BigDecimal inputSum)
	{
		set = (ArrayList)inputList.clone();
		sum = inputSum;
	}
}