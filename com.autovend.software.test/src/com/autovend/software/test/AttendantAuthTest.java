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
import com.autovend.software.attendant.auth.AttendantAccount;
import com.autovend.software.attendant.auth.AttendantAccountDatabases;
import com.autovend.software.customer.CustomerStationLogic;


/**
 * JUNIT test for AttendantAuth
 *
 */
public class AttendantAuthTest {

	private static List<CustomerStationLogic> customerStations = new ArrayList<>();
	private AttendantModel model = new AttendantModel();
	private AttendantView view = new AttendantView();

	public AttendantController attendantController = new AttendantController(model, view);

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test for comparing attendant accounts
	 */
	@Test
	public void AttendantEqualTest() {
		AttendantAccount demoAttendant = AttendantAccountDatabases.demoAccount();
		assertTrue(demoAttendant.equals(demoAttendant));
		assertFalse(demoAttendant.equals(new String("demoAttendant")));
	}

	/**
	 * Test for printing attendant account info
	 */
	@Test
	public void AttendantPrintTest() {
		AttendantAccount demoAttendant = AttendantAccountDatabases.demoAccount();
		assertTrue(demoAttendant.toString().equals(new String("attendant userName: " + demoAttendant.getUserName())));
	}

	/**
	 * Test for creating attendant account with null user name.
	 */
	@Test
	public void NullUsernameTest() {
		AttendantAccount testAttendantAccount = null;
		try {
			testAttendantAccount = new AttendantAccount(null, "123456");
		} catch (Exception e) {
			assertNull(testAttendantAccount);
		}
	}

	/**
	 * Test for creating attendant account with null password.
	 */
	@Test
	public void NullPasswordTest() {
		AttendantAccount testAttendantAccount = null;
		try {
			testAttendantAccount = new AttendantAccount("user", null);
		} catch (Exception e) {
			assertNull(testAttendantAccount);
		}
	}

	/**
	 * Test for logging in successfully.
	 */
	@Test
	public void SuccesfulLogInTest() {
		AttendantAccount demoAttendant = AttendantAccountDatabases.demoAccount();
		assertTrue(attendantController.startLogIn(demoAttendant));
	}

	/**
	 * Test for logging out successfully.
	 */
	@Test
	public void SuccesfulLogOutTest() {
		AttendantAccount demoAttendant = AttendantAccountDatabases.demoAccount();
		assertTrue(attendantController.startLogOut(demoAttendant));
	}

	/**
	 * Test for logging in unsuccessfully.
	 */
	@Test
	public void UnsuccesfulLogInTest() {
		AttendantAccount fakeAttendant = new AttendantAccount("Fake", "Fake");
		assertFalse(attendantController.startLogIn(fakeAttendant));
	}

	/**
	 * Test for logging out unsuccessfully.
	 */
	@Test
	public void UnsuccesfulLogOutTest() {
		AttendantAccount fakeAttendant = new AttendantAccount("Fake", "Fake");
		assertFalse(attendantController.startLogOut(fakeAttendant));
	}

	/**
	 * Test for adding/deleting account successfully.
	 */
	@Test
	public void SuccesfulAddingAndDeletingAccountTest() {
		AttendantAccount addedAccount = new AttendantAccount("addedDemo", "addedDemo");
		assertTrue(attendantController.startAddAccount(AttendantAccountDatabases.godAccount(), addedAccount));
		assertTrue(attendantController.startDeleteAccount(AttendantAccountDatabases.godAccount(), addedAccount));
	}

	/**
	 * Test for adding/deleting account unsuccessfully.
	 */
	@Test
	public void UnsuccesfulAddingAndDeletingAccountTest() {
		AttendantAccount addedAccount = new AttendantAccount("addedDemo", "addedDemo");
		assertFalse(attendantController.startAddAccount(AttendantAccountDatabases.demoAccount(), addedAccount));
		assertFalse(attendantController.startDeleteAccount(AttendantAccountDatabases.demoAccount(), addedAccount));
	}
}
