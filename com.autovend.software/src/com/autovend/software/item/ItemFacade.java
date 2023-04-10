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

@SuppressWarnings("serial")
public class ItemFacade extends AbstractFacade<ItemEventListener> {
	private static ItemFacade instance;
	private List<ItemFacade> children;
	private static List<Product> itemList;

	public ItemFacade(SelfCheckoutStation station, boolean isChild) {
		super(station);
		// Initialize this instance and children once.

		if (!isChild || instance == null) {
			instance = this;
			itemList = new ArrayList<Product>();
			children = new ArrayList<ItemFacade>();
			children.add(new ByScanning(station));
			children.add(new ByBrowsing(station));
			children.add(new ByPLUCode(station));
			children.add(new ByTextSearch(station));
		}
	}

	public void addProduct(Product product) {
		if (product != null)
			itemList.add(product);
	}

	public boolean removeProduct(Product removedProduct) {
		if (removedProduct != null) {
			if (removedProduct instanceof BarcodedProduct) {
				for (Product product : itemList) {
					if (product instanceof BarcodedProduct) {
						if (((BarcodedProduct) product).getBarcode()
								.equals(((BarcodedProduct) removedProduct).getBarcode())
								&& ((BarcodedProduct) product).getDescription()
										.equals(((BarcodedProduct) removedProduct).getDescription())
								&& ((BarcodedProduct) product)
										.getExpectedWeight() == (((BarcodedProduct) removedProduct).getExpectedWeight())
								&& ((BarcodedProduct) product).getPrice()
										.equals(((BarcodedProduct) removedProduct).getPrice())) {
							itemList.remove(product);
							return true;
						}
					}
				}
			} else if (removedProduct instanceof PLUCodedProduct) {
				for (Product product : itemList) {
					if (product instanceof PLUCodedProduct) {
						if (((PLUCodedProduct) product).getPLUCode()
								.equals(((PLUCodedProduct) removedProduct).getPLUCode())
								&& ((PLUCodedProduct) product).getDescription()
										.equals(((PLUCodedProduct) removedProduct).getDescription())
								&& ((PLUCodedProduct) product).getPrice()
										.equals(((PLUCodedProduct) removedProduct).getPrice())) {
							itemList.remove(product);
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	public List<Product> getItemList() {
		return itemList;
	}

	/**
	 * @return The listeners relative to the parent instance.
	 */
	protected ArrayList<ItemEventListener> getListeners() {
		return instance.listeners;
	}

}
