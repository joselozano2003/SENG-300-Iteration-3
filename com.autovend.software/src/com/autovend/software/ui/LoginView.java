package com.autovend.software.ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import auth.AttendantAccount;

public class LoginView extends JPanel {
	
	private JFrame loginFrame;
	private JPanel loginPanel;
	private JLabel userLabel;
	private JTextField userText;
	private JLabel passLabel;
	private JTextField passText;
	private JLabel errorLabel;
	private JButton loginButton;
	
	List<UIEventListener> listeners;
	
	public LoginView() {
		JFrame loginFrame = new JFrame("Attendant Login");
		JPanel loginPanel = new JPanel();
		loginPanel.setLayout(new GridLayout(3, 2));
		
		JLabel userLabel = new JLabel("Username:");
		JTextField userText = new JTextField();
		JLabel passLabel = new JLabel("Password:");
		JTextField passText = new JTextField();
		JLabel errorLabel = new JLabel("");
		JButton loginButton = new JButton("Login");
		loginButton.addActionListener(e -> {
			// When button is pressed, have UI event listeners react to loginEvent.
			for (UIEventListener listener : listeners) listener.reactToLoginEvent();
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

}
