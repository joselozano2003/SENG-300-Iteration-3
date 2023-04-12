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

import com.autovend.devices.SelfCheckoutStation;
import com.autovend.products.Product;
import com.autovend.software.ui.CustomerView;

public class ByTextSearch extends ItemFacade {

	public ByTextSearch(SelfCheckoutStation station, CustomerView customerView) {
		super(station, customerView, true);
	}
	
	/**
	 * Method that takes in a string of what the attendant typed in as input for the search, and finds products that match the input keywords.
	 * 
	 * @param attendantInputString: string that the attendant types in, and searches for any products that match the input. 
	 * @return: an arraylist of products that are a match, which the attendant station displays as the search result 
	 */
	public ArrayList getProductsCorrespondingToTextSearch(String attendantInputString) {
		ArrayList<Product> productsToShow = new ArrayList<Product>();
		String[] InputStringWords = attendantInputString.split(" ");

		for(String keywords: ProductsDatabase2.Products_Textsearch_Keywords_Database.keySet()) {
			for(String eachWord: InputStringWords) {
				if(keywords.contains(eachWord) && !productsToShow.contains(ProductsDatabase2.Products_Textsearch_Keywords_Database.get(keywords))){
					productsToShow.add(ProductsDatabase2.Products_Textsearch_Keywords_Database.get(keywords));
				}
			}
		}

		return productsToShow;
	}

	/**
	 * Method is called when the customer gui detects an attendant selecting a product from text search to be added to a customer's session.
	 *
	 * @param product: product the attendant selected from text search
	 */
	public void productFromTextSearchSelected(Product product) {
		if(product != null) {
			for (ItemEventListener listener : listeners)
				listener.onItemAddedEvent(product, 1);;
		}
	}

}
