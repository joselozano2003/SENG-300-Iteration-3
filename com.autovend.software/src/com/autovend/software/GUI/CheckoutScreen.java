package com.autovend.software.GUI;


import com.autovend.Card;
import com.autovend.GiftCard;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.external.CardIssuer;
import com.autovend.software.customer.CustomerController;
import com.autovend.software.customer.CustomerSession;
import com.autovend.software.payment.GiftCardDatabase;
import com.autovend.software.payment.PayWithCard;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.math.BigDecimal;
import com.autovend.software.BankIO;
import com.autovend.software.payment.PayWithGiftCard;

public class CheckoutScreen extends JPanel{

    private  TextArea receipt;
    private  JTextArea infoText;
    private JButton returnAddItems;
    private JButton doneButton;
    private com.autovend.devices.SelfCheckoutStation SelfCheckoutStation;
    private String num;
    JPanel jPanel = new JPanel();

    PayWithCard payWithCard = new PayWithCard(SelfCheckoutStation);
    PayWithGiftCard payWithGiftCard = new PayWithGiftCard(SelfCheckoutStation);
    CustomerSession session = new CustomerSession();

    private BigDecimal amountEntered;
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

        update();

        JButton doneButton = new JButton("Done");
        doneButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //session.loadStartGUI();
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
                //session.loadMainGUI();
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
                String creditCardNumber = JOptionPane.showInputDialog("Please enter your Credit Card Number: ", "");
                if(creditCardNumber.equals("")){
                    JOptionPane.showMessageDialog(new JPanel(), "Invalid Inputs!",
                            "Please Try Again!",
                            JOptionPane.ERROR_MESSAGE);
                    return;

                }
                if(BankIO.CARD_ISSUER_DATABASE.containsKey(creditCardNumber)) {
                    CardIssuer card = BankIO.CARD_ISSUER_DATABASE.get(creditCardNumber);
                    Object[] optionToPay = {"Tap", "Insert", "Swipe"};
                    int cardPayType = JOptionPane.showOptionDialog(
                            new JPanel(), "Please choose how you like to use your card",
                            "Pay With Credit Card",
                            JOptionPane.YES_NO_CANCEL_OPTION,
                            JOptionPane.PLAIN_MESSAGE,
                            null, optionToPay,
                            optionToPay[0]
                    );
                    boolean transactionApproved = false;
                    try {
                        if (cardPayType == 0) {
                            transactionApproved = payWithCard.tapCard(card);
                        } else if (cardPayType == 1) {
                            String pin = JOptionPane.showInputDialog("Please enter Card's PIN number: ", "");
                            transactionApproved = payWithCard.insertCard(card, pin);
                        } else if (cardPayType == 2) {
                            transactionApproved = payWithCard.swipeCard(card, null);
                        } else {
                            JOptionPane.showMessageDialog(new JPanel(),
                                    "You did not choose a valid method for payment", "Error!", JOptionPane.ERROR_MESSAGE);
                        }


                    } catch (Exception error) {
                        transactionApproved = false;
                    }
                    if (transactionApproved) {
                        amountEntered = amountEntered.add(session.getTotalCost());
                        JOptionPane.showMessageDialog(new JPanel(),
                                "Payment Successful", "Success!", JOptionPane.PLAIN_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(new JPanel(), "Payment Failed!", "Error!", JOptionPane.ERROR_MESSAGE);
                    }
                }else {
                    JOptionPane.showMessageDialog(new JPanel(), "Card Data not found!", "Error!", JOptionPane.ERROR_MESSAGE);


                }

                update();

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

                String debitCardNumber = JOptionPane.showInputDialog("Please enter your Credit Card Number: ", "");
                if(debitCardNumber.equals("")){
                    JOptionPane.showMessageDialog(new JPanel(), "Invalid Inputs!",
                            "Please Try Again!",
                            JOptionPane.ERROR_MESSAGE);
                    return;

                }
                if(BankIO.CARD_ISSUER_DATABASE.containsKey(debitCardNumber)) {
                    CardIssuer card = BankIO.CARD_ISSUER_DATABASE.get(debitCardNumber);
                    Object[] optionToPay = {"Tap", "Insert", "Swipe"};
                    int cardPayType = JOptionPane.showOptionDialog(
                            new JPanel(), "Please choose how you like to use your card",
                            "Pay With Debit Card",
                            JOptionPane.YES_NO_CANCEL_OPTION,
                            JOptionPane.PLAIN_MESSAGE,
                            null, optionToPay,
                            optionToPay[0]
                    );
                    boolean transactionApproved = false;
                    try {
                        if (cardPayType == 0) {
                            transactionApproved = payWithCard.tapCard(card);
                        } else if (cardPayType == 1) {
                            String pin = JOptionPane.showInputDialog("Please enter Card's PIN number: ", "");
                            transactionApproved = payWithCard.insertCard(card, pin);
                        } else if (cardPayType == 2) {
                            transactionApproved = payWithCard.swipeCard(card, null);
                        } else {
                            JOptionPane.showMessageDialog(new JPanel(),
                                    "You did not choose a valid method for payment", "Error!", JOptionPane.ERROR_MESSAGE);
                        }


                    } catch (Exception error) {
                        transactionApproved = false;
                    }
                    if (transactionApproved) {
                        amountEntered = amountEntered.add(session.getTotalCost());
                        JOptionPane.showMessageDialog(new JPanel(),
                                "Payment Successful", "Success!", JOptionPane.PLAIN_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(new JPanel(), "Payment Failed!", "Error!", JOptionPane.ERROR_MESSAGE);
                    }
                }else {
                    JOptionPane.showMessageDialog(new JPanel(), "Card Data not found!", "Error!", JOptionPane.ERROR_MESSAGE);
                }

                update();


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
                String giftCardNumber = JOptionPane.showInputDialog("Please enter your Gift Card Number: ", "");
                if(giftCardNumber.equals("")){
                    JOptionPane.showMessageDialog(new JPanel(), "Invalid Inputs!",
                            "Please Try Again!",
                            JOptionPane.ERROR_MESSAGE);
                    return;

                }

                if(GiftCardDatabase.getGiftCard(String num) == giftCardNumber) {
                    CardIssuer card = BankIO.CARD_ISSUER_DATABASE.get(giftCardNumber);
                    Object[] optionToPay = {"Insert", "Swipe"};
                    int cardPayType = JOptionPane.showOptionDialog(
                            new JPanel(), "Please choose how you like to use your gift card",
                        "Pay With Gift Card",
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE,
                        null, optionToPay,
                        optionToPay[0]
                );
                boolean transactionApproved = false;
                try {
                    if (cardPayType == 0) {
                        String pin = JOptionPane.showInputDialog("Please enter Card's PIN number: ", "");
                        transactionApproved = payWithGiftCard.insertCard(card, pin);
                    } else if (cardPayType == 1) {
                        transactionApproved = payWithGiftCard.swipeCard(card, null);
                    } else {
                        JOptionPane.showMessageDialog(new JPanel(),
                                "You did not choose a valid method for payment", "Error!", JOptionPane.ERROR_MESSAGE);
                    }


                } catch (Exception error) {
                    transactionApproved = false;
                }
                if (transactionApproved) {
                    amountEntered = amountEntered.add(session.getTotalCost());
                    JOptionPane.showMessageDialog(new JPanel(),
                            "Payment Successful", "Success!", JOptionPane.PLAIN_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(new JPanel(), "Payment Failed!", "Error!", JOptionPane.ERROR_MESSAGE);
                }
            }else {
                JOptionPane.showMessageDialog(new JPanel(), "Card Data not found!", "Error!", JOptionPane.ERROR_MESSAGE);
            }

            update();


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
    private void update(){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                infoText.setText("" + getInfoText());
                boolean paid = false;
                if(amountEntered.compareTo(session.getTotalCost()) >= 0){
                    paid = true;
                    try {
                    // generate receipt from the session
                    }catch (Exception error){
                        JOptionPane.showMessageDialog(new JPanel(),"Failed to generate receipt!", "Error!", JOptionPane.ERROR_MESSAGE);
                    }
                }
                if(paid){
                    returnAddItems.setVisible(false);
                    doneButton.setVisible(true);
                }

            }
        });
    }


    private String getInfoText(){
        return "\n Total: " + session.getTotalCost().toString() + "\n Entered: " + amountEntered.doubleValue() + "\n Changes Due: " + amountEntered.subtract(session.getTotalCost());
    }

}
