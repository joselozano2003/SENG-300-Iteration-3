package com.autovend.software.GUI;

import com.autovend.software.customer.CustomerSession;
import com.autovend.software.customer.CustomerView;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class StartScreen {
	JFrame thisFrame = new JFrame();
    JPanel jPanel = new JPanel();
    CustomerSession session = new CustomerSession();
    
    int WIDTH = 1280;
    int HEIGHT = 720;

    public StartScreen(CustomerView view) {
    	
     
   	 jPanel.setForeground(new Color(65, 73, 96));
     jPanel.setBackground(new Color(65, 73, 96));
     jPanel.setMinimumSize(new Dimension(WIDTH, HEIGHT));
     jPanel.setMaximumSize(new Dimension(WIDTH, HEIGHT));
     jPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
     jPanel.setSize(new Dimension(WIDTH, HEIGHT));
     jPanel.setLayout(null);
     

        JButton goToMainScreen = new JButton("Start");
        goToMainScreen.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //StartScreen.this.session.loadMainGUI(); will connect to mainScreen
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
        thisFrame.add(jPanel);
        thisFrame.pack();
        thisFrame.setVisible(true);
        thisFrame.setLocationRelativeTo(view.mainFrame);
        
        
        
        goToMainScreen.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		thisFrame.setVisible(false);
        		view.appearMain();
        	}
        });



    }



}