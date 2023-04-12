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
package com.autovend.software.item;

import com.autovend.products.Product;
import com.autovend.devices.SimulationException;

import java.math.BigDecimal;

public class TextSearchProduct extends Product{

    private final String name;
    private final String description;
    private final double expectedWeight;


    /**
     * Create a product instance.
     *
     * @param name The name of the product.
     *             Cannot be null.
     * @param description The description of the product.
     *
     * @param price The price of the product.
     *
     * @param expectedWeight The expected weight of one unit of the product.
     *
     * @throws SimulationException If the price is null or &le;0.
     */
    public TextSearchProduct(String name, String description, BigDecimal price, double expectedWeight) {
        super(price, true);
        this.name = name;
        this.description = description;
        this.expectedWeight = expectedWeight;

        if (name == null) {
            throw new SimulationException(new NullPointerException("name is null"));
        }

        if (description == null) {
            throw new SimulationException(new NullPointerException("description is null"));
        }

        if (expectedWeight <= 0) {
            throw new SimulationException(new IllegalArgumentException("expectedWeight cannot be negative"));
        }
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getExpectedWeight() {
        return expectedWeight;
    }

}
