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

package com.autovend.software.attendant;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.autovend.devices.SupervisionStation;
import com.autovend.software.attendant.AttendantController;
import com.autovend.software.attendant.AttendantModel;
//import com.autovend.software.attendant.AttendantView;
import com.autovend.software.customer.CustomerStationLogic;
import com.autovend.software.ui.*;


import auth.AttendantAccount;
import auth.AttendantAccountDatabases;


public class AttendantAuthTest {

	private static List<CustomerStationLogic> customerStations = new ArrayList<>();
	private AttendantModel model = new AttendantModel();
	private AttendantView view = new AttendantView(4);
	private SupervisionStation station = new SupervisionStation();

	public AttendantController attendantController = new AttendantController(station, view);

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void AttendantEqualTest() {
		AttendantAccount demoAttendant = AttendantAccountDatabases.demoAccount();
		assertTrue(demoAttendant.equals(demoAttendant));
		assertFalse(demoAttendant.equals(new String("demoAttendant")));
	}

	@Test
	public void AttendantPrintTest() {
		AttendantAccount demoAttendant = AttendantAccountDatabases.demoAccount();
		assertTrue(demoAttendant.toString().equals(new String("attendant userName: " + demoAttendant.getUserName())));
	}

	@Test
	public void NullUsernameTest() {
		AttendantAccount testAttendantAccount = null;
		try {
			testAttendantAccount = new AttendantAccount(null, "123456");
		} catch (Exception e) {
			assertNull(testAttendantAccount);
		}
	}

	@Test
	public void NullPasswordTest() {
		AttendantAccount testAttendantAccount = null;
		try {
			testAttendantAccount = new AttendantAccount("user", null);
		} catch (Exception e) {
			assertNull(testAttendantAccount);
		}
	}

	@Test
	public void SuccesfulLogInTest() {
		AttendantAccount demoAttendant = AttendantAccountDatabases.demoAccount();
		assertTrue(attendantController.startLogIn(demoAttendant));
	}

//	@Test
//	public void SuccesfulLogOutTest() {
//		AttendantAccount demoAttendant = AttendantAccountDatabases.demoAccount();
//		assertTrue(attendantController.startLogOut());
//	}

	@Test
	public void UnsuccesfulLogInTest() {
		AttendantAccount fakeAttendant = new AttendantAccount("Fake", "Fake");
		assertFalse(attendantController.startLogIn(fakeAttendant));
	}

	@Test
	public void UnsuccesfulLogOutTest() {
		AttendantAccount fakeAttendant = new AttendantAccount("Fake", "Fake");
		assertFalse(attendantController.startLogOut());
	}

	@Test
	public void SuccesfulAddingAndDeletingAccountTest() {
		AttendantAccount addedAccount = new AttendantAccount("addedDemo", "addedDemo");
		assertTrue(attendantController.startAddAccount(AttendantAccountDatabases.godAccount(), addedAccount));
		assertTrue(attendantController.startDeleteAccount(AttendantAccountDatabases.godAccount(), addedAccount));
	}

	@Test
	public void UnsuccesfulAddingAndDeletingAccountTest() {
		AttendantAccount addedAccount = new AttendantAccount("addedDemo", "addedDemo");
		assertFalse(attendantController.startAddAccount(AttendantAccountDatabases.demoAccount(), addedAccount));
		assertFalse(attendantController.startDeleteAccount(AttendantAccountDatabases.demoAccount(), addedAccount));
	}
}
