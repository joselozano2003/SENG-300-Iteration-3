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
//package com.autovend.software;
//
//import com.autovend.products.BarcodedProduct;
//import com.autovend.products.Product;
//
//import java.math.BigDecimal;
//import java.util.ArrayList;
//
//public class PurchasedItems{
//
//    private static ArrayList<BarcodedProduct> listOfProducts;
//    private static ArrayList<Product> listOfBags;
//    private static BigDecimal totalPrice;
//    private static double totalExpectedWeight;
//    private static BigDecimal change;
//    private static BigDecimal amountPaid;
//    private static boolean isPaid;
//
//    static {
//        listOfProducts = new ArrayList<>();
//        listOfBags = new ArrayList<>();
//        totalPrice = new BigDecimal(0);
//        amountPaid = new BigDecimal(0);
//        totalExpectedWeight = 0;
//        change = new BigDecimal(0);
//        isPaid = false;
//    }
//
//    public static void addProduct(BarcodedProduct product){
//        listOfProducts.add(product);
//        totalPrice = totalPrice.add(product.getPrice());
//        totalExpectedWeight += product.getExpectedWeight();
//        if (totalPrice.compareTo(amountPaid) >= 0) {
//            isPaid = false;
//        }
//    }
//
//    public static void addBag(Bag bag){
//        listOfBags.add(bag);
//        totalPrice = totalPrice.add(bag.getPrice());
//        totalExpectedWeight += bag.getWeight();
//        if (totalPrice.compareTo(amountPaid) >= 0) {
//            isPaid = false;
//        }
//    }
//
//    public static ArrayList<BarcodedProduct> getListOfProducts(){
//        return listOfProducts;
//    }
//
//    public static ArrayList<Product> getListOfBags(){
//        return listOfBags;
//    }
//
//    // I think this is not necessary for this iteration but will be useful for future ones
//    public static void removeProduct(BarcodedProduct product){
//        listOfProducts.remove(product);
//        totalPrice = totalPrice.subtract(product.getPrice());
//        totalExpectedWeight -= product.getExpectedWeight();
//    }
//
//    public static void removeOtherProduct(Product product, double weight){
//        listOfBags.remove(product);
//        totalPrice = totalPrice.subtract(product.getPrice());
//        totalExpectedWeight -= weight;
//    }
//
//    public static BigDecimal getTotalPrice(){
//        return totalPrice;
//    }
//
//    public static double getTotalExpectedWeight(){
//        return totalExpectedWeight;
//    }
//
//    public static void setChange(BigDecimal amount){
//        change = amount;
//    }
//
//    public static BigDecimal getChange(){
//        return change;
//    }
//
//    public static void addAmountPaid(BigDecimal amount) {
//        amountPaid = amountPaid.add(amount);
//        if (amountPaid.compareTo(totalPrice) >= 0) {
//        	isPaid = true;
//        }
//    }
//
//    public static boolean isPaid() {
//    	return isPaid;
//    }
//
//    public static BigDecimal getAmountPaid(){
//        return amountPaid;
//    }
//
//    public static BigDecimal getAmountLeftToPay() {
//    	return totalPrice.subtract(amountPaid);
//    }
//
//    public static void reset() {
//    	listOfProducts = new ArrayList<>();
//        listOfBags = new ArrayList<>();
//        totalPrice = new BigDecimal(0);
//        amountPaid = new BigDecimal(0);
//        totalExpectedWeight = 0;
//        change = new BigDecimal(0);
//        isPaid = false;
//    }
//}
