package auth;

import com.autovend.software.AbstractEventListener;

public interface AuthEvenListener extends AbstractEventListener {

	public void onLogIn();

	public void onLogOut();

}
