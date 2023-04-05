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
package com.autovend.software.test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;

import org.junit.After;
import org.junit.Before;

import com.autovend.Barcode;
import com.autovend.Bill;
import com.autovend.Coin;
import com.autovend.Numeral;
import com.autovend.devices.BillDispenser;
import com.autovend.devices.CoinDispenser;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.products.BarcodedProduct;
import com.autovend.software.PurchasedItems;

public class PayWithCashTest {

    private SelfCheckoutStation station;

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

        Coin coin = new Coin(fiveCent,currency);
        Coin coinTwo = new Coin (toonie,currency);
        Bill bill = new Bill(5,currency);
        Bill billTwenty = new Bill(20,currency);
        BillDispenser fiveDollarDispenser = station.billDispensers.get(5);
        CoinDispenser fiveCentDispenser = station.coinDispensers.get(fiveCent);

        Barcode barcode = new Barcode(Numeral.zero, Numeral.two, Numeral.three, Numeral.two, Numeral.seven);
        BarcodedProduct product = new BarcodedProduct(barcode,"product name", new BigDecimal("12.95"),1);
        BarcodedProduct anotherProduct = new BarcodedProduct(barcode,"product name", new BigDecimal("1.95"),1);


    }

    @After
    public void teardown() {
        station = null;
        PurchasedItems.reset();
    }

    //Test completing a purchase using bills
}

