
package com.autovend.software;

import java.math.BigDecimal;

import com.autovend.products.Product;

public class Bag extends Product {
	private final String description;
	private final double bagWeight;
	public static BigDecimal price = new BigDecimal("0.25");

	public Bag(String description, BigDecimal price, double expectedWeight) {
		super(price, true);
		if (price == null || expectedWeight <= 0 || description == null){
			throw new IllegalArgumentException("Parameters cannot be null");
		}
		this.bagWeight = expectedWeight;
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public double getWeight() {
		return bagWeight;
	}


}



