/* P3-4 Group Members
 *
 * Abdelrhafour, Achraf (30022366)
 * Campos, Oscar (30057153)
 * Cavilla, Caleb (30145972)
 * Crowell, Madeline (30069333)
 * Debebe, Abigia (30134608)
 * Dhuka, Sara Hazrat (30124117)
 * Drissi, Khalen (30133707)
 * Ferreira, Marianna (30147733)
 * Frey, Ben (30088566)
 * Himel, Tanvir (30148868)
 * Huayhualla Arce, Fabricio (30091238)
 * Kacmar, Michael (30113919)
 * Lee, Jeongah (30137463)
 * Li, Ran (10120152)
 * Lokanc, Sam (30114370)
 * Lozano Cetina, Jose Camilo (30144736)
 * Maahdie, Monmoy (30149094)
 * Malik, Akansha (30056048)
 * Mehedi, Abdullah (30154770)
 * Polton, Scott (30138102)
 * Rahman, Saadman (30153482)
 * Rodriguez, Gabriel (30162544)
 * Samin Rashid, Khondaker (30143490)
 * Sloan, Jaxon (30123845)
 * Tran, Kevin (30146900)
 */
package com.autovend.software.membership;

import com.autovend.Barcode;
import com.autovend.Card.CardData;
import com.autovend.devices.AbstractDevice;
import com.autovend.devices.BarcodeScanner;
import com.autovend.devices.CardReader;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.BarcodeScannerObserver;
import com.autovend.devices.observers.CardReaderObserver;
import com.autovend.software.AbstractSoftware;

@SuppressWarnings("serial")
public class MembershipFacade extends AbstractSoftware<MembershipListener> {
	
	// GUI will have to have a button that forces the system into scanning for membership mode
	private	boolean scanningForMembership = false;
	private boolean membershipEntered = false;

	public MembershipFacade(SelfCheckoutStation station) {
		super(station);
		try {
			InnerListener inner = new InnerListener();
			station.mainScanner.register(inner);
			station.handheldScanner.register(inner);
			station.cardReader.register(inner);
		} catch (Exception e) {
			for (MembershipListener listener : listeners)
				listener.reactToHardwareFailure();
		}
	}
	
	private class InnerListener implements BarcodeScannerObserver, CardReaderObserver {
		@Override
		public void reactToEnabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {}
		@Override
		public void reactToDisabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {}
		@Override
		
		/**
		 * If the system is in 'scanning for membership' mode, this function checks if a barcode exists in the current
		 * MembershipDataBase, if it does it sets membershipEntered = true, otherwise false
		 */
		public void reactToBarcodeScannedEvent(BarcodeScanner barcodeScanner, Barcode barcode) {
			//if valid
			membershipEntered = true;
			if (scanningForMembership) {
				if (MemberShipDatabase.userExists(barcode.toString()) == false) {
					System.out.println("Invalid Membership Card detetected. Please try again");
					membershipEntered = false;
				}
			}
		}
		@Override
		public void reactToCardRemovedEvent(CardReader reader) {
			// TO DO, UNSURE OF FUNCTIONALITY
		}
		@Override
		public void reactToCardInsertedEvent(CardReader reader) {
			// reactToCardDataReadEvent is called regardless of input method, not sure what the point of these 3 functions is.
		}
		@Override
		public void reactToCardTappedEvent(CardReader reader) {
			// reactToCardDataReadEvent is called regardless of input method, not sure what the point of these 3 functions is.
		}
		@Override
		public void reactToCardSwipedEvent(CardReader reader) {
			// reactToCardDataReadEvent is called regardless of input method, not sure what the point of these 3 functions is.
		}
		@Override
		public void reactToCardDataReadEvent(CardReader reader, CardData data) {
			//if valid
			membershipEntered = true;
			if (scanningForMembership) {
				if (MemberShipDatabase.userExists(data.getNumber()) == false) {
					System.out.println("Invalid Membership Card detetected. Please try again");
					membershipEntered = false;
				}
			}
		}
			
	
	}
	
	/**
	 * @return True or false if a valid membership number has been entered.
	 */
	public boolean membershipEntered() {
		return membershipEntered;
	}

	public boolean isScanningForMembership() {
		return scanningForMembership;
	}

	public void setScanningForMembership(boolean scanningForMembership) {
		this.scanningForMembership = scanningForMembership;
	}

}
