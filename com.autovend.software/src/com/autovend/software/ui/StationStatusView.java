package com.autovend.software.ui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.*;

import com.autovend.software.attendant.AttendantController;

import auth.AttendantAccount;

public class StationStatusView extends JPanel {
	
	int num_stations;
	
	AttendantController controller;
	AttendantAccount account;
	
	JPanel attendantPanel = this;
	JLabel discrepancyLabels[];
	JButton overrideButtons[];
	JButton addByTextButtons[];
	JButton removeItemButtons[];
	JButton lockStationButtons[];
	JButton unlockStationButtons[];
	JButton shutdownStationButtons[];
	JButton turnonStationButtons[];
	GridLayout gLayout;
	
	private List<UIEventListener> observers;
	
	List<UIEventListener> listeners;
	
	public static void main(String args[]) {
		JFrame testFrame = new JFrame();
		StationStatusView ssv = new StationStatusView(10);
		testFrame.add(ssv);
		testFrame.setVisible(true);
		testFrame.validate();
		testFrame.pack();
	}
	
	
	// Display attendant interface.
	// Number of stations is preset when AttendantView is constructed. Should incorporate option to change language.
	public StationStatusView(int num_stations) {
		this.num_stations = num_stations;
		discrepancyLabels = new JLabel[num_stations];
		overrideButtons = new JButton[num_stations];
		addByTextButtons = new JButton[num_stations];
		removeItemButtons = new JButton[num_stations];
		lockStationButtons = new JButton[num_stations];
		unlockStationButtons = new JButton[num_stations];
		shutdownStationButtons = new JButton[num_stations];
		turnonStationButtons = new JButton[num_stations];
		gLayout = new GridLayout(num_stations, 3, 0, 10);
		this.setLayout(gLayout);
		
		// Set up each row. Initially, no weight discrepancies.
		for (int i = 0; i < num_stations; ++i) {
			discrepancyLabels[i] = new JLabel("Station"
					+ " " + (i + 1) + ": " + "Working normally" + "...");
			overrideButtons[i] = new JButton("Override");
			addByTextButtons[i] = new JButton("Add Item By Name");
			removeItemButtons[i] = new JButton("Remove Item");
			lockStationButtons[i] = new JButton("Lock Station");
			unlockStationButtons[i] = new JButton("Unlock Station");
			shutdownStationButtons[i] = new JButton("Shutdown Station");
			turnonStationButtons[i] = new JButton("Start Station");
			
			final int buttonIndex = i;
			
			overrideButtons[i].addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                notifyOverrideButtonPressed(buttonIndex);
	            }
	        });
			
			addByTextButtons[i].addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                // add method
	            }
	        });
			
			removeItemButtons[i].addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                notifyRemoveItemButtonPressed(buttonIndex);// add method
	            }
	        });
			
			lockStationButtons[i].addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                // add method
	            }
	        });
			
			unlockStationButtons[i].addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                // add method
	            }
	        });
			
			shutdownStationButtons[i].addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                // add method
	            }
	        });
			
			turnonStationButtons[i].addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                // add method
	            }
	        });
			
			
			attendantPanel.add(discrepancyLabels[i]);
			attendantPanel.add(overrideButtons[i]);
			attendantPanel.add(addByTextButtons[i]);
			attendantPanel.add(removeItemButtons[i]);
			attendantPanel.add(lockStationButtons[i]);
			attendantPanel.add(unlockStationButtons[i]);
			attendantPanel.add(turnonStationButtons[i]);
			attendantPanel.add(shutdownStationButtons[i]);
			
		}
		
    	
	}
	
	//////////////////////
	// BUTTON LISTENERS //
	//////////////////////
	
	
	public void register(UIEventListener listener) {
		observers.add(listener);
	}
	
	public void notifyOverrideButtonPressed(int stationNum) {
		for (UIEventListener listener : observers) {
			listener.onOverride(stationNum);
		}
	}
	
	public void notifyRemoveItemButtonPressed(int stationNum) {
		for (UIEventListener listener : observers) {
			listener.onStationRemoveItemPressed(stationNum);
		}
	}
	
	
	
	
	

}
