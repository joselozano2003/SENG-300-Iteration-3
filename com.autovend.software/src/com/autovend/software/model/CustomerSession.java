package com.autovend.software.model;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.autovend.devices.SelfCheckoutStation;
import com.autovend.products.BarcodedProduct;
import com.autovend.products.PLUCodedProduct;
import com.autovend.products.Product;

public class CustomerSession {
    private State state;

    private Map<Product, Double> shoppingCart;

    private double expectedWeight = 0.0;

    private BigDecimal totalPaid;
    private PaymentMethod paymentMethod;

    public CustomerSession() {
        shoppingCart = new HashMap<>();
        state = State.INITIAL;
        paymentMethod = null;
    }

    public void addItem(Product product, double quantityOrWeight) {
        if (shoppingCart.containsKey(product)) {
            double currentProductAmount = shoppingCart.get(product);
            shoppingCart.put(product, currentProductAmount + quantityOrWeight);
        } else {
            shoppingCart.put(product, quantityOrWeight);
        }

        if (product instanceof BarcodedProduct) {
            expectedWeight += ((BarcodedProduct) product).getExpectedWeight();
        } else if (product instanceof PLUCodedProduct) {
            expectedWeight += quantityOrWeight;
        }
    }

    public void removeItem(Product product) {
        shoppingCart.remove(product);
    }

    public BigDecimal getTotalCost() {
        BigDecimal totalCost = BigDecimal.ZERO;
        for (Product product : shoppingCart.keySet()) {
            double productAmount = shoppingCart.get(product);
            totalCost = totalCost.add(product.getPrice().multiply(BigDecimal.valueOf(productAmount)));
        }
        return totalCost;
    }

    public BigDecimal getTotalPaid() {
        return totalPaid;
    }

    public Map<Product, Double> getShoppingCart() {
        return shoppingCart;
    }

    public void addPayment(BigDecimal amount) {
        totalPaid = totalPaid.add(amount);
    }

    public boolean isPaymentComplete() {
        return totalPaid.compareTo(getTotalCost()) >= 0;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public double getExpectedWeight() {
        return this.expectedWeight;
    }

    public enum State {
        INITIAL, ADDING_ITEMS, WEIGHING_ITEMS, PAYMENT_METHOD_SELECTION, PAYMENT_PROCESSING, PAYMENT_COMPLETE, DISPENSE_CHANGE,
        RECEIPT_PRINTING, FINISHED, DISABLED,
    }


}
