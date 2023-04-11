package com.autovend.software.ui;

import javax.swing.*;
import java.awt.*;

public class PaymentView extends JPanel {

    private JLabel amountDueLabel;
    private JButton cashPaymentButton;
    private JButton cardPaymentButton;
    private JLabel totalCostLabel;
    private JLabel totalPaidLabel;

    public PaymentView() {
        initComponents();
        setupLayout();
    }

    private void initComponents() {
        amountDueLabel = new JLabel("Amount due: $0.00");
        totalCostLabel = new JLabel("Total Cost: $0.00");
        totalPaidLabel = new JLabel("Total Paid: $0.00");
        cashPaymentButton = new JButton("Pay with Cash");
        cardPaymentButton = new JButton("Pay with Card");
    }

    private void setupLayout() {
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 3;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(10, 10, 10, 10);
        add(amountDueLabel, constraints);

        constraints.gridy = 1;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(10, 10, 10, 10);
        add(cashPaymentButton, constraints);

        constraints.gridx = 1;
        add(cardPaymentButton, constraints);

        constraints.gridx = 2;
        
    }

    public void setAmountDueLabelText(String amountDueText) {
        amountDueLabel.setText(amountDueText);
    }

    public JButton getCashPaymentButton() {
        return cashPaymentButton;
    }

    public JButton getCardPaymentButton() {
        return cardPaymentButton;
    }

}
