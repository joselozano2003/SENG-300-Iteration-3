package auth;

import com.autovend.software.AbstractEventListener;

public interface AuthEvenListener extends AbstractEventListener {

	public void reactToLogInSuccesfully(AttendantAccount attendantAccount);

	public void reactToLogOutSuccesfully(AttendantAccount attendantAccount);

	public void reactToLogInUnsuccesfully(AttendantAccount attendantAccount);

	public void reactToLogOutUnsuccesfully(AttendantAccount attendantAccount);

}
