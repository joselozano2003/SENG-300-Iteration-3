
package com.autovend.software.test;


import static org.junit.Assert.*;
import java.math.BigDecimal;
import java.util.ArrayList;

import com.autovend.devices.SimulationException;
import org.junit.*;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.software.*;

public class AddOrRemoveBagsTest {
    AddOrRemoveBags addBags;
    SelfCheckoutStation scs;
    ArrayList<Bag> listOfStoreBags = new ArrayList<Bag>();
    Bag reusableBagSmall;
    Bag reusableBagMedium;
    Bag reusableBagLarge;
    Bag disposableBagSmall;
    Bag disposableBagMedium;
    Bag disposableBagLarge;
    //sets up for testing

    @Before
    public void setup() {
        reusableBagSmall = new Bag("Small Reusable Bag", new BigDecimal("0.25"), 0.1);
        listOfStoreBags.add(reusableBagSmall);
        reusableBagMedium = new Bag("Medium Reusable Bag", new BigDecimal("0.50"), 0.2);
        listOfStoreBags.add(reusableBagMedium);
        reusableBagLarge = new Bag("Large Reusable Bag", new BigDecimal("1"), 0.3);
        listOfStoreBags.add(reusableBagLarge);
        disposableBagSmall = new Bag("Small Disposable Bag", new BigDecimal("0.25"), 0.1);
        listOfStoreBags.add(disposableBagSmall);
        disposableBagMedium = new Bag("Medium Disposable Bag", new BigDecimal("0.50"), 0.2);
        listOfStoreBags.add(disposableBagMedium);
        disposableBagLarge = new Bag("Large Disposable Bag", new BigDecimal("1"), 0.3);
        listOfStoreBags.add(disposableBagLarge);
        addBags = new AddOrRemoveBags(scs);
    }
    @After
    public void tearDown() {
        addBags = null;
        PurchasedItems.reset();
    }
    //tests if the class is constructed correctly
    @Test
    public void AddOrRemoveBagsConstructorTest1() {
        boolean flag;
        try {
            AddOrRemoveBags testConstructor = new AddOrRemoveBags(scs);
            flag = true;
        }
        catch (Exception e) {
            flag = false;
        }
        assertTrue(flag);
    }
    //tests if a bag is added correctly
    @Test
    public void AddValidStoreBag() {
        addBags.purchaseBag(reusableBagSmall);
        addBags.purchaseBag(reusableBagMedium);
        addBags.purchaseBag(reusableBagLarge);

        assertEquals(3, PurchasedItems.getListOfBags().size());
    }
    @Test
    public void AddValidStoreBag2() {
        addBags.purchaseBag(disposableBagSmall);
        addBags.purchaseBag(disposableBagMedium);
        addBags.purchaseBag(disposableBagLarge);

        assertEquals(3, PurchasedItems.getListOfBags().size());
    }

    //tests if an invalid bag is added
    @Test (expected = SimulationException.class)
    public void AddInvalidStoreBag() {
        Bag invalidBag = new Bag("Invalid Bag", new BigDecimal("0.25"), 0.1);
        addBags.purchaseBag(invalidBag);
    }

    @Test (expected = SimulationException.class)
    public void AddInvalidStoreBag2() {
        Bag invalidBag = new Bag("Small Reusable Bag", new BigDecimal("0.25"), 0.4);
        addBags.purchaseBag(invalidBag);
    }


    @Test
    public void AddOwnBag() {
        addBags.addOwnBag(0.1);
        addBags.addOwnBag(0.2);
        addBags.addOwnBag(0.3);

        assertEquals(3, PurchasedItems.getListOfBags().size());
    }



}
