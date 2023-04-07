package com.autovend.software.GUI;


import com.sun.tools.javac.Main;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class MainScreen  {
    JPanel jPanel = new JPanel();


    public MainScreen(Controller controller){
        jPanel.setForeground(new Color(65, 73, 96));
        jPanel.setBackground(new Color(65, 73, 96));
        jPanel.setMinimumSize(new Dimension(1280, 720));
        jPanel.setMaximumSize(new Dimension(1280, 720));
        jPanel.setPreferredSize(new Dimension(1280, 720));
        jPanel.setSize(new Dimension(1280, 720));
        jPanel.setLayout(null);



    }
}