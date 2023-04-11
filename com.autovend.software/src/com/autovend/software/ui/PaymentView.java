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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;

import com.autovend.software.BankIO;

public class PaymentView<object> extends JPanel{

    private  TextArea receipt;
    private  JTextArea infoText;
    private JButton returnAddItems;
    private JButton doneButton;
    private com.autovend.devices.SelfCheckoutStation SelfCheckoutStation;
    private String num;
    JPanel jPanel = this;
    private ArrayList<Coin> coins = new ArrayList<Coin>();
    private ArrayList<Bill> bills = new ArrayList<Bill>();
    private Object value;
    private Object currency;
    private object validator;

    CustomerSession session = new CustomerSession();
    private BigDecimal amountEntered;



    public PaymentView(){


        jPanel.setForeground(new Color(65, 73, 96));
        jPanel.setBackground(new Color(65, 73, 96));
        jPanel.setMinimumSize(new Dimension(1280, 720));
        jPanel.setMaximumSize(new Dimension(1280, 720));
        jPanel.setPreferredSize(new Dimension(1280, 720));
        jPanel.setSize(new Dimension(1280, 720));
        jPanel.setLayout(null);


        JTextArea receipt = new JTextArea("\n Receipt: ");
        receipt.validate();
        receipt.setForeground(new Color(15, 17, 26));
        receipt.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
        receipt.setEditable(false);
        receipt.setBounds(20, 20, 480, 510);
        receipt.setBackground(new Color(142, 185, 151));
        jPanel.add(receipt);

        JTextArea infoText = new JTextArea();
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

        JButton doneButton = new JButton("Done");

        doneButton.setOpaque(true);
        doneButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        doneButton.setBorder(new LineBorder(new Color(15, 17, 26), 1, true));
        doneButton.setBackground(new Color(40, 167, 69));
        doneButton.setBounds(980, 475, 280, 55);
        doneButton.setVisible(false);
        jPanel.add(doneButton);



        JButton returnAddItems = new JButton("Return To Adding Items");

        returnAddItems.setOpaque(true);
        returnAddItems.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        returnAddItems.setBorder(new LineBorder(new Color(15, 17, 26), 1, true));
        returnAddItems.setBackground(new Color(204, 62, 68));
        returnAddItems.setBounds(980, 650, 280, 55);
        jPanel.add(returnAddItems);



        JButton debitCard = new JButton("Pay with Credit / Debit Card");

        debitCard.setOpaque(true);
        debitCard.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        debitCard.setBorder(new LineBorder(new Color(15, 17, 26), 1, true));
        debitCard.setBackground(new Color(255, 203, 107));
        debitCard.setBounds(980, 325 , 280, 55);
        jPanel.add(debitCard);

        

        JButton giftCard = new JButton("Pay with Gift Card");

        giftCard.setOpaque(true);
        giftCard.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        giftCard.setBorder(new LineBorder(new Color(15, 17, 26), 1, true));
        giftCard.setBackground(new Color(255, 203, 107));
        giftCard.setBounds(980, 250 , 280, 55);
        jPanel.add(giftCard);

        JButton cash = new JButton("Pay with Cash / Coin");


        cash.setOpaque(true);
        cash.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        cash.setBorder(new LineBorder(new Color(15, 17, 26), 1, true));
        cash.setBackground(new Color(255, 203, 107));
        cash.setBounds(980, 175 , 280, 55);
        jPanel.add(cash);

        JLabel title =  new JLabel("Check Out");
        title.setFont(new Font("Lucida Grande", Font.BOLD, 60));
        title.setForeground(new Color(255, 255, 255));
        title.setBounds(570, 20, 656, 72);
        jPanel.add(title);
        


    }

    
    public void setAmountDueLabelText(String amountDueText) {
        return;
    }
    
    public static void main(String[] args) {
    	JFrame frame = new JFrame();
    	frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setUndecorated(true);
		frame.setVisible(true);
		frame.add(new PaymentView());
		frame.validate();
    }

}
