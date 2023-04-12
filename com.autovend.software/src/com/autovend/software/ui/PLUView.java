package com.autovend.software.ui;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class PLUView extends JPanel {
	private JTextField inputField;

	private JLabel infoLabel;
	private JLabel notificationLabel;

	private JButton addButton;
	private JButton backButton;
	private List<PLUViewObserver> observers;
	private List<UIEventListener> listeners;

	/**
	 *PLU constructor and swing GUI creations 
	 * 
	 */
	
	public PLUView() {
		observers = new ArrayList<>();
		listeners = new ArrayList<>();

		setLayout(null);
		setBackground(new Color(65, 73, 96));
		setMinimumSize(new Dimension(1280, 720));
		setMaximumSize(new Dimension(1280, 720));
		setPreferredSize(new Dimension(1280, 720));
		setSize(new Dimension(1280, 720));

		infoLabel = new JLabel("Enter PLU Code:");
		infoLabel.setForeground(new Color(137, 221, 255));
		infoLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
		infoLabel.setBounds(515, 150, 400, 25);
		add(infoLabel);

		inputField = new JTextField(20);
		inputField.setBounds(515, 200, 280, 30);
		add(inputField);

		addButton = new JButton("Add Item");
		addButton.setOpaque(true);
		addButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		addButton.setBorder(new LineBorder(new Color(15, 17, 26), 1, true));
		addButton.setBackground(new Color(142, 185, 151));
		addButton.setBounds(515, 250, 280, 55);
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
		add(addButton);

		backButton = new JButton("Go Back");
		backButton.setOpaque(true);
		backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		backButton.setBorder(new LineBorder(new Color(15, 17, 26), 1, true));
		backButton.setBackground(new Color(204, 62, 68));
		backButton.setBounds(515, 325, 280, 55);
		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				notifyGoBackToCheckout();
			}
		});
		add(backButton);

		notificationLabel = new JLabel("");
		notificationLabel.setForeground(new Color(137, 221, 255));
		notificationLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
		notificationLabel.setBounds(515, 400, 400, 25);
		add(notificationLabel);
	}
	
	/**
	 * Notifies all registered UIEventListener objects to go back to the checkout view.
	 */
	
	public void notifyGoBackToCheckout() {
		for (UIEventListener listener : listeners) {
			listener.goBackToCheckout();
		}
	}

	/**
	 * Notifies all registered PLUViewObserver objects that an item has been added using its PLU code.
	 *
	 * @param pluCode the PLU code of the added item
	 */
	
	public void notifyItemAdded(String pluCode) {
		for (PLUViewObserver observer : observers) {
			notificationLabel.setText("Please place item on scale.");
			observer.reactToPLUCodeEntered(pluCode);
		}
	}
	
	/**
	 * Returns the input field component.
	 *
	 * @return the JTextField representing the input field
	 */
	
	public JTextField getInputField() {
		return this.inputField;
	}

	/**
	 * Returns the Add button component.
	 *
	 * @return the JButton representing the Add button
	 */
	
	public JButton getAddButton() {
		return this.addButton;
	}

	/**
	 * Returns the Done button component.
	 *
	 * @return the JButton representing the Done button
	 */
	
	public JButton getDoneButton() {
		return this.backButton;
	}

	/**
	 * Registers a UIEventListener to the list of listeners.
	 *
	 * @param listener the UIEventListener to be added to the list of listeners
	 */
	
	public void register(UIEventListener listener) {
		listeners.add(listener);
	}

	/**
	 * Adds a PLUViewObserver to the list of observers.
	 *
	 * @param observer the PLUViewObserver to be added to the list of observers
	 */
	
	public void addObserver(PLUViewObserver observer) {
		observers.add(observer);
	}

	/**
	 * Removes a PLUViewObserver from the list of observers.
	 *
	 * @param observer the PLUViewObserver to be removed from the list of observers
	 */
	
	public void removeObserver(PLUViewObserver observer) {
		observers.remove(observer);
	}

}
