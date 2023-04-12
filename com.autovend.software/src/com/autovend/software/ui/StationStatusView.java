package com.autovend.software.ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.*;

public class StationStatusView extends JPanel {
	
	private int num_stations;
	private JFrame attendantFrame;
	private JLabel discrepancyLabels[];
	private JButton approveButtons[];
	private JButton refuseButtons[];
	
	List<UIEventListener> listeners;
	
	public static void main(String args[]) {
		StationStatusView ssv = new StationStatusView(10);
		for (int i = 0; i < 10; ++ i) {
			ssv.notifyWeightDiscrepancy(i + 1, 10.0);
		}
	}
	
	
	// Display attendant interface.
	// Number of stations is preset when AttendantView is constructed. Should incorporate option to change language.
	public StationStatusView(int num_stations) {
		this.num_stations = num_stations;
		discrepancyLabels = new JLabel[num_stations];
		approveButtons = new JButton[num_stations];
		refuseButtons = new JButton[num_stations];
		
		attendantFrame = new JFrame("Attendant interface");
		this.setLayout(new GridLayout(num_stations, 3));
		
		// Set up each row. Initially, no weight discrepancies.
		for (int i = 0; i < num_stations; ++i) {
			discrepancyLabels[i] = new JLabel("Station " + (i + 1) + " working normally...");
			approveButtons[i] = new JButton("");
			refuseButtons[i] = new JButton("");
			
			this.add(discrepancyLabels[i]);
			this.add(approveButtons[i]);
			this.add(refuseButtons[i]);
		}
		
    	attendantFrame.getContentPane().add(this, BorderLayout.CENTER);
    	
    	attendantFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	attendantFrame.pack();
    	attendantFrame.setVisible(true);
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
			
			// REACT TO OVERRIDE EVENT
			for (UIEventListener listener : listeners) listener.onOverride(station_number);
			
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
			
			// REACT TO BAG APPROVAL EVENT
			for (UIEventListener listener : listeners) listener.onBagApproval(station_number);
			
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
			
			// REACT TO BAG REFUSAL EVENT
			for (UIEventListener listener : listeners) listener.onBagRefusal(station_number);
			
			// Set back to normal on interface.
			deregisterButtonListeners(station_number);
		
		}
	
	}
	
	
	// Deregister all button listeners for a particular station.
	// This is done whenever issues have been resolved and station should now be
	// working normally.
	public void deregisterButtonListeners(int station_number) {
		discrepancyLabels[station_number - 1].setText("Station " + station_number + " working normally...");
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
		discrepancyLabels[station_number - 1].setText("Station " + station_number + ": Weight discrepancy of" 
				+ " " + discrepancy + " grams");
		approveButtons[station_number - 1].setText("Override");
		
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
		discrepancyLabels[station_number - 1].setText("Station " + station_number + ": Bag approval needed");
		approveButtons[station_number - 1].setText("Approve");
		refuseButtons[station_number - 1].setText("Refuse");
		
		// Set up and add button listeners.
		ApproveBagsListener abl = new ApproveBagsListener(station_number);
		approveButtons[station_number - 1].addActionListener(abl);
		RefuseBagsListener rbl = new RefuseBagsListener(station_number);
		refuseButtons[station_number - 1].addActionListener(rbl);
	}

}
