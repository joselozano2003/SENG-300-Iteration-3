package com.autovend.software.bagging;

import java.math.BigDecimal;

import com.autovend.ReusableBag;
import com.autovend.products.Product;

public class ReusableBagProduct extends Product {

	private double expectedWeight;
	private static BigDecimal DEFAULT_PRICE = new BigDecimal("0.05");
	
	public ReusableBagProduct() {
		super(DEFAULT_PRICE, true);
		ReusableBag reusableBag = new ReusableBag();
		this.expectedWeight = reusableBag.getWeight();
	}

	public double getExpectedWeight() {
		return expectedWeight;
	}
	
	public void setPrice(BigDecimal newPrice) {
		DEFAULT_PRICE = newPrice;
	}

}
