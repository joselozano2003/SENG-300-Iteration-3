package auth;

import java.util.ArrayList;

public class AttendantAccountDatabases {
	private AttendantAccountDatabases() {
	};

	public static final ArrayList<AttendantAccount> ATTENDANT_ACCOUNTS = new ArrayList<AttendantAccount>() {
		{
			add(godAccount());
			add(demoAccount());
		}
	};

	public static AttendantAccount godAccount() {
		return new AttendantAccount("god", "god");
	}

	public static AttendantAccount demoAccount() {
		return new AttendantAccount("demo", "demo");
	}

}
