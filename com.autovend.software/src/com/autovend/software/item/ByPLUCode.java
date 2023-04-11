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

import com.autovend.Numeral;
import com.autovend.PriceLookUpCode;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.external.ProductDatabases;
import com.autovend.products.PLUCodedProduct;
import com.autovend.software.ui.CustomerView;
import com.autovend.software.ui.PLUView;
import com.autovend.software.ui.PLUViewObserver;

public class ByPLUCode extends ItemFacade implements PLUViewObserver {

	protected ByPLUCode(SelfCheckoutStation station, CustomerView customerView) {
		super(station, customerView, true);
		customerView.pluView.addObserver(this);
	}

	@Override
	public void reactToPLUCodeEntered(String pluCode, double quantity) {
		processPLUInput(pluCode, quantity);

	}

	public void processPLUInput(String stringToProcess, double quantity) {
		Numeral[] numerals = new Numeral[stringToProcess.length()];

		for (int i = 0; i < stringToProcess.length(); i++) {
			byte number = Byte.parseByte(Character.toString(stringToProcess.charAt(i)));
			Numeral numeral = Numeral.valueOf(number);
			numerals[i] = numeral;
		}

		PLUCodedProduct pluCodedProduct = ProductDatabases.PLU_PRODUCT_DATABASE.get(new PriceLookUpCode(numerals));

		if (pluCodedProduct.getPLUCode() != null) {
			for (ItemEventListener listener : listeners)
				listener.onItemAddedEvent(pluCodedProduct, quantity);
		} else {
			for (ItemEventListener listener : listeners)
				listener.onItemNotFoundEvent();
		}

	}

}
