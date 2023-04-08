package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.autovend.PriceLookUpCode;
import com.autovend.products.PLUCodedProduct;
import com.autovend.external.ProductDatabases;

import javax.swing.*;

public class AttendantInterface {
	JFrame attendantFrame;
	JPanel attendantPanel;
	String lang_code;			// Language code for interface.
	
	// Interface can support any number of stations. Window will
	// consist of rows representing each station. First column
	// indicates whether there is a weight discrepancy at any stations,
	// second column contains buttons to resolve.
	int num_stations;
	JLabel[] discrepancyLabels;
	JButton[] approveButtons;
	JButton[] refuseButtons;
	
	public AttendantInterface(int number_stations, String language) {
		num_stations = number_stations;
		lang_code = language;
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
	//Add Item By text search added to the Attendant Interface
	JButton textSearchItem = new JButton("Lookup Product");
		textSearchItem.addMouseListener(new MouseAdapter() {
			@Override
			//prompts the attendant to enter product details
			public void mouseClicked(MouseEvent e) {
				String productName = JOptionPane.showInputDialog("Please enter the product details: ", "");
				if(productName.equals("")) {
					JOptionPane.showMessageDialog(new JPanel(),
						"Invalid Product Name",
						"Please enter the details again.",
						JOptionPane.ERROR_MESSAGE);
					return;
				}
				//checks if the product is there in the product database
				PriceLookupCode plu = control.attendantLookUpProductCode(productName);
				if(plu != null) {
					PLUCodedProduct prod = ProductDatabases.PLU_PRODUCT_DATABASE.get(plu);
					JOptionPane.showMessageDialog(new JPanel(),
					"The product you searched for is: " + prod.getPLUCode() + " " + prod.getDescription() +".",
					"Product found!",
					JOptionPane.PLAIN_MESSAGE);
					//displays error message if the product is not found in the database
				} else {
					JOptionPane.showMessageDialog(new JPanel(),
					"The product: " + productName + " was not found in the database!",
					"Product not found in the Database. ",
					JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		textSearchItem.setOpaque(true);
		textSearchItem.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	    textSearchItem.setBorder(new LineBorder(new Color(15, 17, 26), 1, true));
		textSearchItem.setBackground(new Color(230, 152, 39));
		textSearchItem.setBounds(60, 335, 280, 55);
		add(textSearchItem);
	
	
	// Ends attendant interface session.
	public void end_session() {
		attendantFrame.setVisible(false);
	}

	// Main - only will be used for testing.
	public static void main(String args[]) {
		AttendantInterface A = new AttendantInterface(20, "LLL");
		for (int i = 1; i <= 10; ++i) A.notifyBagApproval(i);
		for (int i = 11; i <= 20; ++i) A.notifyWeightDiscrepancy(i, 10.0);
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
			
			// SOFTWARE: OVERRIDE WEIGHT DISCREPANCY - SET EXPECTED WEIGHT = ACTUAL
			
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
			
			// SOFTWARE: UPDATE EXPECTED WEIGHT AND CONTINUE CHECKOUT.
			
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
			
			// SOFTWARE: MAKE CALL TO ATTENDANT INTERFACE PROMPTING TO REMOVE BAGS.
			
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
	
	
	// ** METHOD TO BE CALLED BY SOFTWARE WHEN A WEIGHT DISCREPANCY IS DETECTED **
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
	
	
	// ** METHOD TO BE CALLED BY BAGGING AREA LISTENER IF WEIGHT DISCREPANCY IS RESOLVED
	// WITHOUT NEEDING AN OVERRIDE (e.g. customer removes problematic item) **
	// 
	// Resets row corresponding to station to default.
	public void weightDiscrepancyResolved(int station_number) {
		deregisterButtonListeners(station_number);
	}
	
	
	// ** METHOD TO BE CALLED BY SOFTWARE WHEN BAG APPROVAL IS NEEDED **
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
	
}
