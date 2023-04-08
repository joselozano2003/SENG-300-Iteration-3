package com.autovend.software.GUI;


import com.autovend.software.customer.CustomerSession;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CheckoutScreen {

    private  TextArea receipt;
    JPanel jPanel = new JPanel();
    CustomerSession session = new CustomerSession();

    public CheckoutScreen(){


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

//        updatePanel();

        JButton doneButton = new JButton("Done");
        doneButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                session.loadStartGUI();
            }
        });

        doneButton.setOpaque(true);
        doneButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        doneButton.setBorder(new LineBorder(new Color(15, 17, 26), 1, true));
        doneButton.setBackground(new Color(40, 167, 69));
        doneButton.setBounds(980, 475, 280, 55);
        doneButton.setVisible(false);
        jPanel.add(doneButton);



        JButton returnAddItems = new JButton("Return To Adding Items");
        returnAddItems.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                session.loadMainGUI();
            }
        });
        returnAddItems.setOpaque(true);
        returnAddItems.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        returnAddItems.setBorder(new LineBorder(new Color(15, 17, 26), 1, true));
        returnAddItems.setBackground(new Color(204, 62, 68));
        returnAddItems.setBounds(980, 650, 280, 55);
        jPanel.add(returnAddItems);



        JButton creditCard = new JButton("Pay with Credit Card");
        creditCard.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //

            }});
                creditCard.setOpaque(true);
                creditCard.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                creditCard.setBorder(new LineBorder(new Color(15, 17, 26), 1, true));
                creditCard.setBackground(new Color(255, 203, 107));
                creditCard.setBounds(980, 400 , 280, 55);
                jPanel.add(creditCard);



        JButton debitCard = new JButton("Pay with Debit Card");
        debitCard.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {


            }});
        debitCard.setOpaque(true);
        debitCard.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        debitCard.setBorder(new LineBorder(new Color(15, 17, 26), 1, true));
        debitCard.setBackground(new Color(255, 203, 107));
        debitCard.setBounds(980, 325 , 280, 55);
        jPanel.add(debitCard);



        JButton giftCard = new JButton("Pay with Gift Card");
        giftCard.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {


            }});
        giftCard.setOpaque(true);
        giftCard.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        giftCard.setBorder(new LineBorder(new Color(15, 17, 26), 1, true));
        giftCard.setBackground(new Color(255, 203, 107));
        giftCard.setBounds(980, 250 , 280, 55);
        jPanel.add(giftCard);


        JButton cash = new JButton("Pay with Cash");
        giftCard.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {


            }});
        cash.setOpaque(true);
        cash.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        cash.setBorder(new LineBorder(new Color(15, 17, 26), 1, true));
        cash.setBackground(new Color(255, 203, 107));
        cash.setBounds(980, 175 , 280, 55);
        jPanel.add(cash);




        JButton coin = new JButton("Pay with Coin");
        giftCard.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {


            }});
        coin.setOpaque(true);
        coin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        coin.setBorder(new LineBorder(new Color(15, 17, 26), 1, true));
        coin.setBackground(new Color(255, 203, 107));
        coin.setBounds(980, 100 , 280, 55);
        jPanel.add(coin);


        JLabel title =  new JLabel("Check Out");
        title.setFont(new Font("Lucida Grande", Font.BOLD, 60));
        title.setForeground(new Color(255, 255, 255));
        title.setBounds(570, 20, 656, 72);
        jPanel.add(title);










    }
    }
