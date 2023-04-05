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
package com.autovend.software.ach.virtual;

import com.autovend.Barcode;
import com.autovend.BarcodedUnit;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.SimulationException;
import com.autovend.external.ProductDatabases;
import com.autovend.products.BarcodedProduct;

/*
 * Virtual bag dispenser that can be controlled by another class.
 */
public class VirtualBagDispenser {
	private SelfCheckoutStation station;
	private int bagsInSystem = 10;
	private Barcode reusableBagBarcode;
	public boolean canDispenseBag = true;

	/*
	 * Basic constructor that takes in a SelfCheckoutStation
	 * 
	 * @station Reference to a self checkout station, so that we dispense things to
	 * the bagging area.
	 */
	public VirtualBagDispenser(SelfCheckoutStation station) {
		this.station = station;
	}

	/*
	 * Basic constructor that takes in a SelfCheckoutStation and an amount of bags
	 * in the system.
	 * 
	 * @station Reference to a self checkout station, so that we dispense things to
	 * the bagging area.
	 * 
	 * @bagsInSystem Number of bags that the bag dispenser should have in its
	 * storage.
	 */
	public VirtualBagDispenser(SelfCheckoutStation station, int bagsInSystem) {
		this.station = station;
		this.bagsInSystem = bagsInSystem;
	}

	/*
	 * Updates the known barcode for a reusable bag.
	 * 
	 * @param barcode The barcode to update to.
	 */
	public void updateReusableBagBarcode(Barcode barcode) {
		this.reusableBagBarcode = barcode;
	}

	/*
	 * Gets the number of bags in the system.
	 */
	public int getBagsInSystem() {
		return bagsInSystem;
	}

	/*
	 * Adds amountToAdd bags to the dispenser
	 * 
	 * @param amountToAdd The number of bags to add
	 */
	public void addBags(int amountToAdd) {
		bagsInSystem += amountToAdd;
	}

	/*
	 * Removes amountToRemove bags from the dispenser
	 * 
	 * @param amountToRemove The number of bags to remove
	 */
	public void removeBags(int amountToRemove) {
		bagsInSystem -= amountToRemove;
		if (bagsInSystem < 0)
			bagsInSystem = 0;
	}

	/*
	 * Dispenses a bag to the bagging area.
	 */
	public void dispenseBag() throws OutOfBagsException, SimulationException {
		if (reusableBagBarcode == null) {
			throw new SimulationException("Reusable bag barcode can't be null");
		}

		if (bagsInSystem <= 0) {
			throw new OutOfBagsException("Bag dispenser is out of bags");
		} else {
			if (canDispenseBag) {
				bagsInSystem--;
				BarcodedProduct product = ProductDatabases.BARCODED_PRODUCT_DATABASE.get(reusableBagBarcode);
				if (product == null) {
					throw new SimulationException("Reusable bag product not found in database");
				}
				BarcodedUnit unit = new BarcodedUnit(reusableBagBarcode, product.getExpectedWeight());
				station.baggingArea.add(unit);
			} else {
				throw new SimulationException("Can't dispense bag(s)");
			}
		}
	}
}