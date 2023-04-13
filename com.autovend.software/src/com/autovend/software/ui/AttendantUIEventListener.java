package com.autovend.software.ui;

import auth.AttendantAccount;

/**
 * The UIEventListener interface defines the contract for objects that need to be
 * notified when user interface events occur. Classes implementing this interface
 * should provide an implementation for each of the defined methods.
 */

public interface AttendantUIEventListener {
	

	public void onOverride(int stationNumber);//
	
	public void onStationShutdown(int stationNumber);//
	
	public void onStationTurnon(int stationNumber);//
	
	public void onStationLock(int stationNumber);
	
	public void onStationUnlock(int stationNumber);
	
	public void onAttendantLoginAttempt(AttendantAccount account);
	
	public void onStationLogout();

	public void onStationAddByTextPressed(int stationNumber);
	
	public void onStationRemoveItemPressed(int stationNumber);

	public boolean startLogOut();
	
	public boolean startLogIn(AttendantAccount aAccount);

	
}
