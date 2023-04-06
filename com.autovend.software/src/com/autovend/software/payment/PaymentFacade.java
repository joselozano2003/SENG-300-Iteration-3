package com.autovend.software.payment;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Map;

import com.autovend.Card.CardData;
import com.autovend.devices.AbstractDevice;
import com.autovend.devices.BillDispenser;
import com.autovend.devices.BillValidator;
import com.autovend.devices.CardReader;
import com.autovend.devices.CoinDispenser;
import com.autovend.devices.CoinValidator;
import com.autovend.devices.DisabledException;
import com.autovend.devices.EmptyException;
import com.autovend.devices.OverloadException;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.BillValidatorObserver;
import com.autovend.devices.observers.CardReaderObserver;
import com.autovend.devices.observers.CoinValidatorObserver;
import com.autovend.external.CardIssuer;
import com.autovend.software.AbstractFacade;
import com.autovend.software.customer.CustomerSession;

public class PaymentFacade extends AbstractFacade<PaymentListener> {

	private SelfCheckoutStation selfCheckoutStation;
	private CustomerSession customerSession;
	private Map<String, CardIssuer> cardIssuers;

	private class InnerListener implements CoinValidatorObserver, BillValidatorObserver, CardReaderObserver {

		@Override
		public void reactToEnabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
			// TODO Auto-generated method stub

		}

		@Override
		public void reactToDisabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
			// TODO Auto-generated method stub

		}

		@Override
		public void reactToCardInsertedEvent(CardReader reader) {
			// TODO Auto-generated method stub

		}

		@Override
		public void reactToCardRemovedEvent(CardReader reader) {
			// TODO Auto-generated method stub

		}

		@Override
		public void reactToCardTappedEvent(CardReader reader) {
			// TODO Auto-generated method stub

		}

		@Override
		public void reactToCardSwipedEvent(CardReader reader) {
			// TODO Auto-generated method stub

		}

		@Override
		public void reactToCardDataReadEvent(CardReader reader, CardData data) {
			processCardPayment(data);

		}

		@Override
		public void reactToValidBillDetectedEvent(BillValidator validator, Currency currency, int value) {
			for (PaymentListener listener : listeners) {
				listener.onPaymentSuccessful(BigDecimal.valueOf(value));
			}

		}

		@Override
		public void reactToInvalidBillDetectedEvent(BillValidator validator) {
			// TODO Auto-generated method stub

		}

		@Override
		public void reactToValidCoinDetectedEvent(CoinValidator validator, BigDecimal value) {
			for (PaymentListener listener : listeners) {
				listener.onPaymentSuccessful(value);
			}

		}

		@Override
		public void reactToInvalidCoinDetectedEvent(CoinValidator validator) {
			// TODO Auto-generated method stub

		}

	}

	public PaymentFacade(SelfCheckoutStation selfCheckoutStation, CustomerSession customerSession) {
		this.selfCheckoutStation = selfCheckoutStation;

		this.selfCheckoutStation.billValidator.register(new InnerListener());
		this.selfCheckoutStation.coinValidator.register(new InnerListener());
		this.selfCheckoutStation.cardReader.register(new InnerListener());

	}

	public void processCardPayment(CardData data) {
		BigDecimal value = customerSession.getAmountLeft();
		String cardIssuerName = data.getType();
		CardIssuer issuer = cardIssuers.get(cardIssuerName);
		if (issuer == null) {
			for (PaymentListener listener : listeners) {
				listener.onPaymentFailure();
			}
		} else {
			int holdNumber = issuer.authorizeHold(data.getNumber(), value);

			if (holdNumber == -1) {
				for (PaymentListener listener : listeners) {
					listener.onPaymentFailure();
				}
			} else {
				boolean transcationResult = issuer.postTransaction(data.getNumber(), holdNumber, value);
				if (transcationResult == true) {
					for (PaymentListener listener : listeners) {
						listener.onPaymentSuccessful(value);
					}

				} else {
					for (PaymentListener listener : listeners) {
						listener.onPaymentFailure();
					}
				}

			}

		}

	}

	public void dispenseChange() {

		BigDecimal changeDue = customerSession.getTotalPaid().subtract(customerSession.getTotalCost());

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
					for (PaymentListener listener : listeners) {
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
					for (PaymentListener listener : listeners) {
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
				for (PaymentListener listener : listeners) {
					listener.onChangeDispensedFailure();
				}

			}

		}

		for (PaymentListener listener : listeners) {
			listener.onChangeDispensed();
		}

	}

}
