package com.autovend.software.ui;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class PaymentView extends JPanel {

	private List<UIEventListener> observers;

	private JTextArea receipt;
	private JLabel amountDueValueLabel;
	private JLabel changeDueValueLabel;
	private JButton cashButton;
	private JButton creditOrDebitButton;
	private JButton giftCardButton;
	private JButton addMoreItemsButton;


	public PaymentView() {
		observers = new ArrayList<>();
		setLayout(null);
		setBackground(new Color(65, 73, 96));
		setMinimumSize(new Dimension(1280, 720));
		setMaximumSize(new Dimension(1280, 720));
		setPreferredSize(new Dimension(1280, 720));
		setSize(new Dimension(1280, 720));

		receipt = new JTextArea("\n Receipt: ");
		receipt.setForeground(new Color(15, 17, 26));
		receipt.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
		receipt.setEditable(false);
		receipt.setBounds(20, 20, 600, 600);
		receipt.setBackground(new Color(142, 185, 151));
		add(receipt);

		JLabel amountDueLabel = new JLabel("Amount Due: ");
		amountDueLabel.setForeground(new Color(137, 221, 255));
		amountDueLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
		amountDueLabel.setBounds(700, 150, 150, 25);
		add(amountDueLabel);

		amountDueValueLabel = new JLabel("0.00");
		amountDueValueLabel.setForeground(new Color(137, 221, 255));
		amountDueValueLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
		amountDueValueLabel.setBounds(900, 150, 150, 25);
		add(amountDueValueLabel);

		JLabel changeDueLabel = new JLabel("Change Due: ");
		changeDueLabel.setForeground(new Color(137, 221, 255));
		changeDueLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
		changeDueLabel.setBounds(700, 200, 150, 25);
		add(changeDueLabel);

		changeDueValueLabel = new JLabel("0.00");
		changeDueValueLabel.setForeground(new Color(137, 221, 255));
		changeDueValueLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
		changeDueValueLabel.setBounds(900, 200, 150, 25);
		add(changeDueValueLabel);

		cashButton = new JButton("Cash");
		cashButton.setOpaque(true);
		cashButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		cashButton.setBorder(new LineBorder(new Color(15, 17, 26), 1, true));
		cashButton.setBackground(new Color(142, 185, 151));
		cashButton.setBounds(700, 250, 280, 55);
		add(cashButton);

		cashButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				notifyPaymentMethod("Cash");
			}
		});

		creditOrDebitButton = new JButton("Credit/Debit");
		creditOrDebitButton.setOpaque(true);
		creditOrDebitButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		creditOrDebitButton.setBorder(new LineBorder(new Color(15, 17, 26), 1, true));
		creditOrDebitButton.setBackground(new Color(142, 185, 151));
		creditOrDebitButton.setBounds(700, 325, 280, 55);
		add(creditOrDebitButton);
		creditOrDebitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				notifyPaymentMethod("Credit/Debit");
			}
		});
		
		giftCardButton = new JButton("Gift Card");
		giftCardButton.setOpaque(true);
		giftCardButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		giftCardButton.setBorder(new LineBorder(new Color(15, 17, 26), 1, true));
		giftCardButton.setBackground(new Color(142, 185, 151));
		giftCardButton.setBounds(700, 400, 280, 55);
		add(giftCardButton);
		giftCardButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				notifyPaymentMethod("Gift Card");
			}
		});
		
		
		addMoreItemsButton = new JButton("Add More Items");
		addMoreItemsButton.setOpaque(true);
		addMoreItemsButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		addMoreItemsButton.setBorder(new LineBorder(new Color(15, 17, 26), 1, true));
		addMoreItemsButton.setBackground(new Color(142, 185, 151));
		addMoreItemsButton.setBounds(700, 475, 280, 55);
		add(addMoreItemsButton);
		addMoreItemsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				notifyAddMoreItems();
			}
		});
		

	}

	public void register(UIEventListener listener) {
		observers.add(listener);
	}
	
	

	public void updateAmountDue(String amountDueText) {
		amountDueValueLabel.setText(amountDueText);
	}

	public void updateChangeDue(String amountDueText) {
		changeDueValueLabel.setText(amountDueText);
	}

	public void updateReceipt(String receiptText) {
		receipt.setText(receiptText); 

	}

	public void notifyPaymentMethod(String paymentMethod) {
		for (UIEventListener listener : observers) {
			listener.onSelectPaymentMethod(paymentMethod);
		}

	}
	
	public void notifyAddMoreItems() {
		for (UIEventListener listener : observers) {
			listener.onStartAddingItems();
		}

	}
}
