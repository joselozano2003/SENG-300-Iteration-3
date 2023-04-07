package auth;

import com.autovend.devices.EmptyException;

public class AttendantAccount {
	private String userName;
	private String password;
	public AttendantAccount (String userName, String password) throws EmptyException {
		if(userName==null) {
			throw new EmptyException("userName is empty!");
		}
		if(password==null) {
			throw new EmptyException("password is empty!");
		}
		setUserName(userName);
		setPassword(password);
	}
	private String getUserName() {
		return userName;
	}
	private void setUserName(String userName) {
		this.userName = userName;
	}
	private String getPassword() {
		return password;
	}
	private void setPassword(String password) {
		this.password = password;
	}
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof AttendantAccount) {
			if(this.getUserName().equals(((AttendantAccount)obj).getUserName())&&this.getPassword().equals(((AttendantAccount)obj).getPassword())) {
				return true;
			}
		}
		return false;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "attendant userName: "+userName;
	}
}
