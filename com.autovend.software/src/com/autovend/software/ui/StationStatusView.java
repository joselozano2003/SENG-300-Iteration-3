package com.autovend.software.ui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

import javax.swing.*;

import com.autovend.Barcode;
import com.autovend.Bill;
import com.autovend.Coin;
import com.autovend.Numeral;
import com.autovend.PriceLookUpCode;
import com.autovend.ReusableBag;
import com.autovend.devices.BillDispenser;
import com.autovend.devices.CoinDispenser;
import com.autovend.devices.OverloadException;
import com.autovend.devices.ReusableBagDispenser;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.SimulationException;
import com.autovend.external.ProductDatabases;
import com.autovend.products.BarcodedProduct;
import com.autovend.products.PLUCodedProduct;
import com.autovend.software.attendant.AttendantController;
import com.autovend.software.customer.CustomerController;
import com.autovend.software.item.ProductsDatabase2;

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

	private List<AttendantUIEventListener> observers;

	List<AttendantUIEventListener> listeners;

	public static void main(String args[]) throws OverloadException {
		JFrame testFrame = new JFrame();
		StationStatusView ssv = new StationStatusView(10);
		testFrame.add(ssv);
		testFrame.setVisible(true);
		testFrame.validate();
		testFrame.pack();
	}


	// Display attendant interface.
	// Number of stations is preset when AttendantView is constructed. Should incorporate option to change language.
	public StationStatusView(int num_stations) throws OverloadException {
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
			removeItemButtons[i] = new JButton("Approve Removal");
			lockStationButtons[i] = new JButton("Lock Station");
			unlockStationButtons[i] = new JButton("Unlock Station");
			shutdownStationButtons[i] = new JButton("Shutdown Station");
			turnonStationButtons[i] = new JButton("Start Station");

			final int buttonIndex = i;

			overrideButtons[i].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					notifyOverrideButtonPressed(buttonIndex + 1);
				}
			});

			addByTextButtons[i].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					notifyItemByTextButtonPressed(buttonIndex + 1);
				}
			});

			removeItemButtons[i].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					notifyRemoveItemButtonPressed(buttonIndex + 1);
				}
			});

			lockStationButtons[i].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					notifyLockButtonPressed(buttonIndex + 1);
				}
			});

			unlockStationButtons[i].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					notifyEnableButtonPressed(buttonIndex + 1);
				}
			});

			shutdownStationButtons[i].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					notifyShutdownButtonPressed(buttonIndex + 1);
				}
			});

			turnonStationButtons[i].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					notifyStationTurnonButtonPressed(buttonIndex + 1);
				}
			});
			
			
			logOutButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					logoutButtonPressed();
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

	
	public void logoutButtonPressed() {
		if (observers == null) return;
		for (AttendantUIEventListener listener : observers) {
			listener.startLogOut();
		}
	}
	
	public void register(AttendantUIEventListener listener) {
		observers.add(listener);
	}

	public void notifyRemoveItemButtonPressed(int stationNum) {
		if (observers == null) return;
		for (AttendantUIEventListener listener : observers) {
			listener.onStationRemoveItemPressed(stationNum);
		}
	}

	public void notifyOverrideButtonPressed(int stationNum) {
		if (observers == null) return;
		for (AttendantUIEventListener listener : observers) {
			listener.onOverride(stationNum);
		}
	}

	public void notifyItemByTextButtonPressed(int stationNum) {
		if (observers == null) return;
		for (AttendantUIEventListener listener : observers) {
			listener.onStationAddByTextPressed(stationNum);
		}
	}

	public void notifyShutdownButtonPressed(int stationNum) {
		if (observers == null) return;
		for (AttendantUIEventListener listener : observers) {
			listener.onStationShutdown(stationNum);
		}
	}

	public void notifyStationTurnonButtonPressed(int stationNum) {
		if (observers == null) return;
		for (AttendantUIEventListener listener : observers) {
			listener.onStationTurnon(stationNum);
		}
	}

	public void notifyLockButtonPressed(int stationNum) {
		if (observers == null) return;
		for (AttendantUIEventListener listener : observers) {
			listener.onStationLock(stationNum);
		}
	}

	public void notifyEnableButtonPressed(int stationNum) {
		if (observers == null) return;
		for (AttendantUIEventListener listener : observers) {
			listener.onStationUnlock(stationNum);
		}
	}
	
	public void changeErrorLabel(int stationNum, String eMessage) {
		discrepancyLabels[stationNum] = new JLabel("Station"
				+ " " + (stationNum + 1) + ": " + eMessage + "!");
	}
	
	public void resetErrorLabel(int stationNum) {
		discrepancyLabels[stationNum] = new JLabel("Station"
				+ " " + (stationNum + 1) + ": " + "Working normally" + "...");
	}


}
