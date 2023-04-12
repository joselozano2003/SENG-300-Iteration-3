package com.autovend.software.attendant.auth;

/**
 * This class deals with Facade of Auth
 *
 */
public class AuthFacade implements AuthEvenListener {
	/**
	 * Constructor for AuthFacade
	 */
	public AuthFacade() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * logIn function
	 * 
	 * @param attendantAccount: account of attendant
	 * @return true if the account successfully logs in, false otherwise
	 */
	public boolean logIn(AttendantAccount attendantAccount) {
		if (AttendantAccountDatabases.ATTENDANT_ACCOUNTS.contains(attendantAccount)) {
			reactToLogIn(attendantAccount);
			return true;
		}
		return false;
	}

	/**
	 * logOut function
	 * 
	 * @param attendantAccount: account of attendant
	 * @return true if the account successfully logs out, false otherwise
	 */
	public boolean logOut(AttendantAccount attendantAccount) {
		if (AttendantAccountDatabases.ATTENDANT_ACCOUNTS.contains(attendantAccount)) {
			reactToLogOut(attendantAccount);
			return true;
		}
		return false;
	}

	/**
	 * addAccount function
	 * 
	 * @param attendantAccount: account of attendant
	 * @param addedAccount:     account to be added
	 * @return true if the account successfully adds a new account, false otherwise
	 */
	public boolean addAccount(AttendantAccount attendantAccount, AttendantAccount addedAccount) {
		if (attendantAccount.equals(AttendantAccountDatabases.godAccount())) {
			AttendantAccountDatabases.ATTENDANT_ACCOUNTS.add(addedAccount);
			reactToAddAccount(attendantAccount, addedAccount);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * deleteAccount function
	 * 
	 * @param attendantAccount: account of attendant
	 * @param addedAccount:     account to be removed
	 * @return true if the account successfully deletes a new account, false
	 *         otherwise
	 */
	public boolean deleteAccount(AttendantAccount attendantAccount, AttendantAccount removedAccount) {
		if (attendantAccount.equals(AttendantAccountDatabases.godAccount())) {
			AttendantAccountDatabases.ATTENDANT_ACCOUNTS.remove(removedAccount);
			reactToDeleteAccount(attendantAccount, removedAccount);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * React to logIn event
	 */
	@Override
	public void reactToLogIn(AttendantAccount attendantAccount) {
		// TODO Auto-generated method stub
		System.out.println(attendantAccount.getUserName() + " has succsesfully logged in.");
	}

	/**
	 * React to logOut event
	 */
	@Override
	public void reactToLogOut(AttendantAccount attendantAccount) {
		// TODO Auto-generated method stub
		System.out.println(attendantAccount.getUserName() + " has succsesfully logged out.");
	}

	/**
	 * React to addAccount event
	 */
	@Override
	public void reactToAddAccount(AttendantAccount attendantAccount, AttendantAccount addedAccount) {
		// TODO Auto-generated method stub
		System.out.println(
				attendantAccount.getUserName() + " has succsesfully added account: " + addedAccount.getUserName());
	}

	/**
	 * React to deleteAccount event
	 */
	@Override
	public void reactToDeleteAccount(AttendantAccount attendantAccount, AttendantAccount removedAccount) {
		// TODO Auto-generated method stub
		System.out.println(
				attendantAccount.getUserName() + " has succsesfully deleted account: " + removedAccount.getUserName());
	}

}
