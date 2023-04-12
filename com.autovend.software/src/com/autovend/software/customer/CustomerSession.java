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

import com.autovend.ReusableBag;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.products.BarcodedProduct;
import com.autovend.products.PLUCodedProduct;
import com.autovend.products.Product;
import com.autovend.software.bagging.ReusableBagProduct;
import com.autovend.software.item.ProductsDatabase2;
import com.autovend.software.item.TextSearchProduct;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CustomerSession {
	private Map<Product, Double> shoppingCart;
	private double expectedWeight;
	private BigDecimal totalCost;
	private BigDecimal totalPaid;
	private Map<Product, Double> heavyItems;

	public CustomerSession() {
		shoppingCart = new HashMap<>();
		expectedWeight = 0.0;
		totalCost = BigDecimal.ZERO;
		totalPaid = BigDecimal.ZERO;
		heavyItems = new HashMap<>();
	}

	public void removeItemFromCart(Product product, double quantityToRemove){
		boolean itemIsHeavy = false;
		if (heavyItems.containsKey(product)) {
			itemIsHeavy = true;
		}

		if (product instanceof BarcodedProduct) {
			for (Product p : shoppingCart.keySet()){
				if (p instanceof BarcodedProduct){
					BarcodedProduct barcodedProduct = (BarcodedProduct) p;
					if (barcodedProduct.getBarcode() == ((BarcodedProduct) product).getBarcode()){
						if (!itemIsHeavy) {
							expectedWeight -= barcodedProduct.getExpectedWeight() * quantityToRemove;
						}
						shoppingCart.put(p, shoppingCart.get(p) - quantityToRemove);
						totalCost = totalCost.subtract(barcodedProduct.getPrice().multiply(BigDecimal.valueOf(quantityToRemove)));
						break;
					}
				}
			}
		}
		else if (product instanceof PLUCodedProduct) {
			for (Product p : shoppingCart.keySet()){
				if (p instanceof PLUCodedProduct){
					PLUCodedProduct pluCodedProduct = (PLUCodedProduct) p;
					if (pluCodedProduct.getPLUCode() == ((PLUCodedProduct) product).getPLUCode()){
						shoppingCart.put(p, shoppingCart.get(p) - quantityToRemove);
						if (!itemIsHeavy) {
							expectedWeight -= quantityToRemove;
						}
						totalCost = totalCost.subtract(pluCodedProduct.getPrice().multiply(BigDecimal.valueOf(quantityToRemove)));
						break;
					}
				}
			}
		}
		else if (product instanceof ReusableBagProduct) {
			for (Product p : shoppingCart.keySet()){
				if (p instanceof ReusableBagProduct){
					shoppingCart.put(p, shoppingCart.get(p) - quantityToRemove);
					totalCost = totalCost.subtract(((ReusableBagProduct) p).getPrice().multiply(BigDecimal.valueOf(quantityToRemove)));
					break;
				}
			}
		}
		else if (product instanceof TextSearchProduct) {
			for (Product p : shoppingCart.keySet()){
				if (p instanceof TextSearchProduct){
					TextSearchProduct textSearchProduct = (TextSearchProduct) p;
					if (textSearchProduct.getName().equals(((TextSearchProduct) product).getName())){
						shoppingCart.put(p, shoppingCart.get(p) - quantityToRemove);
						if (!itemIsHeavy) {
							expectedWeight -= textSearchProduct.getExpectedWeight() * quantityToRemove;
						}
						totalCost = totalCost.subtract(textSearchProduct.getPrice().multiply(BigDecimal.valueOf(quantityToRemove)));
						break;
					}
				}
			}
		}
	}

	public void addItemToCart(Product product, double quantityToAdd) {
		// Checks if item has been previously added to cart
		if (shoppingCart.containsKey(product)) {
			double updatedQuantity = shoppingCart.get(product) + quantityToAdd;
			shoppingCart.put(product, updatedQuantity);
		} else {
			shoppingCart.put(product, quantityToAdd);
		}

		// Checks if product is barcoded
		if (product instanceof BarcodedProduct) {
			BarcodedProduct barcodedProduct = (BarcodedProduct) product;
			expectedWeight += barcodedProduct.getExpectedWeight() * quantityToAdd;
			totalCost = totalCost.add(barcodedProduct.getPrice().multiply(BigDecimal.valueOf(quantityToAdd)));
		}
		// Checks if product is PLU coded
		else if (product instanceof PLUCodedProduct) {
			PLUCodedProduct pluCodedProduct = (PLUCodedProduct) product;
			expectedWeight += quantityToAdd; // Assuming quantityToAdd represents the weight for PLUCodedProduct
			totalCost = totalCost.add(pluCodedProduct.getPrice().multiply(BigDecimal.valueOf(quantityToAdd)));
		}

		// Checks if product is a bag product
		else if (product instanceof ReusableBagProduct) {
			ReusableBagProduct bagProduct = (ReusableBagProduct) product;
			expectedWeight += bagProduct.getExpectedWeight() * quantityToAdd;
			totalCost = totalCost.add(bagProduct.getPrice().multiply(BigDecimal.valueOf(quantityToAdd)));
			
		}
		else if (product instanceof TextSearchProduct) {
			TextSearchProduct textSearchProduct = (TextSearchProduct) product;
			expectedWeight += textSearchProduct.getExpectedWeight() * quantityToAdd;
			totalCost = totalCost.add(textSearchProduct.getPrice().multiply(BigDecimal.valueOf(quantityToAdd)));
		}
	}
		
	public void addHeavyItem(BarcodedProduct item) {
		if (heavyItems.containsKey(item)) {
			double updatedQuantity = shoppingCart.get(item) + 1;
			heavyItems.put(item, updatedQuantity);
		} else {
			heavyItems.put(item, 1.0);
		}
		expectedWeight -= item.getExpectedWeight();

	}
	
	public void addPayment(BigDecimal amount) {
		totalPaid = totalPaid.add(amount);
	}

	public Map<Product, Double> getShoppingCart() {
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

	public BigDecimal getChangeDue() {
		return totalPaid.subtract(totalCost);
	}

	public boolean isPaymentComplete() {
		if (totalPaid.compareTo(totalCost) >= 0) {
			return true;
		} else {
			return false;
		}
	}

}
