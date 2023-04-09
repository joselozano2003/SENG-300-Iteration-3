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
package com.autovend.software.attendant;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import com.autovend.software.Translate;

import auth.AttendantAccount;

public class AttendantView {

	String lang_code;			// Language code for interface.
	
	// Interface can support any number of stations. Window will
	// consist of rows representing each station. First column
	// indicates whether there is a weight discrepancy at any stations,
	// second column contains buttons to resolve.
	int num_stations;
	
	AttendantController controller;
	AttendantAccount account;
	
	// Widgets - treated as global variables so that they can be accessed and
	// modified by methods called by the controller.
	JFrame attendantFrame;
	JPanel attendantPanel;
	JLabel discrepancyLabels[];
	JButton approveButtons[];
	JButton refuseButtons[];
	
	
	// Constructor
	public AttendantView(AttendantController controller, int num_stations, String lang_code) {
		if (controller == null || lang_code == null) 
			throw new NullPointerException("Null arguments given");
		this.controller = controller;
		this.num_stations = num_stations;
		this.lang_code = lang_code;
	}
	
	
	// Override to set main frame to be visible/invisible.
	public void setVisible(boolean b) {
		attendantFrame.setVisible(b);
	}
	

	// Main - used for testing.
	public static void main(String args[]) {
		
	}
	
	
	//////////////////////
	// BUTTON LISTENERS //
	//////////////////////
	
	
	// Listener class used to listen for overriding weight discrepancies.
	private class OverrideListener implements ActionListener {

		private int station_number;
		
		public OverrideListener(int n) {
			station_number = n;
		}
		
		
		@Override
		public void actionPerformed(ActionEvent e) {
			// When button is pressed, override weight discrepancy and continue checkout.
			
			// CALL CONTROLLER METHOD TO OVERRIDE WEIGHT DISCREPANCY
			controller.overrideWeightDiscrepancy(station_number);
			
			// Set back to normal on interface.
			deregisterButtonListeners(station_number);
		}
		
	}
	
	
	// Listener class used for approve bags button.
	private class ApproveBagsListener implements ActionListener {
		
		private int station_number;
		
		public ApproveBagsListener(int n) {
			station_number = n;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// Approve bags...
			
			// CALL CONTROLLER METHOD TO UPDATE EXPECTED WEIGHT, CONTINUE CHECKOUT
			controller.bagsApproved(station_number);
			
			// Set back to normal on interface.
			deregisterButtonListeners(station_number);
			
		}
		
	}
	
	// Listener class used for refuse bags button.
	private class RefuseBagsListener implements ActionListener {
		
		private int station_number;
		
		public RefuseBagsListener(int n) {
			station_number = n;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// Approve bags...
			
			// CONTROLLER: MAKE CALL TO CUSTOMER INTERFACE PROMPTING TO REMOVE BAGS.
			controller.bagsRejected(station_number);
			
			// Set back to normal on interface.
			deregisterButtonListeners(station_number);
			
		}
		
	}
	
	
	// Deregister all button listeners for a particular station.
	// This is done whenever issues have been resolved and station should now be
	// working normally.
	public void deregisterButtonListeners(int station_number) {
		discrepancyLabels[station_number - 1].setText(Translate.translate("Station", lang_code)
				+ " " + station_number + ": " + Translate.translate("Working normally", lang_code) + "...");
		// Lastly, de-register this listener and reset button text...
		approveButtons[station_number - 1].setText("");
		refuseButtons[station_number - 1].setText("");
		// Remove all button listeners so that action of buttons can be reset.
		for (int i = 0; i < refuseButtons[station_number - 1].getActionListeners().length; ++i) {
			refuseButtons[station_number - 1].removeActionListener(refuseButtons[station_number - 1].getActionListeners()[i]);
		}
		for (int i = 0; i < approveButtons[station_number - 1].getActionListeners().length; ++i) {
			approveButtons[station_number - 1].removeActionListener(approveButtons[station_number - 1].getActionListeners()[i]);
		}
	}
	
	
	// ** METHOD TO BE CALLED BY CONTROLLER WHEN A WEIGHT DISCREPANCY IS DETECTED **
	//
	// Notifies attendant of weight discrepancy. Attendant can then either override
	// the discrepancy, or can prompt the customer to remove items so that the discrepancy is resolved.
	//
	// NOTE: STATION NUMBER IS AN INT >= 1, MUST SUBTRACT ONE TO INDEX INTO COMPONENT ARRAYS.
	public void notifyWeightDiscrepancy(int station_number, double discrepancy) {
		discrepancyLabels[station_number - 1].setText(Translate.translate("Station", lang_code) +" " + station_number
				+ ": " + Translate.translate("Weight discrepancy of", lang_code) + " " + discrepancy + " " 
				+ Translate.translate("grams", lang_code));
		approveButtons[station_number - 1].setText(Translate.translate("Override", lang_code));
		
		// Set up and add button listener.
		OverrideListener rl = new OverrideListener(station_number);
		approveButtons[station_number - 1].addActionListener(rl);
	}
	
	
	// ** METHOD TO BE CALLED BY CONTROLLER IF WEIGHT DISCREPANCY IS RESOLVED
	// WITHOUT NEEDING AN OVERRIDE (e.g. customer removes problematic item) **
	// 
	// Resets row corresponding to station to default.
	public void weightDiscrepancyResolved(int station_number) {
		deregisterButtonListeners(station_number);
	}
	
	
	// ** METHOD TO BE CALLED BY CONTROLLER WHEN BAG APPROVAL IS NEEDED **
	//
	// Notifies attendant that customer is awaiting bag approval. Attendant can then
	// either approve bags or refuse bags. If approve, call method in software that
	// updates expected weight and continues checout. If refuse, then call method
	// in customer interface to prompt customer to remove bags.
	public void notifyBagApproval(int station_number) {
		discrepancyLabels[station_number - 1].setText(Translate.translate("Station", lang_code) +" " + station_number
				+ ": " + Translate.translate("Bag approval needed", lang_code));
		approveButtons[station_number - 1].setText(Translate.translate("Approve", lang_code));
		refuseButtons[station_number - 1].setText(Translate.translate("Refuse", lang_code));
		
		// Set up and add button listeners.
		ApproveBagsListener abl = new ApproveBagsListener(station_number);
		approveButtons[station_number - 1].addActionListener(abl);
		RefuseBagsListener rbl = new RefuseBagsListener(station_number);
		refuseButtons[station_number - 1].addActionListener(rbl);
	}
	
	
	// Prompt a login to attendant station.
	public void attendantLogin() {
		JFrame loginFrame = new JFrame(Translate.translate("Attendant Login", lang_code));
		JPanel loginPanel = new JPanel();
		loginPanel.setLayout(new GridLayout(3, 2));
		
		JLabel userLabel = new JLabel(Translate.translate("Username:", lang_code));
		JTextField userText = new JTextField();
		JLabel passLabel = new JLabel(Translate.translate("Password:", lang_code));
		JTextField passText = new JTextField();
		JLabel errorLabel = new JLabel("");
		JButton loginButton = new JButton(Translate.translate("Login", lang_code));
		loginButton.addActionListener(e -> {
			// When button is pressed, call controller to check for a valid account.
			boolean valid = controller.startLogIn(new AttendantAccount(userText.getText(), passText.getText()));
			if (valid) {
				// Login.
				loginFrame.setVisible(false);		// Make login frame invisible.
				attendantDisplay();					// Display full attendant interface.
			} else {
				// Display error message, let user try again.
				errorLabel.setText(Translate.translate("Account not found", lang_code));
			}
		});
		
		loginPanel.add(userLabel);
		loginPanel.add(userText);
		loginPanel.add(passLabel);
		loginPanel.add(passText);
		loginPanel.add(errorLabel);
		loginPanel.add(loginButton);
		
    	loginFrame.getContentPane().add(loginPanel, BorderLayout.CENTER);
    	
    	loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	loginFrame.pack();
    	loginFrame.setVisible(true);
		
	}
	
	
	// Log out attendant.
	public void attendantLogout() {
		JFrame logoutFrame = new JFrame(Translate.translate("Attendant Logout", lang_code));
		JPanel logoutPanel = new JPanel();
		logoutPanel.setLayout(new GridLayout(2, 1));
		
		JLabel logoutLabel = new JLabel(Translate.translate("Successfully logged out.", lang_code));
		JButton logoutButton = new JButton(Translate.translate("Close", lang_code));
		logoutButton.addActionListener(e -> {
			// When button is pressed, close window.
			logoutFrame.setVisible(false);
		});
		
		logoutPanel.add(logoutLabel);
		logoutPanel.add(logoutButton);
		
    	logoutFrame.getContentPane().add(logoutPanel, BorderLayout.CENTER);
    	
    	logoutFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	logoutFrame.pack();

		controller.startLogOut(account);
		logoutFrame.setVisible(true);
	}
	
	
	// Display attendant interface.
	// Number of stations is preset when AttendantView is constructed. Should incorporate option to change language.
	public void attendantDisplay() {
		discrepancyLabels = new JLabel[num_stations];
		approveButtons = new JButton[num_stations];
		refuseButtons = new JButton[num_stations];
		
		attendantFrame = new JFrame(Translate.translate("Attendant Interface", lang_code));
		attendantPanel = new JPanel();
		attendantPanel.setLayout(new GridLayout(num_stations, 3));
		
		// Set up each row. Initially, no weight discrepancies.
		for (int i = 0; i < num_stations; ++i) {
			discrepancyLabels[i] = new JLabel(Translate.translate("Station", lang_code)
					+ " " + (i + 1) + ": " + Translate.translate("Working normally", lang_code) + "...");
			approveButtons[i] = new JButton("");
			refuseButtons[i] = new JButton("");
			
			attendantPanel.add(discrepancyLabels[i]);
			attendantPanel.add(approveButtons[i]);
			attendantPanel.add(refuseButtons[i]);
		}
		
    	attendantFrame.getContentPane().add(attendantPanel, BorderLayout.CENTER);
    	
    	attendantFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	attendantFrame.pack();
    	attendantFrame.setVisible(true);
	}
	

}
