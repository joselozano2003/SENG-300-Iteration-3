package auth;

import java.util.ArrayList;
import java.util.Arrays;

public class AttendantAccountDatabases {
	private AttendantAccountDatabases() {
	};

	public static final ArrayList<AttendantAccount> attendantAccount = (ArrayList<AttendantAccount>) Arrays
			.asList(godAccount());

	private static AttendantAccount godAccount() {
		return new AttendantAccount("god", "god");
	}

}
