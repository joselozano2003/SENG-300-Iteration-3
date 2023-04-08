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
package com.autovend.software.item;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;

import com.autovend.devices.SelfCheckoutStation;
import com.autovend.products.BarcodedProduct;
import com.autovend.products.Product;

@SuppressWarnings("serial")
public class ByBrowsing extends ItemFacade  {
	
	//Database for products shown on the visual catalogue and their corresponding JButton.
	//The products displayed in the visual catalogue does not have a barcode or PLU code available to the customer. 
	//Type BarcodedProduct is used for convenience, but the barcode is random and does not mean anything since these are not actually barcoded products.
	public static Map<JButton, BarcodedProduct> Products_In_Visual_Catalogue_Database = new HashMap<>();

	protected ByBrowsing(SelfCheckoutStation station) {
		super(station, true);
	}
	
	/**
	 * This method is called when a customer selects a product from the visual catalogue to add to their items to purchase.
	 * 
	 * @param product: product that the customer has selected from the visual catalogue
	 */
	public void addProductFromVisualCataloguToItemList(BarcodedProduct product) {
		if (product != null) {
        	addProduct(product); //add item to item list
			adjustExpectedWeight(product.getExpectedWeight()); //updated expected weight
			adjustTotalCost(product.getPrice()); //updated total cost of item list
			
			//announce event of an item being added
			for (ItemListener listener : listeners)
				listener.reactToItemAdded(product);
		} else {
        	//announce event of a null product is trying to be added
			for (ItemListener listener : listeners)
				listener.reactToNullProductEvent(product);
		}
	}
	
	
}
