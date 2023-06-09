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
import java.util.List;

import com.autovend.devices.SelfCheckoutStation;
import com.autovend.products.BarcodedProduct;
import com.autovend.products.PLUCodedProduct;
import com.autovend.products.Product;
import com.autovend.software.AbstractFacade;
import com.autovend.software.ui.CustomerView;
import com.autovend.software.ui.PLUView;

@SuppressWarnings("serial")
public class ItemFacade extends AbstractFacade<ItemEventListener> {
	private static ItemFacade instance;
	private List<ItemFacade> children;
	private static List<Product> itemList;

	public ItemFacade(SelfCheckoutStation station, CustomerView customerView, boolean isChild) {
		super(station, customerView);

		// Initialize this instance and children once.

		if (!isChild) {
			itemList = new ArrayList<Product>();
			children = new ArrayList<ItemFacade>();
			children.add(new ByScanning(station, customerView));
			children.add(new ByBrowsing(station, customerView));
			children.add(new ByPLUCode(station, customerView));
			children.add(new ByTextSearch(station, customerView));
		}
	}

	/**
	 * @return List of active subclasses.
	 */
	public List<ItemFacade> getChildren() {
		return children;
	}

	/**
	 * @return This current active instance of this class. Could be null.
	 */
	public static ItemFacade getInstance() {
		return instance;
	}

}
