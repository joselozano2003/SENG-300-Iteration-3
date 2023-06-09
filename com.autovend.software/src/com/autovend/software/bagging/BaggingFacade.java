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
package com.autovend.software.bagging;

import com.autovend.devices.AbstractDevice;
import com.autovend.devices.ElectronicScale;
import com.autovend.devices.EmptyException;
import com.autovend.devices.ReusableBagDispenser;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.ElectronicScaleObserver;
import com.autovend.devices.observers.ReusableBagDispenserObserver;
import com.autovend.software.AbstractFacade;
import com.autovend.software.ui.CustomerView;

@SuppressWarnings("serial")
public class BaggingFacade extends AbstractFacade<BaggingEventListener>
		implements ElectronicScaleObserver, ReusableBagDispenserObserver {

	ReusableBagProduct bagProduct;

	public BaggingFacade(SelfCheckoutStation station, CustomerView customerView) {
		super(station, customerView);
		bagProduct = new ReusableBagProduct();

		// station.scale.register(this);
		station.baggingArea.register(this);
		station.bagDispenser.register(this);

	}

	@Override
	public void reactToEnabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {}

	@Override
	public void reactToDisabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {}

	@Override
	public void reactToWeightChangedEvent(ElectronicScale scale, double weightInGrams) {
		for (BaggingEventListener listener : listeners)
			listener.onWeightChanged(weightInGrams);

	}

	@Override
	public void reactToOverloadEvent(ElectronicScale scale) {
		for (BaggingEventListener listener : listeners)
			listener.onWeightChanged(-1);
	}

	@Override
	public void reactToOutOfOverloadEvent(ElectronicScale scale) {}

	@Override
	public void bagDispensed(ReusableBagDispenser dispenser) {}

	@Override
	public void outOfBags(ReusableBagDispenser dispenser) {}

	@Override
	public void bagsLoaded(ReusableBagDispenser dispenser, int count) {}

	public void dispenseBags(int amount) {
		int successfullyDispensed = 0;
		boolean fail = false;
		while (amount > 0) {
			try {
				station.bagDispenser.dispense();
				amount--;
				successfullyDispensed++;

			} catch (EmptyException e) {
				for (BaggingEventListener listener : listeners)
					listener.onBagsDispensedFailure(bagProduct, amount, successfullyDispensed);
				fail = true;
				break;
			}
		}
		
		if (!fail && amount == 0) {
			for (BaggingEventListener listener : listeners)
				listener.onBagsDispensedEvent(bagProduct, successfullyDispensed);		
		}

		
	}

}
