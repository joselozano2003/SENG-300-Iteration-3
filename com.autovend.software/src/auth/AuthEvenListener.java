package auth;

/**
 * This class deals with listeners for authorization events
 *
 */
public interface AuthEvenListener {

	/**
	 * React to logIn event
	 * 
	 * @param attendantAccount: account of attendant
	 */
	public void reactToLogIn(AttendantAccount attendantAccount);

	/**
	 * React to logOut event
	 * 
	 * @param attendantAccount: account of attendant
	 */
	public void reactToLogOut(AttendantAccount attendantAccount);

	/**
	 * React to addAccount event
	 * 
	 * @param attendantAccount: account of attendant
	 * @param addedAccount:     added account of attendant
	 */
	public void reactToAddAccount(AttendantAccount attendantAccount, AttendantAccount addedAccount);

	/**
	 * React to deleteAccount event
	 * 
	 * @param attendantAccount: account of attendant
	 * @param addedAccount:     removed account of attendant
	 */
	public void reactToDeleteAccount(AttendantAccount attendantAccount, AttendantAccount addedAccount);
}
