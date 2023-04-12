package com.autovend.software.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import com.autovend.PriceLookUpCode;
import com.autovend.Numeral;
import com.autovend.external.ProductDatabases;
import com.autovend.products.PLUCodedProduct;
import com.autovend.products.PLUCodedProduct;
import com.autovend.products.Product;
import com.autovend.software.item.ItemEventListener;
import com.autovend.software.item.ProductsDatabase2;

public class AddItemByBrowsingView extends JPanel {

	// CODE TO COPY BELOW:

	// a

	// Maximum number of items per screen.
	static int MAX_ITEMS = 20;

	private List<BrowsingViewObserver> observers;
	private List<UIEventListener> listeners;

	JButton backButton;
	JLabel messageLabel;

	JPanel browsingPanel = this;
	JPanel middlePanel;
	JPanel cataloguePanel;
	JPanel letterPanel;
	JScrollPane scrollBar;
	JPanel previousPanel;
	JPanel nextPanel;
	JButton nextButton;
	JButton previousButton;
	JLabel searchLabel;
	JPanel borderPanel;
	JButton itemButtons[] = new JButton[MAX_ITEMS];
	int num_items = 0; // Actual number of items to display on screen.

	// Character array to represent string entered so far by user.
	ArrayList<Character> stringEntered = new ArrayList<Character>();

	static char[] letters = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
			'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
	JButton[] letterButtons = new JButton[letters.length];
	JButton deleteButton;

	// Get collection of products to display.
	private Collection<PLUCodedProduct> products = ProductDatabases.PLU_PRODUCT_DATABASE.values();

	// Catalogue currently being displayed to customer.
	ArrayList<PLUCodedProduct> catalogue;

	// Tracks page number we're currently on in the catalogue.
	int page_number = 1;

	// Called when add item by browsing button is pressed, this method will
	// make calls to the controller to add items to shopping cart.
	public AddItemByBrowsingView() {
		// swing compounents needed for the GUI

		observers = new ArrayList<>();
		listeners = new ArrayList<>();

		Map<JButton, Product> productMap = ProductsDatabase2.Products_In_Visual_Catalogue_Database;
		System.out.println(productMap.size());

		// Create back button
		backButton = new JButton("Go Back");
		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				notifyGoBackToCheckout();
			}
		});

		messageLabel = new JLabel("");
		browsingPanel.add(backButton);
		browsingPanel.add(messageLabel);

		cataloguePanel = new JPanel();
		middlePanel = new JPanel();
		borderPanel = new JPanel();
		letterPanel = new JPanel();
		previousPanel = new JPanel();
		nextPanel = new JPanel();
		scrollBar = new JScrollPane(borderPanel);
		// scrollBar.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		browsingPanel.setLayout(new BoxLayout(browsingPanel, BoxLayout.PAGE_AXIS));
		middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.LINE_AXIS));
		borderPanel.setLayout(new BorderLayout());

		JLabel Title = new JLabel();
		searchLabel = new JLabel("");
		// Item buttons will be placed in the catalogue panel.
		cataloguePanel.setLayout(new FlowLayout());

		// icon set up currently with a bug
		ImageIcon logoIcon = new ImageIcon("logo.png");
		// Title for the page set up
		Title.setText("Store Catalogue");
		Title.setFont(new Font("Mv Boli", Font.PLAIN, 20));

		cataloguePanel.setBackground(Color.WHITE);

		cataloguePanel.setAlignmentX(browsingPanel.CENTER_ALIGNMENT);

		cataloguePanel.setPreferredSize(new Dimension(1200, 5000));
		scrollBar.setPreferredSize(new Dimension(1200, 400));

		Title.setAlignmentX(browsingPanel.CENTER_ALIGNMENT);

		// adding everything to the main frame
		// browsingFrame.pack();
		borderPanel.add(cataloguePanel);
		browsingPanel.add(Box.createVerticalStrut(50));
		browsingPanel.add(Title);
		browsingPanel.add(Box.createVerticalStrut(30));
		browsingPanel.add(searchLabel);
		browsingPanel.add(middlePanel);
		middlePanel.add(Box.createGlue());
		middlePanel.add(scrollBar);
		middlePanel.add(Box.createGlue());
		browsingPanel.add(Box.createGlue());
		browsingPanel.setVisible(true);

		for (JButton currentButton : productMap.keySet()) {

			Product productGot = productMap.get(currentButton);

			cataloguePanel.add(currentButton);
			currentButton.setPreferredSize(new Dimension(400, 200));

			if (productGot instanceof PLUCodedProduct) {
				PLUCodedProduct pluProduct = (PLUCodedProduct) productGot;
				currentButton.setText(pluProduct.getDescription());
			}

			currentButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					notifyProductSelected(productGot);
					currentButton.setBackground(Color.GREEN);
				}
			});
		}

	}

	public void notifyProductSelected(Product product) {
		for (BrowsingViewObserver observer : observers) {
			messageLabel.setText("Please place item on scale.");
			observer.reactToProductSelected(product);;
		}
	}
	public void register(UIEventListener listener) {
		listeners.add(listener);
	}

	public void addObserver(BrowsingViewObserver observer) {
		observers.add(observer);
	}

	public void notifyGoBackToCheckout() {
		for (UIEventListener listener : listeners) {
			listener.goBackToCheckout();
		}
	}

}
