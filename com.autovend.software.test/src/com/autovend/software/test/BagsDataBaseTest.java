///* P3-4 Group Members
// *
// * Abdelrhafour, Achraf (30022366)
// * Campos, Oscar (30057153)
// * Cavilla, Caleb (30145972)
// * Crowell, Madeline (30069333)
// * Debebe, Abigia (30134608)
// * Dhuka, Sara Hazrat (30124117)
// * Drissi, Khalen (30133707)
// * Ferreira, Marianna (30147733)
// * Frey, Ben (30088566)
// * Himel, Tanvir (30148868)
// * Huayhualla Arce, Fabricio (30091238)
// * Kacmar, Michael (30113919)
// * Lee, Jeongah (30137463)
// * Li, Ran (10120152)
// * Lokanc, Sam (30114370)
// * Lozano Cetina, Jose Camilo (30144736)
// * Maahdie, Monmoy (30149094)
// * Malik, Akansha (30056048)
// * Mehedi, Abdullah (30154770)
// * Polton, Scott (30138102)
// * Rahman, Saadman (30153482)
// * Rodriguez, Gabriel (30162544)
// * Samin Rashid, Khondaker (30143490)
// * Sloan, Jaxon (30123845)
// * Tran, Kevin (30146900)
// */
//package com.autovend.software.test;
//
//import static org.junit.Assert.*;
//import java.math.BigDecimal;
//import java.util.ArrayList;
//
//import com.autovend.devices.SimulationException;
//import com.autovend.software.BagsDataBase;
//import org.junit.*;
//import com.autovend.devices.SelfCheckoutStation;
//import com.autovend.software.*;
//
//public class BagsDataBaseTest {
//    BagsDataBase addBags;
//    SelfCheckoutStation scs;
//    ArrayList<Bag> listOfStoreBags = new ArrayList<Bag>();
//    Bag reusableBagSmall;
//    Bag reusableBagMedium;
//    Bag reusableBagLarge;
//    Bag disposableBagSmall;
//    Bag disposableBagMedium;
//    Bag disposableBagLarge;
//    //sets up for testing
//
//    @Before
//    public void setup() {
//        reusableBagSmall = new Bag("Small Reusable Bag", new BigDecimal("0.25"), 0.1);
//        listOfStoreBags.add(reusableBagSmall);
//        reusableBagMedium = new Bag("Medium Reusable Bag", new BigDecimal("0.50"), 0.2);
//        listOfStoreBags.add(reusableBagMedium);
//        reusableBagLarge = new Bag("Large Reusable Bag", new BigDecimal("1"), 0.3);
//        listOfStoreBags.add(reusableBagLarge);
//        disposableBagSmall = new Bag("Small Disposable Bag", new BigDecimal("0.25"), 0.1);
//        listOfStoreBags.add(disposableBagSmall);
//        disposableBagMedium = new Bag("Medium Disposable Bag", new BigDecimal("0.50"), 0.2);
//        listOfStoreBags.add(disposableBagMedium);
//        disposableBagLarge = new Bag("Large Disposable Bag", new BigDecimal("1"), 0.3);
//        listOfStoreBags.add(disposableBagLarge);
//        addBags = new BagsDataBase(scs);
//    }
//
//    @After
//    public void tearDown() {
//        addBags = null;
//        PurchasedItems.reset();
//    }
//
//    //tests if the class is constructed correctly
//    @Test
//    public void AddOrRemoveBagsConstructorTest1() {
//        boolean flag;
//        try {
//            BagsDataBase testConstructor = new BagsDataBase(scs);
//            flag = true;
//        }
//        catch (Exception e) {
//            flag = false;
//        }
//        assertTrue(flag);
//    }
//
//    //tests if a bag is added correctly
//    @Test
//    public void AddValidStoreBag() {
//        addBags.purchaseBag(reusableBagSmall);
//        addBags.purchaseBag(reusableBagMedium);
//        addBags.purchaseBag(reusableBagLarge);
//
//        assertEquals(3, PurchasedItems.getListOfBags().size());
//    }
//
//    @Test
//    public void AddValidStoreBag2() {
//        addBags.purchaseBag(disposableBagSmall);
//        addBags.purchaseBag(disposableBagMedium);
//        addBags.purchaseBag(disposableBagLarge);
//
//        assertEquals(3, PurchasedItems.getListOfBags().size());
//    }
//
//    //tests if an invalid bag is added
//    @Test (expected = SimulationException.class)
//    public void AddInvalidStoreBag() {
//        Bag invalidBag = new Bag("Invalid Bag", new BigDecimal("0.25"), 0.1);
//        addBags.purchaseBag(invalidBag);
//    }
//
//    @Test (expected = SimulationException.class)
//    public void AddInvalidStoreBag2() {
//        Bag invalidBag = new Bag("Small Reusable Bag", new BigDecimal("0.25"), 0.4);
//        addBags.purchaseBag(invalidBag);
//    }
//
//    @Test
//    public void AddOwnBag() {
//        addBags.addOwnBag(0.1);
//        addBags.addOwnBag(0.2);
//        addBags.addOwnBag(0.3);
//
//        assertEquals(3, PurchasedItems.getListOfBags().size());
//    }
//
//}
