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
package com.autovend.software.ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import com.autovend.software.attendant.AttendantController;

import auth.AttendantAccount;

public class StationTableView extends JPanel {
	
	// Interface can support any number of stations. Window will
	// consist of rows representing each station. First column
	// indicates whether there is a weight discrepancy at any stations,
	// second column contains buttons to resolve.
	int num_stations;
	
	AttendantController controller;
	AttendantAccount account;
	
	// Widgets - treated as global variables so that they can be accessed and
	// modified by methods called by the controller.
	JPanel attendantPanel = this;
	JLabel discrepancyLabels[];
	JButton approveButtons[];
	JButton refuseButtons[];
	
	
	// Constructor
	public StationTableView(int num_stations) {
		this.num_stations = num_stations;
		discrepancyLabels = new JLabel[num_stations];
		approveButtons = new JButton[num_stations];
		refuseButtons = new JButton[num_stations];
	
		attendantPanel.setLayout(new GridLayout(num_stations, 3));
		
		// Set up each row. Initially, no weight discrepancies.
		for (int i = 0; i < num_stations; ++i) {
			discrepancyLabels[i] = new JLabel("Station"
					+ " " + (i + 1) + ": " + "Working normally" + "...");
			approveButtons[i] = new JButton("");
			refuseButtons[i] = new JButton("");
			
			attendantPanel.add(discrepancyLabels[i]);
			attendantPanel.add(approveButtons[i]);
			attendantPanel.add(refuseButtons[i]);
		}
	}

	
	
	// Main - used for testing.
	public static void main(String args[]) {
		JFrame testFrame = new JFrame("test");
		StationTableView testView = new StationTableView(10);
		testFrame.add(testView);
		testFrame.setVisible(true);
		testFrame.pack();
	}
	
	
	
	
	public void attendantDisplay() {
		
	}
		
}
