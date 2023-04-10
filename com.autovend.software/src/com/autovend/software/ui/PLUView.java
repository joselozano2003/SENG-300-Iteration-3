package com.autovend.software.ui;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.autovend.PriceLookUpCode;
import com.autovend.devices.SelfCheckoutStation;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class PLUView extends JFrame {
	private JPanel panel;
	private JTextField inputField;
	private JTextField quantityField;

	private JLabel infoLabel;
	private JButton addButton;
	private List<PLUViewObserver> observers;

	public PLUView() {

		observers = new ArrayList<>();

		setTitle("Add Item By PLU");
		setSize(400, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		panel = new JPanel();
		inputField = new JTextField(20);
		quantityField = new JTextField(20);
		infoLabel = new JLabel("Enter PLU Code and Quantity for the product:");
		addButton = new JButton("Add Item");
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String pluString = inputField.getText();
				double pluQuantity = Double.valueOf(quantityField.getText());
				if (pluString.equals("")) {
					JOptionPane.showMessageDialog(new JPanel(), "Please enter a PLU code.",
							"Invalid PLU! Please try again!", JOptionPane.ERROR_MESSAGE);
					return;
				}

				notifyItemAdded(pluString, pluQuantity);

			}
		});

		panel.add(infoLabel);
		panel.add(inputField);
		panel.add(quantityField);
		panel.add(addButton);
		add(panel);
	}

	public void addObserver(PLUViewObserver observer) {
		observers.add(observer);
	}

	public void removeObserver(PLUViewObserver observer) {
		observers.remove(observer);
	}

	public void notifyItemAdded(String pluCode, double pluQuantity) {
		for (PLUViewObserver observer : observers) {
			observer.reactToPLUCodeEntered(pluCode, pluQuantity);
		}
		JOptionPane.showMessageDialog(new JPanel(), "Item with PLU code " + pluCode + " was added to the cart.",
				"Item is added", JOptionPane.PLAIN_MESSAGE);
	}

	private static void createAndShowGUI() {
		PLUView frame = new PLUView();
		frame.setVisible(true);
	}

	
}
