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

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;

import com.autovend.products.Product;

public class ProductsDatabase2 {

	//Database for products shown on the visual catalogue and their corresponding JButton.
	//The products displayed in the visual catalogue does not have a barcode or PLU code available to the customer. 
	//Type BarcodedProduct is used for convenience, but the barcode is random and does not mean anything since these are not actually barcoded products.
	public static Map<JButton, Product> Products_In_Visual_Catalogue_Database = new HashMap<>();
	
	//map with keywords that correspond to a product
	//will need to populate this map with all products that we want the attendant to be able to search up
	public static Map<String, TextSearchProduct> Products_Textsearch_Keywords_Database = new HashMap<>();
	
	//cost of a reusable bag
	public static BigDecimal costOfReusableBag;
	
	/**
	 * Sets the cost of a reusable bag. 
	 * This method has to be called and price has to be set before bags can be purchased.
	 * 
	 * @param cost: cost of a reusable bag
	 */
	public static void setPriceOfReusableBag(BigDecimal cost) {
		costOfReusableBag = cost;
	}
	
	public static BigDecimal getPriceOfReusableBag() {
		return costOfReusableBag;
	}
	

}
