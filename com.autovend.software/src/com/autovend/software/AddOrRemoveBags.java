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
package com.autovend.software;

import java.math.*;
import java.util.ArrayList;

import com.autovend.devices.*;

// extend the Parent class
public class AddOrRemoveBags extends AddItem{
    ArrayList<Bag> listOfStoreBags;

    public AddOrRemoveBags(SelfCheckoutStation station) {
        super(station);

        listOfStoreBags = new ArrayList<Bag>();

        Bag reusableBagSmall = new Bag("Small Reusable Bag", new BigDecimal("0.25"), 0.1);
        listOfStoreBags.add(reusableBagSmall);
        Bag reusableBagMedium = new Bag("Medium Reusable Bag", new BigDecimal("0.50"), 0.2);
        listOfStoreBags.add(reusableBagMedium);
        Bag reusableBagLarge = new Bag("Large Reusable Bag", new BigDecimal("1"), 0.3);
        listOfStoreBags.add(reusableBagLarge);
        Bag disposableBagSmall = new Bag("Small Disposable Bag", new BigDecimal("0.25"), 0.1);
        listOfStoreBags.add(disposableBagSmall);
        Bag disposableBagMedium = new Bag("Medium Disposable Bag", new BigDecimal("0.50"), 0.2);
        listOfStoreBags.add(disposableBagMedium);
        Bag disposableBagLarge = new Bag("Large Disposable Bag", new BigDecimal("1"), 0.3);
        listOfStoreBags.add(disposableBagLarge);
    }

    public void purchaseBag(Bag typeofBag) throws SimulationException {

        if (typeofBag == null ) {
            throw new SimulationException("Please select a valid bag type.");
        }
        for(Bag bag : listOfStoreBags) {
            if(bag.getWeight() == typeofBag.getWeight() && bag.getPrice().equals(typeofBag.getPrice()) && bag.getDescription().equals(typeofBag.getDescription())) {
                addBag(bag);
                return;
            }
        }
        throw new SimulationException("Please select a valid bag type.");
    }

    public void addOwnBag(double bagWeight) {
        Bag ownBag = new Bag("Own Bag", new BigDecimal(0.000001), bagWeight);
        addBag(ownBag);
        PurchasedItems.addAmountPaid(ownBag.getPrice()); // This is done to avoid the exception thrown by the addBag method as the price cannot be zero
    }
}
