package com.autovend.software.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.autovend.software.attendant.AttendantController;
import com.autovend.software.attendant.AttendantModel;
import com.autovend.software.attendant.AttendantView;
import com.autovend.software.customer.CustomerStationLogic;

import auth.AttendantAccount;
import auth.AttendantAccountDatabases;

public class AttendantAuthTest {

	private static List<CustomerStationLogic> customerStations = new ArrayList<>();
	private AttendantModel model = new AttendantModel();
	private AttendantView view = new AttendantView();

	public AttendantController attendantController = new AttendantController(model, view, customerStations);

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

	@Test
	public void SuccesfulLogOutTest() {
		AttendantAccount demoAttendant = AttendantAccountDatabases.demoAccount();
		assertTrue(attendantController.startLogOut(demoAttendant));
	}

	@Test
	public void UnsuccesfulLogInTest() {
		AttendantAccount fakeAttendant = new AttendantAccount("Fake", "Fake");
		assertFalse(attendantController.startLogIn(fakeAttendant));
	}

	@Test
	public void UnsuccesfulLogOutTest() {
		AttendantAccount fakeAttendant = new AttendantAccount("Fake", "Fake");
		assertFalse(attendantController.startLogOut(fakeAttendant));
	}

}
