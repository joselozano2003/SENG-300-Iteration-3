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
import java.util.List;

import com.autovend.devices.BillDispenser;
import com.autovend.devices.CoinDispenser;
import com.autovend.devices.DisabledException;
import com.autovend.devices.EmptyException;
import com.autovend.devices.OverloadException;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.software.AbstractFacade;

@SuppressWarnings("serial")
public class PaymentFacade extends AbstractFacade<PaymentEventListener> {
	private static PaymentFacade instance;
	private List<PaymentFacade> children;
	private static BigDecimal amountDue;
	
    public PaymentFacade(SelfCheckoutStation station, boolean isChild) {
        super(station);
        //this.selfCheckoutStation = station;
        if (!isChild) {
            children = new ArrayList<PaymentFacade>();
            children.add(new PayWithCoin(station));
            children.add(new PayWithBill(station));
            children.add(new PayWithCard(station));
            amountDue = BigDecimal.ZERO;
        }
    }


	
	public void addAmountDue(BigDecimal amountToAdd) {
		amountDue = amountDue.add(amountToAdd);
	}
	
	public void subtractAmountDue(BigDecimal amountToSubtract) {
		amountDue = amountDue.subtract(amountToSubtract);
	}
	
	/**
	 * @return amountPayed
	 */
	public BigDecimal getAmountDue() {
		return amountDue;
	}
	
	/**
	 * @return List of active subclasses.
	 */
	public List<PaymentFacade> getChildren() {
		return children;
	}
	
	/**
	 * @return This current active instance of this class. Could be null.
	 */
	public static PaymentFacade getInstance() {
		return instance;
	}
	
	public void dispenseChange(BigDecimal changeDue) {


		if (changeDue.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException("Change required must be non-negative.");
		}

		// Dispense bills
		for (int i = station.billDenominations.length - 1; i >= 0; i--) {
			int billDenomination = station.billDenominations[i];
			BillDispenser billDispenser = station.billDispensers.get(billDenomination);
			BigDecimal billValue = BigDecimal.valueOf(billDenomination);
			while (changeDue.compareTo(billValue) >= 0 && billDispenser.size() > 0) {
				try {
					billDispenser.emit();
					changeDue = changeDue.subtract(billValue);
				} catch (DisabledException | OverloadException | EmptyException e) {
					for (PaymentEventListener listener : listeners) {
						listener.onChangeDispensedFailure();
					}
				}
			}
		}

		// Dispense coins
		for (int i = station.coinDenominations.size() - 1; i >= 0; i--) {
			BigDecimal coinDenomination = station.coinDenominations.get(i);
			CoinDispenser coinDispenser = station.coinDispensers.get(coinDenomination);

			while (changeDue.compareTo(coinDenomination) >= 0 && coinDispenser.size() > 0) {

				try {
					coinDispenser.emit();
					changeDue = changeDue.subtract(coinDenomination);
				} catch (DisabledException | OverloadException | EmptyException e) {
					for (PaymentEventListener listener : listeners) {
						listener.onChangeDispensedFailure();
					}
				}
			}
		}

		// Edge case for if the change due is less than the smallest coin denomination
		if (changeDue.compareTo(BigDecimal.ZERO) > 0) {
			BigDecimal smallestCoinDenomination = station.coinDenominations.get(0);
			CoinDispenser smallestCoinDispenser = station.coinDispensers.get(smallestCoinDenomination);
			try {
				smallestCoinDispenser.emit();
			} catch (DisabledException | OverloadException | EmptyException e) {
				for (PaymentEventListener listener : listeners) {
					listener.onChangeDispensedFailure();
				}

			}

		}

		for (PaymentEventListener listener : listeners) {
			listener.onChangeDispensedEvent();
		}

	}
	
}
