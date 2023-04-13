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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.autovend.devices.OverloadException;
import com.autovend.products.Product;
import com.autovend.software.ui.LoginView;
import com.autovend.software.ui.StationStatusView;
import com.autovend.software.ui.UIEventListener;

import auth.AttendantAccount;

public class LoginViewTest {
	
	private LoginView view;
	// Boolean inticating whether entered account is valid.
	private boolean isValid;
	// A hypothetical valid account - rather than checking in the database,
	// will just check against this account.
	private AttendantAccount validAccount;
	private LoginTestListener listener;
	
	// Can test login view without constructing full system.
	
	private class LoginTestListener implements UIEventListener {

		@Override
		public void onStartAddingItems() {}

		@Override
		public void onStartPaying() {}

		@Override
		public void onSelectAddItemByPLU() {}

		@Override
		public void onSelectAddItemByBrowsing() {}

		@Override
		public void onStartAddingOwnBags() {}

		@Override
		public void onFinishAddingOwnBags() {}

		@Override
		public void onAddMembershipNumber() {}

		@Override
		public void onPurchaseBags(int amount) {}

		@Override
		public void goBackToCheckout() {}

		@Override
		public void onSelectPaymentMethod(String payment) {}

		@Override
		public void onOverride(int stationNumber) {}

		@Override
		public void onBagApproval(int stationNumber) {}

		@Override
		public void onBagRefusal(int stationNumber) {}

		@Override
		public void onAttendantLoginAttempt(AttendantAccount account) {
			// Check if account is valid. Set isValid accordingly.
			if (account.equals(validAccount)) {
				// If entered account is valid, set isValid to be true.
				isValid = true;
				System.out.println("Valid account info");
			} else {
				
				isValid = false;
				view.loginFailure();
				System.out.println("Invalid account info");
			}
		}

		@Override
		public void onStationShutdown(int stationNumber) {}

		@Override
		public void onStationTurnon(int stationNumber) {}

		@Override
		public void onStationLock(int stationNumber) {}

		@Override
		public void onStationUnlock(int stationNumber) {}

		@Override
		public void onStationLogout() {}

		@Override
		public void onStationAddByTextPressed(int stationNumber) {}

		@Override
		public void onStationRemoveItemPressed(int stationNumber) {}

		@Override
		public void onAddItem(Product product) {}
		
	}
	
	
	@Before
	public void setup() {
		listener = new LoginTestListener();
		validAccount = new AttendantAccount("user", "password");
	}
	
	
	@Test
	public void testValidLogin() {
		// Initialize isValid to be false, expect it to be made true.
		isValid = false;
		// Create login view...
		view = new LoginView();
		view.listeners = new ArrayList<UIEventListener>();
		view.listeners.add(listener);
		view.override = true;
		// Enter valid credentials.
		view.userEntered = "user";
		view.passEntered = "password";
		// Press login button.
		view.loginListen.actionPerformed(null);
		// isValid should now be true...
		assertTrue(isValid);
	}
	
	
	@Test
	public void testInvalidLogin() {
		// Initialize isValid to be true, expect it to be made false.
		isValid = true;
		// Create login view...
		view = new LoginView();
		view.listeners = new ArrayList<UIEventListener>();
		view.listeners.add(listener);
		view.override = true;
		// Enter invalid credentials.
		view.userEntered = "user1";
		view.passEntered = "password1";
		// Press login button.
		view.loginListen.actionPerformed(null);
		// isValid should now be false...
		assertFalse(isValid);
	}
	
	
	@Test
	public void testStationStatusView() {
		// Didn't have time to complete testing for this class. Simple test
		// displaying GUI.
		try {
			StationStatusView.main(null);
		} catch (OverloadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
