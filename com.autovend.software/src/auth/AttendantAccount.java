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

import com.autovend.devices.EmptyException;

public class AttendantAccount {
	private String userName;
	private String password;

	public AttendantAccount(String userName, String password) {
		try {
			setUserName(userName);
			setPassword(password);
		} catch (Exception e) {
		}
	}

	public String getUserName() {
		return userName;
	}

	private void setUserName(String userName) throws EmptyException {
		if (userName == null)
			throw new EmptyException("User name is empty!");
		this.userName = userName;
	}

	private String getPassword() {
		return password;
	}

	private void setPassword(String password) throws EmptyException {
		if (password == null)
			throw new EmptyException("Password is empty!");
		this.password = password;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof AttendantAccount) {
			if (this.getUserName().equals(((AttendantAccount) obj).getUserName())
					&& this.getPassword().equals(((AttendantAccount) obj).getPassword())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "attendant userName: " + userName;
	}
}
