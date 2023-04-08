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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;

import com.autovend.devices.SelfCheckoutStation;
import com.autovend.products.Product;

public class ByTextSearch extends ItemFacade {
	
	//map with keywords that correspond to a product
	//will need to populate this map with all products that we want the attendant to be able to search up
	public static Map<String, Product> Products_Textsearch_Keywords_Database = new HashMap<>();

	protected ByTextSearch(SelfCheckoutStation station) {
		super(station);
	}
	
	/**
	 * Method that takes in a string of 
	 * 
	 * @param attendantInputString: string that the attendant types in, and searches for any products that match the input. 
	 * @return: an arraylist of products that are a match, which the attendant station displays as the search result 
	 */
	public ArrayList getProductsCorrespondingToTextSearch(String attendantInputString) {
		ArrayList<Product> productsToShow = new ArrayList<Product>();
		String[] InputStringWords = attendantInputString.split(" ");
		
		for(String keywords: Products_Textsearch_Keywords_Database.keySet()) {
			for(String eachWord: InputStringWords) {
				if(keywords.contains(eachWord) && !productsToShow.contains(Products_Textsearch_Keywords_Database.get(keywords))){
					productsToShow.add(Products_Textsearch_Keywords_Database.get(keywords));
				}
			}
		}
		
		return productsToShow;
	}

}
