package com.autovend.software.item;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;

import com.autovend.products.BarcodedProduct;
import com.autovend.products.Product;

public class ProductsDatabase2 {

	//Database for products shown on the visual catalogue and their corresponding JButton.
	//The products displayed in the visual catalogue does not have a barcode or PLU code available to the customer. 
	//Type BarcodedProduct is used for convenience, but the barcode is random and does not mean anything since these are not actually barcoded products.
	public static Map<JButton, Product> Products_In_Visual_Catalogue_Database = new HashMap<>();
	
	//map with keywords that correspond to a product
	//will need to populate this map with all products that we want the attendant to be able to search up
	public static Map<String, Product> Products_Textsearch_Keywords_Database = new HashMap<>();
	
	public static BigDecimal costOfReusableBag;
	
	public static void setPriceOfReusableBag(BigDecimal cost) {
		costOfReusableBag = cost;
	}
	

}
