/* P3-4 Group Members
 * 
 * Abdelrhafour, Achraf (30022366)
 * Campos, Oscar (30057153)
 * Cavilla, Caleb (30145972)
 * Crowell, Madeline (30069333)
 * Debebe, Abigia (30134608)
 * Dhuka, Sara Hazrat (30124117)
 * Drissi, Khalen (30133707)
 * Ferreira, Marianna (30147733)
 * Frey, Ben (30088566)
 * Himel, Tanvir (30148868)
 * Huayhualla Arce, Fabricio (30091238)
 * Kacmar, Michael (30113919)
 * Lee, Jeongah (30137463)
 * Li, Ran (10120152)
 * Lokanc, Sam (30114370)
 * Lozano Cetina, Jose Camilo (30144736)
 * Maahdie, Monmoy (30149094)
 * Malik, Akansha (30056048)
 * Mehedi, Abdullah (30154770)
 * Polton, Scott (30138102)
 * Rahman, Saadman (30153482)
 * Rodriguez, Gabriel (30162544)
 * Samin Rashid, Khondaker (30143490)
 * Sloan, Jaxon (30123845)
 * Tran, Kevin (30146900)
 */
package com.autovend.software.customer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.*;

import com.autovend.external.ProductDatabases;
import com.autovend.products.PLUCodedProduct;

import java.math.*;

public class AddItemByBrowsing {
	
	public static void main(String[] args) {
		CustomerView cv = new CustomerView();
		cv.addItemByBrowsing();
		cv.cataloguePanel.add(new JLabel("Hello world"));
	}
	
	// CODE TO COPY BELOW:
	
	static int MAX_ITEMS = 20;
	
	CustomerController controller;
	
	JFrame browsingFrame;
	JPanel cataloguePanel;
	JPanel letterPanel;
	JPanel previousPanel;
	JPanel nextPanel;
	JButton nextCatalogue;
	JButton previousCatalogue;
	JButton itemButtons[] = new JButton[MAX_ITEMS];
	int num_items;			// Actual number of items to display on screen.
	
	// Character array to represent string entered so far by user.
	ArrayList<Character> stringEntered = new ArrayList<Character>();
	
	static char[] letters = {'A', 'B', 'C', 'D', 'E', 'F',
    	    'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
    	    'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    JButton[] letterButtons = new JButton[letters.length];
    
    // Get collection of products to display.
    static Collection<PLUCodedProduct> products = ProductDatabases.PLU_PRODUCT_DATABASE.values();
	
    // Catalogue currently being displayed to customer.
    Collection<PLUCodedProduct> catalogue;
    
    
	// Called when add item by browsing button is pressed, this method will
	// make calls to the controller to add items to shopping cart.
	public void addItemByBrowsing() {
    	//swing compounents needed for the GUI
    	browsingFrame = new JFrame();
    	cataloguePanel = new JPanel();
    	letterPanel = new JPanel();
    	previousPanel = new JPanel();
    	nextPanel = new JPanel();
    	
    	JLabel Title = new JLabel();
    	// Item buttons will be placed in the catalogue panel.
    	cataloguePanel.setLayout(new GridLayout(5,5, 2, 2)); 
    	letterPanel.setLayout(new GridLayout(0,26, 2, 2));
    	
    	//frame set up 
    	browsingFrame.setTitle("Add Item By Browsing");
    	browsingFrame.setSize(1500,750);
    	browsingFrame.setLayout(null);
    	browsingFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    	
    	//icon set up currently with a bug 
    	ImageIcon logoIcon = new ImageIcon("logo.png");
    	browsingFrame.setIconImage(logoIcon.getImage());
    	browsingFrame.getContentPane().setBackground(Color.LIGHT_GRAY);
    	
    	 
    	//Title for the page set up 
		Title.setText("Store Catalogue");
		Title.setFont(new Font("Mv Boli",Font.PLAIN,20));
		//Title.setVerticalAlignment(JLabel.TOP);
		//Title.setHorizontalAlignment(JLabel.CENTER);
		Title.setBounds(650, 25, 200, 50); //set x,y position within a frame as well as dimensions
		
		
		//butons set up for panel search by letter and catalogue panel 
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
		
		// Initialize number of items to be zero, button array to be empty.
		// Buttons will be added to this list as customer types in letters.
		num_items = 0;
		
		//current widgets to the GUI
		addAlphabet();
		addCatalogue();
		
		
		//adding everything to the main frame 
		//browsingFrame.pack();
		browsingFrame.add(Title);
		browsingFrame.add(letterPanel);	
		browsingFrame.add(cataloguePanel);
		browsingFrame.add(nextPanel);
		browsingFrame.add(previousPanel);
		browsingFrame.setVisible(true);
	}
	
	
	// Listener for letter buttons.
	private class letterButtonListener implements ActionListener {
	
		private char letter;
		
		letterButtonListener(char c) {letter = c;}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			// When button is pressed, add letter to stringEntered array.
			stringEntered.add(letter);
			// Update catalogue.
			updateCatalogue();
		}
	}
	
	
	// Add alphabet to GUI, define button action for each letter button.
    private void addAlphabet() {
    	
        for (int i = 0; i < letters.length; ++i) {
            letterButtons[i] = new JButton(letters[i] + "");
            // Register button listener.
            letterButtonListener listener = new letterButtonListener(letters[i]);
            // When button is pressed, character will be added to stringEntered.
            letterButtons[i].addActionListener(listener);
            letterPanel.add(letterButtons[i]);
        }	
        
    }
    
    
    // Update catalogue based on current contents of stringEntered.
    // Search database and display relevant results.
    private void updateCatalogue() {
    	// We have a collection of PLUCodedProducts. search by description.
    	PLUCodedProduct[] productArray = (PLUCodedProduct[]) products.toArray();
    	for (int i = 0; i < productArray.length; ++i) {
    		// If first stringEntered.length letters are the same, add to catalogue.
    		if (stringEntered.toString() == productArray[i].getDescription().substring(0, stringEntered.size())) {
    			// Match! Add to catalogue.
    			catalogue.add(productArray[i]);
    		}
    	}
    	
    	// Catalogue has been updated. Now add these as buttons in the catalogue panel.
    	// Number of items to display is minimum of catalogue size and max # of items.
    	num_items = Math.min(catalogue.size(), MAX_ITEMS);
    	for (int i = 0; i < num_items; ++i) {
    		// Create button with product description.
    		itemButtons[i] = new JButton(productArray[i].getDescription());
    		// Add button to catalogue panel.
    		cataloguePanel.add(itemButtons[i]);
    	}
    	
    }
    
    
    private void addCatalogue() {
    	
    	//Catalogue Next and Previous Buttons 
    	nextCatalogue = new JButton(">");
    	nextCatalogue.setBounds(0, 0, 75, 130);
    	
    	previousCatalogue = new JButton("<");
    	previousCatalogue.setBounds(0, 0, 75, 130);
    	
    	nextPanel.add(nextCatalogue);
    	previousPanel.add(previousCatalogue);
        	
    	//add the items
    	for(int j = 1; j < num_items; j++) {
    		cataloguePanel.add(itemButtons[j]);
    	}	
    	
    }
	
	
}
