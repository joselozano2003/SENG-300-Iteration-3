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

public class PLUView extends JPanel {
	private JPanel panel;
	private JTextField inputField;

	private JLabel infoLabel;
	private JButton addButton;
	private JButton backButton;
	private List<PLUViewObserver> observers;
	private List <UIEventListener> listeners;

	public PLUView() {

		observers = new ArrayList<>();
		listeners = new ArrayList<>();

		setSize(400, 400);

		panel = new JPanel();
		inputField = new JTextField(20);
		infoLabel = new JLabel("Enter PLU Code and Quantity for the product:");
		addButton = new JButton("Add Item");
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String pluString = inputField.getText();
				if (pluString.equals("")) {
					JOptionPane.showMessageDialog(new JPanel(), "Please enter a PLU code.",
							"Invalid PLU! Please try again!", JOptionPane.ERROR_MESSAGE);
					return;
				}

				notifyItemAdded(pluString);

			}
		});
		
		backButton = new JButton("Go Back");
		
		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				notifyGoBackToCheckout();

			}
		});
		

		panel.add(infoLabel);
		panel.add(inputField);
		panel.add(addButton);
		panel.add(backButton);
		add(panel);
	}

	public void notifyGoBackToCheckout() {
		for (UIEventListener listener : listeners) {
            listener.goBackToCheckout();
        }
		
	}
	
	
	public void register(UIEventListener listener) {
		listeners.add(listener);
	}

	public void addObserver(PLUViewObserver observer) {
		observers.add(observer);
	}

	public void removeObserver(PLUViewObserver observer) {
		observers.remove(observer);
	}

	public void notifyItemAdded(String pluCode) {
		for (PLUViewObserver observer : observers) {
			observer.reactToPLUCodeEntered(pluCode);
		}
		JOptionPane.showMessageDialog(new JPanel(), "Please place item on scale.",
				"Notification", JOptionPane.PLAIN_MESSAGE);
	}

}
