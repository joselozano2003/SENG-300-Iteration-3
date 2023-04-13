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
