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

import com.autovend.devices.SelfCheckoutStation;
import com.autovend.products.Product;
import com.autovend.software.customer.CustomerView;

@SuppressWarnings("serial")
public class ByBrowsing extends ItemFacade  {

	protected ByBrowsing(SelfCheckoutStation station, CustomerView customerView) {
		super(station, customerView, true);
	}
	
	/**
	 * Method is called when the customer gui detects a customer selecting an item from the visual catalogue
	 * 
	 * @param product: product the customer selected from the visual catalogue 
	 */
	public void productFromVisualCatalogueSelected(Product product) {
		if(product != null) {
        	for (ItemEventListener listener : listeners)
				listener.onItemAddedEvent(product, 1);;
		}
	}
	
	
}
