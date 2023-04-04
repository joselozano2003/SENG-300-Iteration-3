
package com.autovend.software;

import java.math.*;
import java.util.ArrayList;

import com.autovend.devices.*;

// extend the Parent class
public class AddOrRemoveBags extends AddItem{
    ArrayList<Bag> listOfStoreBags;

    public AddOrRemoveBags(SelfCheckoutStation scs) {
        super(scs);

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
