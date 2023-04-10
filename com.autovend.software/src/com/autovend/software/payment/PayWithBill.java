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
import java.util.Currency;

import com.autovend.Bill;
import com.autovend.devices.*;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.BillDispenserObserver;
import com.autovend.devices.observers.BillSlotObserver;
import com.autovend.devices.observers.BillStorageObserver;
import com.autovend.devices.observers.BillValidatorObserver;
import com.autovend.software.customer.CustomerSession;

@SuppressWarnings("serial")
public class WithBill extends PaymentFacade implements BillDispenserObserver, BillSlotObserver, BillStorageObserver, BillValidatorObserver {
	private BigDecimal changeDue;
	private  SelfCheckoutStation station;
	CustomerSession session;
	BigDecimal amountPaid;
	int validator;
	Object currency;
	public WithBill(SelfCheckoutStation station) {
		super(station);
		try {
			station.billDispensers.forEach((k,v) -> v.register(this));
			station.billInput.register(this);
			station.billOutput.register(this);
			station.billStorage.register(this);
			station.billValidator.register(this);
		} catch (Exception e) {
			for (PaymentListener listener : listeners)
				listener.reactToHardwareFailure();
		}
	}

	@Override
	public void reactToEnabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {}
	@Override
	public void reactToDisabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {}
	@Override
	public void reactToValidBillDetectedEvent(BillValidator validator, Currency currency, int value) {}
	@Override
	public void reactToInvalidBillDetectedEvent(BillValidator validator) {}
	@Override
	public void reactToBillsFullEvent(BillStorage unit) {}
	@Override
	public void reactToBillAddedEvent(BillStorage unit) {}
	@Override
	public void reactToBillsLoadedEvent(BillStorage unit) {}
	@Override
	public void reactToBillsUnloadedEvent(BillStorage unit) {}
	@Override
	public void reactToBillInsertedEvent(BillSlot slot) {}
	@Override
	public void reactToBillEjectedEvent(BillSlot slot) {}
	@Override
	public void reactToBillRemovedEvent(BillSlot slot) {}
	@Override
	public void reactToBillsFullEvent(BillDispenser dispenser) {}
	@Override
	public void reactToBillsEmptyEvent(BillDispenser dispenser) {}
	@Override
	public void reactToBillAddedEvent(BillDispenser dispenser, Bill bill) {}
	@Override
	public void reactToBillRemovedEvent(BillDispenser dispenser, Bill bill) {}
	@Override
	public void reactToBillsLoadedEvent(BillDispenser dispenser, Bill... bills) {}
	@Override
	public void reactToBillsUnloadedEvent(BillDispenser dispenser, Bill... bills) {}

	public boolean payWithBill(ArrayList<Bill> bills) throws SimulationException, OverloadException, DisabledException, EmptyException{
		boolean addingItems =false;
		if(bills == null) throw new NullPointerException("No argument may be null");
		double bValue = 0.0;
		for (Bill bill : bills){
			bValue += bill.getValue();
		}
		amountPaid = new BigDecimal(bValue);
		changeDue = amountPaid.subtract(session.getTotalCost());

		boolean billAccepted = false;
		for(Bill bill: bills){
			billAccepted = acceptBill(bill);
			if(!billAccepted) bValue -= bill.getValue();

		}

		if(bValue < session.getTotalCost().doubleValue()){
			return false;
		}

		if (bValue >session.getTotalCost().doubleValue()){
			BigDecimal changeDue = amountPaid.subtract(session.getTotalCost());
			return dispenseChange(changeDue);
		}
		return true;

	}

	private boolean acceptBill(Bill bill) throws SimulationException, OverloadException, DisabledException{
		if (station.billStorage.hasSpace()) {
			station.billInput.accept(bill);

			if (reactToValidBillDetectedEvent(BillValidator validator, Currency currency, int value)){
				return true;
			}else{
				station.billInput.disable();
			}
		}
		return false;

	}
}
