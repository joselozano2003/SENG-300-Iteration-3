package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class AttendantInterface {
	JFrame attendantFrame;
	JPanel attendantPanel;
	
	// Interface can support any number of stations. Window will
	// consist of rows representing each station. First column
	// indicates whether there is a weight discrepancy at any stations,
	// second column contains buttons to resolve.
	int num_stations;
	JLabel[] discrepancyLabels;
	JButton[] resolveButtons;
	
	public AttendantInterface(int number_stations) {
		num_stations = number_stations;
		discrepancyLabels = new JLabel[num_stations];
		resolveButtons = new JButton[num_stations];
		
		attendantFrame = new JFrame("Attendant Interface");
		attendantPanel = new JPanel();
		attendantPanel.setLayout(new GridLayout(num_stations, 2));
		
		// Set up each row. Initially, no weight discrepancies.
		for (int i = 0; i < num_stations; ++i) {
			discrepancyLabels[i] = new JLabel("Station " + (i + 1) + ": Working normally...");
			resolveButtons[i] = new JButton("");
			
			attendantPanel.add(discrepancyLabels[i]);
			attendantPanel.add(resolveButtons[i]);
		}
		
    	attendantFrame.getContentPane().add(attendantPanel, BorderLayout.CENTER);
    	
    	attendantFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	attendantFrame.pack();
    	attendantFrame.setVisible(true);
	}

	// Main - only will be used for testing.
	public static void main(String args[]) {
		AttendantInterface A = new AttendantInterface(5);
		A.notifyWeightDiscrepancy(1, 10.0);
	}
	
	
	// Listener class used to listen for resolve button being pressed.
	private class ResolveListener implements ActionListener {

		private int station_number;
		
		public ResolveListener(int n) {
			// Listener needs to track interface and station number so that the
			// interface can be properly updated. Will probably need to track the
			// station too so that it can resolve weight discrepancy and continue checkout.
			station_number = n;
		}
		
		
		// When button is pressed...
		public void actionPerformed(ActionEvent e) {
			// When button is pressed, resolve weight discrepancy so that checkout
			// can continue.
			
			// SOFTWARE: CALL SOMETHING TO RESOLVE WD.
			
			// Set back to normal on interface.
			discrepancyLabels[station_number - 1].setText("Station " + station_number + ": Working normally...");
			// Lastly, de-register this listener and reset button text...
			resolveButtons[station_number - 1].setText("");
			resolveButtons[station_number - 1].removeActionListener(this);
		}
		
	}
	
	
	// Notifies attendant of weight discrepancy by adding a row to the
	// attendant interface window containing station # and discrepancy,
	// and will then either override the system so that the
	// expected weight is now the current weight, or will get
	// the customer to remove items so that the discrepancy is resolved.
	// No matter what happens, when the discrepancy has been resolved
	// and the expected weight has been updated the attendant will be able
	// to press the "resolved" button in this row to continue the checkout.
	//
	// NOTE: STATION NUMBER IS AN INT >= 1, MUST SUBTRACT ONE TO INDEX INTO COMPONENT ARRAYS.
	public void notifyWeightDiscrepancy(int station_number, double discrepancy) {
		discrepancyLabels[station_number - 1].setText("Station " + station_number
				+ ": Weight discrepancy of " + discrepancy);
		resolveButtons[station_number - 1].setText("Resolve");
		
		// Set up and add button listener.
		ResolveListener rl = new ResolveListener(station_number);
		resolveButtons[station_number - 1].addActionListener(rl);
	}
	
}
