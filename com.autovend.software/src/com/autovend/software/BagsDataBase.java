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
public class BagsDataBase {
    public static ArrayList<Bag> BagsDataBase;

    public BagsDataBase() {}

    public static void initialize() {
        BagsDataBase = new ArrayList<Bag>();

        Bag reusableBagSmall = new Bag("Small Reusable Bag", new BigDecimal("0.25"), 0.1);
        BagsDataBase.add(reusableBagSmall);
        Bag reusableBagMedium = new Bag("Medium Reusable Bag", new BigDecimal("0.50"), 0.2);
        BagsDataBase.add(reusableBagMedium);
        Bag reusableBagLarge = new Bag("Large Reusable Bag", new BigDecimal("1"), 0.3);
        BagsDataBase.add(reusableBagLarge);
        Bag disposableBagSmall = new Bag("Small Disposable Bag", new BigDecimal("0.25"), 0.1);
        BagsDataBase.add(disposableBagSmall);
        Bag disposableBagMedium = new Bag("Medium Disposable Bag", new BigDecimal("0.50"), 0.2);
        BagsDataBase.add(disposableBagMedium);
        Bag disposableBagLarge = new Bag("Large Disposable Bag", new BigDecimal("1"), 0.3);
        BagsDataBase.add(disposableBagLarge);
    }

    public static boolean isInDatabase(Bag typeofBag) throws SimulationException {

        if (typeofBag == null) {
            throw new SimulationException("Please select a valid bag type.");
        }
        for (Bag bag : BagsDataBase) {
            if (bag.getWeight() == typeofBag.getWeight() && bag.getPrice().equals(typeofBag.getPrice()) && bag.getDescription().equals(typeofBag.getDescription())) {
                return true;
            }
        }
        return false;
    }
}
