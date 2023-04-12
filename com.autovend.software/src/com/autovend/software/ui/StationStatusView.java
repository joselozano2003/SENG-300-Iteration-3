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

	JPanel attendantPanel;

	JPanel mainPanel = this;
	JLabel discrepancyLabels[];
	JButton overrideButtons[];
	JButton addByTextButtons[];
	JButton removeItemButtons[];
	JButton lockStationButtons[];
	JButton unlockStationButtons[];
	JButton shutdownStationButtons[];
	JButton turnonStationButtons[];

	JButton logOutButton;
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
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		attendantPanel = new JPanel();
		logOutButton = new JButton("Logout");
		discrepancyLabels = new JLabel[num_stations];
		overrideButtons = new JButton[num_stations];
		addByTextButtons = new JButton[num_stations];
		removeItemButtons = new JButton[num_stations];
		lockStationButtons = new JButton[num_stations];
		unlockStationButtons = new JButton[num_stations];
		shutdownStationButtons = new JButton[num_stations];
		turnonStationButtons = new JButton[num_stations];
		gLayout = new GridLayout(num_stations, 3, 0, 10);
		attendantPanel.setLayout(gLayout);

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
					notifyItemByTextButtonPressed(buttonIndex);
				}
			});

			removeItemButtons[i].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					notifyRemoveItemButtonPressed(buttonIndex);
				}
			});

			lockStationButtons[i].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					notifyLockButtonPressed(buttonIndex);
				}
			});

			unlockStationButtons[i].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					notifyEnableButtonPressed(buttonIndex);
				}
			});

			shutdownStationButtons[i].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					notifyShutdownButtonPressed(buttonIndex);
				}
			});

			turnonStationButtons[i].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					notifyStationTurnonButtonPressed(buttonIndex);
				}
			});

			mainPanel.add(attendantPanel);
			attendantPanel.add(discrepancyLabels[i]);
			attendantPanel.add(overrideButtons[i]);
			attendantPanel.add(addByTextButtons[i]);
			attendantPanel.add(removeItemButtons[i]);
			attendantPanel.add(lockStationButtons[i]);
			attendantPanel.add(unlockStationButtons[i]);
			attendantPanel.add(turnonStationButtons[i]);
			attendantPanel.add(shutdownStationButtons[i]);
			mainPanel.add(logOutButton);


		}


	}

	//////////////////////
	// BUTTON LISTENERS //
	//////////////////////


	public void register(UIEventListener listener) {
		observers.add(listener);
	}

	public void notifyRemoveItemButtonPressed(int stationNum) {
		if (observers == null) return;
		for (UIEventListener listener : observers) {
			listener.onStationRemoveItemPressed(stationNum);
		}
	}

	public void notifyOverrideButtonPressed(int stationNum) {
		if (observers == null) return;
		for (UIEventListener listener : observers) {
			listener.onOverride(stationNum);
		}
	}

	public void notifyItemByTextButtonPressed(int stationNum) {
		if (observers == null) return;
		for (UIEventListener listener : observers) {
			listener.onStationAddByTextPressed(stationNum);
		}
	}

	public void notifyShutdownButtonPressed(int stationNum) {
		if (observers == null) return;
		for (UIEventListener listener : observers) {
			listener.onStationShutdown(stationNum);
		}
	}

	public void notifyStationTurnonButtonPressed(int stationNum) {
		if (observers == null) return;
		for (UIEventListener listener : observers) {
			listener.onStationTurnon(stationNum);
		}
	}

	public void notifyLockButtonPressed(int stationNum) {
		if (observers == null) return;
		for (UIEventListener listener : observers) {
			listener.onStationLock(stationNum);
		}
	}

	public void notifyEnableButtonPressed(int stationNum) {
		if (observers == null) return;
		for (UIEventListener listener : observers) {
			listener.onStationUnlock(stationNum);
		}
	}


}
