package auth;

import com.autovend.software.AbstractEventListener;

public interface AuthEvenListener extends AbstractEventListener {

	public void reactToLogIn(AttendantAccount attendantAccount);

	public void reactToLogOut(AttendantAccount attendantAccount);

}
