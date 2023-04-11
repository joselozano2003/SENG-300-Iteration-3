package com.autovend.software.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.LineBorder;

import com.autovend.products.BarcodedProduct;
import com.autovend.products.PLUCodedProduct;
import com.autovend.products.Product;
import com.autovend.software.bagging.ReusableBagProduct;
import com.autovend.software.customer.CustomerSession;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CheckoutView extends JPanel {

    private List<UIEventListener> observers;

    // Declare the shopping cart table and its model
    private JTable shoppingCartTable;
    private JButton startPaymentButton;
    private DefaultTableModel shoppingCartTableModel;

    public CheckoutView() {
        observers = new ArrayList<>();
        setLayout(null);
        setForeground(new Color(65, 73, 96));
        setBackground(new Color(65, 73, 96));
        setMinimumSize(new Dimension(1280, 720));
        setMaximumSize(new Dimension(1280, 720));
        setPreferredSize(new Dimension(1280, 720));
        setSize(new Dimension(1280, 720));

        // Labels
        JLabel cartLabel = new JLabel("Current Items in Cart:");
        cartLabel.setFont(new Font("Lucida Grande", Font.BOLD, 20));
        cartLabel.setForeground(new Color(255, 255, 255));
        cartLabel.setBounds(100, 20, 400, 30);

        JLabel totalLabel = new JLabel("Current Total: $0.00 CAD");
        totalLabel.setFont(new Font("Lucida Grande", Font.BOLD, 20));
        totalLabel.setForeground(new Color(255, 255, 255));
        totalLabel.setBounds(100, 600, 400, 30);

        // Buttons
        JButton searchPLUButton = new JButton("by PLU code");
        searchPLUButton.setOpaque(true);
        searchPLUButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        searchPLUButton.setBorder(new LineBorder(new Color(15, 17, 26), 1, true));
        searchPLUButton.setBackground(new Color(255, 203, 107));
        searchPLUButton.setBounds(800, 50, 280, 50);
        searchPLUButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                notifyAddItemByPLUButtonPressed();
            }
        });
        add(searchPLUButton);

        JButton purchaseBagButton = new JButton("Purchase Bag");
        purchaseBagButton.setOpaque(true);
        purchaseBagButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        purchaseBagButton.setBorder(new LineBorder(new Color(15, 17, 26), 1, true));
        purchaseBagButton.setBackground(new Color(255, 203, 107));
        purchaseBagButton.setBounds(800, 150, 280, 50);
        purchaseBagButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                notifyPurchaseBagsButtonPressed();
            }
        });
        add(purchaseBagButton);

        JButton addOwnBagsButton = new JButton("Add own Bags");
        addOwnBagsButton.setOpaque(true);
        addOwnBagsButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        addOwnBagsButton.setBorder(new LineBorder(new Color(15, 17, 26), 1, true));
        addOwnBagsButton.setBackground(new Color(255, 203, 107));
        addOwnBagsButton.setBounds(800, 250, 280, 50);
        addOwnBagsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                notifyStartAddOwnBagsButtonPressed();
            }
        });
        add(addOwnBagsButton);

        JButton doneAddOwnBagsButton = new JButton("Finished adding own bags");
        doneAddOwnBagsButton.setOpaque(true);
        doneAddOwnBagsButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        doneAddOwnBagsButton.setBorder(new LineBorder(new Color(15, 17, 26), 1, true));
        doneAddOwnBagsButton.setBackground(new Color(255, 203, 107));
        doneAddOwnBagsButton.setBounds(800, 350, 280, 50);
        doneAddOwnBagsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                notifyStopAddOwnBagsButtonPressed();
            }
        });
        add(doneAddOwnBagsButton);

        JButton paymentButton = new JButton("Proceed to Payment");
        paymentButton.setOpaque(true);
        paymentButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        paymentButton.setBorder(new LineBorder(new Color(15, 17, 26), 1, true));
        paymentButton.setBackground(new Color(255, 203, 107));
        paymentButton.setBounds(800, 450, 280, 50);
        paymentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                notifyStartPayingButtonPressed();
            }
        });
        add(paymentButton);

        // Shopping cart table
        shoppingCartTableModel = new DefaultTableModel(new Object[] { "Product", "Quantity", "Price" }, 0);
        shoppingCartTable = new JTable(shoppingCartTableModel);
        shoppingCartTable.setFont(new Font("Lucida Grande", Font.PLAIN, 14));
        shoppingCartTable.setFillsViewportHeight(true);
        shoppingCartTable.setBackground(Color.WHITE);
        shoppingCartTable.setGridColor(Color.BLACK);
        shoppingCartTable.setRowHeight(30);
        shoppingCartTable.setSelectionBackground(new Color(230, 230, 230));

        JScrollPane scrollBar = new JScrollPane(shoppingCartTable);
        scrollBar.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollBar.setBounds(100, 70, 600, 500);
        add(scrollBar);

        // Add components to the panel
        add(cartLabel);
        add(totalLabel);
    }
    
    public void register(UIEventListener listener) {
        observers.add(listener);
    }

    private void notifyStartAddOwnBagsButtonPressed() {
        for (UIEventListener listener : observers) {
            listener.onStartAddingOwnBags();
        }
    }

    private void notifyStopAddOwnBagsButtonPressed() {
        for (UIEventListener listener : observers) {
            listener.onFinishAddingOwnBags();
        }
    }

    public void notifyPurchaseBagsButtonPressed() {
        for (UIEventListener listener : observers) {
            listener.onPurchaseBags(1);
        }
    }

    public void notifyAddItemByPLUButtonPressed() {
        for (UIEventListener listener : observers) {
            listener.onSelectAddItemByPLU();
        }
    }
    
    public void notifyStartPayingButtonPressed() {
        for (UIEventListener listener : observers) {
            listener.onStartPaying();
        }
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
            shoppingCartTableModel.addRow(new Object[] { name, quantity, price });
        }
    }
}


   

