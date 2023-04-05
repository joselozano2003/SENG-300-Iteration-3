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