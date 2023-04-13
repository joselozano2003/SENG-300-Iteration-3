// P3-4 Group Members
//
// Abdelrhafour, Achraf (30022366)
// Campos, Oscar (30057153)
// Cavilla, Caleb (30145972)
// Crowell, Madeline (30069333)
// Debebe, Abigia (30134608)
// Dhuka, Sara Hazrat (30124117)
// Drissi, Khalen (30133707)
// Ferreira, Marianna (30147733)
// Frey, Ben (30088566)
// Himel, Tanvir (30148868)
// Huayhualla Arce, Fabricio (30091238)
// Kacmar, Michael (30113919)
// Lee, Jeongah (30137463)
// Li, Ran (10120152)
// Lokanc, Sam (30114370)
// Lozano Cetina, Jose Camilo (30144736)
// Maahdie, Monmoy (30149094)
// Malik, Akansha (30056048)
// Mehedi, Abdullah (30154770)
// Polton, Scott (30138102)
// Rahman, Saadman (30153482)
// Rodriguez, Gabriel (30162544)
// Samin Rashid, Khondaker (30143490)
// Sloan, Jaxon (30123845)
// Tran, Kevin (30146900)
//

package com.autovend.software.ui;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class MembershipView extends JPanel {
	private JTextField inputField;

	private JLabel infoLabel;
	private JLabel notificationLabel;

	private JButton addButton;
	private JButton backButton;

	private List<MembershipViewObserver> observers;
	private List<CustomerUIEventListener> listeners;

	public MembershipView() {
		observers = new ArrayList<>();
		listeners = new ArrayList<>();

		setLayout(null);
		setBackground(new Color(65, 73, 96));
		setMinimumSize(new Dimension(1280, 720));
		setMaximumSize(new Dimension(1280, 720));
		setPreferredSize(new Dimension(1280, 720));
		setSize(new Dimension(1280, 720));

		infoLabel = new JLabel("Enter Membership Number, or use the Card:");
		infoLabel.setForeground(new Color(137, 221, 255));
		infoLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
		infoLabel.setBounds(515, 150, 400, 25);
		add(infoLabel);

		inputField = new JTextField(20);
		inputField.setBounds(515, 200, 280, 30);
		add(inputField);

		addButton = new JButton("Confirm Number");
		addButton.setOpaque(true);
		addButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		addButton.setBorder(new LineBorder(new Color(15, 17, 26), 1, true));
		addButton.setBackground(new Color(142, 185, 151));
		addButton.setBounds(515, 250, 280, 55);
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String membershipString = inputField.getText();

				notifyMembershipNumberEntered(membershipString);
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

	public void notifyGoBackToCheckout() {
		for (CustomerUIEventListener listener : listeners) {
			listener.goBackToCheckout();
		}
	}

	public void notifyMembershipNumberEntered(String number) {
		for (MembershipViewObserver observer : observers) {
			notificationLabel.setText("Thanks for entering membership number.");
			observer.reactToMembershipCodeEntered(number);
			
		}
	}

	public JTextField getInputField() {
		return this.inputField;
	}

	public JButton getAddButton() {
		return this.addButton;
	}

	public JButton getDoneButton() {
		return this.backButton;
	}

	public void register(CustomerUIEventListener listener) {
		listeners.add(listener);
	}

	public void addObserver(MembershipViewObserver observer) {
		observers.add(observer);
	}

	public void removeObserver(MembershipViewObserver observer) {
		observers.remove(observer);
	}

}
