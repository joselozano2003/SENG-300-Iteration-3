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
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.*;

import com.autovend.devices.TouchScreen;
import com.autovend.software.GUI.AddItemByBrowsing;
import com.autovend.software.GUI.StartScreen;

public class CustomerView {
	
	public JFrame mainFrame;
	private JPanel waitPanel;
	private JPanel mainPanel;
	private JTextArea cartDis;
	
	public static void main(String[] args) {
		TouchScreen tScreen = new TouchScreen();
		CustomerView mainView = new CustomerView(tScreen.getFrame());
		tScreen.getFrame().setLayout(new OverlayLayout(tScreen.getFrame().getContentPane()));
		tScreen.getFrame().setVisible(true);
		mainView.updateCart();
		mainView.updateCart();
		mainView.updateCart();
	}
	public CustomerView(JFrame touchFrame) {
		
		{
			
			CustomerView customerView = this;
			
			mainFrame = touchFrame;
			mainPanel = new JPanel();
			JLabel cartLabel = new JLabel();
			JLabel searchLabel = new JLabel();
			JLabel totalLabel = new JLabel();
			JPanel cartPanel = new JPanel();
			waitPanel = new JPanel();
			
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
		    
		    cartPanel.setBorder(new TitledBorder(new EtchedBorder (),"Cart"));

		    cartDis = new JTextArea(32, 58);
		    cartDis.setEditable(false); 
		    JScrollPane scrollBar = new JScrollPane(cartDis);
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
		    mainFrame.add(mainPanel);
		    mainFrame.add(waitPanel);
		    waitPanel.setVisible(false);
		    mainFrame.validate();
		    
		    searchBrowseButton.addActionListener(new ActionListener() {
		    	public void actionPerformed(ActionEvent e) {
		    		AddItemByBrowsing browseWindow = new AddItemByBrowsing();
		    		browseWindow.addItemByBrowsing(customerView);
		    		mainPanel.setVisible(false);
		    	}
		    });
		    
		}
		
	}
	
	public void appearMain() {
		mainFrame.setVisible(true);
	}
	
	public void waitForAttendant() {
		mainPanel.setVisible(false);
		waitPanel.setVisible(true);
		
	}
	
	public void attendantVerified() {
		mainPanel.setVisible(true);
		waitPanel.setVisible(false);
	}
	
	public void updateCart() {
		cartDis.insert("Test\n",0);
	}
}