package auth;

public class AuthFacade implements AuthEvenListener {
	public AuthFacade() {
		// TODO Auto-generated constructor stub
	}

	public boolean logIn(AttendantAccount attendantAccount) {
		if (AttendantAccountDatabases.ATTENDANT_ACCOUNTS.contains(attendantAccount)) {
			reactToLogIn(attendantAccount);
			return true;
		}
		return false;
	}

	public boolean logOut(AttendantAccount attendantAccount) {
		if (AttendantAccountDatabases.ATTENDANT_ACCOUNTS.contains(attendantAccount)) {
			reactToLogOut(attendantAccount);
			return true;
		}
		return false;
	}

	public boolean addAccount(AttendantAccount attendantAccount, AttendantAccount addedAccount) {
		if (attendantAccount.equals(AttendantAccountDatabases.godAccount())) {
			AttendantAccountDatabases.ATTENDANT_ACCOUNTS.add(addedAccount);
			reactToAddAccount(attendantAccount, addedAccount);
			return true;
		} else {
			return false;
		}
	}

	public boolean deleteAccount(AttendantAccount attendantAccount, AttendantAccount removedAccount) {
		if (attendantAccount.equals(AttendantAccountDatabases.godAccount())) {
			AttendantAccountDatabases.ATTENDANT_ACCOUNTS.remove(removedAccount);
			reactToDeleteAccount(attendantAccount, removedAccount);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void reactToLogIn(AttendantAccount attendantAccount) {
		// TODO Auto-generated method stub
		System.out.println(attendantAccount.getUserName() + " has succsesfully logged in.");
	}

	@Override
	public void reactToLogOut(AttendantAccount attendantAccount) {
		// TODO Auto-generated method stub
		System.out.println(attendantAccount.getUserName() + " has succsesfully logged out.");
	}

	@Override
	public void reactToAddAccount(AttendantAccount attendantAccount, AttendantAccount addedAccount) {
		// TODO Auto-generated method stub
		System.out.println(
				attendantAccount.getUserName() + " has succsesfully added account: " + addedAccount.getUserName());
	}

	@Override
	public void reactToDeleteAccount(AttendantAccount attendantAccount, AttendantAccount removedAccount) {
		// TODO Auto-generated method stub
		System.out.println(
				attendantAccount.getUserName() + " has succsesfully deleted account: " + removedAccount.getUserName());
	}

}
