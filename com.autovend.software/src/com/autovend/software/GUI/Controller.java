package com.autovend.software.GUI;

import com.autovend.devices.SelfCheckoutStation;

import javax.swing.*;
import java.awt.*;


public class Controller {
    private SelfCheckoutStation station = null;



    public Controller(SelfCheckoutStation station){
        if(station == null) throw new NullPointerException("No argument can be null");
        this.station = station;

    }

    public void loadMainGUI(){
        JFrame frame = station.screen.getFrame();
        station.screen.setVisible(false);
        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());
        MainScreen mainScreen = new MainScreen(this);
        JPanel container  = new JPanel(new GridBagLayout());
        container.setPreferredSize(frame.getSize());
        container.setBackground(new Color(9,11,16));
        container.add(mainScreen);
        frame.getContentPane().add(container);
        frame.validate();
        frame.repaint();

        station.screen.setVisible(true);


    }
}