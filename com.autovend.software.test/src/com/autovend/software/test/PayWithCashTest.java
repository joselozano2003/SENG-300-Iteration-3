
package com.autovend.software.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.autovend.Barcode;
import com.autovend.Bill;
import com.autovend.Coin;
import com.autovend.Numeral;
import com.autovend.devices.BillDispenser;
import com.autovend.devices.BillValidator;
import com.autovend.devices.CoinDispenser;
import com.autovend.devices.CoinValidator;
import com.autovend.devices.OverloadException;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.SimulationException;
import com.autovend.products.BarcodedProduct;
import com.autovend.software.ChangeCalculator;
import com.autovend.software.PayWithCash;
import com.autovend.software.PurchasedItems;

public class PayWithCashTest {

    private SelfCheckoutStation station;
    private BarcodedProduct product;
    private BarcodedProduct anotherProduct;
    private Currency currency;
    private Bill bill;
    private Bill billTwenty;
    private Coin coin;
    private Coin coinTwo;
    private CoinDispenser fiveCentDispenser;
    private CoinDispenser twoDollarDispenser;
    private BillDispenser fiveDollarDispenser;
    private PayWithCash payWithCash;

    @Before
    public void setup() {
        Currency currency = Currency.getInstance(Locale.CANADA);
        int[] billDenominations = {5, 10 , 15, 20, 50, 100};
        BigDecimal fiveCent = new BigDecimal("0.05");
        BigDecimal tenCent = new BigDecimal("0.10");
        BigDecimal twentyFiveCent = new BigDecimal("0.25");
        BigDecimal loonie = new BigDecimal("1");
        BigDecimal toonie = new BigDecimal("2");
        BigDecimal[] coinDenominations = {fiveCent, tenCent, twentyFiveCent, loonie, toonie};
        station = new SelfCheckoutStation(currency, billDenominations, coinDenominations,10,1);

        coin = new Coin(fiveCent,currency);
        coinTwo = new Coin (toonie,currency);
        bill = new Bill(5,currency);
        billTwenty = new Bill(20,currency);
        BillDispenser fiveDollarDispenser = station.billDispensers.get(5);
        CoinDispenser fiveCentDispenser = station.coinDispensers.get(fiveCent);

        Barcode barcode = new Barcode(Numeral.zero, Numeral.two, Numeral.three, Numeral.two, Numeral.seven);
        product = new BarcodedProduct(barcode,"product name", new BigDecimal("12.95"),1);
        anotherProduct = new BarcodedProduct(barcode,"product name", new BigDecimal("1.95"),1);


    }

    @After
    public void teardown() {
        station = null;
        product = null;
        PurchasedItems.reset();
    }

    //Test completing a purchase using bills
}

