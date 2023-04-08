package auth;

import com.autovend.software.AbstractEventListener;

public interface AuthEvenListener extends AbstractEventListener {

	public void reactToLogIn(AttendantAccount attendantAccount);

	public void reactToLogOut(AttendantAccount attendantAccount);

	public void reactToAddAccount(AttendantAccount attendantAccount, AttendantAccount addedAccount);

	public void reactToDeleteAccount(AttendantAccount attendantAccount, AttendantAccount addedAccount);
}
