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

import com.autovend.BarcodedUnit;
import com.autovend.PriceLookUpCodedUnit;
import com.autovend.SellableUnit;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.products.BarcodedProduct;
import com.autovend.products.PLUCodedProduct;
import com.autovend.products.Product;
import com.autovend.software.GUI.MainScreen;
import com.autovend.software.GUI.StartScreen;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CustomerSession {
	private Map<Product, Double> shoppingCart;
	private double expectedWeight;
	private BigDecimal totalCost;
	private BigDecimal totalPaid;
	private boolean receiptPrinted;
	private boolean addingItems;
	private SelfCheckoutStation station;
	private ArrayList<SellableUnit> barcodedProduct;
	private ArrayList<SellableUnit> pluCodedProduct;
	private ArrayList<SellableUnit> barcodedProductInBaggingArea;
	private ArrayList<SellableUnit> pluCodedProductInBaggingArea;
	private ArrayList<SellableUnit> personalBags;
	private String receipt;
	BigDecimal amountLeft;

	public CustomerSession() {
		shoppingCart = new HashMap<>();
		expectedWeight = 0.0;
		totalCost = BigDecimal.ZERO;
		totalPaid = BigDecimal.ZERO;
		receiptPrinted = false;
		station = null;
		addingItems = false;

		barcodedProduct  = new ArrayList<SellableUnit>();
		pluCodedProduct = new ArrayList<SellableUnit>();

		barcodedProductInBaggingArea = new ArrayList<SellableUnit>();
		pluCodedProductInBaggingArea = new ArrayList<SellableUnit>();
		personalBags = new ArrayList<SellableUnit>();
		BigDecimal amountLeft = getAmountLeft();





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
	    else if (product instanceof PLUCodedProduct pluCodedProduct) {
			expectedWeight += quantityToAdd; // Assuming quantityToAdd represents the weight for PLUCodedProduct
	        totalCost = totalCost.add(pluCodedProduct.getPrice().multiply(BigDecimal.valueOf(quantityToAdd)));
	    }
	    
	}

	public void addPayment(BigDecimal amount) {
		totalPaid = totalPaid.add(amount);
	}

	public void setPrintStatus(boolean status) {
		receiptPrinted = status;
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






	//GUI
	public void resetCart(){
		barcodedProduct.clear();
		pluCodedProduct.clear();
		for (SellableUnit i : barcodedProductInBaggingArea){
			station.baggingArea.remove(i);
		}
		for(SellableUnit i: pluCodedProductInBaggingArea){
			station.baggingArea.remove(i);
		}
		personalBags.clear();
		totalCost = new BigDecimal("0");
		receipt = null;
		totalPaid = new BigDecimal("0");
		amountLeft = new BigDecimal("0");





	}





	public void loadStartGUI(){
		JFrame jFrame = station.screen.getFrame();
		station.screen.setVisible(false);
		jFrame.getContentPane().removeAll();
		jFrame.setLayout(new BorderLayout());
		StartScreen startScreen = new StartScreen(this);
		JPanel newPanel = new JPanel(new GridBagLayout());
		newPanel.setBackground(new Color(65,73,96));
		newPanel.add(startScreen);
		jFrame.getContentPane().add(startScreen);
		jFrame.validate();
		jFrame.repaint();
		station.screen.setVisible(true);
		resetCart();
		addingItems = false;

	}


	public void loadMainGUI(){
		JFrame frame = station.screen.getFrame();
		station.screen.setVisible(false);
		frame.getContentPane().removeAll();
		frame.setLayout(new BorderLayout());
		MainScreen mainScreen = new MainScreen();
		JPanel container  = new JPanel(new GridBagLayout());
		container.setPreferredSize(frame.getSize());
		container.setBackground(new Color(9,11,16));
		container.add(mainScreen);
		frame.getContentPane().add(container);
		frame.validate();
		frame.repaint();

		station.screen.setVisible(true);


	}


}
