package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;

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
			discrepancyLabels[i] = new JLabel("Station " + (i + 1) + " working normally...");
			resolveButtons[i] = new JButton("");
			
			attendantPanel.add(discrepancyLabels[i]);
			attendantPanel.add(resolveButtons[i]);
		}
		
    	attendantFrame.getContentPane().add(attendantPanel, BorderLayout.CENTER);
    	
    	attendantFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	attendantFrame.pack();
    	attendantFrame.setVisible(true);
	}

	// Only will be used for testing.
	public static void main(String args[]) {
		AttendantInterface A = new AttendantInterface(5);
	}
	
	
	// Notifies attendant of weight discrepancy by adding a row to the
	// attendant interface window containing station # and discrepancy,
	// and will then either override the system so that the
	// expected weight is now the current weight, or will get
	// the customer to remove items so that the discrepancy is resolved.
	// No matter what happens, when the discrepancy has been resolved
	// and the expected weight has been updated the attendant will be able
	// to press the "resolved" button in this row to continue the checkout.
	public void notifyWeightDiscrepancy() {
		
	}
	
}
