package com.autovend.software.ui;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class StartView extends JPanel {
    private JButton startButton;
    private List<UIEventListener> observers;

    public StartView() {
        // ...
        observers = new ArrayList<>();
        startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                notifyStartButtonPressed();
            }
        });
        add(startButton);
    }

    public void register(UIEventListener listener) {
        observers.add(listener);
    }

    private void notifyStartButtonPressed() {
        for (UIEventListener listener : observers) {
            listener.onStartAddingItems();
        }
    }
}
