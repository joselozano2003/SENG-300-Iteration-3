package com.autovend.software.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.autovend.products.BarcodedProduct;
import com.autovend.products.PLUCodedProduct;
import com.autovend.products.Product;
import com.autovend.software.bagging.ReusableBagProduct;
import com.autovend.software.customer.CustomerSession;

import java.awt.*;
import java.math.BigDecimal;
import java.util.Map;

public class CheckoutView extends JPanel {

    // Declare the shopping cart table and its model
    private JTable shoppingCartTable;
    private JButton startPaymentButton;
    private DefaultTableModel shoppingCartTableModel;

    public CheckoutView() {
        // Set the layout for the CheckoutView
        setLayout(new BorderLayout());

        // Initialize the shopping cart table model
        shoppingCartTableModel = new DefaultTableModel(new Object[]{"Product", "Quantity", "Price"}, 0);

        // Initialize the shopping cart table
        shoppingCartTable = new JTable(shoppingCartTableModel);
        shoppingCartTable.setPreferredScrollableViewportSize(new Dimension(450, 200));
        shoppingCartTable.setFillsViewportHeight(true);

        // Add the shopping cart table to the CheckoutView
        add(new JScrollPane(shoppingCartTable), BorderLayout.CENTER);

        // Add any other UI components for the CheckoutView
    }

    public void updateShoppingCart(CustomerSession session) {
        // Clear the current shopping cart table
        shoppingCartTableModel.setRowCount(0);

        // Add the updated shopping cart items to the table
        for (Map.Entry<Product, Double> entry : session.getShoppingCart().entrySet()) {
            Product product = entry.getKey();
            Double quantity = entry.getValue();
            BigDecimal price = product.getPrice().multiply(new BigDecimal(quantity));
            String name;
            if (product instanceof BarcodedProduct) {
				name = ((BarcodedProduct) product).getDescription();
			} else if (product instanceof PLUCodedProduct) {
				name = ((PLUCodedProduct) product).getDescription();
			} else if (product instanceof ReusableBagProduct) {
				name = "Reusable Bag";
			} else {
				name = "Unknown";
			}

            // Add a row to the table model for each item in the shopping cart
            shoppingCartTableModel.addRow(new Object[]{name, quantity, price});
        }
    }
}
