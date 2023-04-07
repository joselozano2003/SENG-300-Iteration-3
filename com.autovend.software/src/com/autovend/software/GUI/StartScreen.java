package com.autovend.software.GUI;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class StartScreen {
    JPanel jPanel = new JPanel();

    public StartScreen(Controller controller){
    	
   	 jPanel.setForeground(new Color(65, 73, 96));
     jPanel.setBackground(new Color(65, 73, 96));
     jPanel.setMinimumSize(new Dimension(1280, 720));
     jPanel.setMaximumSize(new Dimension(1280, 720));
     jPanel.setPreferredSize(new Dimension(1280, 720));
     jPanel.setSize(new Dimension(1280, 720));
     jPanel.setLayout(null);

        JButton goToMainScreen = new JButton("Start");
        goToMainScreen.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                controller.loadMainGUI();
            }
        });

        goToMainScreen.setOpaque(true);
        goToMainScreen.setBorder(new LineBorder(new Color(15,17,26),1, true));
        goToMainScreen.setBackground(new Color(142, 185, 151));
        goToMainScreen.setBounds(500, 332, 280, 55);
        jPanel.add(goToMainScreen);

        JLabel title = new JLabel("Self Checkout Station");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setFont(new Font("Apple Color Emoji", Font.BOLD, 60));

        title.setForeground(new Color(98, 145, 139));
        title.setBounds(6,20,1268,300);
        jPanel.add(title);



    }



}