package com.autovend.software.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.autovend.external.ProductDatabases;
import com.autovend.products.PLUCodedProduct;

public class AddItemByBrowsingView extends JPanel {
	
	// CODE TO COPY BELOW:
	
		// a
	
		// Maximum number of items per screen.
		static int MAX_ITEMS = 20;
		
		JPanel browsingPanel = this;
		JPanel cataloguePanel;
		JPanel letterPanel;
		JPanel previousPanel;
		JPanel nextPanel;
		JButton nextButton;
		JButton previousButton;
		JLabel searchLabel;
		JButton itemButtons[] = new JButton[MAX_ITEMS];
		int num_items = 0;			// Actual number of items to display on screen.
		
		// Character array to represent string entered so far by user.
		ArrayList<Character> stringEntered = new ArrayList<Character>();
		
		static char[] letters = {'A', 'B', 'C', 'D', 'E', 'F',
	    	    'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
	    	    'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
	    JButton[] letterButtons = new JButton[letters.length];
	    JButton deleteButton;
	    
	    // Get collection of products to display.
	    static Collection<PLUCodedProduct> products = ProductDatabases.PLU_PRODUCT_DATABASE.values();
		
	    // Catalogue currently being displayed to customer.
	    ArrayList<PLUCodedProduct> catalogue;
	    
	    // Tracks page number we're currently on in the catalogue.
	    int page_number = 1;
	    
	    
		// Called when add item by browsing button is pressed, this method will
		// make calls to the controller to add items to shopping cart.
		public void addItemByBrowsing(CustomerView cusView) {
	    	//swing compounents needed for the GUI
	    	browsingPanel = new JPanel();
	    	cataloguePanel = new JPanel();
	    	letterPanel = new JPanel();
	    	previousPanel = new JPanel();
	    	nextPanel = new JPanel();
	    	
	    	JLabel Title = new JLabel();
	    	searchLabel = new JLabel("");
	    	// Item buttons will be placed in the catalogue panel.
	    	cataloguePanel.setLayout(new GridLayout(5,5, 2, 2)); 
	    	letterPanel.setLayout(new GridLayout(0,26, 2, 2));
	    	
	    	//frame set up 
	    	browsingPanel.setLayout(null);
	    	
	    	//icon set up currently with a bug 
	    	ImageIcon logoIcon = new ImageIcon("logo.png"); 
	    	//Title for the page set up 
			Title.setText("Store Catalogue");
			Title.setFont(new Font("Mv Boli",Font.PLAIN,20));
			//Title.setVerticalAlignment(JLabel.TOP);
			//Title.setHorizontalAlignment(JLabel.CENTER);
			Title.setBounds(650, 25, 200, 50); //set x,y position within a frame as well as dimensions
			 
			letterPanel.setBackground(Color.WHITE);
			letterPanel.setBounds(75,190,1250,100);
			
			cataloguePanel.setBackground(Color.WHITE);
			cataloguePanel.setBounds(100,300,1200,400);
			
			//Catalogue slider panel set up 
			nextPanel.setBackground(Color.WHITE);
			nextPanel.setBounds(1302,425, 75, 130);
			nextPanel.setLayout(null);
			
			previousPanel.setBackground(Color.WHITE);
			previousPanel.setBounds(22,425, 75, 130);
			previousPanel.setLayout(null);
			
			// Set up previous and next buttons.
			AddItemByBrowsingView reference = this;
	    	nextButton = new JButton(">");
	    	nextButton.setBounds(0, 0, 75, 130);
	    	nextButton.addActionListener(e -> {
	    		// Upon button being pressed, increment page number if possible.
	    		// page_number*MAX_ITEMS gives the total number of items displayed
	    		// on this page and all previous pages. If number of items is greater, increment.
	    		if (num_items > page_number*MAX_ITEMS) {
	    			++page_number;
	    			// Update screen.
	    			reference.displayCatalogue();
	    		}
	    	});
	    	
	    	previousButton = new JButton("<");
	    	previousButton.setBounds(0, 0, 75, 130);
	    	previousButton.addActionListener(e -> {
	    		// Upon button being pressed, decrement page number if possible.
	    		if (page_number > 1) {
	    			--page_number;
	    			reference.displayCatalogue();
	    		}
	    	});
	    	
	    	nextPanel.add(nextButton);
	    	previousPanel.add(previousButton);
			
			// Initialize number of items to be zero, button array to be empty.
			// Buttons will be added to this list as customer types in letters.
			num_items = 0;
			
			// Add letter buttons.
			addAlphabet();
			// Add delete button.
			deleteButton = new JButton("Delete");
			deleteButton.addActionListener(e -> {
				// When delete button is pressed, pop a character from entered string and update display.
				stringEntered.remove(stringEntered.size() - 1);
				// Update catalogue and display.
				updateCatalogue();
				displayCatalogue();
			});
			
			//adding everything to the main frame 
			//browsingFrame.pack();
			browsingPanel.add(Title);
			browsingPanel.add(searchLabel);
			browsingPanel.add(letterPanel);	
			browsingPanel.add(cataloguePanel);
			browsingPanel.add(nextPanel);
			browsingPanel.add(previousPanel);
			browsingPanel.setVisible(true);
		}
		
		// Listener for letter buttons.
		private class LetterButtonListener implements ActionListener {
		
			private char letter;
			
			LetterButtonListener(char c) {letter = c;}
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// When button is pressed, add letter to stringEntered array.
				stringEntered.add(letter);
				// Update catalogue based on new string.
				updateCatalogue();
				// Display current catalogue based on stringEntered and page_number.
				displayCatalogue();
			}
		}
		
		private class ItemButtonListener implements ActionListener {
			
			PLUCodedProduct item;
			
			ItemButtonListener(PLUCodedProduct pcp) {item = pcp;}

			@Override
			public void actionPerformed(ActionEvent e) {
				
				// WHEN ITEM BUTTON IS PRESSED, CALL SOME METHOD IN THE CONTROLLER THAT
				// ADDS THE ITEM TO CUSTOMERS SHOPPING CART. TO BE ADDED.
				System.out.println("Item added");
				
			}
			
		}
		
		private void addAlphabet() {
	    	
	        for (int i = 0; i < letters.length; ++i) {
	            letterButtons[i] = new JButton(letters[i] + "");
	            // Register button listener.
	            LetterButtonListener listener = new LetterButtonListener(letters[i]);
	            // When button is pressed, character will be added to stringEntered.
	            letterButtons[i].addActionListener(listener);
	            letterPanel.add(letterButtons[i]);
	        }	
	        
	    }
		
		private void updateCatalogue() {
	    	// Begin by clearing catalogue.
	    	catalogue.clear();
	    	// We have a collection of PLUCodedProducts. search by description.
	    	for (PLUCodedProduct product : products) {
	    		// If first stringEntered.length letters are the same, add to catalogue.
	    		if (stringEntered.toString() == product.getDescription().substring(0, stringEntered.size())) {
	    			// Match! Add to catalogue.
	    			catalogue.add(product);
	    		}
	    	}
	    	num_items = catalogue.size();
	    }
		
		public void displayCatalogue() {
	    	// Display all items in range [(page_number - 1)*MAX_ITEMS, page_number*MAX_ITEMS)
	    	for (int i = (page_number - 1)*MAX_ITEMS; i < page_number*MAX_ITEMS; ++i) {
	    		// Create button with product description.
	    		itemButtons[i] = new JButton(catalogue.get(i).getDescription());
	    		// Set action of button...
	    		ItemButtonListener listener = new ItemButtonListener(catalogue.get(i));
	    		itemButtons[i].addActionListener(listener);
	    		// Add button to catalogue panel.
	    		cataloguePanel.add(itemButtons[i]);
	    	}
	    	
	    	// Update searchLabel to be stringEntered.
	    	searchLabel.setText(stringEntered.toString());
	    	
	    }

}
