// P3-4 Group Members
//
// Abdelrhafour, Achraf (30022366)
// Campos, Oscar (30057153)
// Cavilla, Caleb (30145972)
// Crowell, Madeline (30069333)
// Debebe, Abigia (30134608)
// Dhuka, Sara Hazrat (30124117)
// Drissi, Khalen (30133707)
// Ferreira, Marianna (30147733)
// Frey, Ben (30088566)
// Himel, Tanvir (30148868)
// Huayhualla Arce, Fabricio (30091238)
// Kacmar, Michael (30113919)
// Lee, Jeongah (30137463)
// Li, Ran (10120152)
// Lokanc, Sam (30114370)
// Lozano Cetina, Jose Camilo (30144736)
// Maahdie, Monmoy (30149094)
// Malik, Akansha (30056048)
// Mehedi, Abdullah (30154770)
// Polton, Scott (30138102)
// Rahman, Saadman (30153482)
// Rodriguez, Gabriel (30162544)
// Samin Rashid, Khondaker (30143490)
// Sloan, Jaxon (30123845)
// Tran, Kevin (30146900)
//

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

	private List<CustomerUIEventListener> observers;

	// Declare the shopping cart table and its model
	private JTable shoppingCartTable;
	private JButton startPaymentButton;
	private DefaultTableModel shoppingCartTableModel;

	private JLabel issueLabel;

    /**
     * Constructor for the Check out view This also construct the 
     * basic GUI
     */
    
    
    public CheckoutView() {
        observers = new ArrayList<>();
        setLayout(null);
        setForeground(new Color(65, 73, 96));
        setBackground(new Color(65, 73, 96));
        setMinimumSize(new Dimension(1280, 720));
        setMaximumSize(new Dimension(1280, 720));
        setPreferredSize(new Dimension(1280, 720));
        setSize(new Dimension(1280, 720));
        
        issueLabel = new JLabel("");
        issueLabel.setFont(new Font("Lucida Grande", Font.BOLD, 20));
        issueLabel.setForeground(new Color(255, 255, 255));
        issueLabel.setBounds(100, 700, 400, 30);
        add(issueLabel);

		// Labels
		JLabel cartLabel = new JLabel("Current Items in Cart:");
		cartLabel.setFont(new Font("Lucida Grande", Font.BOLD, 20));
		cartLabel.setForeground(new Color(255, 255, 255));
		cartLabel.setBounds(100, 20, 400, 30);

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

		// Buttons
		JButton searchByBrowsing = new JButton("by Browsing");
		searchByBrowsing.setOpaque(true);
		searchByBrowsing.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		searchByBrowsing.setBorder(new LineBorder(new Color(15, 17, 26), 1, true));
		searchByBrowsing.setBackground(new Color(255, 203, 107));
		searchByBrowsing.setBounds(800, 150, 280, 50);
		searchByBrowsing.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				notifyAddItemByBrowsingButtonPressed();
			}
		});
		add(searchByBrowsing);

		JButton purchaseBagButton = new JButton("Purchase Bag");
		purchaseBagButton.setOpaque(true);
		purchaseBagButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		purchaseBagButton.setBorder(new LineBorder(new Color(15, 17, 26), 1, true));
		purchaseBagButton.setBackground(new Color(255, 203, 107));
		purchaseBagButton.setBounds(800, 250, 280, 50);
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
		addOwnBagsButton.setBounds(800, 350, 280, 50);
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
		doneAddOwnBagsButton.setBounds(800, 450, 280, 50);
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
		paymentButton.setBounds(800, 550, 280, 50);
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
       //xw add(totalLabel);
    }
    
    /**xw
     * Registers a UIEventListener to the list of observers.
     *
     * @param listener the UIEventListener to be added to the list of observers
     */
    
    public void register(CustomerUIEventListener listener) {
        observers.add(listener);
    }

    /**
     * Notifies all registered UIEventListener objects that the Start Add Own Bags button has been pressed.
     * This method iterates through the list of observers and calls the onStartAddingOwnBags() method on each of them.
     */
    
    private void notifyStartAddOwnBagsButtonPressed() {
        for (CustomerUIEventListener listener : observers) {
            listener.onStartAddingOwnBags();
        }
    }

    /**
     * Notifies all registered UIEventListener objects that the Stop Add Own Bags button has been pressed.
     * This method iterates through the list of observers and calls the onFinishAddingOwnBags() method on each of them.
     */
    
    private void notifyStopAddOwnBagsButtonPressed() {
        for (CustomerUIEventListener listener : observers) {
            listener.onFinishAddingOwnBags();
        }
    }
    
    /**
     * Notifies all registered UIEventListener objects that the Purchase Bags button has been pressed.
     * This method iterates through the list of observers and calls the onPurchaseBags() method on each of them
     * with a quantity of 1.
     */

    public void notifyPurchaseBagsButtonPressed() {
        for (CustomerUIEventListener listener : observers) {
            listener.onPurchaseBags(1);
        }
    }
    
    /**
     * Notifies all registered UIEventListener objects that the Add Item By PLU button has been pressed.
     * This method iterates through the list of observers and calls the onSelectAddItemByPLU() method on each of them.
     */

    public void notifyAddItemByPLUButtonPressed() {
        for (CustomerUIEventListener listener : observers) {
            listener.onSelectAddItemByPLU();
        }
    }
    
    /**
     * Notifies all registered UIEventListener objects that the Add Item By Browsing button has been pressed.
     * This method iterates through the list of observers and calls the onSelectAddItemByBrowsing() method on each of them.
     */
    
    public void notifyAddItemByBrowsingButtonPressed() {
        for (CustomerUIEventListener listener : observers) {
            listener.onSelectAddItemByBrowsing();
        }
    }
    
    /**
     * Notifies all registered UIEventListener objects that the Start Paying button has been pressed.
     * This method iterates through the list of observers and calls the onStartPaying() method on each of them.
     */
    
    public void notifyStartPayingButtonPressed() {
        for (CustomerUIEventListener listener : observers) {
            listener.onStartPaying();
        }
    }

    /**
     * Updates the shopping cart table with the current items and quantities from the given CustomerSession.
     * This method clears the current shopping cart table, then iterates through the shopping cart items
     * and adds the updated items to the table with their respective names, quantities, and prices.
     *
     * @param session the CustomerSession containing the shopping cart to be updated
     */
    
    

	

	public void updateShoppingCart(CustomerSession session) {
		
		BigDecimal totalPrice = BigDecimal.ZERO;
		// Clear the current shopping cart table
		shoppingCartTableModel.setRowCount(0);

		// Add the updated shopping cart items to the table
		for (Map.Entry<Product, Double> entry : session.getShoppingCart().entrySet()) {
			Product product = entry.getKey();
			Double quantity = entry.getValue();
			BigDecimal price = product.getPrice().multiply(new BigDecimal(quantity));
			totalPrice = totalPrice.add(price);
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

		shoppingCartTableModel.addRow(new Object[] { "Total:", "", totalPrice });

	}
	
	public void displayWeightDiscrepancy() {
		issueLabel.setText("Weight Discrepancy Detected");
	}
}
