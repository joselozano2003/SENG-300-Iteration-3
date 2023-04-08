package auth;

import java.util.ArrayList;
import java.util.Arrays;

public class AttendantAccountDatabases {
	private AttendantAccountDatabases() {
	};

	public static final ArrayList<AttendantAccount> ATTENDANT_ACCOUNTS = (ArrayList<AttendantAccount>) Arrays
			.asList(godAccount(), demoAccount());

	public static AttendantAccount godAccount() {
		return new AttendantAccount("god", "god");
	}

	public static AttendantAccount demoAccount() {
		return new AttendantAccount("demo", "demo");
	}

}
