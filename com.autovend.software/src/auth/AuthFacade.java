package auth;

import com.autovend.devices.AbstractDevice;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.software.AbstractFacade;
import com.autovend.software.item.ItemEventListener;

public class AuthFacade extends AbstractFacade<ItemEventListener> implements AuthEvenListener {
	public AuthFacade(SelfCheckoutStation station) {
		super(station);
		// TODO Auto-generated constructor stub
	}

	public boolean logIn(AttendantAccount attendantAccount) {
		if (AttendantAccountDatabases.ATTENDANT_ACCOUNTS.contains(attendantAccount)) {
			reactToLogInSuccesfully(attendantAccount);
			return true;
		}
		reactToLogInUnsuccesfully(attendantAccount);
		return false;
	}

	public boolean logOut(AttendantAccount attendantAccount) {
		if (AttendantAccountDatabases.ATTENDANT_ACCOUNTS.contains(attendantAccount)) {
			reactToLogOutSuccesfully(attendantAccount);
			return true;
		}
		reactToLogOutUnsuccesfully(attendantAccount);
		return false;
	}

	@Override
	public void reactToHardwareFailure() {
		// TODO Auto-generated method stub

	}

	@Override
	public void reactToDisableDeviceRequest(AbstractDevice<? extends AbstractDeviceObserver> device) {
		// TODO Auto-generated method stub

	}

	@Override
	public void reactToEnableDeviceRequest(AbstractDevice<? extends AbstractDeviceObserver> device) {
		// TODO Auto-generated method stub

	}

	@Override
	public void reactToDisableStationRequest() {
		// TODO Auto-generated method stub

	}

	@Override
	public void reactToEnableStationRequest() {
		// TODO Auto-generated method stub

	}

	@Override
	public void reactToLogInSuccesfully(AttendantAccount attendantAccount) {
		// TODO Auto-generated method stub
		System.out.println(attendantAccount.getUserName() + " has succsesfully logged in.");
	}

	@Override
	public void reactToLogOutSuccesfully(AttendantAccount attendantAccount) {
		// TODO Auto-generated method stub
		System.out.println(attendantAccount.getUserName() + " has succsesfully logged out.");
	}

	@Override
	public void reactToLogInUnsuccesfully(AttendantAccount attendantAccount) {
		// TODO Auto-generated method stub
		System.out.println(attendantAccount.getUserName() + " has failed to log in.");
	}

	@Override
	public void reactToLogOutUnsuccesfully(AttendantAccount attendantAccount) {
		// TODO Auto-generated method stub
		System.out.println(attendantAccount.getUserName() + " has failed to log in.");
	}
}
