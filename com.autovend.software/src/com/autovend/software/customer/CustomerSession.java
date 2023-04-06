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

import com.autovend.devices.SelfCheckoutStation;
import com.autovend.products.BarcodedProduct;
import com.autovend.products.Product;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class CustomerSession {
	private Map<Product, Double> shoppingCart;
	private double expectedWeight;
	private BigDecimal totalCost;
	private BigDecimal totalPaid;
	private boolean receiptPrinted;

	public CustomerSession() {
		shoppingCart = new HashMap<>();
		expectedWeight = 0.0;
		totalCost = BigDecimal.ZERO;
		totalPaid = BigDecimal.ZERO;
		receiptPrinted = false;
	}

	public void addItemToCart(Product product, double quantityToAdd) {
		if (shoppingCart.containsKey(product)) {
			double currentQuantity = shoppingCart.get(product);
			shoppingCart.put(product, currentQuantity + quantityToAdd);
		} else {
			shoppingCart.put(product, quantityToAdd);
		}

		totalCost = totalCost.add(product.getPrice().multiply(BigDecimal.valueOf(quantityToAdd)));

		if (product instanceof BarcodedProduct) {
			expectedWeight += ((BarcodedProduct) product).getExpectedWeight();
		} else if (product instanceof BarcodedProduct) {
			expectedWeight += quantityToAdd;
		}
	}

	public void addPayment(BigDecimal amount) {
		totalPaid = totalPaid.add(amount);
	}

	public boolean isReceiptPrinted() {
		return receiptPrinted;
	}

	public Map getShoppingCart() {
		return shoppingCart;
	}

	public double getExpectedWeight() {
		return expectedWeight;
	}

	public BigDecimal getTotalCost() {
		return totalCost;
	}

	public BigDecimal getTotalPaid() {
		return totalPaid;
	}

	public BigDecimal getAmountLeft() {
		return totalCost.subtract(totalPaid);
	}

}
