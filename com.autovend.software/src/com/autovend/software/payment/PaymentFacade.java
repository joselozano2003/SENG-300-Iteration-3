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
    private SelfCheckoutStation selfCheckoutStation; 
	
    public PaymentFacade(SelfCheckoutStation station, boolean isChild) {
        super(station);
        this.selfCheckoutStation = station;
        if (!isChild) {
            children = new ArrayList<PaymentFacade>();
            children.add(new PayWithCoin(selfCheckoutStation));
            children.add(new PayWithBill(selfCheckoutStation));
//            children.add(new PayWithCard(selfCheckoutStation));
            amountDue = BigDecimal.ZERO;
        }
    }


	
	public void addAmountDue(BigDecimal amountToAdd) {
		amountDue.add(amountToAdd);
  
	}
	
	public void subtractAmountDue(BigDecimal amountToSubtract) {
		amountDue.subtract(amountToSubtract);
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
		for (int i = selfCheckoutStation.billDenominations.length - 1; i >= 0; i--) {
			int billDenomination = selfCheckoutStation.billDenominations[i];
			BillDispenser billDispenser = selfCheckoutStation.billDispensers.get(billDenomination);
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
		for (int i = selfCheckoutStation.coinDenominations.size() - 1; i >= 0; i--) {
			BigDecimal coinDenomination = selfCheckoutStation.coinDenominations.get(i);
			CoinDispenser coinDispenser = selfCheckoutStation.coinDispensers.get(coinDenomination);

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
			BigDecimal smallestCoinDenomination = selfCheckoutStation.coinDenominations.get(0);
			CoinDispenser smallestCoinDispenser = selfCheckoutStation.coinDispensers.get(smallestCoinDenomination);
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
