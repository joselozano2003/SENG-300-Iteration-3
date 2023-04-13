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

public class StartView extends JPanel {
    private JButton startButton;
    private JButton addMembershipNumberButton;
    private List<CustomerUIEventListener> observers;
    
    /**
     * Constructor for start view with SWING guy creation and functionality
     */
    
    public StartView() {
        observers = new ArrayList<>();
        setLayout(null);
        setForeground(new Color(65, 73, 96));
        setBackground(new Color(65, 73, 96));
        setMinimumSize(new Dimension(1280, 720));
        setMaximumSize(new Dimension(1280, 720));
        setPreferredSize(new Dimension(1280, 720));
        setSize(new Dimension(1280, 720));

        startButton = new JButton("Start");
        startButton.setOpaque(true);
        startButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        startButton.setBorder(new LineBorder(new Color(15, 17, 26), 1, true));
        startButton.setBackground(new Color(255, 203, 107));
        startButton.setBounds(500, 325, 280, 70);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                notifyStartButtonPressed();
            }
        });
        add(startButton);
        
        addMembershipNumberButton = new JButton("Add Membership Number");
        addMembershipNumberButton.setOpaque(true);
        addMembershipNumberButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        addMembershipNumberButton.setBorder(new LineBorder(new Color(15, 17, 26), 1, true));
        addMembershipNumberButton.setBackground(new Color(255, 203, 107));
        addMembershipNumberButton.setBounds(500, 425, 280, 70);
        addMembershipNumberButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                notifyAddMembershipButtonPressed();
            }
        });
        add(addMembershipNumberButton);

        JLabel title = new JLabel("Welcome");
        title.setFont(new Font("Lucida Grande", Font.BOLD, 60));
        title.setForeground(new Color(255, 255, 255));
        title.setBounds(570, 20, 656, 72);
        add(title);
        
        
    }

    /**
     * Registers a UIEventListener to the list of observers.
     *
     * @param listener the UIEventListener to be added to the list of observers
     */
    
    public void register(CustomerUIEventListener listener) {
        observers.add(listener);
    }


/**
 * Notifies all registered UIEventListener objects that the start button has been pressed.
 */
    
    public void notifyStartButtonPressed() {
        for (CustomerUIEventListener listener : observers) {
            listener.onStartAddingItems();
        }
    }
    
    public void notifyAddMembershipButtonPressed() {
        for (CustomerUIEventListener listener : observers) {
            listener.onAddMembershipNumber();
        }
    }
}
