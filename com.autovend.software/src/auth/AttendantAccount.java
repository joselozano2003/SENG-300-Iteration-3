package auth;

import com.autovend.devices.EmptyException;

/**
 * This class deals with AttendantAccount.
 *
 */
public class AttendantAccount {
	private String userName;
	private String password;

	/**
	 * Constructor of AttendantAccount
	 * 
	 * @param userName: user name of account
	 * @param password: password of account
	 */
	public AttendantAccount(String userName, String password) {
		try {
			setUserName(userName);
			setPassword(password);
		} catch (Exception e) {
		}
	}

	/**
	 * Getter for userName
	 * 
	 * @return userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * Setter for userName
	 * 
	 * @param userName: user name of account
	 * @throws EmptyException
	 */
	private void setUserName(String userName) throws EmptyException {
		if (userName == null)
			throw new EmptyException("User name is empty!");
		this.userName = userName;
	}

	/**
	 * Getter for password
	 * 
	 * @return password
	 */
	private String getPassword() {
		return password;
	}

	/**
	 * Setter for password
	 * 
	 * @param password: password of account
	 * @throws EmptyException
	 */
	private void setPassword(String password) throws EmptyException {
		if (password == null)
			throw new EmptyException("Password is empty!");
		this.password = password;
	}

	/**
	 * Override equals method to compare two attendant accounts
	 */
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

	/**
	 * Override toString() to print attendant information.
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "attendant userName: " + userName;
	}
}
