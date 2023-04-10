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
package com.autovend.software.payment;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.autovend.Coin;
import com.autovend.devices.*;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.CoinDispenserObserver;
import com.autovend.devices.observers.CoinSlotObserver;
import com.autovend.devices.observers.CoinStorageObserver;
import com.autovend.devices.observers.CoinTrayObserver;
import com.autovend.devices.observers.CoinValidatorObserver;
import com.autovend.software.customer.CustomerSession;

@SuppressWarnings("serial")
public class PayWithCoin extends PaymentFacade implements CoinDispenserObserver, CoinSlotObserver, CoinStorageObserver,
		CoinTrayObserver, CoinValidatorObserver {
	private  SelfCheckoutStation station;
	private CustomerSession session;
	private BigDecimal amountPaid;
	BigDecimal change;
	Object validator;
	Object value;

	public PayWithCoin(SelfCheckoutStation station) {
		super(station, true);
		try {
			station.coinDispensers.forEach((k, v) -> v.register(this));
			station.coinSlot.register(this);
			station.coinStorage.register(this);
			station.coinTray.register(this);
			station.coinValidator.register(this);
		} catch (Exception e) {
			for (PaymentEventListener listener : listeners)
				listener.reactToHardwareFailure();
		}
	}

	@Override
	public void reactToEnabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
	}

	@Override
	public void reactToDisabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
	}

	@Override
	public void reactToValidCoinDetectedEvent(CoinValidator validator, BigDecimal value) {
		for (PaymentEventListener listener : listeners)
			listener.onPaymentAddedEvent(value);

	}

	@Override
	public void reactToInvalidCoinDetectedEvent(CoinValidator validator) {
	}

	@Override
	public void reactToCoinAddedEvent(CoinTray tray) {
	}

	@Override
	public void reactToCoinsFullEvent(CoinStorage unit) {
	}

	@Override
	public void reactToCoinAddedEvent(CoinStorage unit) {
	}

	@Override
	public void reactToCoinsLoadedEvent(CoinStorage unit) {
	}

	@Override
	public void reactToCoinsUnloadedEvent(CoinStorage unit) {
	}

	@Override
	public void reactToCoinInsertedEvent(CoinSlot slot) {
	}

	@Override
	public void reactToCoinsFullEvent(CoinDispenser dispenser) {
	}

	@Override
	public void reactToCoinsEmptyEvent(CoinDispenser dispenser) {
	}

	@Override
	public void reactToCoinAddedEvent(CoinDispenser dispenser, Coin coin) {
	}

	@Override
	public void reactToCoinRemovedEvent(CoinDispenser dispenser, Coin coin) {
	}

	@Override
	public void reactToCoinsLoadedEvent(CoinDispenser dispenser, Coin... coins) {
	}

	@Override
	public void reactToCoinsUnloadedEvent(CoinDispenser dispenser, Coin... coins) {
	}

	public boolean payWithCoin(ArrayList<Coin> coins) throws DisabledException, OverloadException, EmptyException{
		boolean addItems = false;
		if(coins == null) throw new NullPointerException("No argument can be null");
		BigDecimal cValue = new BigDecimal("0");
		for (Coin coin: coins){
			cValue = cValue.add(coin.getValue());
		}
		 amountPaid = session.getTotalPaid();
		amountPaid = cValue;
		BigDecimal change = cValue.subtract(session.getTotalCost());

		boolean coinAccepted = false;
		for (Coin coin: coins){
			coinAccepted = acceptCoin(coin);
			if (!coinAccepted) cValue = cValue.subtract(coin.getValue());
		}
		if (cValue.compareTo(session.getTotalCost()) < 0){
			return  false;
		}

		if (cValue.compareTo(session.getTotalCost())> 0){
			BigDecimal changeDue = cValue.subtract(session.getTotalCost());
			return dispenseChange(changeDue);
		}

		return true;
	}

	private boolean acceptCoin(Coin coin) throws DisabledException{
		if(station.coinStorage.hasSpace()){
			station.coinSlot.accept(coin);

			if (reactToValidCoinDetectedEvent(CoinValidator validator, BigDecimal value)){
				return true;
			}else {
				station.coinSlot.disable();
			}
		}
		return false;
	}

}
