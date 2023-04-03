package com.autovend.software;

import com.autovend.products.BarcodedProduct;

import java.math.BigDecimal;
import java.util.ArrayList;


public class PurchasedItems{

    private static ArrayList<BarcodedProduct> listOfProducts;
    private BigDecimal totalPrice;
    private double totalExpectedWeight;
    private BigDecimal change;

    public PurchasedItems(){
        listOfProducts = new ArrayList<>();
        totalPrice = new BigDecimal(0);
        totalExpectedWeight = 0;
        change = new BigDecimal(0);
    }

    public void addProduct(BarcodedProduct product){
        listOfProducts.add(product);
        totalPrice = totalPrice.add(product.getPrice());
        totalExpectedWeight += product.getExpectedWeight();
    }

    public ArrayList<BarcodedProduct> getListOfProducts(){
        return listOfProducts;
    }

    // I think this is not necessary for this iteration but will be useful for future ones
    public void removeProduct(BarcodedProduct product){
    	listOfProducts.remove(product);
    	totalPrice = totalPrice.subtract(product.getPrice());
    	totalExpectedWeight -= product.getExpectedWeight();
    }

    public BigDecimal getTotalPrice(){
        return totalPrice;
    }

    public double getTotalExpectedWeight(){
        return totalExpectedWeight;
    }

    public void setChange(BigDecimal change){
        this.change = change;
    }

    public BigDecimal getChange(){
        return change;
    }

}

