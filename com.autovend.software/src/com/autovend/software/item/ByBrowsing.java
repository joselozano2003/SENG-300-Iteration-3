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

import com.autovend.Numeral;
import com.autovend.PriceLookUpCode;
import com.autovend.devices.AbstractDevice;
import com.autovend.devices.ElectronicScale;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.ElectronicScaleObserver;
import com.autovend.external.ProductDatabases;
import com.autovend.products.PLUCodedProduct;
import com.autovend.products.Product;
import com.autovend.software.ui.BrowsingViewObserver;
import com.autovend.software.ui.CustomerView;
import com.autovend.software.ui.PLUViewObserver;

@SuppressWarnings("serial")
public class ByBrowsing extends ItemFacade implements BrowsingViewObserver, ElectronicScaleObserver {
	private Product currentSelectedProduct;

	protected ByBrowsing(SelfCheckoutStation station, CustomerView customerView) {
		super(station, customerView, true);
		station.scale.register(this);
		customerView.browsingView.addObserver(this);
	}

	/**
	 * Method is called when the customer gui detects a customer selecting an item
	 * from the visual catalogue
	 * 
	 * @param product: product the customer selected from the visual catalogue
	 */

	@Override
	public void reactToEnabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
		// TODO Auto-generated method stub

	}

	@Override
	public void reactToDisabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
		// TODO Auto-generated method stub

	}

	@Override
	public void reactToWeightChangedEvent(ElectronicScale scale, double weightInGrams) {
		if (weightInGrams > 0 && currentSelectedProduct != null)
			for (ItemEventListener listener : listeners)
				listener.onItemAddedEvent(currentSelectedProduct, weightInGrams);
		this.currentSelectedProduct = null;

	}

	@Override
	public void reactToOverloadEvent(ElectronicScale scale) {
		// TODO Auto-generated method stub

	}

	@Override
	public void reactToOutOfOverloadEvent(ElectronicScale scale) {
		// TODO Auto-generated method stub

	}

	@Override
	public void reactToProductSelected(Product product) {
		this.currentSelectedProduct = product;

	}

}
