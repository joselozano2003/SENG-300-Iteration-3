package com.autovend.software.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.autovend.products.Product;

import auth.AttendantAccount;

public class LoginView extends JPanel {
	
	private JFrame loginFrame;
	private JLabel userLabel;
	private JTextField userText;
	private JLabel passLabel;
	private JTextField passText;
	private JLabel errorLabel;
	private JButton loginButton;
	
	public String userEntered;
	public String passEntered;
	
	public LoginListener loginListen;
	
	public List<UIEventListener> listeners;
	
	// Can be set during testing to not display windows and not get text from fields.
	public boolean override = false;
	
	/**
	 * Constructor for the Log in View wich contructs the Swing 
	 * GUI and functionality for the Buttons 
	 */
	
	public LoginView() {
		
		loginFrame = new JFrame("Login view");

		this.setLayout(new GridLayout(3, 2));
		
		userLabel = new JLabel("Username:");
		userText = new JTextField();
		passLabel = new JLabel("Password:");
		passText = new JTextField();
		errorLabel = new JLabel("");
		loginButton = new JButton("Login");
		
		loginListen = new LoginListener();
		loginButton.addActionListener(loginListen);


		
		this.add(userLabel);
		this.add(userText);
		this.add(passLabel);
		this.add(passText);
		this.add(errorLabel);
		this.add(loginButton);
		
		loginFrame.add(this);
    	
    	loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
    	if (!override) loginFrame.setVisible(true);

	}


	public void loginFailure() {
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
	
	
	public class LoginListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// When button is pressed, have UI event listeners react to loginEvent.
			if (listeners == null) return;
			if (!override) {
				userEntered = userText.getText();
				passEntered = passText.getText();
			}
			AttendantAccount account = new AttendantAccount(userEntered, passEntered);
			for (UIEventListener listener : listeners) listener.onAttendantLoginAttempt(account);
			
		}

		
	}

}
