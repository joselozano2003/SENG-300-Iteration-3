package com.autovend.software.ach.virtual;
/*
Group Members: 
- Achraf Abdelrhafour: (30022366)
- Marianna Ferreira (30147733)
- Ryan Chrumka (30144174)
- Alireza vafisani  (30150496)
- Ali Savab Pour (30154744) 
- Aryan Nambiar (30140671)
- Shijia Wang (30018276)
- Carson Bergen (30127827)
- Md Abdullah Mehedi Patwary (30154770)
- Vita Vysochina (30118374)
- Michael Kacmar, (30113919)
- Deepshikha Dhammi (30140157) 
*/

import com.autovend.Barcode;
import com.autovend.BarcodedUnit;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.SimulationException;
import com.autovend.external.ProductDatabases;
import com.autovend.products.BarcodedProduct;

/*
    Virtual bag dispenser that can be controlled by another class.
*/
public class VirtualBagDispenser {
    private SelfCheckoutStation station;
    private int bagsInSystem = 10;
    private Barcode reusableBagBarcode;
    public boolean canDispenseBag = true;

    /*
        Basic constructor that takes in a SelfCheckoutStation
        @station
                Reference to a self checkout station, so that we dispense things to the bagging area.
    */
    public VirtualBagDispenser(SelfCheckoutStation station) {
        this.station = station;
    }

    /*
        Basic constructor that takes in a SelfCheckoutStation and an amount of bags in the system.
        @station
                Reference to a self checkout station, so that we dispense things to the bagging area.
        @bagsInSystem
                Number of bags that the bag dispenser should have in its storage.
    */
    public VirtualBagDispenser(SelfCheckoutStation station, int bagsInSystem) {
        this.station = station;
        this.bagsInSystem = bagsInSystem;
    }

    /*
        Updates the known barcode for a reusable bag.
        @param barcode
                The barcode to update to.
    */
    public void updateReusableBagBarcode(Barcode barcode) {
        this.reusableBagBarcode = barcode;
    }

    /*
        Gets the number of bags in the system. 
    */
    public int getBagsInSystem() {
    	return bagsInSystem;
    }
    
    /*
        Adds amountToAdd bags to the dispenser
        @param amountToAdd
                The number of bags to add
    */
    public void addBags(int amountToAdd) {
        bagsInSystem += amountToAdd;
    }

    /*
        Removes amountToRemove bags from the dispenser
        @param amountToRemove
                The number of bags to remove
    */
    public void removeBags(int amountToRemove) {
        bagsInSystem -= amountToRemove;
        if (bagsInSystem < 0) bagsInSystem = 0;
    }

    /*
        Dispenses a bag to the bagging area.
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