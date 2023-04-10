package com.autovend.software.attendant.auth;

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
