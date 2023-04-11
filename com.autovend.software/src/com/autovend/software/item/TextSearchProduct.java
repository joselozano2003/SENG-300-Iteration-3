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
