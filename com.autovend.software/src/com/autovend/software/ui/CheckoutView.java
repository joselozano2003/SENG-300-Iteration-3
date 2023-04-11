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
    
	private JPanel waitPanel;
	private JPanel mainPanel;
	private JTextArea cartDis;

    public CheckoutView() {
        
    	CheckoutView customerView = this;
    	
    	JPanel mainPanel = this;
		
		JLabel cartLabel = new JLabel();
		JLabel searchLabel = new JLabel();
		JLabel totalLabel = new JLabel();
		JPanel cartPanel = new JPanel();
		JPanel waitPanel = new JPanel();
		
		JLabel waitLabel = new JLabel();
		
		waitLabel.setText("Please wait for an attendant to verify your station.");
		
		cartLabel.setText("Current Items in Cart: ");
		searchLabel.setText("Search for item: ");
		totalLabel.setText("Current Total: $0.00 CAD");
		
		waitPanel.setLayout(new BoxLayout(waitPanel,BoxLayout.LINE_AXIS));
	    
	    cartPanel.setLayout(new BoxLayout(cartPanel,BoxLayout.PAGE_AXIS));
	    
	    mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.LINE_AXIS));
	    
	    JPanel buttonPanel = new JPanel();
	    buttonPanel.setLayout(new BoxLayout(buttonPanel,BoxLayout.PAGE_AXIS));
	   
	    JButton searchBrowseButton = new JButton("by Browsing");
	    JButton searchPLUButton = new JButton("by PLU code");
	    JButton purchaseBagButton = new JButton("Purchase Bag");
	    JButton addOwnBagsButton = new JButton("Add own Bags");
	    JButton paymentButton = new JButton("Proceed to Payment");  
	    
	    
	    
	    shoppingCartTableModel = new DefaultTableModel(new Object[]{"Product", "Quantity", "Price"}, 0);
	    shoppingCartTable = new JTable(shoppingCartTableModel);
	    
	    JScrollPane scrollBar = new JScrollPane(shoppingCartTable);
	    scrollBar.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	    
	    waitPanel.add(waitLabel);
	    
	    cartPanel.add(Box.createVerticalStrut(10));
	    cartPanel.add(cartLabel);
	    cartLabel.setAlignmentX(cartPanel.LEFT_ALIGNMENT);
	    cartPanel.add(Box.createVerticalStrut(10));
	    cartPanel.add(scrollBar);
	    cartPanel.add(Box.createVerticalStrut(10));
	    cartPanel.add(totalLabel);
	    cartPanel.add(Box.createVerticalStrut(10));
	    
	    
	    mainPanel.add(Box.createHorizontalStrut(10));
	    mainPanel.add(cartPanel);
	    mainPanel.add(Box.createHorizontalStrut(10));
	    mainPanel.add(buttonPanel);
	    mainPanel.add(Box.createHorizontalStrut(10));
	    
	    
	    searchLabel.setAlignmentX(buttonPanel.CENTER_ALIGNMENT);
	    buttonPanel.add(searchLabel);
	    buttonPanel.add(Box.createVerticalStrut(10));
	    buttonPanel.add(searchBrowseButton);
	    searchBrowseButton.setAlignmentX(buttonPanel.CENTER_ALIGNMENT);
	    buttonPanel.add(Box.createVerticalStrut(10));
	    buttonPanel.add(searchPLUButton);
	    searchPLUButton.setAlignmentX(buttonPanel.CENTER_ALIGNMENT);
	    buttonPanel.add(Box.createVerticalStrut(90));
	    buttonPanel.add(purchaseBagButton);
	    purchaseBagButton.setAlignmentX(buttonPanel.CENTER_ALIGNMENT);
	    buttonPanel.add(Box.createVerticalStrut(10));
	    buttonPanel.add(addOwnBagsButton);
	    addOwnBagsButton.setAlignmentX(buttonPanel.CENTER_ALIGNMENT);
	    buttonPanel.add(Box.createVerticalStrut(90));
	    buttonPanel.add(paymentButton);
	    paymentButton.setAlignmentX(buttonPanel.CENTER_ALIGNMENT);
	    buttonPanel.add(Box.createVerticalStrut(10));
	    add(waitPanel);
	    waitPanel.setVisible(false);

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
