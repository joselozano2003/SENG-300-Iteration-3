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
package com.autovend.software;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Map;

import com.autovend.Bill;
import com.autovend.Coin;
import com.autovend.devices.AbstractDevice;
import com.autovend.devices.BillDispenser;
import com.autovend.devices.BillValidator;
import com.autovend.devices.CoinDispenser;
import com.autovend.devices.CoinValidator;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.BillDispenserObserver;
import com.autovend.devices.observers.BillValidatorObserver;
import com.autovend.devices.observers.CoinDispenserObserver;
import com.autovend.devices.observers.CoinValidatorObserver;

/**
 * Class that allows customer to pay with cash.
 *
 */
@SuppressWarnings("serial")
public class PayWithCash extends Pay implements BillDispenserObserver, BillValidatorObserver, CoinDispenserObserver, CoinValidatorObserver {

    public final int[] billDenominations;

	private final Map<Integer, BillDispenser> billDispensers;

	private final List<BigDecimal> coinDenominations;
	private final Map<BigDecimal, CoinDispenser> coinDispensers;
    private ArrayList<BigDecimal> changeValues;
    private ArrayList<Bill> billsChange;
    private ArrayList<Coin> coinsChange;

	// If there is a change, sum up all bills+coins and send to dp
	// paid == due -> Disable/sth
    public PayWithCash(SelfCheckoutStation station) {
        super(station);
        this.billDenominations = station.billDenominations;
        this.billDispensers = station.billDispensers;
        this.coinDenominations = station.coinDenominations;
        this.coinDispensers = station.coinDispensers;

        changeValues = new ArrayList<>();
        coinsChange = new ArrayList<>();
        billsChange =  new ArrayList<>();
    }
    
    public void PassChange(BigDecimal change) {
		ArrayList<BigDecimal> res = ChangeCalculator.calculateChange(changeValues, change);
        for(BigDecimal value : res)
        {   
            if(coinDispensers.containsKey(value))
            {
                CoinDispenser dis = coinDispensers.get(value);
                try
                {
                    dis.emit();
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
            else{
                int billVal = value.intValue();
                if(billDispensers.containsKey(billVal))
                {
                    BillDispenser dis = billDispensers.get(billVal);
                    try
                    {
                        dis.emit();
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }

                }
                else{
                    
                }
                changeValues.remove(value);
            }
        }
    }
    public ArrayList<Coin> getCoinChange()
    {
        return coinsChange;
    }
    public ArrayList<Bill> getBillChange()
    {
        return billsChange;
    }
    
    @Override
    public void reactToEnabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {}
    @Override
    public void reactToDisabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {}
    @Override
    public void reactToBillsFullEvent(BillDispenser dispenser) {}
    @Override
    public void reactToBillsEmptyEvent(BillDispenser dispenser) {}
    
    @Override
    public void reactToBillAddedEvent(BillDispenser dispenser, Bill bill) {
    	// station.billValidator.accept(bill);
    }
    
    @Override
    public void reactToBillRemovedEvent(BillDispenser dispenser, Bill bill) {
    	// station.billOutput.removeDanglingBill();
        billsChange.add(bill);
    }
    
    @Override
    public void reactToBillsLoadedEvent(BillDispenser dispenser, Bill... bills) {
        for(Bill b : bills) {
            changeValues.add(new BigDecimal(b.getValue()));
        }
    }
    
    @Override
    public void reactToBillsUnloadedEvent(BillDispenser dispenser, Bill... bills) {}

    @Override
    public void reactToValidBillDetectedEvent(BillValidator validator, Currency currency, int value) {
		PurchasedItems.addAmountPaid(new BigDecimal(value));
		// If the AmountLeftToPay < 0 => There is a change need to be returned => call PassChange
    	if (PurchasedItems.getAmountLeftToPay().compareTo(new BigDecimal(0)) < 0) {
    		BigDecimal change = PurchasedItems.getAmountLeftToPay().abs();
    		PassChange(change);
    	}
    	
    }

    @Override
    public void reactToInvalidBillDetectedEvent(BillValidator validator) {}

	@Override
	public void reactToValidCoinDetectedEvent(CoinValidator validator, BigDecimal value) {
		PurchasedItems.addAmountPaid(value);

		// If the AmountLeftToPay < 0 => There is a change need to be returned => call PassChange
    	if (PurchasedItems.getAmountLeftToPay().compareTo(new BigDecimal(0)) < 0) {
    		BigDecimal change = PurchasedItems.getAmountLeftToPay().abs();
    		PassChange(change);
    	}
    }

	@Override
	public void reactToInvalidCoinDetectedEvent(CoinValidator validator) {}
	@Override
	public void reactToCoinsFullEvent(CoinDispenser dispenser) {}
	@Override
	public void reactToCoinsEmptyEvent(CoinDispenser dispenser) {}
	
	@Override
	public void reactToCoinAddedEvent(CoinDispenser dispenser, Coin coin) {
		station.coinValidator.accept(coin);
	}

	@Override
	public void reactToCoinRemovedEvent(CoinDispenser dispenser, Coin coin) {
		coinsChange.add(coin);
	}

	@Override
	public void reactToCoinsLoadedEvent(CoinDispenser dispenser, Coin... coins) {
		for(Coin c: coins) {
            changeValues.add(c.getValue());
        }
	}

	@Override
	public void reactToCoinsUnloadedEvent(CoinDispenser dispenser, Coin... coins) {}

}