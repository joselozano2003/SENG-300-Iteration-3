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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.autovend.external.ProductDatabases;
import com.autovend.products.PLUCodedProduct;

public class AddItemByBrowsingView extends JPanel {
	
	// CODE TO COPY BELOW:
	
		//
	
		// Maximum number of items per screen.
		static int MAX_ITEMS = 20;
		
		JPanel browsingPanel = this;
		JPanel mainPanel;
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
		public AddItemByBrowsingView() {
	    	//swing compounents needed for the GUI
			mainPanel = new JPanel();
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
			
			cataloguePanel.setBackground(Color.WHITE);
			cataloguePanel.setBounds(100,300,1200,400);
			
			

			//adding everything to the main frame 
			//browsingFrame.pack();
			browsingPanel.add(Title);
			browsingPanel.add(searchLabel);
			browsingPanel.add(cataloguePanel);
			browsingPanel.setVisible(true);
		}
		
		public static void main(String[] args) {
	    	JFrame frame = new JFrame();
	    	frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
			frame.setUndecorated(true);
			frame.setVisible(true);
			frame.add(new AddItemByBrowsingView());
			frame.validate();
	    }

}
