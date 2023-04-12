package auth;

import java.util.ArrayList;

/**
 * This class deals with the database of attendant accounts
 *
 */
public class AttendantAccountDatabases {
	/**
	 * Constructor for AttendantAccountDatabases
	 */
	private AttendantAccountDatabases() {
	};

	/**
	 * Attendant accounts including god account and demo account.
	 */
	public static final ArrayList<AttendantAccount> ATTENDANT_ACCOUNTS = new ArrayList<AttendantAccount>() {
		{
			add(godAccount());
			add(demoAccount());
		}
	};

	/**
	 * @return god account
	 */
	public static AttendantAccount godAccount() {
		return new AttendantAccount("god", "god");
	}

	/**
	 * @return demo account
	 */
	public static AttendantAccount demoAccount() {
		return new AttendantAccount("demo", "demo");
	}

}
