package com.autovend.software.ui;

import java.awt.*;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import auth.AttendantAccount;

public class LoginView extends JPanel {
	
	private JFrame loginFrame;
	private JLabel userLabel;
	private JTextField userText;
	private JLabel passLabel;
	private JTextField passText;
	private JLabel errorLabel;
	private JButton loginButton;
	
	List<AttendantUIEventListener> listeners;
	
	/**
	 * Constructor for the Log in View which constructs the Swing 
	 * GUI and functionality for the Buttons 
	 */

	public static void main(String[] args) {
		JFrame testFrame = new JFrame();
		LoginView testView = new LoginView();
		testFrame.add(testView);
		testFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		testFrame.setUndecorated(true);
		testFrame.setVisible(true);
		testFrame.validate();

		testView.LoginFailure();

	}
	
	public LoginView() {

		this.setLayout(new GridLayout(3, 2));
		
		JLabel userLabel = new JLabel("Username:");
		JTextField userText = new JTextField();
		JLabel passLabel = new JLabel("Password:");
		JTextField passText = new JTextField();
		JLabel errorLabel = new JLabel("");
		JButton loginButton = new JButton("Login");
		
		loginButton.addActionListener(e -> {
			// When button is pressed, have UI event listeners react to loginEvent.
			if (listeners == null) return;
			AttendantAccount account = new AttendantAccount(userText.getText(), passText.getText());
			for (AttendantUIEventListener listener : listeners) listener.onAttendantLoginAttempt(account);
		});


		
		this.add(userLabel);
		this.add(userText);
		this.add(passLabel);
		this.add(passText);
		this.add(errorLabel);
		this.add(loginButton);

	}


	public void LoginFailure() {
		JFrame failFrame = new JFrame();
		JPanel failPanel = new JPanel();
		failPanel.setLayout(new BorderLayout());
		JLabel failLabel = new JLabel("Login unsuccessful, please try again.");

		failFrame.setLayout(new BorderLayout());
		failPanel.setPreferredSize(new Dimension(400,200));
		failFrame.add(failPanel, BorderLayout.CENTER);
		failFrame.setLocationRelativeTo(null);

		failFrame.setSize(400,200);
		failPanel.add(failLabel, BorderLayout.CENTER);
		failFrame.setVisible(true);
		failFrame.validate();
	}

}
