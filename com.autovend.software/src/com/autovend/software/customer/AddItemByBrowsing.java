package com.autovend.software.customer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.*;

public class AddItemByBrowsing {

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
		}
	}
	
	
	// Add alphabet to GUI, define button action.
    private void addAlphabet() {
    	
        for (int i = 0; i < letters.length; ++i) {
            letterButtons[i] = new JButton(letters[i] + "");
            // Define action of pressing button.
            // The action of pressing a letter button is simply to add that
            // letter to stringEntered!
            letterButtonListener listener = new letterButtonListener(letters[i]);
            // When button is pressed, character will be added to stringEntered.
            letterButtons[i].addActionListener(listener);
            letterPanel.add(letterButtons[i]);
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
