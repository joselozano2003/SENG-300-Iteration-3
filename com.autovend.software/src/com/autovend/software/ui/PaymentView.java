package com.autovend.software.ui;

import com.autovend.Bill;
import com.autovend.Card;
import com.autovend.Coin;
import com.autovend.GiftCard;
import com.autovend.devices.*;
import com.autovend.devices.observers.BillValidatorObserver;
import com.autovend.devices.observers.CoinValidatorObserver;
import com.autovend.external.CardIssuer;
import com.autovend.software.customer.CustomerController;
import com.autovend.software.customer.CustomerSession;
import com.autovend.software.payment.*;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import com.autovend.software.BankIO;

public class PaymentView extends JPanel {

	private JTextArea receipt;
	private JTextArea infoText;
	private JButton returnAddItems;
	private JButton doneButton;

	private JLabel amountDueValueLabel;
	private JLabel changeDueValueLabel;

	private String num;
	JPanel jPanel = this;

	private BigDecimal amountEntered;

	private List<UIEventListener> listeners;

	public PaymentView() {

		listeners = new ArrayList<>();

		jPanel.setForeground(new Color(65, 73, 96));
		jPanel.setBackground(new Color(65, 73, 96));
		jPanel.setMinimumSize(new Dimension(1280, 720));
		jPanel.setMaximumSize(new Dimension(1280, 720));
		jPanel.setPreferredSize(new Dimension(1280, 720));
		jPanel.setSize(new Dimension(1280, 720));
		jPanel.setLayout(null);

		receipt = new JTextArea("\n Receipt: ");
		receipt.validate();
		receipt.setForeground(new Color(15, 17, 26));
		receipt.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
		receipt.setEditable(false);
		receipt.setBounds(20, 20, 480, 510);
		receipt.setBackground(new Color(142, 185, 151));
		jPanel.add(receipt);

		infoText = new JTextArea();
		infoText.validate();
		infoText.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		infoText.setForeground(new Color(137, 221, 255));
		infoText.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
		infoText.setEditable(false);
		infoText.setLineWrap(true);
		infoText.setBounds(20, 550, 480, 150);
		infoText.setBorder(new LineBorder(new Color(137, 221, 255), 1, true));
		infoText.setBackground(new Color(15, 17, 26));
		jPanel.add(infoText);

		// Amount Due Label
		JLabel amountDueLabel = new JLabel("Amount Due: ");
		amountDueLabel.setForeground(new Color(137, 221, 255));
		amountDueLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
		amountDueLabel.setBounds(515, 150, 150, 25);
		jPanel.add(amountDueLabel);

		// Amount Due Value Label
		amountDueValueLabel = new JLabel("0.00");
		amountDueValueLabel.setForeground(new Color(137, 221, 255));
		amountDueValueLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
		amountDueValueLabel.setBounds(670, 150, 150, 25);
		jPanel.add(amountDueValueLabel);

		// Change Due Label
		JLabel changeDueLabel = new JLabel("Change Due: ");
		changeDueLabel.setForeground(new Color(137, 221, 255));
		changeDueLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
		changeDueLabel.setBounds(515, 200, 150, 25);
		jPanel.add(changeDueLabel);

		// Change Due Value Label
		changeDueValueLabel = new JLabel("0.00");
		changeDueValueLabel.setForeground(new Color(137, 221, 255));
		changeDueValueLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
		changeDueValueLabel.setBounds(670, 200, 150, 25);
		jPanel.add(changeDueValueLabel);

		JButton doneButton = new JButton("Done");
		doneButton.setOpaque(true);
		doneButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		doneButton.setBorder(new LineBorder(new Color(15, 17, 26), 1, true));
		doneButton.setBackground(new Color(40, 167, 69));
		doneButton.setBounds(980, 475, 280, 55);
		doneButton.setVisible(false);
		jPanel.add(doneButton);

		JButton returnAddItems = new JButton("Return To Adding Items");
		returnAddItems.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				notifyGoBackToCheckout();

			}
		});

		returnAddItems.setOpaque(true);
		returnAddItems.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		returnAddItems.setBorder(new LineBorder(new Color(15, 17, 26), 1, true));
		returnAddItems.setBackground(new Color(204, 62, 68));
		returnAddItems.setBounds(980, 650, 280, 55);
		jPanel.add(returnAddItems);

		JButton debitCard = new JButton("Pay with Credit / Debit Card");
		debitCard.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				notifyPaymentMethod("Debit/Credit Card");
			}
		});

		debitCard.setOpaque(true);
		debitCard.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		debitCard.setBorder(new LineBorder(new Color(15, 17, 26), 1, true));
		debitCard.setBackground(new Color(255, 203, 107));
		debitCard.setBounds(980, 325, 280, 55);
		jPanel.add(debitCard);

		JButton giftCard = new JButton("Pay with Gift Card");
		giftCard.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				notifyPaymentMethod("Gift Card");
			}
		});

		giftCard.setOpaque(true);
		giftCard.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		giftCard.setBorder(new LineBorder(new Color(15, 17, 26), 1, true));
		giftCard.setBackground(new Color(255, 203, 107));
		giftCard.setBounds(980, 250, 280, 55);
		jPanel.add(giftCard);

		JButton cash = new JButton("Pay with Cash");
		cash.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				notifyPaymentMethod("Cash");
			}
		});

		cash.setOpaque(true);
		cash.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		cash.setBorder(new LineBorder(new Color(15, 17, 26), 1, true));
		cash.setBackground(new Color(255, 203, 107));
		cash.setBounds(980, 175, 280, 55);
		jPanel.add(cash);

		JLabel title = new JLabel("Check Out");
		title.setFont(new Font("Lucida Grande", Font.BOLD, 60));
		title.setForeground(new Color(255, 255, 255));
		title.setBounds(570, 20, 656, 72);
		jPanel.add(title);

	}

	public void notifyPaymentMethod(String payment) {
		for (UIEventListener listener : listeners) {
			infoText.setText(payment);
			listener.onSelectPaymentMethod(payment);
		}
	}

	public void register(UIEventListener listener) {
		listeners.add(listener);
	}

	public void notifyGoBackToCheckout() {
		for (UIEventListener listener : listeners) {
			listener.goBackToCheckout();
		}
	}

	public void updateAmountDue(String amountDueText) {
		amountDueValueLabel.setText(amountDueText);
	}
	
	public void updateChangeDue(String amountDueText) {
		changeDueValueLabel.setText(amountDueText);
	}
	
	public void updateReceipt(String receiptText) {
		receipt.setText(receiptText); // Clear the current content
	    receipt.setCaretPosition(0); // Set the caret position to the beginning
	    receipt.repaint(); // Repaint the JTextArea
	    receipt.revalidate(); // Revalidate the JTextArea
	}

	

}
