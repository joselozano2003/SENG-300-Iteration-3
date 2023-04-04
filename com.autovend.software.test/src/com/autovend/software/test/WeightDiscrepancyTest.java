

package com.autovend.software.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Members for Iteration 2:
 * Ethan Oke (30142180)
 * Jose Camilo Lozano Cetina (30144736)
 * Quinn Leonard (30145315)
 * Efren Garcia (30146181)
 * Nam Anh Vu (30127597)
 * Tyler Nguyen (30158563)
 * Victor Han (30112492)
 * Francisco Huayhualla (30091238)
 * Md Minhazur Rahman Hamim (30145446)
 * Imran Haji (30141571)
 * Sara Dhuka (30124117)
 * Robert (William) Engel (30119608)
 */

import java.math.BigDecimal;
import java.util.Currency;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.autovend.Barcode;
import com.autovend.BarcodedUnit;
import com.autovend.Numeral;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.products.BarcodedProduct;
import com.autovend.software.PurchasedItems;
import com.autovend.software.WeightDiscrepancy;

//Test the WeightDiscrepency class
public class WeightDiscrepancyTest {

    //Initialization of systems
    private SelfCheckoutStation station;
    private BarcodedProduct product;
    private BarcodedUnit item1;
    private BarcodedUnit item2;

    @Before
    public void setup() {
        // Setup and create station
        int[] billDenominations = {5, 10 , 15, 20, 50, 100};
        BigDecimal fiveCent = new BigDecimal("0.05");
        BigDecimal tenCent = new BigDecimal("0.10");
        BigDecimal twentyFiveCent = new BigDecimal("0.25");
        BigDecimal loonie = new BigDecimal("1");
        BigDecimal toonie = new BigDecimal("2");
        BigDecimal[] coinDenominations = {fiveCent, tenCent, twentyFiveCent, loonie, toonie};
        int scaleMaximumWeight = 50;
        int scaleSensitivity = 10;
        station = new SelfCheckoutStation(Currency.getInstance("CAD"),
                billDenominations, coinDenominations, scaleMaximumWeight, scaleSensitivity);
        // Create barcodes for products
        Barcode productBarcode = new Barcode(Numeral.zero, Numeral.one, Numeral.three, Numeral.five, Numeral.zero);
        // Create products
        product = new BarcodedProduct(productBarcode, "product name", new BigDecimal("9.99"), 40);
        item1 = new BarcodedUnit(productBarcode,40);
        item2 = new BarcodedUnit(productBarcode,20);
    }

    @After
    public void teardown() {
        //reset all the systems
        station = null;
        product = null;
        item1 = null;
        item2 = null;
    }

    //Tests a weight change when the expected weight matches the actual weight
    @Test
    public void validWeightDiscrepancyTest() {
        WeightDiscrepancy discrepancy = new WeightDiscrepancy(station);
        station.baggingArea.add(item1);
        PurchasedItems.addProduct(product);
        assertFalse(station.handheldScanner.isDisabled());
        assertFalse(station.mainScanner.isDisabled());
    }

    //Tests a weight change when the expected weight does not match the actual weight
    @Test
    public void invalidWeightDiscrepencyTest() {
        WeightDiscrepancy discrepancy = new WeightDiscrepancy(station);
        station.baggingArea.add(item2);
        PurchasedItems.addProduct(product);
        assertTrue(station.handheldScanner.isDisabled());
        assertTrue(station.mainScanner.isDisabled());
    }

    @Test
    public void dummyReactToEnabledEvent(){
        WeightDiscrepancy discrepancy = new WeightDiscrepancy(station);
        discrepancy.reactToEnabledEvent(null);
    }

    @Test
    public void dummyReactToDisabledEvent(){
        WeightDiscrepancy discrepancy = new WeightDiscrepancy(station);
        discrepancy.reactToDisabledEvent(null);
    }

    //Tests the state (enabled/disabled) of the scanners in overload and outofoverload events
    @Test
    public void reactToOverloadEventTest(){
        WeightDiscrepancy discrepancy = new WeightDiscrepancy(station);
        PurchasedItems.addProduct(product);
        PurchasedItems.addProduct(product);
        station.baggingArea.add(item1);
        station.baggingArea.add(item2);
        assertTrue(station.handheldScanner.isDisabled());
        assertTrue(station.mainScanner.isDisabled());
        PurchasedItems.removeProduct(product);
        station.baggingArea.remove(item1);
        assertFalse(station.handheldScanner.isDisabled());
        assertFalse(station.mainScanner.isDisabled());
    }

}
